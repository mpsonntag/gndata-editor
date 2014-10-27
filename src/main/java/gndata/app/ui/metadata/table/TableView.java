// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.metadata.table;

import com.google.inject.*;
import gndata.app.ui.util.DIView;

/**
 * Table view for the selected metadata item.
 */
public class TableView extends DIView {

    @Inject
    public TableView(Injector injector) {
        super(injector);
    }

}
