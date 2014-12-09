package gndata.lib.srv;

import java.util.*;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Adapter for navigation on rdf resources.
 *
 * TODO implement
 */
public class ResourceAdapter extends FileAdapter {

    private Resource resource, parent;

    public ResourceAdapter(Resource resource, Resource parent) {
        this.resource = resource;
        this.parent = parent;
    }

    @Override
    public Optional<ResourceAdapter> getParent() {
        return null;
    }

    @Override
    public List<ResourceAdapter> getChildren() {
        return null;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public String getFileName() {
        return null;
    }

    public Resource getResource() {
        return resource;
    }

    public Model getModel() {
        return resource.getModel();
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

}
