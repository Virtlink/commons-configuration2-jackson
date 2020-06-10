[![Travis](https://img.shields.io/travis/Virtlink/commons-configuration2-jackson)][8]
[![javadoc](https://javadoc.io/badge2/com.virtlink.commons/commons-configuration2-jackson/javadoc.svg)][5]
[![GitHub](https://img.shields.io/github/v/release/Virtlink/commons-configuration2-jackson)][1]
[![Bintray](https://img.shields.io/bintray/v/virtlink/maven/commons-configuration2-jackson)][6]
[![Maven Central](https://img.shields.io/maven-central/v/com.virtlink.commons/commons-configuration2-jackson)][7]
[![License](https://img.shields.io/github/license/Virtlink/commons-configuration2-jackson)][9]

# Jackson for Commons Configuration 2
This project adds support for FasterXML's Jackson to [Apache Commons Configuration 2][10].


## Installation
Add the library as a dependency to your project. In Gradle, add to your `dependencies` block

```gradle
compile 'com.virtlink.commons:commons-configuration2-jackson:0.10.0'
```

In Maven, add to your `<dependencies>` tag

```maven-pom
<dependency>
    <groupId>com.virtlink.commons</groupId>
    <artifactId>commons-configuration2-jackson</artifactId>
    <version>0.10.0</version>
</dependency>
```

Otherwise download the [latest release][1]
and add the `.jar` to your project's classpath.


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

Also refer to the [API documentation][5].

## Questions and Issues
If you have a question, ask it on [Stack Overflow][2] under the `apache-commons-config` tag.

If you have an issue or found a bug, please search the [list of reported issues][3]
for a solution or workaround, or create a new issue.


## Copyright and License
Copyright © 2015-2020 - Daniel Pelsmaeker

The code and files in this project are licensed under the [Apache License, Version 2.0][4].
You may use the files in this project in compliance with the license.



[1]: https://github.com/Virtlink/commons-configuration2-jackson/releases/latest
[2]: https://stackoverflow.com/questions/tagged/apache-commons-config
[3]: https://github.com/Virtlink/commons-configuration2-jackson/issues
[4]: https://www.apache.org/licenses/LICENSE-2.0
[5]: https://javadoc.io/doc/com.virtlink.commons/commons-configuration2-jackson
[6]: https://bintray.com/virtlink/maven/commons-configuration2-jackson/_latestVersion
[7]: https://maven-badges.herokuapp.com/maven-central/com.virtlink.commons/commons-configuration2-jackson
[8]: https://travis-ci.org/Virtlink/commons-configuration2-jackson
[9]: https://github.com/Virtlink/commons-configuration2-jackson/blob/master/LICENSE
[10]: https://commons.apache.org/proper/commons-configuration/