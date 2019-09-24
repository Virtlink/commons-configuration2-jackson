[![Travis](https://img.shields.io/travis/Virtlink/commons-configuration2-jackson.svg)](https://travis-ci.org/Virtlink/commons-configuration2-jackson)
[![GitHub version](https://badge.fury.io/gh/Virtlink%2Fcommons-configuration2-jackson.svg)](https://github.com/Virtlink/commons-configuration2-jackson/releases/latest)
[![Bintray](https://api.bintray.com/packages/virtlink/maven/commons-configuration2-jackson/images/download.svg)](https://bintray.com/virtlink/maven/commons-configuration2-jackson/_latestVersion)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.virtlink.commons/commons-configuration2-jackson/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.virtlink.commons/commons-configuration2-jackson)
[![GitHub license](https://img.shields.io/github/license/Virtlink/commons-configuration2-jackson.svg)](https://github.com/Virtlink/commons-configuration2-jackson/blob/master/LICENSE)

# Jackson for Commons Configuration 2
This project adds support for FasterXML's Jackson to [Apache Commons Configuration 2](https://commons.apache.org/proper/commons-configuration/).


## Installation
Add the library as a dependency to your project. In Gradle, add to your `dependencies` block

```gradle
compile 'com.virtlink.commons:commons-configuration2-jackson:0.7.0'
```

In Maven, add to your `<dependencies>` tag

```maven-pom
<dependency>
    <groupId>com.virtlink.commons</groupId>
    <artifactId>commons-configuration2-jackson</artifactId>
    <version>0.8.0</version>
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


## Questions and Issues
If you have a question, ask it on [Stack Overflow][2] under the `apache-commons-config` tag.

If you have an issue or found a bug, please search the [list of reported issues][3]
for a solution or workaround, or create a new issue.


## Copyright and License
Copyright © 2015-2016 - Daniel Pelsmaeker

The code and files in this project are licensed under the [Apache License, Version 2.0][4].
You may use the files in this project in compliance with the license.



[1]: https://github.com/Virtlink/commons-configuration2-jackson/releases/latest
[2]: https://stackoverflow.com/questions/tagged/apache-commons-config
[3]: https://github.com/Virtlink/commons-configuration2-jackson/issues
[4]: https://www.apache.org/licenses/LICENSE-2.0
