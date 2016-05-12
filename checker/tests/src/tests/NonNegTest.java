package tests;

import java.io.File;

import org.checkerframework.framework.test.CheckerFrameworkTest;
import org.junit.runners.Parameterized.Parameters;

public class NonNegTest extends CheckerFrameworkTest {

    public NonNegTest(File testFile) {
        super(testFile,
                org.checkerframework.checker.nonneg.NonNegChecker.class,
                "nonneg",
                "-Anomsgtext");
    }

    @Parameters
    public static String[] getTestDirs() {
        return new String[]{"nonneg"};
    }

}
