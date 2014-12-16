// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.lib.srv;

/**
 * Provides adapter for accessing RDF resources via notes favorites
 */
public class NotesFavoritesResourceAdapter {

    private final String id;
    private final String rdfType;
    private final String value;
    private final Boolean useFavorite;

    public NotesFavoritesResourceAdapter(String id, String rdfType, String value, Boolean useFavorite) {
        this.id = id;
        this.rdfType = rdfType;
        this.value = value;
        this.useFavorite = useFavorite;
    }

    public String getId() { return this.id; }

    public String getRdfType() { return this.rdfType; }

    public String getValue() { return this.value; }

    public Boolean use() { return this.useFavorite; }
}
