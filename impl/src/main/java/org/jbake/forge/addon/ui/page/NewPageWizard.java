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

import java.util.Date;

import javax.inject.Inject;

import org.jbake.forge.addon.facets.JBakeFacet;
import org.jbake.forge.addon.types.ContentType;
import org.jbake.forge.addon.types.PublishType;
import org.jbake.forge.addon.ui.AbstractJBakeCommand;
import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.projects.ProjectFactory;
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
import static org.jbake.forge.addon.utils.MessageUtil.properties;
/**
 * 
 * 
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 */
@FacetConstraint({ JBakeFacet.class })
public class NewPageWizard extends AbstractJBakeCommand {

	@Inject
	@WithAttributes(label = "Page Title", required = true)
	private UIInput<String> pageTitle;

	@Inject
	@WithAttributes(label = "Target Directory", type = InputType.DIRECTORY_PICKER)
	private UIInput<String> targetDirectory;

	@Inject
	@WithAttributes(label = "File Type", type = InputType.RADIO, required = true)
	private UISelectOne<ContentType> fileType;

	@Inject
	@WithAttributes(label = "Date of Created/Modified")
	private UIInput<String> dateOfCreated;

	@Inject
	@WithAttributes(label = "Page Type", required = true)
	private UIInput<String> pageType;

	@Inject
	@WithAttributes(label = "Page Tags")
	private UIInput<String> pageTags;

	@Inject
	@WithAttributes(label = "Page Status", type = InputType.RADIO, required = true)
	private UISelectOne<PublishType> pageStatus;

	@Inject
	private ProjectFactory projectFactory;

	@Override
	public void initializeUI(final UIBuilder builder) throws Exception {

		configureInputs(builder);

		builder.add(pageTitle).add(targetDirectory).add(dateOfCreated)
				.add(pageType).add(pageTags).add(fileType).add(pageStatus);
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		return Results.success(properties.getMessage("page.create.success"));
	}

	@Override
	protected boolean isProjectRequired() {
		return true;
	}

	private void configureInputs(UIBuilder builder) {

		dateOfCreated.setValue(new Date().toString());

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
								.getCategory(),  properties.getKeyValue("jbakeName")));
	}

}
