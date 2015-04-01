package gndata.lib.util;

import java.util.*;
import java.util.stream.*;

import static java.util.Spliterator.*;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import org.apache.jena.atlas.lib.StrUtils;

/**
 * A helper for building and executing RDF queries.
 */
public class QueryHelper {

    public static final String stdPrefix = StrUtils.strjoinNL(
            "PREFIX rdf: <" + RDF.getURI() + ">",
            "PREFIX rdfs: <" + RDFS.getURI() + ">",
            "PREFIX owl: <" + OWL.getURI() + ">"
    );

    private Model model;

    public QueryHelper(Model m) {
        this.model = m;
    }

    /**
     * Provides a string with available PREFIX'es.
     * May be used to build queries for current model.
     *
     * @return  String with prefixes
     */
    public String getPrefixHeader() {
        return StrUtils.strjoinNL(this.model
                .getNsPrefixMap()
                .entrySet()
                .stream()
                .map(a -> "PREFIX " + a.getKey() + ": " + "<" + a.getValue() + ">")
                .collect(Collectors.toList()));
    }

    /**
     * Fulltext search on literal values inside the metadata model.
     *
     * @param search The search parameter.
     *
     * @return A stream of resources with a property having a part of the
     *         search string as literal value.
     */
    public Stream<Resource> streamSearchResults(String search) {
        String qs = StrUtils.strjoin("",
                "SELECT DISTINCT ?s ",
                "WHERE {",
                "  ?s ?p ?o ",
                "  FILTER regex(?o, '", search, "', 'i')}"
        );

        Query query = QueryFactory.create(qs);
        ResultSet rs = QueryExecutionFactory.create(query, this.model).execSelect();

        String var = rs.getResultVars().get(0);
        Stream<QuerySolution> stream =StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(rs, NONNULL | IMMUTABLE | DISTINCT), false);

        return stream.map(sol -> sol.getResource(var));
    }

    public ResultSet ExecSelect(String queryString) {
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, this.model);

        ResultSet resultModel = ResultSetFactory.copyResults(qexec.execSelect());
        qexec.close();

        return resultModel;
    }

    public Model ExecConstruct(String queryString) {
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, this.model);

        Model resultModel = qexec.execConstruct();
        qexec.close();

        return resultModel;
    }
}
