[中文文档](./README_CN.md) | [English Document](./README.md)        

# Fastjson-replacement
[![codecov](https://codecov.io/gh/qingmo/fastjson-replacement/branch/main/graph/badge.svg?token=OZQG1NVXDX)](https://codecov.io/gh/qingmo/fastjson-replacement)

## `Fastjson-replacement`是什么    
`Fastjson-replacement` 是一个用于替换`Fastjson`实现替换为`Jackson`实现的桥接模式工具，这让习惯于`Fastjson`用法的开发者或者已经大批量使用`Fastjson`的遗留工程代码可以轻松的做出改变，而无需改变现有的使用习惯与人体工程学体验。

## 特性
* 支持`java.util.Date`, `java 8 jsr310 time packages`(`LocalDate`,`LocalDateTime`,`LocalTime`)        
* 支持多种时间格式反序列化（目前：`yyyy-MM-dd HH:mm:ss`,`yyyy-MM-dd`)

## 开发原因

为什么要替换`fastjson`? 因为其漏洞多，修复速度慢。

为什么重造轮子？测试完备度不够，没有自动发布和及时更新版本。

## 使用方法

**步骤1**:添加依赖

使用默认的`jackson`版本（当前`1.12.3`) 

`maven`

```xml
<dependency>
    <groupId>io.github.qingmo</groupId>
    <artifact>fastjson-replacement</artifact>
    <version>${currentVersion}</version>
</dependency>
```

`gradle`

```kotlin
implementation("io.github.qingmo:fastjson-replacement:${currentVersion}")
```



使用自定义版本的`jackson`版本

`maven`

```xml
<dependency>
    <groupId>io.github.qingmo</groupId>
    <artifactId>fastjson-replacement</artifactId>
    <version>${currentVersion}</version>
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
implementation("io.github.qingmo:fastjson-replacement:${currentVersion}") {
		exclude(group = "com.fasterxml.jackson.core")
    exclude(group = "com.fasterxml.jackson"
}
```

**步骤2**:替换fastjson的包

**替换方法**

```shell
import com.alibaba.fastjson.JSON
替换为
import io.github.qingmo.json.JSON

import com.alibaba.fastjson.JSONArray
替换为
import io.github.qingmo.json.JSONArray

import com.alibaba.fastjson.JSONObject
替换为
import io.github.qingmo.json.JSONObject
```



## 参考
[jackson-datatype-fastjson](https://github.com/larva-zhang/jackson-datatype-fastjson/blob/master/src/test/java/com/github/larva/zhang/jackson/datatype/fastjson/SimpleReadTest.java)

[Jackson替换fastjson](https://www.cnblogs.com/larva-zhh/p/11544317.html)

[如何让jackson与kotlin友好相处](https://cloud.tencent.com/developer/article/1372442)