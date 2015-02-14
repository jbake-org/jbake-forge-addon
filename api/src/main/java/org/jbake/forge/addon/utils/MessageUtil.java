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
package org.jbake.forge.addon.utils;

import java.util.ResourceBundle;

/**
 * Class for retrieve the messages from ResourceBundle.
 * 
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 * 
 */
public enum MessageUtil {

    /** The Constant INSTANCE. */
   // public static final MessageUtil properties = new MessageUtil();
    properties;

    /** The bundle. */
    private final ResourceBundle bundle;

    /**
     * Instantiates a new messages.
     */
    private MessageUtil() {
        bundle = ResourceBundle.getBundle("ResourceBundle");
    }

    /**
     * Gets the key value.
     * 
     * @param key the key
     * @return the key value
     */
    public String getKeyValue(final String key) {
        return bundle.getString(key);
    }

    /**
     * Gets the message.
     * 
     * @param key the key
     * @return the message
     */
    public String getMessage(final String key) {
        return bundle.getString("message." + key);
    }

    /**
     * Gets the metadata value.
     *
     * @param key the key
     * @return the message
     */
    public String getMetadataValue(final String key) {
        return bundle.getString("metadata." + key);
    }

    /**
     * Gets the message.
     * 
     * @param key the key
     * @param args the args
     * @return the message
     */
    public String getMessage(final String key, final Object... args) {
        return String.format(bundle.getString("message." + key), args);

    }
}