package org.codehaus.xsite.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Mauro Talevi
 */
public class XSiteTaskTest {
    protected String testSrcDir;

    @Before
    public void setTestDir() {
        testSrcDir = System.getProperty("test.src.dir");
        if (testSrcDir == null) {
            testSrcDir = "xsite-core/src/test/site";
        } else if (!testSrcDir.endsWith(File.separator)) {
            testSrcDir = testSrcDir + File.separator;
        }
    }

    @Test
    public void testGoalCanBeRunWithDefaultComposition() throws Exception {
        XSiteTask task = new XSiteTask();
        task.setSourceDirectoryPath(testSrcDir);
        task.setSitemapPath("content/sitemap.xml");
        task.setSkinPath("templates/skin.html");
        task.setOutputDirectoryPath("target/xsite");
        task.execute();
    }
    
    @Test
    public void testGoalCanBeRunWithOptionalResources() throws Exception {
        XSiteTask task = new XSiteTask();
        task.setSourceDirectoryPath(testSrcDir);
        task.setSitemapPath("content/sitemap.xml");
        task.setSkinPath("skin.html");
        task.setTemplatesDirectoryPath(testSrcDir+"/templates");
        task.setResourcePaths("resources");
        task.setOutputDirectoryPath("target/xsite");
        task.execute();
    }

    @Test
    public void testGoalCanBeRunWithOptionalCustomProperties() throws Exception {
        XSiteTask task = new XSiteTask();
        task.setSourceDirectoryPath(testSrcDir);
        task.setSitemapPath("content/sitemap.xml");
        task.setSkinPath("templates/skin.html");
        task.setOutputDirectoryPath("target/xsite");
        task.setPublishedVersion("1.0");
        task.setPublishedDate("10/10/2010");
        task.execute();
    }

    @Test(expected=BuildException.class)
    public void testGoalFailsWithInvalidPath() throws Exception {
        XSiteTask task = new XSiteTask();
        task.setSourceDirectoryPath(testSrcDir);
        task.setSitemapPath("content/nonexistent.xml");
        task.setSkinPath("templates/skin.html");
        task.setOutputDirectoryPath("target/xsite");
        task.execute();
    }
}
