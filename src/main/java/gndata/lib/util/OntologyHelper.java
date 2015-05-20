package gndata.lib.util;

import java.util.*;
import java.util.stream.*;

import com.hp.hpl.jena.datatypes.*;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.*;
import org.apache.commons.lang3.tuple.Pair;

/**
 * A helper class to perform common actions on the Ontology.
 */
public class OntologyHelper {

    private OntModel ontology;

    public OntologyHelper(OntModel ontology) {
        this.ontology = ontology;
    }


    /* ontology Class methods */


    /**
     * Returns list of available classes in the current ontology.
     *
     * @return Set<OntClass>
     */
    public Set<OntClass> listClasses() {
        return ontology.listClasses().toSet();
    }

    /**
     * Returns list of classes a current resource belongs to.
     *
     * @return Set<OntClass>
     */
    public Set<OntClass> listClasses(Resource res) {
        Set<OntClass> classes = new HashSet<>();

        OntClass cls = ontology.getOntClass(res.listProperties(RDF.type)
                .toList().get(0).getObject().asResource().getURI());

        classes.add(cls);
        classes.addAll(cls.listSuperClasses().toSet());

        return classes;
    }

    /**
     * Returns a set of Classes defined as a Range for a given Property.
     *
     * @return Set<OntClass>
     */
    public Set<OntClass> getRange(ObjectProperty prop) {
        return prop.listRange().toList().stream()
                .map(res -> ontology.getOntClass(res.getURI()))
                .collect(Collectors.toSet());
    }

    /**
     * Returns a set of Classes defined as a Range for a given Property.
     *
     * @return Set<RDFDatatype>
     */
    public Set<RDFDatatype> getRange(DatatypeProperty prop) {
        Set<RDFDatatype> range = new HashSet<>();

        TypeMapper tm = TypeMapper.getInstance();
        range.addAll(prop.listRange().toList().stream()
                .map(p -> tm.getTypeByName(p.getURI()))
                .filter(p -> p != null)
                .collect(Collectors.toSet()));

        if (range.isEmpty()) {
            range.add(XSDDatatype.XSDstring);
        }

        return range;
    }

    /**
     * Returns a map of related (via owl:range) properties/classes for the given class.
     *
     * @return Set<Pair<OntProperty, OntClass>>
     */
    public Set<Pair<ObjectProperty, OntClass>> listRelated(OntClass cls) {
        Set<Pair<ObjectProperty, OntClass>> range = new HashSet<>();

        listObjectProperties(cls).stream().forEach(prop ->
                range.addAll(getRange(prop).stream()
                        .map(rcls -> Pair.of(prop, rcls))
                        .collect(Collectors.toSet())));

        return range;
    }

    /**
     * Returns a map of related (via owl:range) properties/classes for the given resource.
     *
     * @return Set<Pair<OntProperty, OntClass>>
     */
    public Set<Pair<ObjectProperty, OntClass>> listRelated(Resource res) {
        Set<Pair<ObjectProperty, OntClass>> range = new HashSet<>();

        listClasses(res).forEach(cls -> range.addAll(listRelated(cls)));

        return range;
    }


    /* ontology Property methods */


    /**
     * Returns a map of related (via owl:range) properties/classes for the given resource.
     *
     * @return Set<Pair<OntProperty, OntClass>>
     */
    public List<DatatypeProperty> listDatatypeProperties(OntClass cls) {
        return cls.listDeclaredProperties()
                .toList().stream().filter(OntProperty::isDatatypeProperty)
                .map(p -> ontology.getDatatypeProperty(p.getURI()))
                .collect(Collectors.toList());
    }

    public List<DatatypeProperty> listDatatypeProperties(Resource res) {
        List<DatatypeProperty> props = new ArrayList<>();

        listClasses(res).forEach(cls -> props.addAll(listDatatypeProperties(cls)));

        // to filter for still addable props use filter below
        // .filter(pr -> !pr.isFunctionalProperty() || !annotations.contains(res, pr))

        return props;
    }

    public List<ObjectProperty> listObjectProperties(OntClass cls) {
        return cls.listDeclaredProperties(true)
                .toList().stream().filter(OntProperty::isObjectProperty)
                .map(p -> ontology.getObjectProperty(p.getURI()))
                .collect(Collectors.toList());
    }

    public List<ObjectProperty> listObjectProperties(Resource res) {
        List<ObjectProperty> props = new ArrayList<>();

        listClasses(res).forEach(cls -> props.addAll(listObjectProperties(cls)));

        return props;
    }
}
