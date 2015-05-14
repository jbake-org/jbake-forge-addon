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
package org.jbake.forge.addon.ui.setup;

import org.jbake.forge.addon.facets.JBakeFacet;
import org.jbake.forge.addon.types.BuildSystemType;
import org.jbake.forge.addon.types.TemplateType;
import org.jbake.forge.addon.ui.AbstractJBakeCommand;
import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

import javax.inject.Inject;
import java.util.logging.Logger;

import static org.jbake.forge.addon.utils.MessageUtil.properties;

/**
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 */

public class SetupWizard extends AbstractJBakeCommand {

    private static final Logger log = Logger.getLogger(SetupWizard.class
            .getName());

    private static final Coordinate JBAKE_MAVEN_PLUGIN_COORDINATE = CoordinateBuilder
            .create().setGroupId("br.com.ingenieux")
            .setArtifactId("jbake-maven-plugin");

    Project project = null;

    @Inject
    @WithAttributes(required = true, label = "JBake Version", defaultValue = "2.0", shortName = 'v', description = "JBake Version")
    private UISelectOne<JBakeFacet> jbakeVersion;

    @Inject
    @WithAttributes(label = "Template Engine", type = InputType.RADIO, required = true, shortName = 't', description = "Template engine to be used for Templates")
    private UISelectOne<TemplateType> templateEngine;

    @Inject
    @WithAttributes(label = "Build System", type = InputType.DROPDOWN, required = true, shortName = 'b', description = "Build system to be used for building the application")
    private UISelectOne<BuildSystemType> buildSystemType;

    @Inject
    @WithAttributes(label = "listen Address", defaultValue = "localhost", shortName = 'l', description = "Give the host address")
    private UIInput<String> listenAddress;

    @Inject
    @WithAttributes(label = "port", defaultValue = "8820", shortName = 'p', description = "port to which your jbake host should listen to")
    private UIInput<String> port;

    @Inject
    private FacetFactory facetFactory;

    @Override
    public void initializeUI(final UIBuilder builder) throws Exception {
        builder.add(jbakeVersion).add(templateEngine).add(buildSystemType).add(listenAddress).add(port);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        project = getSelectedProject(context);
        jbakeVersion.getValue().setTemplateType(templateEngine.getValue());
        jbakeVersion.getValue().setBuildSystemType(buildSystemType.getValue());
        jbakeVersion.getValue().setListenAddress(listenAddress.getValue());
        jbakeVersion.getValue().setPort(port.getValue());
        if (facetFactory.install(getSelectedProject(context.getUIContext()),
                jbakeVersion.getValue())) {
            return Results.success(properties.getMessage("plugin.install.success"));
        } else {
            return Results.fail(properties.getMessage("plugin.install.failure"));
        }
    }

    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata
                .from(super.getMetadata(context), getClass())
                .name(properties.getMetadataValue("setup.name"))
                .description(properties.getMetadataValue("setup.description"))
                .category(
                        Categories.create(super.getMetadata(context)
                                .getCategory(), properties.getKeyValue("jbakeName")));
    }

    @Override
    protected boolean isProjectRequired() {
        return true;
    }

}
