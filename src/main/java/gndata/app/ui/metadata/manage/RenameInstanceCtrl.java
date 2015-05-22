// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata.manage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.*;

import gndata.app.state.*;

/**
 * Controller for RenameInstance.fxml
 * Used to update the label of an RDF class instance
 */
public class RenameInstanceCtrl extends Pane implements Initializable {

    private final MetadataNavState navState;
    private final Stage st = new Stage();

    private final DoubleProperty paneWidth;
    private final ObjectProperty<Insets> paddingInsets;
    private final StringProperty renameLabel;
    private final StringProperty renameValue;
    private final StringProperty promptValue;


    public RenameInstanceCtrl(MetadataNavState navState) {

        this.navState = navState;

        // construct properties
        paneWidth = new SimpleDoubleProperty();
        // padding insets are required here, since the label padding attribute
        // cannot be set in the fxml file w/o eliciting a javafx.fxml.LoadException
        paddingInsets = new SimpleObjectProperty<>(new Insets(5));
        renameLabel = new SimpleStringProperty();
        renameValue = new SimpleStringProperty();
        promptValue = new SimpleStringProperty();

        // load corresponding FXML and display contents in popup stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RenameInstance.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        st.setScene(new Scene(this));
        st.sizeToScene();
        st.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set application modality to prohibit actions
        // while the add object property window is open.
        st.initModality(Modality.APPLICATION_MODAL);

        final String parent = navState.getSelectedParent().getFileName();
        st.setTitle("Rename " + parent);
        renameLabel.set("Rename " + parent + ":");
        renameValue.set(parent);

        // set the width of the window dependent
        // on the length of the label
        Text text = new Text(renameLabel.get());
        double useWidth = text.getLayoutBounds().getWidth() + paddingInsets.get().getLeft() * 4;
        paneWidth.set(useWidth < 190 ? 190 : useWidth);
    }


    // -------------------------------------------
    // FXML binding properties
    // -------------------------------------------

    public final DoubleProperty paneWidthProperty() { return paneWidth; }
    public final ObjectProperty<Insets> paddingInsetsProperty() { return paddingInsets; }
    public final StringProperty renameLabelProperty() { return renameLabel; }
    public final StringProperty renameValueProperty() { return renameValue; }
    public final StringProperty promptValueProperty() { return promptValue; }


    // -----------------------------------------
    // Methods
    // -----------------------------------------

    /**
     * Discard changes, if the cancel button is used
     */
    public void cancel() {
        st.hide();
    }

    /**
     * Update the RDF label of the selected parent,
     * if a value has been provided
     */
    // TODO check, if all classes actually support RDFS label or if owl:name has to be used
    // TODO implement logic to update the labels of the navigation bar after label has been set
    // how should a refresh actually be done:
    // we directly tap into the resource adapter. the resource adapter is also registered
    // in the navigation bar. should we add an observable (resourceContentChange) to the resource adapter, which
    // state changes, when we update the label. if a resource adapter is added to the navigation bar,
    // we also register a listener to this particular observable of the resource adapter
    // if the state of the observable of the resource adapter changes, the navigation bar
    // calls the private updateButtons method to redraw the whole bar.
    public void rename() {
        if(renameValue.getValue() != null && !renameValue.getValue().isEmpty()) {
            navState.getSelectedParent().updateLabel(renameValue.getValue());
            st.hide();
        } else {
            promptValue.set("Please enter a name");
        }
    }

}
