/*
 * Copyright 2015-2020 Daniel Pelsmaeker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.virtlink.commons.configuration2.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;

/**
 * A configuration in JSON.
 */
public class JsonConfiguration extends JacksonConfiguration {

    /**
     * Initializes a new instance of the {@link JsonConfiguration} class.
     */
    public JsonConfiguration() {
        super(new JsonFactory());
    }

    /**
     * Initializes a new instance of the {@link JsonConfiguration} class.
     *
     * @param config the configuration whose nodes to copy into this configuration
     */
    public JsonConfiguration(final HierarchicalConfiguration<ImmutableNode> config) {
        super(new JsonFactory(), config);
    }
}
