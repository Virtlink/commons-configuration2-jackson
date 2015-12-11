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
     * @param factory The Jackson factory to use.
     */
    protected JacksonConfiguration(final JsonFactory factory) {
        this(factory, null);
    }

    /**
     * Initializes a new instance of the {@link JacksonConfiguration} class.
     *
     * @param factory The Jackson factory to use.
     * @param config  The configuration whose nodes to copy into this configuration.
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
     * @param inputStream The input stream to read from.
     * @throws ConfigurationException
     * @throws IOException
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
     * @param reader The reader to read from.
     * @throws ConfigurationException
     * @throws IOException
     */
    @Override
    public void read(final Reader reader) throws ConfigurationException, IOException {
        Preconditions.checkNotNull(reader);

        HashMap<String, Object> settings = this.mapper.readValue(reader, HASH_MAP_TYPE_REFERENCE);
        ImmutableNode rootNode = toNodes(null, settings).iterator().next();
        this.getSubConfigurationParentModel().mergeRoot(rootNode, null, null, null, this);
    }

    /**
     * Writes the configuration.
     *
     * @param writer The writer to write to.
     * @throws ConfigurationException
     * @throws IOException
     */
    @Override
    public void write(final Writer writer) throws ConfigurationException, IOException {
        Preconditions.checkNotNull(writer);

        @SuppressWarnings("unchecked")
        HashMap<String, Object> settings = (HashMap<String, Object>) fromNode(this.getModel().getInMemoryRepresentation());
        this.mapper.writerWithDefaultPrettyPrinter().writeValue(writer, settings);
    }

    /**
     * Sets the file locator to use for the next invocation of {@link #read(InputStream)} or {@link #write(Writer)}.
     *
     * @param locator The file locator to use; or <code>null</code>.
     */
    @Override
    public void initFileLocator(@Nullable final FileLocator locator) {
        this.locator = locator;
    }

    /**
     * Gets a node from a serialization object.
     *
     * @param name The name of the node.
     * @param tree The tree of values.
     * @return A node representing the tree.
     */
    private Collection<ImmutableNode> toNodes(String name, Map<String, Object> tree) {
        Builder builder = new Builder();
        if (name != null)
            builder.name(name);
        for (Map.Entry<String, Object> entry : tree.entrySet()) {
            Collection<ImmutableNode> nodes = toNodes(entry.getKey(), entry.getValue());
            builder.addChildren(nodes);
        }
        return Collections.singletonList(builder.create());
    }

    /**
     * Gets a node from a serialization object.
     *
     * @param name The name of the node.
     * @param list The list of values.
     * @return All the nodes representing the objects in the list.
     */
    private Collection<ImmutableNode> toNodes(String name, List<Object> list) {
        ArrayList<ImmutableNode> nodes = new ArrayList<>();
        for (Object value : list) {
            Collection<ImmutableNode> children = toNodes(name, value);
            nodes.addAll(children);
        }
        return nodes;
    }

    /**
     * Gets a node from a serialization object.
     *
     * @param name  The name of the node.
     * @param value The value of the node.
     * @return A single node representing the object.
     */
    private Collection<ImmutableNode> toNodes(String name, Object value) {
        if (value == null) {
            return Collections.emptyList();
        } else if (value instanceof Map) {
            return toNodes(name, (Map<String, Object>) value);
        } else if (value instanceof List) {
            return toNodes(name, (List<Object>) value);
        } else {
            return Collections.singletonList(new Builder().name(name).value(value).create());
        }
    }

    /**
     * Gets a serialization object from a node.
     *
     * @param node The node.
     * @return The object representing the node.
     */
    private Object fromNode(ImmutableNode node) {
        if (!node.getChildren().isEmpty()) {
            Map<String, List<ImmutableNode>> children = getChildrenWithName(node);

            HashMap<String, Object> map = new HashMap<>();
            for (Map.Entry<String, List<ImmutableNode>> entry : children.entrySet()) {
                assert entry.getValue().size() > 0;
                if (entry.getValue().size() == 1) {
                    // Just one node.
                    ImmutableNode child = entry.getValue().get(0);
                    Object childValue = fromNode(child);
                    map.put(entry.getKey(), childValue);
                } else {
                    // Multiple nodes.
                    ArrayList<Object> list = new ArrayList<>();
                    for (ImmutableNode child : entry.getValue()) {
                        Object childValue = fromNode(child);
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

    private Map<String, List<ImmutableNode>> getChildrenWithName(ImmutableNode node) {
//        return node.getChildren()
//                .stream()
//                .collect(Collectors.groupingBy(ImmutableNode::getNodeName));
        Map<String, List<ImmutableNode>> children = new HashMap<>();
        for (ImmutableNode child : node.getChildren()) {
            String name = child.getNodeName();
            if (!children.containsKey(name)) {
                children.put(name, new ArrayList<ImmutableNode>());
            }
            children.get(name).add(child);
        }
        return children;
    }
}
