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
 * A wrapper over Resource to provide common helper functions.
 */
public class ResourceAdapter {

    private static int characteristics = DISTINCT | NONNULL;
    protected Resource resource;

    /**
     * Assuming a resource is linked to a Model with other RDF data triples.
     *
     * @param resource
     */
    public ResourceAdapter(Resource resource) {
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
     * Returns all statements containing literal values as object for a certain resource.
     *
     * @return A collection with literals.
     */
    public Collection<Statement> getLiterals() {
        return resource.listProperties().filterKeep(new LiteralFilter()).toList();
    }

    /**
     * Returns all resources directly related to the given resource. Blank nodes and
     * Resources that represent a type are ignored. If the resource itself is a {@link OWL#Class}
     * the method will return all instances of this class.
     *
     * For instance resources the method also resolves reverse relationships.
     *
     * @return A collection with related resources.
     */
    public Collection<ResourceAdapter> getResources() {
        ExtendedIterator<Statement> it;

        if (resource.hasProperty(RDF.type, OWL.Class)) {  //resource is an OWL Class
            return resource.getModel().listStatements(null, RDF.type, resource).toList().stream()
                    .map(Statement::getSubject)
                    .map(ResourceAdapter::new)
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
                    .map(ResourceAdapter::new)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Returns all available resources (in the related Model) of a particular OntClass
     * that can be connected via a given ObjectProperty.
     *
     * @return A collection with available to add resources.
     */
    public Collection<ResourceAdapter> availableToAdd(ObjectProperty p, OntClass cls) {
        List<Resource> lst = new ArrayList<>();

        // all available Resources of type cls
        List<Resource> available = resource.getModel()
                .listStatements(null, RDF.type, cls).toList().stream()
                .map(Statement::getSubject)
                .filter(res -> !res.equals(resource))  // exclude self
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

        return lst.stream().map(ResourceAdapter::new).collect(Collectors.toList());
    }

    /**
     * Removes given object properties from the related Model.
     * (Resources stay in the Model, only connections are removed)
     */
    public void removeObjectProperties(List<Resource> objs) {
        Model toRemove = ModelFactory.createDefaultModel();

        toRemove.add(resource.listProperties()
                .toList().stream()
                .filter(st -> st.getObject().isResource())
                .filter(st -> objs.contains(st.getObject().asResource()))
                .collect(Collectors.toList()));

        toRemove.add(resource.getModel().listStatements(null, null, resource)
                    .toList().stream()
                    .filter(st -> objs.contains(st.getSubject()))
                    .collect(Collectors.toList()));

        resource.getModel().remove(toRemove);  // remove in a single change
    }

    /**
     * Removes this resource from the related Model.
     */
    public void remove() {
        Model from = resource.getModel();

        Model what = ModelFactory.createDefaultModel();
        what.add(from.listStatements(resource, null, (RDFNode) null));
        what.add(from.listStatements(null, null, resource));

        from.remove(what);
    }


    /* helper classes */


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;

        ResourceAdapter that = (ResourceAdapter) o;

        return resource.equals(that.resource);
    }

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
