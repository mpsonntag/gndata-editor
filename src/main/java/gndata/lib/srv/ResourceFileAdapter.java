package gndata.lib.srv;

import java.util.*;

import static java.util.stream.Collectors.toList;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Created by andrey on 20.05.15.
 */
public class ResourceFileAdapter extends ResourceWrapper implements IFileAdapter {

    private ResourceFileAdapter parent;

    public ResourceFileAdapter(Resource resource, ResourceFileAdapter parent) {
        super(resource);

        this.parent = parent;
    }

    @Override
    public Optional<ResourceFileAdapter> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public List<ResourceFileAdapter> getChildren() {
        return getResources().stream()
                .map(r -> new ResourceFileAdapter(r, this))
                .collect(toList());
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public String getFileName() {
        return toNameString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        ResourceFileAdapter that = (ResourceFileAdapter) o;

        return resource.equals(that.resource);
    }
}
