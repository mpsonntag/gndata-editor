// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.state;

import javax.inject.Singleton;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Class that provides state information about metadata selections, changes and
 * other related information.
 */
@Singleton
public class QueryState {

    private ObjectProperty<Statement> selectedStatement;
    private ObjectProperty<String> currentQuery;
    private ObjectProperty<Model> selectedModel;

    private ObservableList<String> queryHistory;  // mockup for the future <-->

    public QueryState() {
        selectedStatement = new SimpleObjectProperty<>();
        currentQuery = new SimpleObjectProperty<>();
        selectedModel = new SimpleObjectProperty<>();
    }

    public ObjectProperty<Statement> getSelectedStatement() {
        return selectedStatement;
    }

    public void setSelectedStatement(Statement selectedStatement) {
        this.selectedStatement.set(selectedStatement);
    }

    public ObjectProperty<String> getCurrentQuery() {
        return currentQuery;
    }

    public void setCurrentQuery(String query) {
        this.currentQuery.set(query);
    }

    public ObjectProperty<Model> getSelectedModel() {
        return selectedModel;
    }

    public void setSelectedModel(Model model) {
        this.selectedModel.set(model);
    }
}
