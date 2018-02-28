package org.mash.harness.file;

import junit.framework.TestCase;
import org.mash.harness.HarnessContext;
import org.mash.harness.HarnessError;

import java.io.File;

/**
 * @author teastlack
 * @since 8/7/17 3:20 PM
 */
public class TestFileCopy extends TestCase
{
    public void testMkdir()
    {
        File present = new File(".");
        System.out.println("PATH:"+present.getAbsolutePath());

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("TestFileCopyData/copy_test.txt").getFile());
        String parent = file.getParent();
        FileCopyHarness copyHarness = new FileCopyHarness();
        copyHarness.setSourceFile(file.getAbsolutePath());
        copyHarness.setTargetFile("copied_test.txt");
        File outDir = new File(parent+"/../");
        copyHarness.setTargetFileNameBaseDir(outDir.getAbsolutePath()+"/TestFileCopyDataOut");
        copyHarness.run(new HarnessContext());
        if(copyHarness.getErrors().size() > 0)
        {
            for (HarnessError harnessError : copyHarness.getErrors())
            {
                System.out.println("ERROR:"+harnessError.getDescription());
            }
        }

        File outFile = new File(classLoader.getResource("TestFileCopyDataOut/copied_test.txt").getFile());
        System.out.println(outFile.getAbsolutePath());
    }
}
