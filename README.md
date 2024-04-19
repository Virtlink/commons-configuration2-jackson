# Jackson for Commons Configuration 2

[![Build](https://github.com/virtlink/commons-configuration2-jackson/actions/workflows/build.yaml/badge.svg)][1]
[![JavaDoc](https://javadoc.io/badge2/com.virtlink.commons/commons-configuration2-jackson/javadoc.svg)][2]
[![GitHub](https://img.shields.io/github/v/release/Virtlink/commons-configuration2-jackson)][3]
[![Maven Central](https://img.shields.io/maven-central/v/com.virtlink.commons/commons-configuration2-jackson)][4]
[![License](https://img.shields.io/github/license/Virtlink/commons-configuration2-jackson)][5]

This project adds support for FasterXML's Jackson to [Apache Commons Configuration 2][6].

## Installation
Add the library as a dependency to your project. In Gradle, add to your `dependencies` block

```groovy
implementation 'com.virtlink.commons:commons-configuration2-jackson:1.3.4'
```

Or in Gradle Kotlin DSL, add to your `dependencies` block

```kotlin
implementation("com.virtlink.commons:commons-configuration2-jackson:1.3.4")
```

In Maven, add to your `<dependencies>` tag

```maven-pom
<dependency>
    <groupId>com.virtlink.commons</groupId>
    <artifactId>commons-configuration2-jackson</artifactId>
    <version>1.3.4</version>
</dependency>
```

Otherwise download the [latest release][3] and add the `.jar` to your project's classpath.


## Usage
Import the project's namespace.

```java
import com.virtlink.commons.configuration2.jackson.JsonConfiguration;
```

Instantiate the configuration class directly

```java
JsonConfiguration config = new JsonConfiguration();
```

or create a new configuration through the configuration builder,

```java
import org.apache.commons.beanutils.PropertyUtils;

Parameters params = new Parameters();
FileBasedConfigurationBuilder<JsonConfiguration> builder
    = new FileBasedConfigurationBuilder<>(JsonConfiguration.class);
JsonConfiguration config = builder.configure(params
     .fileBased()
     .setFileName("example.json")).getConfiguration();
```

> **Note**: You need to add `commons-beanutils` as a dependency and import
> `org.apache.commons.beanutils.PropertyUtils` to make builders work, otherwise you
> get a `ClassNotFoundException: org.apache.commons.beanutils.PropertyUtils` when creating the
> builder parameters.


You can use the configuration to read from a `Reader`

```java
try (Reader reader = new BufferedReader(new FileReader("settings.json"))) {
	config.read(reader);
}
```

or write to a `Writer`

```java
try (Writer writer = new PrintWriter("settings.json", "UTF-8")) {
	config.write(writer);
}
```

Also refer to the [API documentation][2].

## Questions and Issues
If you have a question, ask it on [Stack Overflow][7] under the `apache-commons-config` tag.

If you have an issue or found a bug, please search the [list of reported issues][8]
for a solution or workaround, or create a new issue.


## Copyright and License
Copyright © 2015-2023 - Daniel A. A. Pelsmaeker

The code and files in this project are licensed under the [Apache License, Version 2.0][9].
You may use the files in this project in compliance with the license.



[1]: https://github.com/Virtlink/commons-configuration2-jackson/actions/workflows/build.yaml
[2]: https://javadoc.io/doc/com.virtlink.commons/commons-configuration2-jackson
[3]: https://github.com/Virtlink/commons-configuration2-jackson/releases/latest
[4]: https://mvnrepository.com/artifact/com.virtlink.commons/commons-configuration2-jackson
[5]: https://github.com/Virtlink/commons-configuration2-jackson/blob/master/LICENSE
[6]: https://commons.apache.org/proper/commons-configuration/
[7]: https://stackoverflow.com/questions/tagged/apache-commons-config
[8]: https://github.com/Virtlink/commons-configuration2-jackson/issues
[9]: https://www.apache.org/licenses/LICENSE-2.0


