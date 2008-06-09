package org.codehaus.xsite.mojo;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.xsite.Main;

/**
 * Mojo to run XSite via Main CLI
 * 
 * @author Mauro Talevi
 * @goal run
 */
public class XSiteRunMojo  extends AbstractMojo {
    
    /**
     * @parameter
     * @required true
     */
    String sourceDirectoryPath;

    /**
     * @parameter
     * @required true
     */
    String sitemapPath;
    
    /**
     * @parameter
     * @required true
     */
    String skinPath;

    /**
     * @parameter
     * @required true
     */
    String outputDirectoryPath;

    /**
     * @parameter
     */
    String resourcePaths;
 
    /**
     * @parameter
     */
    String localisations;

    /**
     * @parameter
     */
    String compositionFilePath;
    
    /**
     * @parameter
     */
    String compositionResourcePath;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            String[] args = getArgs();
            getLog().debug("Executing XSite run goal with args "+toString(args));
            Main.main(args);
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to run xsite", e);
        }
    }

    private String toString(String[] args) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]);
            if ( i < args.length - 1 ){
                sb.append(",");
            }
        }        
        return sb.toString();
    }
    
    private String[] getArgs() {
        List args = new ArrayList();
        args.add("-S" + sourceDirectoryPath);
        args.add("-m" + sitemapPath);
        args.add("-s" + skinPath);
        args.add("-o" + outputDirectoryPath);
        if (resourcePaths != null) {
            args.add("-R" + resourcePaths);
        }
        if (localisations != null) {
            args.add("-L" + localisations);
        }
        if (compositionFilePath != null) {
            args.add("-f" + compositionFilePath);
        }
        if (compositionResourcePath != null) {
            args.add("-r" + compositionResourcePath);
        }
        return (String[]) args.toArray(new String[args.size()]);
    }

}