// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.lib.srv;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.*;

import static java.util.Spliterator.*;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.*;
import com.hp.hpl.jena.vocabulary.*;
import gndata.lib.util.*;
import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.riot.RDFDataMgr;

/**
 * Class implementing main functions working with project metadata
 */
public class MetadataService {

    public QueryHelper query;
    public ChangeHelper change;

    private OntModel schema;    // union model for all imported ontology files
    private Model annotations;  // model for data annotations

    public MetadataService(OntModel schema, Model annotations) {
        this.schema = schema;
        this.annotations = annotations;

        this.query = new QueryHelper(annotations);
        this.change = new ChangeHelper(annotations, schema);
    }

    /**
     * Returns a Ontology RDF Model instance to access default and
     * custom ontology terms.
     *
     * @return Ontology Model
     */
    public OntModel getSchema() {
        return schema;
    }

    /**
     * Returns an RDF Model instance to access annotations as stored RDF triples.
     *
     * @return RDF Model
     */
    public Model getAnnotations() {
        return annotations;
    }

    /**
     * Filters annotation literals by a given string.
     *
     * @return RDF Model with Subjects with matched literals and their RDF:types.
     */
    public Model getAnnotations(String literalFilter) {
        if (literalFilter.length() > 0) {
            String qs = StrUtils.strjoinNL(
                    "CONSTRUCT { ",
                    "?s rdf:type ?t .",
                    "?s ?p ?o",
                    "}",
                    "WHERE { ",
                    "?s rdf:type ?t .",
                    "?s ?p ?o . ",
                    "FILTER (",
                    "(STR(?p) != rdf:type) && ",
                    "isLiteral(?o) && ",
                    "regex(?o, '" + literalFilter + "', 'i')",
                    ")}"
            );

            return query.ExecConstruct(QueryHelper.stdPrefix + "\n" + qs);
        } else {
            return getAnnotations();
        }
    }

    /**
     * Creates a new model with inferred relations based on loaded ontology,
     * annotations and reasoner.
     *
     * @return Model with inferred relations
     */
    public InfModel getAnnotationsWithInference() {
        return ModelFactory.createInfModel(getReasoner(), annotations);
    }

    /**
     * Creates a new reasoner based on actual schema and annotations.
     *
     * @return Reasoner
     */
    public Reasoner getReasoner() {
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        return reasoner.bindSchema(schema);
    }

    /**
     * Returns available types (resources) in the current annotations.
     *
     * @return List<Resource>
     */
    public List<Resource> getAvailableTypes() {
        return getAnnotations().listObjectsOfProperty(RDF.type).toList()
                .stream()
                .map(RDFNode::asResource)
                .filter(r -> !r.getNameSpace().equals(OWL.getURI()))
                .collect(Collectors.toList());
    }

    public void importMetadata(String path) {
        Model newData =  RDFDataMgr.loadModel(path);
        annotations.add(newData);
    }


    public void dummy() {

        OntClass cls = schema.getOntClass("all-foos");
        String somePrefix = schema.getNsPrefixMap().get("foaf");

        Resource parent = annotations.getResource("foo");
        Resource child = annotations.getResource("bar");
        Property p = annotations.getProperty("property");

        // remove object property between parent & child Resources

        Optional<Statement> rem = parent.listProperties(p)
                .toList().stream().filter(st -> st.getObject().asResource().equals(child))
                .findFirst();
        if (rem.isPresent()) {
            rem.get().remove();
        }

        // remove resource

        Model toRemove = ModelFactory.createDefaultModel();
        toRemove.add(annotations.listStatements(parent, null, (RDFNode) null));
        toRemove.add(annotations.listStatements(null, null, parent));

        annotations.remove(toRemove);

        // create typed literal and add it to model

        Literal o = ResourceFactory.createTypedLiteral("42", XSDDatatype.XSDint);
        parent.addLiteral(p, o);

        // get datatypes of property

        List<? extends OntResource> types = schema.getOntProperty(p.getURI()).listRange().toList();
        for (int i=1; i < types.size(); i++) {
            types.get(i).equals(XSD.date);  // is date
            types.get(i).equals(XSD.integer);  // is int
            // etc.
        }

        // all classes;

        schema.listClasses();

        // class of a particular resource

        schema.getOntClass(parent.getProperty(RDF.type).getObject().asResource().getURI());

        // allowed connections for a particular class

        List<OntClass> range = new ArrayList<>();
        parent.listProperties(RDF.type).toList().forEach(st -> {
            OntClass dt = schema.getOntClass(st.getObject().asResource().getURI());
            dt.listDeclaredProperties().toList().forEach(prop -> {
                prop.listRange().toList().forEach(ontRes -> {
                    range.add(schema.getOntClass(ontRes.getURI()));
                });
            });
        });

        // get all DataProperties for a resource

        OntClass foo = schema.getOntClass(parent.getProperty(RDF.type).getObject().asResource().getURI());
        List<OntProperty> dataProperties = foo.listDeclaredProperties()
                .toList().stream().filter(OntProperty::isDatatypeProperty)
                .collect(Collectors.toList());

        // get all still addable DataProperties for an Instance

        List<OntProperty> possibleDataProperties = foo.listDeclaredProperties()
                .toList().stream().filter(OntProperty::isDatatypeProperty)
                .filter(pr -> !pr.isFunctionalProperty() || !annotations.contains(parent, pr))
                .collect(Collectors.toList());

        // get the Label DataProperty for a resource

        Statement labelSt = parent.getProperty(RDFS.label);
        Optional<String> label = labelSt == null ? Optional.ofNullable(null) : Optional.of(labelSt.getObject().asLiteral().getString());

        // add new instance + DataProperties

        Model newObject = ModelFactory.createDefaultModel();

        Resource res = ResourceFactory.createResource(UUID.randomUUID().toString());

        newObject.add(res, RDF.type, cls);
        newObject.add(res, p, "some value");
        newObject.add(res, p, "some other value");

        annotations.add(newObject);

    }
    /**
     * Creates a new Metadata Service using a given path. Combines existing
     * project RDF schemas (ontology files) and metadata storage (annotations)
     * into a common Model. Creates default schemas if some do not exist.
     *
     * @return MetadataService
     */
    public static MetadataService create(String projectPath) throws IOException {
        if (projectPath == null) {
            throw new IOException("Cannot create metadata service at non-existing path");
        }

        MetadataFilesManager metaFiles = new MetadataFilesManager(projectPath);

        OntModel schema = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);

        // TODO figure out if using OntDocumentManager to read files makes sense
        for (Path p : metaFiles.schemaPaths()) {
            schema.addSubModel(RDFDataMgr.loadModel(p.toString()));
        }

        Model data = RDFDataMgr.loadModel(metaFiles.annotationsPath().toString());

        return new MetadataService(schema, data);
    }

}
