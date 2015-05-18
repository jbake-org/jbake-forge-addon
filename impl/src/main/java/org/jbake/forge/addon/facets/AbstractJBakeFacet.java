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
import org.jbake.forge.addon.utils.ContentUtil;
import org.jbake.forge.addon.utils.JBakeUtil;
import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFacet;
import org.jboss.forge.addon.resource.DirectoryResource;

import java.io.File;
import java.io.IOException;
import java.util.Set;


/**
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 *         modified by @author Mani Manasa Mylavarapu <manimanasamylavarapu@gmail.com>
 */
public abstract class AbstractJBakeFacet extends AbstractFacet<Project>
        implements ProjectFacet, JBakeFacet {

    protected BuildSystemType buildSystemType=BuildSystemType.maven;
    protected TemplateType templateType;

    protected String jbakeFolderPath;
    protected ContentType contentType;
    protected PublishType pageStatusType;
    protected String postTitle;
    protected String pageTitle;
    protected String targetDirectory;
    protected String creationOrModificationDate;
    protected String tags;
    protected PublishType postStatusType;
    protected String listenAddress;
    protected String port;

    public abstract boolean install();


    public abstract void installMavenPluginDependencies();


    public abstract void createJbakeFolderStructure() throws IOException;

    public abstract boolean isJbakeFolderCreated();


    public abstract boolean isJbakeInstalled();

    public abstract boolean isDependencyRequirementsMet();

    public abstract Set<Coordinate> getRequiredDependencyOptions();

    @Override
    public String toString() {
        return getSpecVersion().toString();
    }


    public String getJbakeFolderPath() {
        return jbakeFolderPath;
    }

    public void setJbakeFolderPath(String jbakeFolderPath) {
        this.jbakeFolderPath = jbakeFolderPath;
    }

    @Override
    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    @Override
    public void setPageStatusType(PublishType pageStatusType) {
        this.pageStatusType = pageStatusType;
    }

    @Override
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    @Override
    public void setTargetDirectory(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    @Override
    public void setCreationOrModificationDate(String creationOrModificationDate) {
        this.creationOrModificationDate = creationOrModificationDate;
    }

    @Override
    public void setPageTags(String tags) {
        this.tags = tags;
    }

    @Override
    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    @Override
    public void setPostTags(String tags) {
        this.tags = tags;
    }

    @Override
    public void setListenAddress(String listenAddress) throws IOException {
        this.listenAddress = listenAddress;
    }

    @Override
    public void setPort(String port) throws IOException {
        this.port = port;
    }

    @Override
    public void setPostStatusType(PublishType postStatusType) {
        this.postStatusType = postStatusType;
    }

    @Override
    public boolean createPage() throws IOException {
        Boolean isCreated = false;
        if (contentType == (ContentType.AsciiDoc)) {
            ContentUtil.createFile(targetDirectory, contentType, JBakeUtil.toSlug(pageTitle));
            isCreated = ContentUtil.writeFile(contentType, pageTitle, creationOrModificationDate, "page", tags, pageStatusType);
        } else if (contentType == (ContentType.HTML)) {
            ContentUtil.createFile(targetDirectory, contentType, JBakeUtil.toSlug(pageTitle));
            isCreated = ContentUtil.writeFile(contentType, pageTitle, creationOrModificationDate, "page", tags, pageStatusType);
        } else if (contentType == (ContentType.Markdown)) {
            ContentUtil.createFile(targetDirectory, contentType, JBakeUtil.toSlug(pageTitle));
            isCreated = ContentUtil.writeFile(contentType, pageTitle, creationOrModificationDate, "page", tags, pageStatusType);
        }
        return isCreated;
    }

    @Override
    public boolean createPost() throws IOException {
        Boolean isCreated = false;
        if (contentType == (ContentType.AsciiDoc)) {
            ContentUtil.createFile(targetDirectory, contentType, JBakeUtil.toSlug(postTitle));
            isCreated = ContentUtil.writeFile(contentType, postTitle, creationOrModificationDate, "post", tags, postStatusType);
        } else if (contentType == (ContentType.HTML)) {
            ContentUtil.createFile(targetDirectory, contentType, JBakeUtil.toSlug(postTitle));
            isCreated = ContentUtil.writeFile(contentType, postTitle, creationOrModificationDate, "post", tags, postStatusType);
        } else if (contentType == (ContentType.Markdown)) {
            ContentUtil.createFile(targetDirectory, contentType, JBakeUtil.toSlug(postTitle));
            isCreated = ContentUtil.writeFile(contentType, postTitle, creationOrModificationDate, "post", tags, postStatusType);
        }
        return isCreated;
    }

}