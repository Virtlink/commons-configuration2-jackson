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

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public final class JsonConfigurationTests {

    @Test
    public void readConfiguration() throws IOException, ConfigurationException {
        JsonConfiguration sut = new JsonConfiguration();

        String input = "{\n"
                + "  \"obj\" : {\n"
                + "    \"name\" : \"test\",\n"
                + "    \"value\" : 1\n"
                + "  },\n"
                + "  \"name\" : \"testName\",\n"
                + "  \"listOfObjs\" : [ {\n"
                + "    \"name\" : \"testname\",\n"
                + "    \"value\" : 4\n"
                + "  }, {\n"
                + "    \"name\" : \"other\",\n"
                + "    \"value\" : 20\n"
                + "  } ]\n"
                + "}";

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
        assertThat(sut.getProperty("nullValue"), is(nullValue()));
        assertThat(sut.getProperty("emptyList"), is(nullValue()));
    }

    @Test
    public void writeConfiguration() throws IOException, ConfigurationException {
        JsonConfiguration sut = new JsonConfiguration();

        sut.setProperty("name", "testName");
        sut.setProperty("obj.name", "test");
        sut.setProperty("obj.value", 1);
        sut.addProperty("listOfObjs(-1).name", "testname");
        sut.addProperty("listOfObjs.value", 4);
        sut.addProperty("listOfObjs(-1).name", "other");
        sut.addProperty("listOfObjs.value", 20);

        StringWriter writer = new StringWriter();
        sut.write(writer);

        String expected = "{\n"
                + "  \"obj\" : {\n"
                + "    \"name\" : \"test\",\n"
                + "    \"value\" : 1\n"
                + "  },\n"
                + "  \"name\" : \"testName\",\n"
                + "  \"listOfObjs\" : [ {\n"
                + "    \"name\" : \"testname\",\n"
                + "    \"value\" : 4\n"
                + "  }, {\n"
                + "    \"name\" : \"other\",\n"
                + "    \"value\" : 20\n"
                + "  } ]\n"
                + "}";

        assertThat(writer.toString(), is(expected));
    }

    @Test
    public void readWriteReadConfiguration() throws IOException, ConfigurationException {
        String str = "{\n"
                + "  \"obj\" : {\n"
                + "    \"name\" : \"test\",\n"
                + "    \"value\" : 1\n"
                + "  },\n"
                + "  \"name\" : \"testName\",\n"
                + "  \"listOfObjs\" : [ {\n"
                + "    \"name\" : \"testname\",\n"
                + "    \"value\" : 4\n"
                + "  }, {\n"
                + "    \"name\" : \"other\",\n"
                + "    \"value\" : 20\n"
                + "  } ]\n"
                + "}";

        JsonConfiguration config = new JsonConfiguration();
        config.read(new StringReader(str));

        StringWriter writer = new StringWriter();
        config.write(writer);
        String str2 = writer.toString();

        JsonConfiguration config2 = new JsonConfiguration();
        config2.read(new StringReader(str2));

        StringWriter writer2 = new StringWriter();
        config.write(writer2);
        String str3 = writer2.toString();

        assertThat(str2, is(str3));
    }
}
