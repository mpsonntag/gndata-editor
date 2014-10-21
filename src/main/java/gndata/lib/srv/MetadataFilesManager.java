package gndata.lib.srv;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class manages locations and initialization of the metadata-related
 * files within a certain file system location.
 */
public class MetadataFilesManager {

    private String basePath;

    public static final Path SCHEMAS_FOLDER         = Paths.get("metadata", "schemas");
    public static final Path SCHEMAS_GNODE_FILE     = Paths.get("gnode.owl");
    public static final Path SCHEMAS_PROV_FILE      = Paths.get("prov.owl");
    public static final Path SCHEMAS_CUSTOM_FILE    = Paths.get("custom.owl");

    public static final Path ANNOTATIONS_FOLDER     = Paths.get("metadata", "annotations");
    public static final Path ANNOTATIONS_FILE       = Paths.get("metadata.rdf");

    public MetadataFilesManager(String basePath) {
        this.basePath = basePath;
    }

    /**
     * Returns a list of paths to the RDF schemas (OWL, RDFS etc.) used in the
     * current project. Creates default schemas and an empty file for custom RDF
     * terms if any of them do not exist.
     *
     * @return  paths to the schema files
     * @throws IOException
     */
    public List<Path> schemaPaths() throws IOException {
        Path schemasFolderPath = getSchemasFolderPath();

        copyIfNotExist(schemasFolderPath, SCHEMAS_GNODE_FILE.toString());
        copyIfNotExist(schemasFolderPath, SCHEMAS_PROV_FILE.toString());
        copyIfNotExist(schemasFolderPath, SCHEMAS_CUSTOM_FILE.toString());

        File schemasFolder = schemasFolderPath.toFile();

        List<Path> paths = new ArrayList<>();
        Arrays.asList(schemasFolder.listFiles())
                .forEach(a -> paths.add(a.toPath()));

        return paths;
    }

    /**
     * Returns a path to the RDF file with actual annotations for the
     * current project. Creates an empty RDF file and appropriate folder
     * structure if any of them do not exist.
     *
     * @return  path to the annotations file
     * @throws IOException
     */
    public Path annotationsPath() throws IOException {
        Path annotationsPath = getAnnotationsFolderPath();

        copyIfNotExist(annotationsPath, ANNOTATIONS_FILE.toString());

        return annotationsPath.resolve(ANNOTATIONS_FILE);
    }

    private Path getSchemasFolderPath() throws IOException {
        return resolveFolder(SCHEMAS_FOLDER);
    }

    private Path getAnnotationsFolderPath() throws IOException {
        return resolveFolder(ANNOTATIONS_FOLDER);
    }

    private Path resolveFolder(Path folder) throws IOException {
        Path absPath  = Paths.get(basePath).resolve(folder);

        Files.createDirectories(absPath);

        return absPath;
    }

    private void copyIfNotExist(Path where, String fileName) throws IOException {
        Path absPath = where.resolve(Paths.get(fileName));

        if (!Files.exists(absPath)) {
            String pathLocal = "/templates/" + fileName;
            InputStream schema = getClass().getResourceAsStream(pathLocal);

            Files.copy(schema, absPath);
        }
    }
}
