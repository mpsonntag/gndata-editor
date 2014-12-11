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
                String type;
                Statement typeStmt = resource.getProperty(RDF.type);

                if (typeStmt != null) {
                    type = typeStmt.getResource().getLocalName();
                } else {
                    type = "Thing";
                }

                String id = resource.getLocalName();
                id = id.length() < 8 ? id : id.substring(0, 7);

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
        long litCount = streamLiteralsFor(resource).count();
        long relCount = streamResourcesFor(resource).count();
        return String.format("Relations: %d, Literals: %d", relCount, litCount);
    }

    /**
     * Streams all statements containing literal values as object for a certain resource.
     *
     * @param resource The resource to get the literals for.
     *
     * @return A stream with literals.
     */
    public static Stream<Statement> streamLiteralsFor(Resource resource) {
        Iterator<Statement> it = resource.listProperties().filterKeep(new LiteralFilter());

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, characteristics), false);
    }

    /**
     * Streams all statements containing literal values as object for a certain resource.
     * This is equivalent to {@link #streamLiteralsFor(Resource)}.
     *
     * @param resource The resource to get the literals for.
     *
     * @return A list with literals.
     */
    public static List<Statement> listLiteralsFor(Resource resource) {
        return streamLiteralsFor(resource).collect(Collectors.toList());
    }

    /**
     * Streams all resources directly related to the given resource. Blank nodes and
     * Resources that represent a type are ignored. If the resource itself is a {@link OWL#Class}
     * the method will return all instances of this class.
     *
     * For instance resources the method also resolves reverse relationships.
     *
     * @param resource The resource to get the related resources for.
     *
     * @return A stream with related resources.
     */
    public static Stream<Resource> streamResourcesFor(Resource resource) {
        ExtendedIterator<Statement> it;

        if (resource.hasProperty(RDF.type, OWL.Class)) {
            it = resource.getModel().listStatements(null, RDF.type, resource);
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, characteristics), false)
                    .sorted(new StatementComparator())
                    .map(Statement::getSubject);
        } else {
            // TODO reverse relationships should be resolved via reasoner once the ontology supports this
            it = resource.listProperties().filterKeep(new ResourceFilter());
            Stream<Statement> forward = StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(it, characteristics), false);

            it = resource.getModel().listStatements(null, null, resource).filterKeep(new ResourceFilter());
            Stream<Statement> reverse = StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(it, characteristics), false);

            return Stream.concat(forward, reverse)
                    .sorted(new StatementComparator())
                    .map(stmt -> stmt.getSubject().equals(resource) ? stmt.getObject().asResource() : stmt.getSubject());
        }
    }

    /**
     * Streams all resources directly related to the given resource. This method is the list
     * equivalent for {@link #streamResourcesFor(Resource)}.
     *
     * @param resource The resource to get the related resources for.
     *
     * @return A list with related resources.
     *
     * @see #streamResourcesFor(Resource)
     */
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
