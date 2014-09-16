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
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.*;

/**
 * Base class for configurations.
 * The class provides methods for de-/serialisation, loading and saving of configuration
 * classes. Configuration classes should be beans.
 */
public abstract class AbstractConfig {

    private String filePath;

    /**
     * Getter for the path to the configuration file from which the configuration was loaded.
     *
     * @return The path to the configuration file.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Setter for the path to the configuration file.
     *
     * @param filePath The path to the configuration file.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Serializes the config to json and writes it to a file. The method  uses {@link #getFilePath()}
     * for storing the configuration.
     *
     * @throws IOException If the storing of the configuration failed.
     */
    public void store() throws IOException {
        Path tmpPath = Paths.get(filePath);
        Files.createDirectories(tmpPath.getParent());

        try (Writer out = Files.newBufferedWriter(tmpPath, TRUNCATE_EXISTING, CREATE)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .serializeNulls()
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
    protected static <T extends AbstractConfig> T load(String filePath, Class<T> cls) throws IOException {
        Path tmpPath = Paths.get(filePath)
                .toAbsolutePath()
                .normalize();

        try (Reader in = Files.newBufferedReader(tmpPath)) {
            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .create();
            T  config = gson.fromJson(in, cls);
            config.setFilePath(tmpPath.toString());
            return config;
        } catch (IOException e) {
            throw new IOException("Unable to read configuration file: " + filePath, e);
        }
    }
}
