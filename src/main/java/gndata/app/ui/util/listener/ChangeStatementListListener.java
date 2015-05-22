// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.util.listener;

import javafx.collections.ListChangeListener;

import com.hp.hpl.jena.rdf.model.*;
import gndata.app.ui.util.StatementTableItem;

/**
 * Listener class facilitating updates and deletions
 * to and from the RDF model upon changes in the registered list
 */
public class ChangeStatementListListener implements ListChangeListener<StatementTableItem> {

    private boolean enabled = true;

    @Override
    public void onChanged(Change<? extends StatementTableItem> c) {
        if(enabled) {
            while (c.next()) {
                if (c.wasReplaced()) {
                    // update statement in RDF model
                    Statement rmstmt = c.getRemoved().get(c.getRemovedSize() - 1).getStatement();
                    Statement addstmt = c.getAddedSubList().get(c.getAddedSize() - 1).getStatement();

                    Model parentModel = rmstmt.getModel();

                    parentModel.remove(rmstmt);
                    parentModel.add(addstmt);

                } else if (c.wasRemoved()) {
                    // remove statement from RDF model
                    StatementTableItem remItem = c.getRemoved().get(c.getRemovedSize() - 1);
                    remItem.getStatement().getModel().remove(remItem.getStatement());

                }
            }
        }
    }

    public void setEnabled() { this.enabled = true; }

    public void setDisabled() { this.enabled = false; }
}
