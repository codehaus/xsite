package org.codehaus.xsite.loaders;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.codehaus.xsite.PageExtractor;
import org.codehaus.xsite.SiteMapLoader;
import org.codehaus.xsite.model.Link;
import org.codehaus.xsite.model.Page;
import org.codehaus.xsite.model.Section;
import org.codehaus.xsite.model.SiteMap;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.basic.AbstractBasicConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Loads a SiteMap from an XML file using XStream.
 *
 * @author Joe Walnes
 */
public class XStreamSiteMapLoader implements SiteMapLoader {
    private PageExtractor pageExtractor;
    private XStream xstream;
    
    /**
     * Creates am XStreamSiteMapLoader
     * @param extractor the PageExtractor
     * @param xstream the XStream instance
     */
    public XStreamSiteMapLoader(PageExtractor extractor, XStream xstream) {
        this.pageExtractor = extractor;
        this.xstream = xstream;
        configureXStream();
    }

    private void configureXStream() {
        xstream.alias("section", Section.class);
        xstream.alias("page", Page.class);
        xstream.alias("link", Link.class);
        xstream.alias("sitemap", SiteMap.class);
        xstream.addImplicitCollection(Section.class, "pages");
        xstream.addImplicitCollection(SiteMap.class, "sections");
        xstream.registerConverter(new LinkConverter());
    }

    public SiteMap loadFrom(File content) throws IOException {
        xstream.registerConverter(new PageConverter(content.getParentFile(),
                pageExtractor));
        Reader reader = new FileReader(content);
        try {
            return (SiteMap) xstream.fromXML(reader);
        } finally {
            reader.close();
        }
    }

    private static class PageConverter extends AbstractBasicConverter {

        private final File baseDirectory;
        private final PageExtractor pageExtractor;

        public PageConverter(File baseDirectory, PageExtractor pageExtractor) {
            this.baseDirectory = baseDirectory;
            this.pageExtractor = pageExtractor;
        }

        public boolean canConvert(Class type) {
            return type == Page.class;
        }

        protected Object fromString(String text) {
            return pageExtractor.extractPage(new File(baseDirectory, text));
        }

        protected String toString(Object o) {
            Page page = (Page) o;
            return page.getFilename();
        }
    }

    private static class LinkConverter implements Converter {

        public boolean canConvert(Class type) {
            return type == Link.class;
        }

        public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
            Link link = (Link) source;
            writer.addAttribute("title", link.getTitle());
            writer.setValue(link.getHref());
        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            String title = reader.getAttribute("title");
            String href = reader.getValue();
            return new Link(title, href);
        }
    }
}
