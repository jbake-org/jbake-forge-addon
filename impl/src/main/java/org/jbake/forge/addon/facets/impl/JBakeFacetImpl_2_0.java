package org.jbake.forge.addon.facets.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jbake.forge.addon.facets.AbstractJBakeFacet;
import org.jbake.forge.addon.facets.JBakeFacet;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
import org.jboss.forge.furnace.versions.SingleVersion;
import org.jboss.forge.furnace.versions.Version;

public class JBakeFacetImpl_2_0 extends AbstractJBakeFacet implements
		JBakeFacet {

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
	public Version getSpecVersion() {
		return new SingleVersion("2.0");
	}

}
