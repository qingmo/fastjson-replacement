[中文文档](./README_CN.md) | [English Document](./README.md)

# Fastjson-replacement
[![codecov](https://codecov.io/gh/qingmo/fastjson-replacement/branch/main/graph/badge.svg?token=OZQG1NVXDX)](https://codecov.io/gh/qingmo/fastjson-replacement) [![License](https://img.shields.io/badge/License-MIT-brightgreen)](https://mit-license.org/) [![quality gate](https://sonarcloud.io/api/project_badges/measure?project=qingmo_fastjson-replacement&metric=alert_status)](https://sonarcloud.io/dashboard?id=qingmo_fastjson-replacement) [![Codacy Security Scan](https://github.com/qingmo/fastjson-replacement/actions/workflows/codacy-analysis.yml/badge.svg)](https://github.com/qingmo/fastjson-replacement/actions/workflows/codacy-analysis.yml) [![build](https://github.com/qingmo/fastjson-replacement/actions/workflows/build.yml/badge.svg)](https://github.com/qingmo/fastjson-replacement/actions/workflows/build.yml)

current version of 1.0 is not product ready, for `jackson-module-kotlin` depends on kotlin-reflect bad benchmark scores in deserialize.
i'm waiting for https://github.com/FasterXML/jackson-module-kotlin/pull/439
## What's `Fastjson-replacement`?
`Fastjson-replacement` is a Bridge Pattern for developers or lagency projects which are used to `Fastjson` usage to replace  
`Fastjson` implementation with `Jackson` implementation.

## Features
* support `java.util.Date`, `java 8 jsr310 time packages`(`LocalDate`,`LocalDateTime`,`LocalTime`)
* support multiple Date Format Deserialize（Current:`yyyy-MM-dd HH:mm:ss`,`yyyy-MM-dd`)

## Why

Why do we need replace `fastjson`? For it security issues, late of fix period and so on.

Why make new tools instead of using existing one? Because their bad tests(even in fastjson), and not auto publish after merged my pull requests that make me can not get new fix version from maven central repository.

## How to Use

**Step 1**: Add Dependencies

Use default `jackson`version（current version is:`1.12.3`) proivded by current lib.

`maven`

```xml
<dependency>
    <groupId>io.github.qingmo</groupId>
    <artifact>fastjson-replacement</artifact>
    <version>1.0.1</version>
</dependency>
```

`gradle`

```kotlin
implementation("io.github.qingmo:fastjson-replacement:1.0.1")
```



Use custom `jackson`version defined by user's project

`maven`

```xml
<dependency>
    <groupId>io.github.qingmo</groupId>
    <artifactId>fastjson-replacement</artifactId>
    <version>1.0.1</version>
    <exclusions>
        <exclusion>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </exclusion>
        <exclusion>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-annotations</artifactId>
        </exclusion>
        <exclusion>
            <groupId>com.fasterxml.jackson</groupId>
            <artifactId>jackson-bom</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

`gradle`

```kotlin
implementation("io.github.qingmo:fastjson-replacement:1.0.1") {
    exclude(group = "com.fasterxml.jackson.core")
    exclude(group = "com.fasterxml.jackson")
}
```

**Step 2**: Replace `fastjson` packages

**Replace method**

```shell
import com.alibaba.fastjson.JSON
replace with
import io.github.qingmo.json.JSON

import com.alibaba.fastjson.JSONArray
replace with
import io.github.qingmo.json.JSONArray

import com.alibaba.fastjson.JSONObject
replace with
import io.github.qingmo.json.JSONObject
```



## References
[jackson-datatype-fastjson](https://github.com/larva-zhang/jackson-datatype-fastjson/blob/master/src/test/java/com/github/larva/zhang/jackson/datatype/fastjson/SimpleReadTest.java)

[Jackson替换fastjson](https://www.cnblogs.com/larva-zhh/p/11544317.html)

[如何让jackson与kotlin友好相处](https://cloud.tencent.com/developer/article/1372442)

