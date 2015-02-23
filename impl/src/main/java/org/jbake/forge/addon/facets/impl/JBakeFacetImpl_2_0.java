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
package org.jbake.forge.addon.facets.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jbake.forge.addon.facets.AbstractJBakeFacet;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.maven.projects.MavenBuildSystem;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.furnace.versions.SingleVersion;
import org.jboss.forge.furnace.versions.Version;

/**
 *
 *
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 * modified by @author Mani Manasa Mylavarapu <manimanasamylavarapu@gmail.com>
 */
public class JBakeFacetImpl_2_0 extends AbstractJBakeFacet {

	@Inject
	private MavenBuildSystem buildSystem;

	@Inject
	public JBakeFacetImpl_2_0(DependencyInstaller installer) {
		super(installer);
	}

	@Override
	protected Map<Dependency, List<Dependency>> getRequiredDependencyOptions() {
		Map<Dependency, List<Dependency>> dependency = new HashMap<Dependency, List<Dependency>>();
		return dependency;
	}

	@Override
	public boolean isJbakeFolderCreated() {
		return false;
	}

	@Override
	public Version getSpecVersion() {
		return new SingleVersion("2.0");
	}

}