// Copyright (c) 2014, German Neuroinformatics Node (G-Node)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package gndata.lib.srv;

import java.io.File;
import java.nio.file.*;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.*;


public class LocalFileTest {

    private static final String tmpRoot = System.getProperty("java.io.tmpdir");
    private static final String testFolderName = "localfiletest";
    private static final String testFileName = "test.txt";

    private static final Path testFileFolder = Paths.get(tmpRoot, testFolderName);
    private static final Path testParallelFolder = Paths.get(tmpRoot, "parallel");
    private static final Path testFileFolderChild = testFileFolder.resolve("childdir");
    private static final Path testLocalFilePath = testFileFolder.resolve(testFileName);


    LocalFile localFile;
    LocalFile localHiddenFile;
    LocalFile localDir;

    @Before
    public void setUp() throws Exception {
        // create directories
        File testDirs = testFileFolderChild.toFile();
        FileUtils.forceMkdir(testDirs);
        File parallelDir = testParallelFolder.toFile();
        FileUtils.forceMkdir(parallelDir);

        // create testfiles
        File testFile = testFileFolder.resolve(testFileName).toFile();
        FileUtils.write(testFile, "This is a normal test file");

        File testHiddenFile = testFileFolder.resolve("." + testFileName).toFile();
        FileUtils.write(testHiddenFile, "This is a hidden test file");

        //Set hidden attribute for Win OS systems
        if ( SystemUtils.IS_OS_WINDOWS ) {
            Files.setAttribute(Paths.get(testHiddenFile.getPath()), "dos:hidden", true);
        }

        localFile = new LocalFile(testLocalFilePath);

        localHiddenFile = new LocalFile(testFileFolder.resolve("." + testFileName));

        localDir = new LocalFile(testFileFolder);
    }

    @After
    public void tearDown() throws Exception {
        if (Files.exists(testFileFolder)) {
            FileUtils.deleteDirectory(testFileFolder.toFile());
        }
    }

    @Test
    public void testGetPath() throws Exception {
        Path filePath = localFile.getPath();
        assertThat(filePath.toString()).isNotEmpty();
        assertThat(filePath).isAbsolute();
    }

    @Test
    public void testHasPath()throws Exception {
        assertThat(localFile.hasPath(testLocalFilePath));
        assertThat(!localFile.hasPath(testFileFolderChild));
    }

    @Test
    public void testIsChildOfAbsolutePath() throws Exception {
        assertThat(localFile.isChildOfAbsolutePath(testFileFolder));
        assertThat(!localFile.isChildOfAbsolutePath(testParallelFolder));
    }

    //TODO test under windows
    @Test
    public void testIsHidden() throws Exception {
        assertThat(!localFile.isHidden());
        assertThat(localHiddenFile.isHidden());
    }

    @Test
    public void testGetParent() throws Exception {
        Optional<LocalFile> currParent = localFile.getParent();
        assertThat(currParent.isPresent());
        assertThat(currParent.get().hasPath(testFileFolder));
    }

    @Test
    public void testGetChildren() throws Exception {
        List<LocalFile> currChildren = localDir.getChildren();
        assertThat(currChildren.size()).isEqualTo(3);

        assertThat(currChildren.get(0).hasPath(testFileFolderChild));
    }

    @Test
    public void testGetFileName() throws Exception {
        assertThat(localFile.getFileName().equals(testFileName));
        assertThat(localDir.getFileName().equals(testFolderName));
    }

    @Test
    public void testEquals() throws Exception {
        LocalFile isEqualFile = new LocalFile(testFileFolder.resolve(testFileName));
        assertThat(localFile).isEqualTo(isEqualFile);
        assertThat(localFile).isNotEqualTo(localHiddenFile);
    }

    @Test
    public void testHashCode() throws Exception {
        assertThat(localFile.hashCode()).isNotNull();
    }

    @Test
    public void testGetSizeInBytes() throws Exception {
        assertThat(localFile.getSizeInBytes()).isEqualTo(26);
    }

    @Test
    public void testGetSizeReadable() throws Exception {
        assertThat(localFile.getSizeReadable().contentEquals("26 B"));
        assertThat(localDir.getSizeReadable().contentEquals("Directory"));
    }

    @Test
    public void testGetFolderContent() throws Exception {
        assertThat(localDir.getFolderContent().contentEquals("Directories: 1, Files: 2"));
        assertThat(localFile.getFolderContent().contentEquals("Directories: 0, Files: 0"));
    }

    @Test
    public void testGetMimeType() throws Exception {
        assertThat(localFile.getMimeType().contentEquals("text/plain"));
    }

    @Test
    public void testGetInfo() throws Exception {
        assertThat(localDir.getInfo().contentEquals("Directories: 1, Files: 2"));
        assertThat(localFile.getInfo().contentEquals("text/plain 26 B"));
    }

    @Test
    public void testGetFileInfoList() throws Exception {
        assertThat(localFile.getFileInfoList().size()).isEqualTo(6);
        assertThat(localFile.getFileInfoList().get(0)).containsSequence(testFileName);

        assertThat(localDir.getFileInfoList()).isEmpty();
    }

    @Test
    public void testGetIcon() throws Exception {
        // Image blub = localFile.getIcon();
        // causes: java.lang.RuntimeException: Internal graphics not initialized yet

        // TODO initialize javafx
    }
}
