/**
 * Copyright 2014 JBake
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbake.forge.addon.facets;

import org.apache.commons.configuration.CompositeConfiguration;
import org.jbake.app.ConfigUtil;
import org.jbake.launcher.Init;
import org.jbake.launcher.LaunchOptions;
import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.maven.plugins.*;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFacet;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
import org.jboss.forge.addon.projects.facets.DependencyFacet;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import javax.inject.Inject;
import java.io.File;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 *         modified by @author Mani Manasa Mylavarapu <manimanasamylavarapu@gmail.com>
 */
public abstract class AbstractJBakeFacet extends AbstractFacet<Project>
        implements ProjectFacet, JBakeFacet {
    @Inject
    private ProjectFactory projectFactory;

    public static final Dependency KUALI_MAVEN_DEPENDENCY =
            DependencyBuilder
                    .create("org.kuali.maven.plugins:wagon-maven-plugin").setVersion("1.0.3");

    public static final Dependency JBAKE_CORE_DEPENDENCY =
            DependencyBuilder
                    .create("org.jbake:jbake-core").setVersion("2.3.2").
                    addExclusion(CoordinateBuilder.create().setGroupId("org.eclipse.jetty").setArtifactId("jetty-server"));

    public static final Dependency JBAKE_POM_DEPENDENCY =
            DependencyBuilder
                    .create("br.com.ingenieux:jbake-maven-plugin")
                    .setPackaging("pom")
                    .setVersion("0.0.9");

    private final DependencyInstaller installer;

    @Inject
    public AbstractJBakeFacet(final DependencyInstaller installer) {
        this.installer = installer;
    }

    abstract protected Map<Dependency, List<Dependency>> getRequiredDependencyOptions();

    @Override
    public boolean install() {
        createJbakeFolderStructure();
        installJbakeCoreDependencies();
        installJBakeMavenPluginDependencies();
        installMavenWarPluginDependencies();
        installKualiMavenPluginDependencies();
        return true;
    }

    private void printUsage(Object options) {
        CmdLineParser parser = new CmdLineParser(options);
        StringWriter sw = new StringWriter();
        sw.append("Usage: jbake\n");
        sw.append("   or  jbake <source> <destination>\n");
        sw.append("   or  jbake [OPTION]... [<value>...]\n\n");
        sw.append("Options:");
        System.out.println(sw.toString());
        parser.setUsageWidth(100);
        parser.printUsage(System.out);
        System.exit(0);
    }

    private LaunchOptions parseArguments(String[] args) {
        LaunchOptions res = new LaunchOptions();
        CmdLineParser parser = new CmdLineParser(res);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            printUsage(res);
        }
        return res;
    }

    private void initStructure(CompositeConfiguration config, String type) {
        Init init = new Init(config);
        try {
            Project selectedProject = getFaceted();
            DirectoryResource directoryResource = (DirectoryResource) selectedProject.getRoot();
            File codeFolder = directoryResource.getUnderlyingResourceObject();
          String filePath=codeFolder.getCanonicalPath()+"/src/main";
            File file=new File(filePath,"jbake");
            init.run(new File("."), file, type);
            System.out.println("Base folder structure successfully created.");
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to initalise structure!");
            e.printStackTrace();
        }
    }

    public boolean createJbakeFolderStructure() {

        String[] args = {"-i"};
        LaunchOptions res = parseArguments(args);

        CompositeConfiguration config = null;
        try {
            config = ConfigUtil.load(res.getSource());
        } catch (org.apache.commons.configuration.ConfigurationException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("JBake " + config.getString("version") + " (" + config.getString("build.timestamp") + ") [http://jbake.org]");
        System.out.println();
        if (res.isInit()) {
            if (res.getSourceValue() != null) {
                initStructure(config, res.getSourceValue());
            } else {
                initStructure(config, "freemarker");
            }
        }




		/*Project selectedProject = getFaceted();
        DirectoryResource directoryResource = (DirectoryResource) selectedProject.getRoot();
		directoryResource.getOrCreateChildDirectory("src");
		directoryResource.getOrCreateChildDirectory("src/main");
		directoryResource.getOrCreateChildDirectory("src/main/jbake");
		directoryResource.getOrCreateChildDirectory("src/main/jbake/assets");
		directoryResource.getOrCreateChildDirectory("src/main/jbake/content");
		directoryResource.getOrCreateChildDirectory("src/main/jbake/templates");*/
        return true;
    }

    private void installKualiMavenPluginDependencies() {
        Coordinate compiler = CoordinateBuilder.create("org.kuali.maven.plugins:wagon-maven-plugin").setVersion("1.0.3");
        MavenPluginBuilder builder = MavenPluginBuilder.create()
                .setCoordinate(compiler).addExecution(ExecutionBuilder.create().addGoal("upload").setId("verify").setPhase("verify")).
                        setConfiguration(ConfigurationBuilder.create().addConfigurationElement(ConfigurationElementBuilder.create().
                                addChild("serverId").setText("jbakehome"))).addPluginDependency(KUALI_MAVEN_DEPENDENCY);
        MavenPlugin plugin = new MavenPluginAdapter(builder);

        MavenPluginFacet pluginFacet = getFaceted().getFacet(MavenPluginFacet.class);
        pluginFacet.addPlugin(plugin);
    }

    private void installMavenWarPluginDependencies() {
        Coordinate compiler = CoordinateBuilder.create("org.apache.maven.plugins:maven-war-plugin").setVersion("2.4");
        MavenPluginBuilder builder = MavenPluginBuilder.create()
                .setCoordinate(compiler);
        MavenPlugin plugin = new MavenPluginAdapter(builder);

        MavenPluginFacet pluginFacet = getFaceted().getFacet(MavenPluginFacet.class);
        pluginFacet.addPlugin(plugin);

    }

    private void installJBakeMavenPluginDependencies() {
        Coordinate compiler = CoordinateBuilder.create("br.com.ingenieux:jbake-maven-plugin");
        MavenPluginBuilder builder = MavenPluginBuilder.create()
                .setCoordinate(compiler).addExecution(ExecutionBuilder.create().setId("default-generate").setPhase("generate-resources").addGoal("generate"));

        MavenPlugin plugin = new MavenPluginAdapter(builder);

        MavenPluginFacet pluginFacet = getFaceted().getFacet(MavenPluginFacet.class);
        pluginFacet.addPlugin(plugin);
    }

    private void installJbakeCoreDependencies() {
        this.installer.install(getFaceted(), JBAKE_CORE_DEPENDENCY);
    }

    public abstract boolean isJbakeFolderCreated();

    @Override
    public boolean isInstalled() {
        if (isDependencyRequirementsMet() && isJbakeFolderCreated())
            return true;
        else
            return false;
    }

    private void addRequiredDependency() {
        boolean isInstalled = false;
        DependencyFacet dependencyFacet = origin
                .getFacet(DependencyFacet.class);
        for (Entry<Dependency, List<Dependency>> group : getRequiredDependencyOptions()
                .entrySet()) {
            for (Dependency dependency : group.getValue()) {
                if (dependencyFacet.hasEffectiveDependency(dependency)) {
                    isInstalled = true;
                    break;
                }
            }
            if (!isInstalled) {
                installer.installManaged(origin, JBAKE_POM_DEPENDENCY);
                installer.install(origin, group.getKey());
            }
        }
    }

    protected boolean isDependencyRequirementsMet() {
        DependencyFacet deps = origin.getFacet(DependencyFacet.class);
        for (Entry<Dependency, List<Dependency>> group : getRequiredDependencyOptions()
                .entrySet()) {
            boolean satisfied = false;
            for (Dependency dependency : group.getValue()) {
                if (deps.hasEffectiveDependency(dependency)) {
                    satisfied = true;
                    break;
                }
            }

            if (!satisfied)
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getSpecVersion().toString();
    }
}