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

import org.apache.commons.io.FileUtils;

import org.apache.commons.lang.SystemUtils;
import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;


public class LocalFileTest {

    private static String tmpRoot;
    private static String testFolderName;
    private static String testFileFolder;
    private static String testParallelFolder;
    private static String testFileFolderChild;
    private static String testFileName;
    private static Path testLocalFilePath;
    LocalFile localFile;
    LocalFile localHiddenFile;
    LocalFile localDir;

    @Before
    public void setUp() throws Exception {
        tmpRoot = "tmpdir";
        testFolderName = "localfiletest";
        testFileFolder = tmpRoot + File.separator + testFolderName;
        testFileFolderChild = testFileFolder + File.separator + "childdir";
        testParallelFolder = tmpRoot + File.separator + "parallel";

        // create directories
        File testDirs = new File(testFileFolderChild);
        FileUtils.forceMkdir(testDirs);
        File parallelDir = new File(testParallelFolder);
        FileUtils.forceMkdir(parallelDir);

        // create testfiles
        testFileName = "test.txt";
        File testFile = new File(testFileFolder + File.separator + testFileName);
        FileUtils.write(testFile, "This is a normal test file");

        File testHiddenFile = new File(testFileFolder + File.separator +"."+ testFileName);
        FileUtils.write(testHiddenFile, "This is a hidden test file");

        //Set hidden attribute for Win OS systems
        if ( SystemUtils.IS_OS_WINDOWS ) {
            Files.setAttribute(Paths.get(testHiddenFile.getPath()), "dos:hidden", true);
        }

        testLocalFilePath = Paths.get(testFileFolder + File.separator + testFileName);
        localFile = new LocalFile(testLocalFilePath);

        localHiddenFile = new LocalFile(Paths.get(testFileFolder + File.separator +"."+ testFileName));

        localDir = new LocalFile(Paths.get(testFileFolder));
    }

    @After
    public void tearDown() throws Exception {
        if (Files.exists(Paths.get(tmpRoot))) {
            FileUtils.deleteDirectory(Paths.get(tmpRoot).toFile());
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
        Path isPath = testLocalFilePath.toAbsolutePath().normalize();
        Path isNotPath = Paths.get(testFileFolderChild);
        assertThat(localFile.hasPath(isPath));
        assertThat(!localFile.hasPath(isNotPath));
    }

    @Test
    public void testIsChildOfAbsolutePath() throws Exception {
        Path parentPath = Paths.get(testFileFolder);
        Path notParentPath = Paths.get(testParallelFolder);
        assertThat(localFile.isChildOfAbsolutePath(parentPath));
        assertThat(!localFile.isChildOfAbsolutePath(notParentPath));
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
        assertThat(currParent.get().hasPath(Paths.get(testFileFolder)));
    }

    @Test
    public void testGetChildren() throws Exception {
        List<LocalFile> currChildren = localDir.getChildren();
        assertThat(currChildren.size()).isEqualTo(3);

        assertThat(currChildren.get(0).hasPath(Paths.get(testFileFolderChild)));
    }

    @Test
    public void testGetFileName() throws Exception {
        assertThat(localFile.getFileName().equals(testFileName));
        assertThat(localDir.getFileName().equals(testFolderName));
    }

    @Test
    public void testEquals() throws Exception {
        LocalFile isEqualFile = new LocalFile(testFileFolder + File.separator + testFileName);
        assertThat(!localFile.equals(isEqualFile));
        assertThat(!localFile.equals(localHiddenFile));
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
        //TODO implement test
    }
}
