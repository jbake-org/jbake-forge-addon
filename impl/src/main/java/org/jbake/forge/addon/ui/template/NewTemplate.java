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
package org.jbake.forge.addon.ui.template;

import java.util.logging.Logger;

import javax.inject.Inject;

import org.jbake.forge.addon.facets.JBakeFacet;
import org.jbake.forge.addon.types.TemplateType;
import org.jbake.forge.addon.ui.AbstractJBakeCommand;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.projects.Project;
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
 * Command for creating new Template for JBake
 * 
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 */
@FacetConstraint({ JBakeFacet.class })
public class NewTemplate extends AbstractJBakeCommand {

	private static final Logger log = Logger.getLogger(NewTemplate.class
			.getName());

	Project project = null;

	@Inject
	@WithAttributes(label = "Template Name", required = true, shortName = 't')
	private UIInput<String> templateName;

	@Inject
	@WithAttributes(label = "Template Type", type = InputType.RADIO, required = true, shortName = 't')
	private UISelectOne<TemplateType> templateType;

	@Inject
	private FacetFactory facetFactory;

	@Override
	public void initializeUI(final UIBuilder builder) throws Exception {
		builder.add(templateName).add(templateType);

	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {

		return Results.success(properties.getMessage("template.create.success"));
	}

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata
				.from(super.getMetadata(context), getClass())
                .name(properties.getMetadataValue("template.name"))
                .description(properties.getMetadataValue("template.description"))
				.category(
                        Categories.create(super.getMetadata(context)
                                .getCategory(), properties.getKeyValue("jbakeName")));
	}

	@Override
	protected boolean isProjectRequired() {
		return true;
	}

}
