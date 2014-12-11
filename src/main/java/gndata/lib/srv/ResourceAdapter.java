package gndata.lib.srv;

import java.util.*;

import static gndata.lib.util.Resources.*;
import static java.util.stream.Collectors.toList;

import com.hp.hpl.jena.rdf.model.*;

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
}
