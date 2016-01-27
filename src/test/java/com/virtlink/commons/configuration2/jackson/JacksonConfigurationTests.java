/*
 * Copyright 2015-2015 Daniel Pelsmaeker
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

import org.apache.commons.configuration2.builder.BasicBuilderParameters;
import org.apache.commons.configuration2.builder.BasicConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public abstract class JacksonConfigurationTests {

    @Test
    public void readConfiguration() throws IOException, ConfigurationException {
        // Arrange
        JacksonConfiguration sut = create();
        String input = getExampleConfiguration();

        // Act
        StringReader reader = new StringReader(input);
        sut.read(reader);

        // Assert
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

    @Test
    public void readWriteReadConfiguration() throws IOException, ConfigurationException {
        // Arrange
        String str = getExampleConfiguration();
        JacksonConfiguration config = create();
        config.read(new StringReader(str));
        StringWriter writer = new StringWriter();
        config.write(writer);
        String str2 = writer.toString();

        // Act
        JacksonConfiguration config2 = create();
        config2.read(new StringReader(str2));
        StringWriter writer2 = new StringWriter();
        config.write(writer2);
        String str3 = writer2.toString();

        // Assert
        assertThat(str2, is(str3));
    }

    @Test
    public void configurationWithVariables() throws IOException, ConfigurationException {
        // Arrange
        JacksonConfiguration configuration = create();
        configuration.setProperty("application.groupid", "org.example");
        configuration.setProperty("application.artifactid", "testapp");
        configuration.setProperty("application.version", "1.0-alpha");
        configuration.setProperty("id", "${application.groupid}:${application.artifactid}:${application.version}");

        // Act
        String id = configuration.getString("id");

        // Assert
        assertThat(id, is("org.example:testapp:1.0-alpha"));
    }

    @Test
    public void readConfigurationWithVariables() throws IOException, ConfigurationException {
        // Arrange
        JacksonConfiguration configuration = create();
        configuration.setProperty("application.groupid", "org.example");
        configuration.setProperty("application.artifactid", "testapp");
        configuration.setProperty("application.version", "1.0-alpha");
        configuration.setProperty("id", "${application.groupid}:${application.artifactid}:${application.version}");
        String input = asString(configuration);

        // Act
        JacksonConfiguration sut = create();
        sut.read(new StringReader(input));
        String id = sut.getString("id");

        // Assert
        assertThat(id, is("org.example:testapp:1.0-alpha"));
    }

    @Test
    public void readWriteReadConfigurationWithVariables() throws IOException, ConfigurationException {
        // Arrange
        JacksonConfiguration configuration = create();
        configuration.setProperty("application.groupid", "org.example");
        configuration.setProperty("application.artifactid", "testapp");
        configuration.setProperty("application.version", "1.0-alpha");
        configuration.setProperty("id", "${application.groupid}:${application.artifactid}:${application.version}");
        String input = asString(configuration);

        JacksonConfiguration sut = create();
        sut.read(new StringReader(input));
        String str2 = asString(sut);
        JacksonConfiguration sut2 = create();
        sut2.read(new StringReader(str2));
        String str3 = asString(sut2);

        // Act
        JacksonConfiguration sut3 = create();
        sut3.read(new StringReader(str3));
        String id = sut3.getString("id");

        // Assert
        assertThat(id, is("org.example:testapp:1.0-alpha"));
    }

    /**
     * Returns an example configuration string.
     * <p>
     * Override this method to provide an example for the configuration implementation.
     * <p>
     * The model must have the following structure:
     * <p>
     * <pre>
     * obj.name: "test"
     * obj.value: 1
     * name: "testName"
     * listOfObjs(0).name: "testname"
     * listOfObjs(0).value: 4
     * listOfObjs(1).name: "other"
     * listOfObjs(1).value: 20
     * </pre>
     *
     * @return The example configuration string.
     * @throws ConfigurationException
     */
    protected String getExampleConfiguration() throws ConfigurationException, IOException {
        JacksonConfiguration configuration = create();

        configuration.setProperty("name", "testName");
        configuration.setProperty("obj.name", "test");
        configuration.setProperty("obj.value", 1);
        configuration.addProperty("listOfObjs(-1).name", "testname");
        configuration.addProperty("listOfObjs.value", 4);
        configuration.addProperty("listOfObjs(-1).name", "other");
        configuration.addProperty("listOfObjs.value", 20);

        return asString(configuration);
    }

    protected String asString(JacksonConfiguration configuration) {
        StringWriter writer = new StringWriter();
        try {
            configuration.write(writer);
        } catch (ConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    /**
     * Creates a new {@link JacksonConfiguration} for use in tests.
     *
     * @return The created configuration.
     */
    protected JacksonConfiguration create() throws ConfigurationException {
        return create(new HashMap<String, Object>());
    }

    /**
     * Creates a new {@link JacksonConfiguration} for use in tests.
     *
     * @param properties The properties in the configuration.
     * @return The created configuration.
     */
    protected abstract JacksonConfiguration create(Map<String, Object> properties) throws ConfigurationException;

}
