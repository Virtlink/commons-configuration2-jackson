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

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.combined.CombinedConfigurationBuilder;
import org.apache.commons.configuration2.builder.combined.ConfigurationBuilderProvider;
import org.apache.commons.configuration2.builder.fluent.CombinedBuilderParameters;
import org.apache.commons.configuration2.builder.fluent.FileBasedBuilderParameters;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public final class JsonConfigurationTests extends ConfigurationTests<JsonConfiguration> {

    @Override
    protected JsonConfiguration create(final Map<String, Object> properties) throws ConfigurationException {
        return new JsonConfiguration();
    }

    @Override
    protected String getExampleConfiguration() throws ConfigurationException, IOException {
        final URL url = Resources.getResource("example.json");
        return Resources.toString(url, Charsets.UTF_8);
    }

    @Test
    public void emptyArrayIsIgnored() throws IOException, ConfigurationException {
        // Arrange
        final String input = "{\n"
                + "\"emptyArray\" : []\n"
                + "}";
        final JsonConfiguration sut = new JsonConfiguration();

        // Act
        sut.read(new StringReader(input));

        // Assert
        assertThat(sut.getProperty("emptyArray"), is(nullValue()));
    }

    @Test
    public void nullIsIgnored() throws IOException, ConfigurationException {
        // Arrange
        final String input = "{\n"
                + "\"nullValue\" : null\n"
                + "}";
        final JsonConfiguration sut = new JsonConfiguration();

        // Act
        sut.read(new StringReader(input));

        // Assert
        assertThat(sut.getProperty("nullValue"), is(nullValue()));
    }

    @Test
    public void readFromBuilder() throws ConfigurationException {
        // Arrange
        final FileBasedBuilderParameters params = new Parameters()
                .fileBased()
                .setThrowExceptionOnMissing(true)
                .setEncoding("UTF-8")
                .setFileName(Resources.getResource("example.json").toString());

        // Act
        final FileBasedConfigurationBuilder<JsonConfiguration> builder = new FileBasedConfigurationBuilder<>(
                JsonConfiguration.class);
        final JsonConfiguration sut = builder.configure(params).getConfiguration();

        // Assert
        assertThat(sut.getString("name"), is("testName"));
    }

    @Test
    public void readCombinedConfig() throws ConfigurationException {
        // Arrange
        final ConfigurationBuilderProvider provider = new JacksonConfigurationBuilderProvider<>(JsonConfiguration.class);

        final Parameters params = new Parameters();
        final CombinedBuilderParameters combinedBuilderParameters = params.combined()
                .setDefinitionBuilderParameters(
                        params.fileBased().setFileName(Resources.getResource("combined.xml").toString())
                )
                .registerProvider("json", provider);

        // Act
        final CombinedConfigurationBuilder builder = new CombinedConfigurationBuilder();
        final CombinedConfiguration sut = builder.configure(combinedBuilderParameters).getConfiguration();

        // Assert
        assertThat(sut.getString("someFolder"), is("default"));
        assertThat(sut.getString("name"), is("testName"));
        assertThat(sut.getString("obj.name"), is("test"));
        assertThat(sut.getInt("obj.value"), is(1));
        assertThat(sut.getString("listOfObjs(0).name"), is("testname"));
        assertThat(sut.getInt("listOfObjs(0).value"), is(4));
        assertThat(sut.getString("listOfObjs(1).name"), is("other"));
        assertThat(sut.getInt("listOfObjs(1).value"), is(20));
        assertThat(sut.getStringArray("listOfObjs.name"), is(new String[]{"testname", "other"}));
        assertThat(sut.getProperty("nullValue"), is(nullValue()));
        assertThat(sut.getProperty("emptyList"), is(nullValue()));
    }

}
