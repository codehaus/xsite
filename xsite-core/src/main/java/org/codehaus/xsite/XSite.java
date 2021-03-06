package org.codehaus.xsite;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.codehaus.xsite.model.Page;
import org.codehaus.xsite.model.Sitemap;
import org.codehaus.xsite.validators.LinkChecker;

/**
 * Facade for building sites
 * 
 * @author Joe Walnes
 * @author Mauro Talevi
 */
public class XSite {

    private final SitemapLoader sitemapLoader;
    private final Skin skin;
    private final LinkValidator[] validators;
    private final FileSystem fileSystem;
    private final XSiteConfiguration configuration;

    /**
     * Creates an XSite
     * 
     * @param loader the SitemapLoader used to load the Sitemap
     * @param skin the Skin used to skin the pages
     * @param validators the array with the LinkValidator instances
     * @param fileSystem the FileSystem used for IO operations
     * @param configuration the XSite configuration
     */
    public XSite(SitemapLoader loader, Skin skin, LinkValidator[] validators, FileSystem fileSystem,
            XSiteConfiguration configuration) {
        this.sitemapLoader = loader;
        this.skin = skin;
        this.validators = validators;
        this.fileSystem = fileSystem;
        this.configuration = configuration;
    }

    public void build(File sitemapFile, File skinFile, File[] resourceDirs, File outputDirectory, Map<String, Object> customProperties) throws IOException {
        // Load sitemap and content
        Sitemap siteMap = sitemapLoader.loadFrom(sitemapFile);

        // Copy resources (css, images, etc) to output
        for (int i = 0; i < resourceDirs.length; i++) {
            File resourceDir = resourceDirs[i];
            System.out.println("Copying resources from " + resourceDir);
            fileSystem.copyDirectory(resourceDir, outputDirectory, true);
        }

        // Apply skin to each page
        skin.load(skinFile);
        outputDirectory.mkdirs();
        for (Page page : siteMap.getAllPages()) {
            System.out.println("Skinning " + page.getFilename() + " (" + page.getTitle() + ")");
			skin.skin(page, siteMap, outputDirectory, customProperties);
        }

        // Verify links
        LinkChecker linkChecker = new LinkChecker(siteMap, validators, new LinkChecker.Reporter() {
            public void badLink(Page page, String link) {
                System.err.println("Invalid link on page " + page.getFilename() + " : " + link);
            }
        });

        if (configuration.validateLinks() && !linkChecker.verify()) {
            System.err.println("Invalid links found with validators " + Arrays.asList(validators));
            System.exit(-1);
        }
    }

}
