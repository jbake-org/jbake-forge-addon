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

import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFacet;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
import org.jboss.forge.addon.projects.facets.DependencyFacet;


import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 */
public abstract class AbstractJBakeFacet extends AbstractFacet<Project>
        implements ProjectFacet, JBakeFacet {


    @Inject
    private ProjectFactory projectFactory;

    protected static final Dependency JBAKE_DEPENDENCY = DependencyBuilder
            .create().setGroupId("").setArtifactId("").setVersion("");

    private final DependencyInstaller installer;

    @Inject
    public AbstractJBakeFacet(final DependencyInstaller installer) {
        this.installer = installer;
    }

    abstract protected Map<Dependency, List<Dependency>> getRequiredDependencyOptions();

    @Override
    public boolean install() {
        addRequiredDependency();
        createJbakeFolderStructure();
        return true;
    }

    public abstract boolean createJbakeFolderStructure();

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
                installer.installManaged(origin, JBAKE_DEPENDENCY);
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
