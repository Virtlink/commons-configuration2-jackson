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

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileBased;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public abstract class ConfigurationTests<T extends FileBased & HierarchicalConfiguration<ImmutableNode>> {

    /**
     * Creates a new configuration for use in tests.
     *
     * @return The created configuration.
     */
    protected T create() throws ConfigurationException {
        return create(new HashMap<String, Object>());
    }

    /**
     * Creates a new configuration for use in tests.
     *
     * @param properties The properties in the configuration.
     * @return The created configuration.
     */
    protected abstract T create(Map<String, Object> properties) throws ConfigurationException;

    @Test
    public void readConfiguration() throws IOException, ConfigurationException {
        // Arrange
        final T sut = create();
        final String input = getExampleConfiguration();

        // Act
        final StringReader reader = new StringReader(input);
        sut.read(reader);

        final HierarchicalConfiguration<ImmutableNode> objs = sut.configurationAt("listOfObjs(0)", true);

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
        assertThat(sut.getString("listOfComplexObjs(0).someObj.name"), is("a name"));
        assertThat(sut.getInt("listOfComplexObjs(0).someObj.value"), is(20));
        assertThat(sut.getString("listOfComplexObjs(1).someObj.name"), is("another name"));
        assertThat(sut.getInt("listOfComplexObjs(1).someObj.value"), is(40));
    }

    @Test
    public void readWriteReadConfiguration() throws IOException, ConfigurationException {
        // Arrange
        final String str = getExampleConfiguration();
        final T config = create();
        config.read(new StringReader(str));
        final StringWriter writer = new StringWriter();
        config.write(writer);
        final String str2 = writer.toString();

        // Act
        final T config2 = create();
        config2.read(new StringReader(str2));
        final StringWriter writer2 = new StringWriter();
        config.write(writer2);
        final String str3 = writer2.toString();

        // Assert
        assertThat(str2, is(str3));
    }

    @Test
    public void configurationWithVariables() throws IOException, ConfigurationException {
        // Arrange
        final T configuration = create();
        configuration.setProperty("application.groupid", "org.example");
        configuration.setProperty("application.artifactid", "testapp");
        configuration.setProperty("application.version", "1.0-alpha");
        configuration.setProperty("id", "${application.groupid}:${application.artifactid}:${application.version}");

        // Act
        final String id = configuration.getString("id");

        // Assert
        assertThat(id, is("org.example:testapp:1.0-alpha"));
    }

    @Test
    public void readConfigurationWithVariables() throws IOException, ConfigurationException {
        // Arrange
        final T configuration = create();
        configuration.setProperty("application.groupid", "org.example");
        configuration.setProperty("application.artifactid", "testapp");
        configuration.setProperty("application.version", "1.0-alpha");
        configuration.setProperty("id", "${application.groupid}:${application.artifactid}:${application.version}");
        final String input = asString(configuration);

        // Act
        final T sut = create();
        sut.read(new StringReader(input));
        final String id = sut.getString("id");

        // Assert
        assertThat(id, is("org.example:testapp:1.0-alpha"));
    }

    @Test
    public void readWriteReadConfigurationWithVariables() throws IOException, ConfigurationException {
        // Arrange
        final T configuration = create();
        configuration.setProperty("application.groupid", "org.example");
        configuration.setProperty("application.artifactid", "testapp");
        configuration.setProperty("application.version", "1.0-alpha");
        configuration.setProperty("id", "${application.groupid}:${application.artifactid}:${application.version}");
        final String input = asString(configuration);

        final T sut = create();
        sut.read(new StringReader(input));
        final String str2 = asString(sut);
        final T sut2 = create();
        sut2.read(new StringReader(str2));
        final String str3 = asString(sut2);

        // Act
        final T sut3 = create();
        sut3.read(new StringReader(str3));
        final String id = sut3.getString("id");

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
     * listOfComplexObjs(0).someObj.name: "a name"
     * listOfComplexObjs(0).someObj.value: 20
     * listOfComplexObjs(1).someObj.name: "another name"
     * listOfComplexObjs(1).someObj.value: 40
     * </pre>
     *
     * @return The example configuration string.
     * @throws ConfigurationException
     */
    protected String getExampleConfiguration() throws ConfigurationException, IOException {
        final T configuration = create();

        configuration.setProperty("name", "testName");
        configuration.setProperty("obj.name", "test");
        configuration.setProperty("obj.value", 1);
        configuration.addProperty("listOfObjs(-1).name", "testname");
        configuration.addProperty("listOfObjs.value", 4);
        configuration.addProperty("listOfObjs(-1).name", "other");
        configuration.addProperty("listOfObjs.value", 20);
        configuration.addProperty("listOfComplexObjs(-1).someObj.name", "a name");
        configuration.addProperty("listOfComplexObjs.someObj.value", 20);
        configuration.addProperty("listOfComplexObjs(-1).someObj.name", "another name");
        configuration.addProperty("listOfComplexObjs.someObj.value", 40);

        return asString(configuration);
    }

    protected String asString(final T configuration) {
        final StringWriter writer = new StringWriter();
        try {
            configuration.write(writer);
        } catch (ConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

}
