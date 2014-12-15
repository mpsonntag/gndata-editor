// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.state;

import javax.inject.Singleton;
import javafx.collections.*;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Class providing the state information about notes favorites
 * and notes filtering
 */
@Singleton
public class NotesState {

    private final ObservableList<Resource> selectedFavorites;

    public NotesState() {
        selectedFavorites = FXCollections.observableArrayList();
    }

    public ObservableList<Resource> getSelectedFavNotes() { return selectedFavorites;}

}
