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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileLocator;
import org.apache.commons.configuration2.io.FileLocatorAware;
import org.apache.commons.configuration2.io.InputStreamSupport;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.commons.configuration2.tree.ImmutableNode.Builder;

import javax.annotation.Nullable;
import java.io.*;
import java.util.*;

/**
 * A configuration using a Jackson language (e.g. YAML, JSON).
 */
@SuppressWarnings("RedundantThrows")
public abstract class JacksonConfiguration extends BaseHierarchicalConfiguration implements FileBasedConfiguration, InputStreamSupport, FileLocatorAware {

    private static final TypeReference<HashMap<String, Object>> HASH_MAP_TYPE_REFERENCE
            = new TypeReference<HashMap<String, Object>>() {};
    private final SimpleModule module;
    private final ObjectMapper mapper;

    /**
     * The {@link FileLocator} that may specify where the source/destination file is.
     * <p>
     * This is for use by derived classses, if necessary.
     */
    @Nullable
    protected FileLocator locator = null;

    /**
     * Initializes a new instance of the {@link JacksonConfiguration} class.
     *
     * @param factory the Jackson factory to use
     */
    protected JacksonConfiguration(final JsonFactory factory) {
        this(factory, null);
    }

    /**
     * Initializes a new instance of the {@link JacksonConfiguration} class.
     *
     * @param factory the Jackson factory to use
     * @param config  the configuration whose nodes to copy into this configuration
     */
    protected JacksonConfiguration(final JsonFactory factory, final HierarchicalConfiguration<ImmutableNode> config) {
        super(config);

        Preconditions.checkNotNull(factory);

        this.module = new SimpleModule();
        this.mapper = new ObjectMapper(factory);
        this.mapper.registerModule(this.module);
    }

    /**
     * Reads the configuration.
     *
     * @param inputStream the input stream to read from
     * @throws IOException if an I/O error occurs
     * @throws ConfigurationException if a non-I/O related problem occurs
     */
    @Override
    public void read(final InputStream inputStream) throws ConfigurationException, IOException {
        try (Reader reader = new InputStreamReader(inputStream)) {
            read(reader);
        }
    }

    /**
     * Reads the configuration.
     *
     * @param reader the reader to read from
     * @throws IOException if an I/O error occurs
     * @throws ConfigurationException if a non-I/O related problem occurs
     */
    @Override
    public void read(final Reader reader) throws ConfigurationException, IOException {
        Preconditions.checkNotNull(reader);

        final HashMap<String, Object> settings = this.mapper.readValue(reader, HASH_MAP_TYPE_REFERENCE);
        final ImmutableNode rootNode = toNode(new Builder(), settings);
        this.getSubConfigurationParentModel().mergeRoot(rootNode, null, null, null, this);
    }

    /**
     * Writes the configuration.
     *
     * @param writer the writer to write to
     * @throws IOException if an I/O error occurs
     * @throws ConfigurationException if a non-I/O related problem occurs
     */
    @Override
    public void write(final Writer writer) throws ConfigurationException, IOException {
        Preconditions.checkNotNull(writer);

        @SuppressWarnings("unchecked")
        final HashMap<String, Object> settings = (HashMap<String, Object>) fromNode(this.getModel().getInMemoryRepresentation());
        this.mapper.writerWithDefaultPrettyPrinter().writeValue(writer, settings);
    }

    /**
     * Sets the file locator to use for the next invocation of {@link #read(InputStream)} or {@link #write(Writer)}.
     *
     * @param locator the file locator to use; or {@code null}
     */
    @Override
    public void initFileLocator(@Nullable final FileLocator locator) {
        this.locator = locator;
    }

    /**
     * Creates a node for the specified object.
     *
     * @param builder the node builder
     * @param obj the object
     * @return the created node
     */
    private ImmutableNode toNode(final Builder builder, final Object obj) {
        assert !(obj instanceof List);

        if (obj instanceof Map) {
            @SuppressWarnings("unchecked")
            final Map<String, Object> map = (Map<String, Object>) obj;
            return mapToNode(builder, map);
        } else {
            return valueToNode(builder, obj);
        }
    }

    /**
     * Creates a node for the specified map.
     *
     * @param builder the node builder
     * @param map the map
     * @return the created node
     */
    private ImmutableNode mapToNode(final Builder builder, final Map<String, Object> map) {
        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            if (value instanceof List) {
                // For a list, add each list item as a child of this node.
                for (final Object item : (List<?>)value) {
                    addChildNode(builder, key, item);
                }
            } else {
                // Otherwise, add the value as a child of this node.
                addChildNode(builder, key, value);
            }
        }
        return builder.create();
    }

    /**
     * Adds a child node to the specified builder.
     *
     * @param builder the builder to add the node to
     * @param name the name of the node
     * @param value the value of the node
     */
    private void addChildNode(final Builder builder, final String name, final Object value) {
        assert !(value instanceof List);

        final Builder childBuilder = new Builder();
        // Set the name of the child node.
        childBuilder.name(name);
        // Set the value of the child node.
        final ImmutableNode childNode = toNode(childBuilder, value);
        // Add the node to the children of the node being built.
        builder.addChild(childNode);
    }

    /**
     * Creates a node for the specified value.
     *
     * @param builder the node builder
     * @param value the value
     * @return the created node
     */
    private ImmutableNode valueToNode(final Builder builder, final Object value) {
        // Set the value of the node being built.
        return builder.value(value).create();
    }

    /**
     * Gets a serialization object from a node.
     *
     * @param node the node
     * @return the object representing the node
     */
    private Object fromNode(final ImmutableNode node) {
        if (!node.getChildren().isEmpty()) {
            final Map<String, List<ImmutableNode>> children = getChildrenWithName(node);

            final HashMap<String, Object> map = new HashMap<>();
            for (final Map.Entry<String, List<ImmutableNode>> entry : children.entrySet()) {
                assert !entry.getValue().isEmpty();
                if (entry.getValue().size() == 1) {
                    // Just one node.
                    final ImmutableNode child = entry.getValue().get(0);
                    final Object childValue = fromNode(child);
                    map.put(entry.getKey(), childValue);
                } else {
                    // Multiple nodes.
                    final ArrayList<Object> list = new ArrayList<>();
                    for (final ImmutableNode child : entry.getValue()) {
                        final Object childValue = fromNode(child);
                        list.add(childValue);
                    }
                    map.put(entry.getKey(), list);
                }
            }
            return map;
        } else {
            return node.getValue();
        }
    }

    /**
     * Returns a map with for each name the child nodes that have that name.
     *
     * @param node the node whose children to search
     * @return a map
     */
    private Map<String, List<ImmutableNode>> getChildrenWithName(final ImmutableNode node) {
        final Map<String, List<ImmutableNode>> children = new HashMap<>();
        for (final ImmutableNode child : node.getChildren()) {
            final String name = child.getNodeName();
            if (!children.containsKey(name)) {
                children.put(name, new ArrayList<ImmutableNode>());
            }
            children.get(name).add(child);
        }
        return children;
    }
}
