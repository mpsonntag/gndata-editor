package gndata.lib.util;

import java.util.*;
import java.util.stream.*;

import static java.util.Spliterator.*;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.iterator.*;
import com.hp.hpl.jena.vocabulary.*;

/**
 * Utility methods for handling of {@link Resource} entities.
 */
public class Resources {

    private static int characteristics = DISTINCT | NONNULL;

    /**
     * Gets a human readable name for a {@link Resource}. If the resource is a class
     * the name will be the class name. Otherwise the method will try to use {@link RDFS#label}
     * as name. If this fails the name will be the class name combined with the first letters
     * of the {@link Resource#getLocalName()} of the resource.
     *
     * @param resource The resource to get the name for.
     *
     * @return A human readable name for the resource.
     */
    public static String toNameString(Resource resource) {
        String name;

        if (resource.hasProperty(RDF.type, OWL.Class)) {
            name = resource.getLocalName();
        } else {
            if (resource.hasProperty(RDFS.label)) {
                name = resource.getProperty(RDFS.label).getLiteral().toString();
            } else {
                String type = resource.getProperty(RDF.type).getResource().getLocalName();
                String id = resource.getLocalName().substring(0, 7);
                name = String.format("%s: %s", type, id);
            }
        }

        return name;
    }

    /**
     * Get a string that provides additional human readable information about a resource.
     * This can be used as additional textual information in tooltips or list cells.
     *
     * @param resource The resource to get the info string for.
     *
     * @return Human readable information about the resource.
     */
    public static String toInfoString(Resource resource) {
        // TODO implement
        return "Resource info";
    }


    public static Stream<Statement> streamLiteralsFor(Resource resource) {
        Iterator<Statement> it = resource.listProperties().filterKeep(new LiteralFilter());

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, characteristics), false);
    }


    public static List<Statement> listLiteralsFor(Resource resource) {
        return streamLiteralsFor(resource).collect(Collectors.toList());
    }

    public static Stream<Resource> streamResourcesFor(Resource resource) {
        ExtendedIterator<Statement> it;

        if (resource.hasProperty(RDF.type, OWL.Class)) {
            it = resource.getModel().listStatements(null, RDF.type, resource);
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, characteristics), false)
                    .sorted(new StatementComparator())
                    .map(Statement::getSubject);
        } else {
            it = resource.listProperties().filterKeep(new ResourceFilter());
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, characteristics), false)
                    .sorted(new StatementComparator())
                    .map(stmt -> stmt.getObject().asResource());
        }
    }

    public static List<Resource> listResourcesFor(Resource resource) {
        return streamResourcesFor(resource).collect(Collectors.toList());
    }


    /**
     * Filters statements with literal objects.
     */
    private static class LiteralFilter extends Filter<Statement> {

        @Override
        public boolean accept(Statement stmt) {
            return stmt.getObject().isLiteral();
        }
    }

    /**
     * Filters statements with resource object that ar not blank nodes and not types.
     */
    private static class ResourceFilter extends Filter<Statement> {

        @Override
        public boolean accept(Statement stmt) {
            RDFNode o = stmt.getObject();

            return o.isResource() && (! o.isAnon()) && (! stmt.getPredicate().equals(RDF.type));
        }
    }
}
