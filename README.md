# NSQ Plugin for Graylog

[![Github Downloads](https://img.shields.io/github/downloads/condevtec/graylog-plugin-nsq/total.svg)](https://github.com/condevtec/graylog-plugin-nsq/releases)
[![GitHub Release](https://img.shields.io/github/release/condevtec/graylog-plugin-nsq.svg)](https://github.com/condevtec/graylog-plugin-nsq/releases)
[![Build Status](https://travis-ci.org/condevtec/graylog-plugin-nsq.svg?branch=master)](https://travis-ci.org/condevtec/graylog-plugin-nsq)

**Required Graylog version:** 2.2.0 and later

This plugin provides inputs for the [NSQ protocol](http://nsq.io/) in Graylog


## Installation

1. [Download](https://github.com/condevtec/graylog-plugin-nsq/releases) the plugin and place the JAR file in your Graylog plugin directory.
2. Restart Graylog


## Build

This project is using Maven and requires Java 8 or higher.
You can build the plugin (JAR) with `mvn package`.
DEB and RPM packages can be build with `mvn jdeb:jdeb` or `mvn rpm:rpm`.


## Plugin Release

In order to release a new version of the plugin, run the following commands:

```
$ mvn release:prepare
$ mvn release:perform
```

This sets the version numbers, creates a tag and pushes to GitHub.

## License

Copyright (c) 2016-2017 Reimund Klain - Condevtec

This library is licensed under the GNU General Public License, Version 3.0.

See https://www.gnu.org/licenses/gpl-3.0.html or the LICENSE.txt file in this repository for the full license text.
