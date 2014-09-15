// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.lib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.*;

/**
 * Base class for configurations.
 * The class provides methods for de-/serialisation, loading and saving of configuration
 * classes. Configuration classes should be beans.
 */
public abstract class AbstractConfig {

    transient private Path filePath;

    public Path getFilePath() {
        return filePath;
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath.toAbsolutePath();
    }

    /**
     * Serializes the config to json and writes it to the specified file.
     *
     * @throws IOException If the storing of the configuration failed.
     */
    public void store() throws IOException {
        Files.createDirectories(filePath.getParent());
        try (Writer out = Files.newBufferedWriter(filePath, TRUNCATE_EXISTING, CREATE)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            String json = gson.toJson(this);
            out.write(json);
        } catch (IOException e) {
            throw new IOException("Unable to write configuration file: " + this.filePath, e);
        }
    }

    /**
     * Loads a configuration of a certain type from a json file.
     * Note: this method should be used by subclasses in order to implement
     * a more specific load method.
     *
     * @param filePath      Path to the json configuration file to read from.
     * @param cls           The class of the configuration type.
     *
     * @throws IOException
     */
    protected static <T extends AbstractConfig> T load(Path filePath, Class<T> cls) throws IOException {
        try (Reader in = Files.newBufferedReader(filePath)) {
            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .create();
            T  config = gson.fromJson(in, cls);
            config.setFilePath(filePath);
            return config;
        } catch (IOException e) {
            throw new IOException("Unable to read configuration file: " + filePath, e);
        }
    }
}
