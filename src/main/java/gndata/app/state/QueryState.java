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

    private StringProperty currentQuery;
    private ObjectProperty<Statement> selectedStatement;
    private ObjectProperty<Model> selectedModel;

    private ObservableList<String> queryHistory;  // mockup for the future <-->

    public QueryState() {
        currentQuery = new SimpleStringProperty();
        selectedStatement = new SimpleObjectProperty<>();
        selectedModel = new SimpleObjectProperty<>();
    }

    public String getCurrentQuery() {
        return currentQuery.get();
    }
    public StringProperty currentQueryProperty() {
        return currentQuery;
    }

    public void setCurrentQuery(String query) {
        this.currentQuery.set(query);
    }

    public Statement getSelectedStatement() {
        return selectedStatement.get();
    }

    public ObjectProperty<Statement> selectedStatementProperty() {
        return selectedStatement;
    }

    public void setSelectedStatement(Statement selectedStatement) {
        this.selectedStatement.set(selectedStatement);
    }

    public Model getSelectedModel() {
        return selectedModel.get();
    }

    public ObjectProperty<Model> selectedModelProperty() {
        return selectedModel;
    }

    public void setSelectedModel(Model model) {
        this.selectedModel.set(model);
    }
}
