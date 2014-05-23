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

import java.util.logging.Logger;

import javax.inject.Inject;

import org.jbake.forge.addon.ui.AbstractJBakeCommand;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.ProjectProvider;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.context.UISelection;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.furnace.services.Imported;
import org.jboss.forge.furnace.util.OperatingSystemUtils;

/**
 * 
 * 
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 */
public class JBakeSetupWizard extends AbstractJBakeCommand {

    private static final Logger log = Logger.getLogger(JBakeSetupWizard.class.getName());

    @Inject
    private ProjectFactory projectFactory;

    @Inject
    private ResourceFactory resourceFactory;

    @Inject
    private Imported<ProjectProvider> buildSystems;
    


    @Override
    public void initializeUI(final UIBuilder builder) throws Exception
    {
        configureTargetLocationInput(builder);

    }

    private void configureTargetLocationInput(final UIBuilder builder)
    {
       
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {

        return Results.success("JBake has been installed.");
    }

    @Override
    public UICommandMetadata getMetadata(UIContext context)
    {
        return Metadata.from(super.getMetadata(context), getClass()).name("JBake: Setup Project")
                .description("Setup a JBake project")
                .category(Categories.create(super.getMetadata(context).getCategory(), "JBake"));
    }

    @Override
    protected boolean isProjectRequired() {
        return true;
    }

}
