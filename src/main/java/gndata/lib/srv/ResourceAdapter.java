package gndata.lib.srv;

import java.util.*;

import static gndata.lib.util.Resources.*;
import static java.util.stream.Collectors.toList;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.RDFS;

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
        return streamResourcesFor(resource)
                    .map(r -> new ResourceAdapter(r, this))
                    .collect(toList());
    }

    public List<Statement> getLiterals() {
        return listLiteralsFor(resource);
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


    public Optional<String> getLabel() {
        Statement labelSt = resource.getProperty(RDFS.label);
        return labelSt == null ? Optional.ofNullable(null) : Optional.of(labelSt.getObject().asLiteral().getString());
    }


    public Resource addLiteral(Property p, String data, RDFDatatype dtype) {
        Literal o = ResourceFactory.createTypedLiteral(data, dtype);
        return resource.addLiteral(p, o);
    }

    public void removeObjectProperty(Property p, Resource obj) {
        Optional<Statement> rem = resource.listProperties(p)
                .toList().stream().filter(st -> st.getObject().asResource().equals(obj))
                .findFirst();
        if (rem.isPresent()) {
            rem.get().remove();
        }
    }

    public void remove() {
        Model from = resource.getModel();

        Model what = ModelFactory.createDefaultModel();
        what.add(from.listStatements(resource, null, (RDFNode) null));
        what.add(from.listStatements(null, null, resource));

        from.remove(what);
    }

}
