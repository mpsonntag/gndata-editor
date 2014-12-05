// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.app.ui.query;


import javafx.scene.web.WebView;

import gndata.app.state.QueryState;


public class TextPane {

    public static WebView getInstance(QueryState qs) {
        WebView wv = new WebView();

        qs.getSelectedNode().addListener((obs, odlVal, newVal) ->
            wv.getEngine().loadContent(
                    String.join("",
                            "<html><body><p>",
                            newVal.toString(),
                            "</p></body></html>"
                    )
            )
        );

        return wv;
    }
}