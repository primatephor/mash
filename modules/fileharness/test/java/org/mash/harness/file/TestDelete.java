package org.mash.harness.file;

import junit.framework.TestCase;
import org.mash.harness.HarnessContext;

import java.io.File;

public class TestDelete extends TestCase{
    public void testFileDelete(){
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("TestFileCopyData/copy_test.txt").getFile());
        String parent = file.getParent();
        FileCopyHarness copyHarness = new FileCopyHarness();
        copyHarness.setSourceFile(file.getAbsolutePath());
        copyHarness.setTargetFile("toDelete_test.txt");
        File outDir = new File(parent+"/../TestFileCopyDataOut");
        copyHarness.setTargetFileNameBaseDir(outDir.getAbsolutePath());
        copyHarness.run(new HarnessContext());

        File outFile = new File(classLoader.getResource("TestFileCopyDataOut/toDelete_test.txt").getFile());
        System.out.println(outFile.getAbsolutePath());

        FileDeleteHarness deleteHarness = new FileDeleteHarness();
        deleteHarness.setFolderName(outDir.getAbsolutePath());
        deleteHarness.run(new HarnessContext());
        System.out.printf("outDir:"+outDir.getAbsolutePath());
        assertEquals(0, outDir.list().length);
    }

    public void testFolderDelete(){
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("TestFileCopyData/copy_test.txt").getFile());
        String parent = file.getParent();
        FileCopyHarness copyHarness = new FileCopyHarness();
        copyHarness.setSourceFile(file.getAbsolutePath());
        copyHarness.setTargetFile("toDelete_test2.txt");
        File outDir = new File(parent+"/../TestFileCopyDataOut2");
        copyHarness.setTargetFileNameBaseDir(outDir.getAbsolutePath());
        copyHarness.run(new HarnessContext());

        File outFile = new File(classLoader.getResource("TestFileCopyDataOut2/toDelete_test2.txt").getFile());
        System.out.println(outFile.getAbsolutePath());

        outDir = new File(parent+"/../TestFileCopyDataOut2");
        assertTrue("Directory exists", outDir.exists());
        FileDeleteHarness deleteHarness = new FileDeleteHarness();
        deleteHarness.setFolderName(outDir.getAbsolutePath());
        deleteHarness.setRemoveFolder("true");
        deleteHarness.run(new HarnessContext());
        assertFalse("Directory exists", outDir.exists());
    }
}
