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
package org.jbake.forge.addon.ui;

import javax.inject.Inject;

import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

/**
 * 
 * 
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 */
public abstract class AbstractJBakeCommand extends AbstractProjectCommand {

    @Inject
    private ProjectFactory projectFactory;

    @Override
    public UICommandMetadata getMetadata(UIContext context)
    {
        return Metadata.from(super.getMetadata(context), getClass())
                .category(Categories.create("JBake"));
    }

    @Override
    protected ProjectFactory getProjectFactory()
    {
        return projectFactory;
    }
}
