package gndata.lib.util;

import java.util.*;
import java.util.stream.Collectors;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Created by andrey on 11.05.15.
 */
public class OntologyHelper {

    private OntModel ontology;

    public OntologyHelper(OntModel ontology) {
        this.ontology = ontology;
    }

    /* ontology Class methods */

    public List<OntClass> listClasses() {
        return ontology.listClasses().toList();
    }

    public List<OntClass> listClasses(Resource res) {
        List<String> types = res.listProperties(RDF.type).toList().stream()
                .map(st -> st.getObject().asResource().getURI())
                .collect(Collectors.toList());

        return ontology.listClasses().toList().stream()
                .filter(cls -> types.contains(cls.getURI()))
                .collect(Collectors.toList());
    }

    public List<OntClass> listRelated(OntClass cls) {
        List<OntClass> range = new ArrayList<>();

        cls.listDeclaredProperties().toList().stream()
                .filter(OntProperty::isObjectProperty)
                .forEach(prop -> {
                    prop.listRange().toList().forEach(ontRes -> {
                        range.add(ontology.getOntClass(ontRes.getURI()));
                    });
                });

        return range;
    }

    public List<OntClass> listRelated(Resource res) {
        List<OntClass> range = new ArrayList<>();

        listClasses(res).forEach(cls -> range.addAll(listRelated(cls)));

        return range;
    }

    /* ontology Property methods */

    public List<OntProperty> listDatatypeProperties(OntClass cls) {
        return cls.listDeclaredProperties()
                .toList().stream().filter(OntProperty::isDatatypeProperty)
                .collect(Collectors.toList());
    }

    public List<OntProperty> listDatatypeProperties(Resource res) {
        List<OntProperty> props = new ArrayList<>();

        listClasses(res).forEach(cls -> props.addAll(listDatatypeProperties(cls)));

        return props;
    }

    public List<OntProperty> listObjectProperties(OntClass cls) {
        return cls.listDeclaredProperties()
                .toList().stream().filter(OntProperty::isObjectProperty)
                .collect(Collectors.toList());
    }

    public List<OntProperty> listObjectProperties(Resource res) {
        List<OntProperty> props = new ArrayList<>();

        listClasses(res).forEach(cls -> props.addAll(listObjectProperties(cls)));

        return props;
    }
}
