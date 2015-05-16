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
package org.jbake.forge.addon.facets;

import org.jbake.forge.addon.types.BuildSystemType;
import org.jbake.forge.addon.types.ContentType;
import org.jbake.forge.addon.types.PublishType;
import org.jbake.forge.addon.types.TemplateType;
import org.jboss.forge.addon.projects.ProjectFacet;
import org.jboss.forge.furnace.versions.Version;

import java.io.IOException;

/**
 * JBake Facet.
 *
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 */

public interface JBakeFacet extends ProjectFacet {

    Version getSpecVersion();

    TemplateType getTemplateType();

    BuildSystemType getBuildSystemType();

    void setBuildSystemType(BuildSystemType buildSystemType);

    void setTemplateType(TemplateType templateType);

    void setContentType(ContentType contentType);

    void setPageStatusType(PublishType pageStatusType);

    void setPageTitle(String pageTitle);

    void setPostTitle(String postTitle);

    void setTargetDirectory(String targetDirectory);

    void setCreationOrModificationDate(String creationOrModificationDate);

    void setPageTags(String tags);

    void setPostTags(String tags);

    boolean createPage() throws IOException;

    boolean createPost() throws IOException;

    void setListenAddress(String listenAddress) throws IOException;

    void setPort(String port) throws IOException;

    void setPostStatusType(PublishType postStatusType);
}
