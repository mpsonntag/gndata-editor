package gndata.lib.util;

import java.util.*;
import java.util.stream.*;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
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
    public Set<OntClass> getRange(OntProperty prop) {
        return prop.listRange().toList().stream()
                .map(res -> ontology.getOntClass(res.getURI()))
                .collect(Collectors.toSet());
    }

    /**
     * Returns a map of related (via owl:range) properties/classes for the given class.
     *
     * @return Set<Pair<OntProperty, OntClass>>
     */
    public Set<Pair<OntProperty, OntClass>> listRelated(OntClass cls) {
        Set<Pair<OntProperty, OntClass>> range = new HashSet<>();

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
    public Set<Pair<OntProperty, OntClass>> listRelated(Resource res) {
        Set<Pair<OntProperty, OntClass>> range = new HashSet<>();

        listClasses(res).forEach(cls -> range.addAll(listRelated(cls)));

        return range;
    }


    /* ontology Property methods */


    /**
     * Returns a map of related (via owl:range) properties/classes for the given resource.
     *
     * @return Set<Pair<OntProperty, OntClass>>
     */
    public List<OntProperty> listDatatypeProperties(OntClass cls) {
        return cls.listDeclaredProperties()
                .toList().stream().filter(OntProperty::isDatatypeProperty)
                .collect(Collectors.toList());
    }

    public List<OntProperty> listDatatypeProperties(Resource res) {
        List<OntProperty> props = new ArrayList<>();

        listClasses(res).forEach(cls -> props.addAll(listDatatypeProperties(cls)));

        // to filter for still addable props use filter below
        // .filter(pr -> !pr.isFunctionalProperty() || !annotations.contains(res, pr))

        return props;
    }

    public List<OntProperty> listObjectProperties(OntClass cls) {
        return cls.listDeclaredProperties(true)
                .toList().stream().filter(OntProperty::isObjectProperty)
                .collect(Collectors.toList());
    }

    public List<OntProperty> listObjectProperties(Resource res) {
        List<OntProperty> props = new ArrayList<>();

        listClasses(res).forEach(cls -> props.addAll(listObjectProperties(cls)));

        return props;
    }
}
