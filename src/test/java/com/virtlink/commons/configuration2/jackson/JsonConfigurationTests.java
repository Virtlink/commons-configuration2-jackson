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

import org.apache.commons.configuration2.builder.BasicConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public final class JsonConfigurationTests extends JacksonConfigurationTests {

    @Override
    protected JsonConfiguration create(Map<String, Object> properties) throws ConfigurationException {
        return new JsonConfiguration();
    }

    @Override
    protected String getExampleConfiguration() throws ConfigurationException {
        return "{\n"
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
    }

    @Test
    public void emptyArrayIsIgnored() throws IOException, ConfigurationException {
        // Arrange
        String input = "{\n"
                + "\"emptyArray\" : []\n"
                + "}";
        JsonConfiguration sut = new JsonConfiguration();

        // Act
        sut.read(new StringReader(input));

        // Assert
        assertThat(sut.getProperty("emptyArray"), is(nullValue()));
    }

    @Test
    public void nullIsIgnored() throws IOException, ConfigurationException {
        // Arrange
        String input = "{\n"
                + "\"nullValue\" : null\n"
                + "}";
        JsonConfiguration sut = new JsonConfiguration();

        // Act
        sut.read(new StringReader(input));

        // Assert
        assertThat(sut.getProperty("nullValue"), is(nullValue()));
    }

}
