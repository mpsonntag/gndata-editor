package gndata.lib.srv;

import java.util.*;
import java.util.stream.*;

import static java.util.Spliterator.*;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.iterator.*;
import com.hp.hpl.jena.vocabulary.*;
import gndata.lib.util.StatementComparator;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by andrey on 20.05.15.
 */
public class ResourceWrapper {

    private static int characteristics = DISTINCT | NONNULL;
    protected Resource resource;

    /**
     * Assuming a resource is linked to a Model with other RDF data triples.
     *
     * @param resource
     */
    public ResourceWrapper(Resource resource) {
        this.resource = resource;
    }

    public int hashCode() {
        return resource.hashCode();
    }

    public Resource getResource() {
        return resource;
    }

    /**
     * Gets a human readable name for a {@link Resource}. If the resource is a class
     * the name will be the class name. Otherwise the method will try to use {@link RDFS#label}
     * as name. If this fails the name will be the class name combined with the first letters
     * of the {@link Resource#getLocalName()} of the resource.
     *
     * @return A human readable name for the resource.
     */
    public String toNameString() {
        String name;

        if (resource.hasProperty(RDF.type, OWL.Class)) {
            name = resource.getLocalName();
        } else {
            if (resource.hasProperty(RDFS.label)) {
                name = resource.getProperty(RDFS.label).getLiteral().toString();
            } else {
                String type;
                Statement typeStmt = resource.getProperty(RDF.type);

                if (typeStmt != null) {
                    type = typeStmt.getResource().getLocalName();
                } else {
                    type = "Thing";
                }

                // TODO investigate why the check for null is necessary (because it shouldn't)
                String id = resource.getLocalName() == null ? "" : resource.getLocalName();
                id = id.length() < 8 ? id : id.substring(0, 7);

                name = String.format("%s: %s", type, id);
            }
        }

        return name;
    }

    /**
     * Get a string that provides additional human readable information about a resource.
     * This can be used as additional textual information in tooltips or list cells.
     *
     * @return Human readable information about the resource.
     */
    public String toInfoString() {
        long litCount = getLiterals().size();
        long relCount = getResources().size();
        return String.format("Relations: %d, Literals: %d", relCount, litCount);
    }

    /**
     * Get a Label of a resource if exists.
     *
     * @return
     */
    public Optional<String> getLabel() {
        Statement labelSt = resource.getProperty(RDFS.label);
        return labelSt == null ? Optional.ofNullable(null) : Optional.of(labelSt.getObject().asLiteral().getString());
    }


    /* Object / Datatype properties methods */


    /**
     * Streams all statements containing literal values as object for a certain resource.
     *
     * @return A stream with literals.
     */
    public Collection<Statement> getLiterals() {
        return resource.listProperties().filterKeep(new LiteralFilter()).toList();
    }

    /**
     * Streams all resources directly related to the given resource. Blank nodes and
     * Resources that represent a type are ignored. If the resource itself is a {@link OWL#Class}
     * the method will return all instances of this class.
     *
     * For instance resources the method also resolves reverse relationships.
     *
     * @return A stream with related resources.
     */
    public Collection<Resource> getResources() {
        ExtendedIterator<Statement> it;

        if (resource.hasProperty(RDF.type, OWL.Class)) {  //resource is an OWL Class
            return resource.getModel().listStatements(null, RDF.type, resource).toList().stream()
                    .map(Statement::getSubject)
                    .collect(Collectors.toList());

        } else {
            it = resource.listProperties().filterKeep(new ResourceFilter());
            Stream<Statement> forward = StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(it, characteristics), false);

            // TODO reverse relationships ??
            it = resource.getModel().listStatements(null, null, resource).filterKeep(new ResourceFilter());
            Stream<Statement> reverse = StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(it, characteristics), false);

            return Stream.concat(forward, reverse)
                    .sorted(new StatementComparator())
                    .map(stmt -> stmt.getSubject().equals(resource) ? stmt.getObject().asResource() : stmt.getSubject())
                    .collect(Collectors.toList());
        }
    }

    public Collection<Resource> availableToAdd(ObjectProperty p, OntClass cls) {
        List<Resource> lst = new ArrayList<>();

        // all available Resources of type cls
        List<Resource> available = resource.getModel()
                .listStatements(null, RDF.type, cls).toList().stream()
                .map(Statement::getSubject)
                .collect(Collectors.toList());

        if (p.isFunctionalProperty() && resource.getProperty(p) == null) {
            lst.addAll(available);
        } else {
            lst.addAll(available);

            // already connected Resources
            lst.removeAll(resource.listProperties(p).toList().stream()
                    .map(Statement::getObject)
                    .map(RDFNode::asResource)
                    .collect(Collectors.toList()));
        }

        return lst;
    }

    public void updateObjectProperties(List<Pair<Property, Resource>> objs) {
        Model toRemove = ModelFactory.createDefaultModel();
        Model toAdd = ModelFactory.createDefaultModel();

        toRemove.add(resource.listProperties().toList().stream()
                .filter(st -> st.getObject().isResource())  // obj props only
                .filter(st -> !objs.contains(Pair.of(st.getPredicate(), st.getObject().asResource())))
                .collect(Collectors.toList()));

        List<Pair<Property, Resource>> notChanged = objs.stream()
                .filter(pair -> resource.listProperties(pair.getLeft())
                        .toList().stream().map(st -> st.getObject().asResource())
                        .collect(Collectors.toList()).contains(pair.getRight()))
                .collect(Collectors.toList());

        toAdd.add(objs.stream().filter(pair -> !notChanged.contains(pair))
                .map(pair -> ResourceFactory.createStatement(resource, pair.getLeft(), pair.getRight()))
                .collect(Collectors.toList()));

        resource.getModel().remove(toRemove);
        resource.getModel().add(toAdd);
    }

    public void removeObjectProperties(List<Resource> objs) {
        Model toRemove = ModelFactory.createDefaultModel();

        toRemove.add(resource.listProperties()
                .toList().stream()
                .filter(st -> st.getObject().isResource())
                .filter(st -> objs.contains(st.getObject().asResource()))
                .collect(Collectors.toList()));

        resource.getModel().remove(toRemove);  // remove in a single change
    }

    public void remove() {
        Model from = resource.getModel();

        Model what = ModelFactory.createDefaultModel();
        what.add(from.listStatements(resource, null, (RDFNode) null));
        what.add(from.listStatements(null, null, resource));

        from.remove(what);
    }


    /* helper classes */


    /**
     * Filters statements with literal objects.
     */
    private static class LiteralFilter extends Filter<Statement> {

        @Override
        public boolean accept(Statement stmt) {
            return stmt.getObject().isLiteral();
        }
    }

    /**
     * Filters statements with resource object that ar not blank nodes and not types.
     */
    private static class ResourceFilter extends Filter<Statement> {

        @Override
        public boolean accept(Statement stmt) {
            RDFNode o = stmt.getObject();

            return o.isResource() && (! o.isAnon()) && (! stmt.getPredicate().equals(RDF.type));
        }
    }
}
