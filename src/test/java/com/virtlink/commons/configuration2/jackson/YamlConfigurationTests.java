/*
 * Copyright 2015-2023 - Daniel A. A. Pelsmaeker
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

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public final class YamlConfigurationTests extends ConfigurationTests<JacksonConfiguration> {

    @Override
    protected JacksonConfiguration create(final Map<String, Object> properties) throws ConfigurationException {
        return new JacksonConfiguration(new YAMLFactory()) {};
    }

    @Override
    protected String getExampleConfiguration() throws ConfigurationException, IOException {
        final URL url = Resources.getResource("example.yaml");
        return Resources.toString(url, Charsets.UTF_8);
    }
}
