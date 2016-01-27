package com.virtlink.commons.configuration2.jackson;

import org.apache.commons.configuration2.builder.FileBasedBuilderParametersImpl;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.combined.BaseConfigurationBuilderProvider;
import org.apache.commons.configuration2.builder.combined.ConfigurationBuilderProvider;

import java.util.Collections;

/**
 * Configuration builder provider for {@link JacksonConfiguration} objects.
 */
public final class JacksonConfigurationBuilderProvider<T extends JacksonConfiguration> extends BaseConfigurationBuilderProvider{
    public JacksonConfigurationBuilderProvider(Class<T> configurationClass) {
        super(
            FileBasedConfigurationBuilder.class.getName(),                          // Normal builder.
            ReloadingFileBasedConfigurationBuilder.class.getName(),                 // Reloading builder.
            configurationClass.getName(),                                           // Configuration class.
            Collections.singleton(FileBasedBuilderParametersImpl.class.getName())   // Parameter class.
        );
    }
}