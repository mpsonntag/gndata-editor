// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.lib.srv;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import javafx.scene.image.Image;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

/**
 * Class implementing methods for handling local files and folders
 * giving access to information like file size or mime type.
 */
public class LocalFile extends FileAdapter<LocalFile> {

    private Path path;
    private Optional<LocalFile> parent;

    public LocalFile(Path path) {
        this.path = path.toAbsolutePath().normalize();
    }

    public LocalFile(String path) {
        this(Paths.get(path));
    }

    /**
     * Method returns true, if the absolute path of the LocalFile
     * is the same as the absolute path of another path
     */
    public boolean hasPath(Path otherPath) {
        return path.equals(otherPath.toAbsolutePath().normalize());
    }

    /**
     * Method returns path of the current LocalFile
     */
    public Path getPath() { return path; }

    /**
     * Method returns true, if the absolute path of the LocalFile
     * starts with the absolute path of another path
     */
    public boolean isChildOfAbsolutePath(Path otherPath) {
        return path.startsWith(otherPath.toAbsolutePath().normalize());
    }

    /**
     * Method returns true, if a folder or file is hidden
     * To stay platform independent create a correct file from string and use
     * isHidden method.
     */
    public boolean isHidden() {
        File checkHidden = new File(path.toString());
        return checkHidden.isHidden();
    }

    @Override
    public Optional<LocalFile> getParent() {
        if (parent == null) {
            parent = path.getParent() == null ? Optional.empty() : Optional.of(new LocalFile(path.getParent()));
        }
        return parent;
    }

    @Override
    public List<LocalFile> getChildren() {
        List<LocalFile> list = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {

            stream.forEach(p -> list.add(new LocalFile(p)));

        } catch(Exception e) {
            e.printStackTrace();
        }

        list.sort((a, b) -> a.compareTo(b));
        return list;
    }

    @Override
    public boolean isDirectory() {
        return Files.isDirectory(path);
    }

    @Override
    public String getFileName() {
        return path.getFileName().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof LocalFile))
            return false;

        LocalFile localFile = (LocalFile) o;
        return path.equals(localFile.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    /**
     * Returns the byte size of the current LocalFile, if it is not a directory.
     */
    public long getSizeInBytes() {
        try {
            return Files.size(path);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Returns the size of the current LocalFile in readable form
     * if it is not a directory.
     */
    public String getSizeReadable() {
        if (!this.isDirectory()) {
            return humanReadableByteCount(this.getSizeInBytes(), true);
        } else {
            return "Directory";
        }
    }

    /**
     * Returns the contents of the current LocalFile, if it is a directory.
     *
     * @return formatted String containing "Directories: " #directories ", Files: " #files
     */
    public String getFolderContent() {

        long files = 0;
        long dir = 0;

        if(Files.isDirectory(path)){
            try {
                files = Files.list(new File(path.toString()).toPath())
                                .filter(p -> !p.toFile().isDirectory())
                                .count();
                dir = Files.list(new File(path.toString()).toPath())
                                .filter(p -> p.toFile().isDirectory())
                                .count();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return String.format("Directories: %d, Files: %d ", dir, files);
    }

    /**
     * This method returns the MimeType of the current LocalFile, if it is not a directory.
     * In case no MimeType can be identified, "application/octet-stream" is returned.
     *
     * @return detected MimeType as String
     */
    public String getMimeType() {

        String returnString = "application/octet-stream";

        if(!Files.isDirectory(path)) {
            TikaConfig tc = TikaConfig.getDefaultConfig();
            Detector detector = tc.getDetector();

            try (TikaInputStream stream = TikaInputStream.get(new FileInputStream(new File(path.toString())))) {
                Metadata metadata = new Metadata();
                metadata.add(Metadata.RESOURCE_NAME_KEY, path.getFileName().toString());
                MediaType mediaType = detector.detect(stream, metadata);
                returnString = mediaType.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return returnString;
    }

    /**
     * This method returns additional information like MimeType, file size or folder content
     * dependent on if the current item is a file or a directory.
     */
    public String getInfo() {
        String info;

        if (this.isDirectory()){
            info = this.getFolderContent();
        } else {
            info = this.getMimeType() +" "+ humanReadableByteCount(this.getSizeInBytes(), true);
        }
        return info;
    }

    /**
     * This method returns different images dependent on whether the current item is a file or a directory
     */
    public Image getIcon() {
        String selectIcon;

        if (this.isDirectory()){
            selectIcon = "icons/folder.png";
        } else {
            selectIcon = "icons/txt.png";
        }

        return new Image(ClassLoader.getSystemResource(selectIcon).toString());
    }

    /**
     * Returns a number of input bytes in readable format
     *
     * @param bytes: number of bytes to be displayed in readable format
     * @param si: use true, if basis for byte conversion should be 1000, false if basis should be 1024
     * @return bytes as formatted String
     */
    private static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit)
            return bytes + " B";

        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");

        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
