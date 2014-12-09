package gndata.lib.srv;

import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.stream.*;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;

/**
 * Adapter for navigation on rdf resources.
 *
 * TODO implement
 */
public class ResourceAdapter extends FileAdapter {

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
        //resource.listProperties().forEachRemaining(p -> System.out.println(p.toString()));

        Stream<Resource> statements;

        if (resource.hasProperty(RDF.type, OWL.Class)) {
            statements = resource.getModel().listStatements(null, RDF.type, resource).toList()
                    .stream()
                    .map(Statement::getSubject);
        } else {
            statements = resource.listProperties().toList()
                    .stream()
                    .filter(stmt -> stmt.getObject().isResource())
                    .filter(stmt -> ! stmt.getPredicate().equals(RDF.type))
                    .map(stmt -> stmt.getObject().asResource());
        }

        return statements
                .map(r -> new ResourceAdapter(r, this))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public String getFileName() {
        String name;

        if (resource.hasProperty(RDF.type, OWL.Class)) {
            name = resource.getLocalName();
        } else {
            if (resource.hasProperty(RDFS.label)) {
                name = resource.getProperty(RDFS.label).getLiteral().toString();
            } else {
                String type = resource.getProperty(RDF.type).getResource().getLocalName();
                String id = resource.getLocalName().substring(0, 7);
                name = String.format("%s: %s", type, id);
            }
        }

        return name;
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
}
