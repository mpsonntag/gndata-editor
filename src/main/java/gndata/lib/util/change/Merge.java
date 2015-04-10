package gndata.lib.util.change;

import java.util.*;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;

/**
 * Class that knows how to update Metadata entities.
 */
public class Merge implements Change {

    private Model new_object;
    private OntModel ontology;
    private boolean applied = false;

    private Model changes = ModelFactory.createDefaultModel();

    public Merge(Model new_object, OntModel ontology) {
        this.new_object = new_object;
        this.ontology = ontology;
    }

    public void applyTo(Model m) {
        // ensure there is only one triple that defines type
        List<Resource> all = new_object.listSubjectsWithProperty(RDF.type).toList();
        assert(all.size() == 1);
        Resource subj = all.get(0);  // this should be the actual Subject

        // ensure all triples are about the same subj
        Set<Resource> st = new_object.listSubjects().toSet();
        st.remove(subj);
        assert st.isEmpty();

        // model should have an RDF.type from related ontology
        List<RDFNode> lst = new_object.listObjectsOfProperty(RDF.type).toList();
        assert(lst.size() == 1);

        RDFNode obj_type_l = lst.get(0);
        Resource obj_type_r = ontology.getResource(obj_type_l.toString());

        if (ontology.contains(obj_type_r, RDFS.subClassOf)) {

            // record changes
            Model tmp = ModelFactory.createDefaultModel();
            tmp.add(m.intersection(new_object));
            tmp.add(new_object);
            changes = tmp;

            m.removeAll(subj, null, null);
            m.add(new_object);

            applied = true;
        }
    }

    public void undoFrom(Model m) {
        // TODO add logic
    }

    public boolean applied() {
        return applied;
    }
}
