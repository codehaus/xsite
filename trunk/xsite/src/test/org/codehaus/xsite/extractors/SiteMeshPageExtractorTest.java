package org.codehaus.xsite.extractors;

import junit.framework.TestCase;

import org.codehaus.xsite.model.Page;

/**
 * @author J&ouml;rg Schaible
 */
public class SiteMeshPageExtractorTest extends TestCase {


    private SiteMeshPageExtractor pageExtractor;

    protected void setUp() throws Exception {
        super.setUp();
        pageExtractor = new SiteMeshPageExtractor();
    }

    public void testPageBodyStartingWithHeader() {
        final String html = "<html><head><title>JUnit</title></head><body><h1>Header</h1></body></html>";
        // fails with NPE because of a bug in CustomTag.addAttribute, when the tag had no attributes yet
        // http://jira.opensymphony.com/browse/SIM-202
        final Page page = pageExtractor.extractPage("JUnit.html", html);
        assertEquals("JUnit", page.getTitle());
        assertEquals("<h1>Header</h1>", page.getBody());
    }
}
