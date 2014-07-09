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

import org.jbake.forge.addon.facets.JBakeFacet;
import org.jbake.forge.addon.ui.AbstractJBakeCommand;
import org.jboss.forge.addon.projects.facets.DependencyFacet;
import org.jboss.forge.addon.projects.facets.ResourcesFacet;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.furnace.versions.SingleVersion;
import org.jboss.forge.furnace.versions.Version;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;

/**
 * 
 * 
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 */
@FacetConstraint({ DependencyFacet.class, ResourcesFacet.class })
public class JBakeSetupWizard extends AbstractJBakeCommand {

	private static final Logger log = Logger.getLogger(JBakeSetupWizard.class
			.getName());

	@Inject
	@WithAttributes(required = true, label = "JBake Version", defaultValue = "2.0")
	private UISelectOne<JBakeFacet> jbakeVersion;

	@Inject
	private FacetFactory facetFactory;

	@Override
	public void initializeUI(final UIBuilder builder) throws Exception {
		builder.add(jbakeVersion);

	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		if (facetFactory.install(getSelectedProject(context.getUIContext()),
				jbakeVersion.getValue())) {
			return Results.success("JBake has been installed.");
		}
		return Results.fail("Could not install JBake.");
	}

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata
				.from(super.getMetadata(context), getClass())
				.name("JBake: Setup Project")
				.description("Setup a JBake project")
				.category(
						Categories.create(super.getMetadata(context)
								.getCategory(), "JBake"));
	}

	@Override
	protected boolean isProjectRequired() {
		return true;
	}

}
