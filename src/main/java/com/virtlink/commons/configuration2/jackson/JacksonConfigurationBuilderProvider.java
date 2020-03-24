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

import org.apache.commons.configuration2.builder.FileBasedBuilderParametersImpl;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.combined.BaseConfigurationBuilderProvider;

import java.util.Collections;

/**
 * Configuration builder provider for {@link JacksonConfiguration} objects.
 */
public final class JacksonConfigurationBuilderProvider<T extends JacksonConfiguration> extends BaseConfigurationBuilderProvider{
    public JacksonConfigurationBuilderProvider(final Class<T> configurationClass) {
        super(
            FileBasedConfigurationBuilder.class.getName(),                          // Normal builder.
            ReloadingFileBasedConfigurationBuilder.class.getName(),                 // Reloading builder.
            configurationClass.getName(),                                           // Configuration class.
            Collections.singleton(FileBasedBuilderParametersImpl.class.getName())   // Parameter class.
        );
    }
}