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

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public final class YamlConfigurationTests {

    @Test
    public void readConfiguration() throws IOException, ConfigurationException {
        JacksonConfiguration sut = new JacksonConfiguration(new YAMLFactory()) {};

        String input = "---\n"
                + "obj:\n"
                + "  name: \"test\"\n"
                + "  value: 1\n"
                + "name: \"testName\"\n"
                + "listOfObjs:\n"
                + "- name: \"testname\"\n"
                + "  value: 4\n"
                + "- name: \"other\"\n"
                + "  value: 20\n";

        StringReader reader = new StringReader(input);
        sut.read(reader);

        assertThat(sut.getString("name"), is("testName"));
        assertThat(sut.getString("obj.name"), is("test"));
        assertThat(sut.getInt("obj.value"), is(1));
        assertThat(sut.getString("listOfObjs(0).name"), is("testname"));
        assertThat(sut.getInt("listOfObjs(0).value"), is(4));
        assertThat(sut.getString("listOfObjs(1).name"), is("other"));
        assertThat(sut.getInt("listOfObjs(1).value"), is(20));
        assertThat(sut.getStringArray("listOfObjs.name"), is(new String[] { "testname", "other" }));
    }

    @Test
    public void writeConfiguration() throws IOException, ConfigurationException {
        JacksonConfiguration sut = new JacksonConfiguration(new YAMLFactory()) {};

        sut.setProperty("name", "testName");
        sut.setProperty("obj.name", "test");
        sut.setProperty("obj.value", 1);
        sut.addProperty("listOfObjs(-1).name", "testname");
        sut.addProperty("listOfObjs.value", 4);
        sut.addProperty("listOfObjs(-1).name", "other");
        sut.addProperty("listOfObjs.value", 20);

        StringWriter writer = new StringWriter();
        sut.write(writer);

        String expected = "---\n"
                + "obj:\n"
                + "  name: \"test\"\n"
                + "  value: 1\n"
                + "name: \"testName\"\n"
                + "listOfObjs:\n"
                + "- name: \"testname\"\n"
                + "  value: 4\n"
                + "- name: \"other\"\n"
                + "  value: 20\n";

        assertThat(writer.toString(), is(expected));
    }
}
