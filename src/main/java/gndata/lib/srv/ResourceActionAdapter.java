package gndata.lib.srv;

import java.util.Optional;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.rdf.model.*;

/**
 * Created by andrey on 11.05.15.
 */
public class ResourceActionAdapter {

    private Resource resource;

    public ResourceActionAdapter(Resource resource) {
        this.resource = resource;
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

    public Resource addLiteral(Property p, String data, RDFDatatype dtype) {
        Literal o = ResourceFactory.createTypedLiteral(data, dtype);
        return resource.addLiteral(p, o);
    }
}
