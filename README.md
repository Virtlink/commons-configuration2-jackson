# Jackson for Commons Configuration 2
This project adds support for FasterXML's Jackson to Apache Commons
Configuration 2.

[![Travis](https://img.shields.io/travis/Virtlink/commons-configuration2-jackson.svg)](https://travis-ci.org/Virtlink/commons-configuration2-jackson) [![GitHub license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/Virtlink/commons-configuration2-jackson/blob/master/LICENSE)

[![Build Status](https://travis-ci.org/Virtlink/commons-configuration2-jackson.svg)](https://travis-ci.org/Virtlink/commons-configuration2-jackson) [![GitHub version](https://badge.fury.io/gh/Virtlink%2Fcommons-configuration2-jackson.svg)](https://badge.fury.io/gh/Virtlink%2Fcommons-configuration2-jackson) 

## Installation
Download the latest release from the [Releases page](https://github.com/Virtlink/commons-configuration2-jackson/releases) and place the `.jar` in your project.


## Usage
Import the project's namespace.

```
import com.virtlink.commons.configuration2.jackson.JsonConfiguration;
```

Create a new configuration through one of the configuration builders,

```
Parameters params = new Parameters();
BasicConfigurationBuilder<JsonConfiguration> builder =
    new BasicConfigurationBuilder<JsonConfiguration>(JsonConfiguration.class)
        .configure(params.basic());
JsonConfiguration config = builder.getConfiguration();
```

or instantiate the configuration class directly.

```
JsonConfiguration config = new JsonConfiguration();
```

You can use the configuration to read from a `Reader`

```
try (Reader reader = new BufferedReader(new FileReader("settings.json"))) {
	config.read(reader);
}
```

or write to a `Writer`

```
try (Writer writer = new PrintWriter("settings.json", "UTF-8")) {
	config.write(writer);
}
```.


## Questions and Issues
If you have a question, ask it on [Stack Overflow](https://stackoverflow.com/questions/tagged/apache-commons-config)
under the `apache-commons-config` tag.

If you have an issue or found a bug, please search the
[list of reported issues](https://github.com/Virtlink/commons-configuration2-jackson/issues)
for a solution or workaround, or create a new issue.


## Copyright and License
Copyright © 2015 - Daniel Pelsmaeker

The code and files in this project are licensed under the
[Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
You may use the files in this project in compliance with the license.



