package gndata.lib.srv;

import java.util.*;
import java.util.stream.*;

import static gndata.lib.util.Resources.*;
import static java.util.stream.Collectors.toList;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Adapter for navigation on rdf resources.
 *
 */
public class ResourceAdapter implements IFileAdapter {

    private Resource resource;
    private ResourceAdapter parent;

    public ResourceAdapter(Resource resource, ResourceAdapter parent) {
        this.resource = resource;
        this.parent = parent;
    }

    @Override
    public Optional<ResourceAdapter> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public List<ResourceAdapter> getChildren() {
        return streamResourcesFor(resource)
                    .map(r -> new ResourceAdapter(r, this))
                    .collect(toList());
    }

    public List<Statement> getLiterals() {
        return streamLiteralsFor(resource).collect(Collectors.toList());
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public String getFileName() {
        return toNameString(resource);
    }

    public String getInfo() {
        return toInfoString(resource);
    }

    public Resource getResource() {
        return resource;
    }

    public Model getModel() {
        return resource.getModel();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        ResourceAdapter that = (ResourceAdapter) o;

        return resource.equals(that.resource);
    }

    @Override
    public int hashCode() {
        return resource.hashCode();
    }


    /* resource action API: TODO refactor in a separate interface later */


    public List<Resource> availableToAdd(ObjectProperty p, OntClass cls) {
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

    public Optional<String> getLabel() {
        Statement labelSt = resource.getProperty(RDFS.label);
        return labelSt == null ? Optional.ofNullable(null) : Optional.of(labelSt.getObject().asLiteral().getString());
    }


    public Resource addLiteral(Property p, String data, RDFDatatype dtype) {
        Literal o = ResourceFactory.createTypedLiteral(data, dtype);
        return resource.addLiteral(p, o);
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

}
