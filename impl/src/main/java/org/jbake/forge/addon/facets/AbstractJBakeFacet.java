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

import org.jbake.forge.addon.types.BuildSystemType;
import org.jbake.forge.addon.types.TemplateType;
import org.jbake.forge.addon.utils.TemplateUtil;
import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.maven.plugins.ExecutionBuilder;
import org.jboss.forge.addon.maven.plugins.MavenPlugin;
import org.jboss.forge.addon.maven.plugins.MavenPluginAdapter;
import org.jboss.forge.addon.maven.plugins.MavenPluginBuilder;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFacet;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
import org.jboss.forge.addon.projects.facets.DependencyFacet;
import org.jboss.forge.addon.resource.DirectoryResource;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 *         modified by @author Mani Manasa Mylavarapu <manimanasamylavarapu@gmail.com>
 */
public abstract class AbstractJBakeFacet extends AbstractFacet<Project>
        implements ProjectFacet, JBakeFacet {
    @Override
    public BuildSystemType getBuildSystemType() {
        return buildSystemType;
    }

    @Override
    public void setBuildSystemType(BuildSystemType buildSystemType) {
        this.buildSystemType = buildSystemType;
    }

    public BuildSystemType buildSystemType;
    public TemplateType templateType;
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
    private ProjectFactory projectFactory;

    @Inject
    public AbstractJBakeFacet(final DependencyInstaller installer) {
        this.installer = installer;
    }

    abstract protected Map<Dependency, List<Dependency>> getRequiredDependencyOptions();

    @Override
    public TemplateType getTemplateType() {
        return templateType;
    }




    @Override
    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    @Override
    public boolean install() {
        try {
            createJbakeFolderStructure();
        } catch (IOException e) {
            e.printStackTrace();
        }
        installJbakeCoreDependencies();
        installMavenPluginDependencies();
        return true;
    }


    private void installMavenPluginDependencies() {
        Coordinate warCompiler = CoordinateBuilder.create("org.apache.maven.plugins:maven-war-plugin").setVersion("2.4");
        Coordinate jbakeCompiler = CoordinateBuilder.create("br.com.ingenieux:jbake-maven-plugin");

        MavenPluginBuilder warBuilder = MavenPluginBuilder.create().setCoordinate(warCompiler);
        MavenPluginBuilder jbakeBuilder = MavenPluginBuilder.create()
                .setCoordinate(jbakeCompiler).addExecution(ExecutionBuilder.create().setId("default-generate").setPhase("generate-resources").addGoal("generate"));

        MavenPlugin warPlugin = new MavenPluginAdapter(warBuilder);
        MavenPlugin jbakePlugin = new MavenPluginAdapter(jbakeBuilder);

        MavenPluginFacet pluginFacet = getFaceted().getFacet(MavenPluginFacet.class);

        pluginFacet.addPlugin(warPlugin);
        pluginFacet.addPlugin(jbakePlugin);

    }

    private void installJbakeCoreDependencies() {
        this.installer.install(getFaceted(), JBAKE_CORE_DEPENDENCY);
    }

    public void createJbakeFolderStructure() throws IOException {

        Project selectedProject = getFaceted();
        DirectoryResource directoryResource = (DirectoryResource) selectedProject.getRoot();
        File codeFolder = directoryResource.getUnderlyingResourceObject();
        String outputFilePath=null;
        if(buildSystemType==(BuildSystemType.maven))
        {
            outputFilePath = codeFolder.getCanonicalPath() + "/src/main/jbake";
        }
        outputFilePath = codeFolder.getCanonicalPath() + "/src/jbake";
        TemplateUtil.unzip(getTemplateType().toString(), outputFilePath);

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