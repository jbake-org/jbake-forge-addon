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
package org.jbake.forge.addon.ui.post;

import javax.inject.Inject;

import org.jbake.forge.addon.facets.JBakeFacet;
import org.jbake.forge.addon.types.ContentType;
import org.jbake.forge.addon.types.PublishType;
import org.jbake.forge.addon.ui.AbstractJBakeCommand;
import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
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
public class NewPostWizard extends AbstractJBakeCommand {

	@Inject
	@WithAttributes(label = "Post Title", required = true, shortName = 't', description = "Post title")
	private UIInput<String> postTitle;

	@Inject
	@WithAttributes(label = "Target Directory", type = InputType.DIRECTORY_PICKER, shortName = 'p', description = "Path of the post")
	private UIInput<String> targetDirectory;

	@Inject
	@WithAttributes(label = "Date of Created/Modified", shortName = 'd', description = "Date of creation for the post")
	private UIInput<String> dateOfCreated;

	@Inject
	@WithAttributes(label = "File Type", type = InputType.RADIO, required = true, shortName = 'c', description = "Post content type")
	private UISelectOne<ContentType> fileType;

	@Inject
	@WithAttributes(label = "Post Type", shortName = 'e', description = "Template to be used")
	private UIInput<String> templateType;

	@Inject
	@WithAttributes(label = "Post Tags", defaultValue = "post", shortName = 't', description = "Tag names in comma separated")
	private UIInput<String> postTags;

	@Inject
	@WithAttributes(label = "Post Status", type = InputType.RADIO,  shortName = 's', description = "Post status Draft/Published")
	private UISelectOne<PublishType> postStatus;

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {

		if (builder.getUIContext().getProvider().isGUI()) {
			postStatus
					.setItemLabelConverter(new Converter<PublishType, String>() {
						@Override
						public String convert(PublishType source) {
							return source != null ? source.name() : null;
						}
					});
		}

		builder.add(postTitle).add(targetDirectory).add(dateOfCreated)
				.add(templateType).add(postTags).add(fileType).add(postStatus);

	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		return Results.success(properties.getMessage("post.create.success"));
	}

	@Override
	protected boolean isProjectRequired() {

		return true;
	}

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata
				.from(super.getMetadata(context), getClass())
                .name(properties.getMetadataValue("post.name"))
                .description(properties.getMetadataValue("post.description"))
				.category(
						Categories.create(super.getMetadata(context)
								.getCategory(),  properties.getKeyValue("jbakeName")));
	}

}
