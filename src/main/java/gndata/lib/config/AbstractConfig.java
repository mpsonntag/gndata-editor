// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.lib.config;

import java.io.IOException;
import java.nio.file.*;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;
import static com.fasterxml.jackson.databind.SerializationFeature.*;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Base class for configurations.
 * The class provides methods for de-/serialisation, loading and saving of configuration
 * classes. Configuration classes should be beans.
 */
public abstract class AbstractConfig {

    private String filePath;

    /**
     * Default constructor.
     */
    public AbstractConfig() {}

    /**
     * Copy constructor.
     *
     * @param other The config to copy.
     */
    public AbstractConfig(AbstractConfig other) {
        filePath = other.filePath;
    }

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

        try {
            ObjectMapper mapper = new ObjectMapper()
                    .enable(INDENT_OUTPUT)
                    .disable(FAIL_ON_EMPTY_BEANS);

            mapper.writeValue(tmpPath.toFile(), this);
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
        Path tmpPath = Paths.get(filePath);
        ObjectMapper mapper = new ObjectMapper()
                .enable(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .enable(ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .disable(FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            return mapper.readValue(tmpPath.toFile(), cls);
        } catch (IOException e) {
            throw new IOException("Unable to read configuration file: " + filePath, e);
        }
    }
}
