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
package org.jbake.forge.addon.ui.page;

import org.jbake.forge.addon.facets.JBakeFacet;
import org.jbake.forge.addon.types.ContentType;
import org.jbake.forge.addon.types.PublishType;
import org.jbake.forge.addon.ui.AbstractJBakeCommand;
import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.context.UIValidationContext;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.validate.UIValidator;

import javax.inject.Inject;
import java.util.Date;

import static org.jbake.forge.addon.utils.MessageUtil.properties;

/**
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 */
//@FacetConstraint({ JBakeFacet.class })
public class NewPageWizard extends AbstractJBakeCommand {
    @Inject
    private JBakeFacet jbakeVersion;

    @Inject
    @WithAttributes(label = "Page Title", required = true, shortName = 'l', description = "Page title")
    private UIInput<String> pageTitle;

    @Inject
    @WithAttributes(label = "Target Directory", type = InputType.DIRECTORY_PICKER, shortName = 'p', description = "Location of the page")
    private UIInput<String> targetDirectory;

    @Inject
    @WithAttributes(label = "File Type", type = InputType.RADIO, required = true, shortName = 'c', description = "Page content type")
    private UISelectOne<ContentType> fileType;

    @Inject
    @WithAttributes(label = "Date of Created/Modified", shortName = 'd', description = "Date of creation for the Page")
    private UIInput<String> dateOfCreated;

	/*@Inject
    @WithAttributes(label = "Template Type", required = true, shortName = 'e', description = "Template to be used")
	private UIInput<String> templateType;*/

    @Inject
    @WithAttributes(label = "Page Tags", defaultValue = "page", shortName = 't', description = "Tag names in comma separated")
    private UIInput<String> pageTags;

    @Inject
    @WithAttributes(label = "Page Status", type = InputType.RADIO, defaultValue = "Draft", required = true, shortName = 's', description = "Page status Draft/Published")
    private UISelectOne<PublishType> pageStatus;

    @Inject
    private ProjectFactory projectFactory;

    @Override
    public void initializeUI(final UIBuilder builder) throws Exception {

        configureInputs(builder);

        builder.add(pageTitle).add(targetDirectory).add(dateOfCreated)
                .add(pageTags).add(fileType).add(pageStatus);
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        jbakeVersion.setPageTitle(pageTitle.getValue());
        jbakeVersion.setTargetDirectory(targetDirectory.getValue());
        jbakeVersion.setContentType(fileType.getValue());
        jbakeVersion.setCreationOrModificationDate(dateOfCreated.getValue());
        jbakeVersion.setPageStatusType(pageStatus.getValue());
        String[] tags=pageTags.getValue().split(",");
        jbakeVersion.setPageTags(tags);
        if (jbakeVersion.createPage()) {
            return Results.success(properties.getMessage("page.create.success"));
        }
        else {
            return Results.fail(properties.getMessage("page.create.failure"));
        }
    }

    @Override
    protected boolean isProjectRequired() {
        return true;
    }

    private void configureInputs(UIBuilder builder) {
        Project project = getSelectedProject(builder);
        final Resource<?> projectRoot = project.getRoot();
        dateOfCreated.setValue(new Date().toString());
        targetDirectory.addValidator(new UIValidator() {
            @Override
            public void validate(UIValidationContext context) {
                try
                {
                    projectRoot.resolveChildren(targetDirectory.getValue());
                }
                catch (RuntimeException re)
                {
                    context.addValidationError(targetDirectory, "Target Directory is not a child of the project root "
                            + projectRoot);
                }
            }
        });
        if (builder.getUIContext().getProvider().isGUI()) {
            pageStatus
                    .setItemLabelConverter(new Converter<PublishType, String>() {
                        @Override
                        public String convert(PublishType source) {
                            return source != null ? source.name() : null;
                        }
                    });
        }

    }

    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata
                .from(super.getMetadata(context), getClass())
                .name(properties.getMetadataValue("page.name"))
                .description(properties.getMetadataValue("page.description"))
                .category(
                        Categories.create(super.getMetadata(context)
                                .getCategory(), properties.getKeyValue("jbakeName")));
    }

}
