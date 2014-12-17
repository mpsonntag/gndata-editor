// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.lib.srv;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class manages how notes entities are handled.
 */
public class NotesAdapter {

    private final String notesID;
    private final LocalDateTime dateCreated;
    private final String author;
    private final String title;
    private final String content;

    public NotesAdapter(String notesID, LocalDateTime dateCreated, String author, String title, String content) {
        this.notesID = notesID;
        this.dateCreated = dateCreated;
        this.author = author;
        this.title = title;
        this.content = content;
    }

    /**
     * This method returns the title of the current note.
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method returns creation date and author of the current note.
     */
    public String getDateAuthor() {

        return String.format("%s, Author: %s", getFormattedDate("yyyy-MM-dd HH:mm"), author);
    }

    /**
     * This method returns the content of the current note.
     */
    public String getContent() {
        return content;
    }

    public String getInfo() {
        String ls = System.getProperty("line.separator");
        return String.format("%s, Author: %s%s%s%s%s%s", getFormattedDate("yyyy-MM-dd HH:mm"), author, ls, title, ls, content, ls);
    }

    public String getFormattedDate(String formatPattern) {
        return dateCreated.format(DateTimeFormatter.ofPattern(formatPattern));
    }
}
