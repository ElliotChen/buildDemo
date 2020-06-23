# Gradle － (1) 從零開始

## 起因
Build Tool我已經習慣用Maven了，在目前基本上沒有特別想換的念頭，大部份的需求都是符合的，沒有不足或不方便到一定要找其他解決方案，但是因為愈來愈多的Open Source，甚至Android都只用Gradle，在有點被半強迫下開始用Gradle。

我會試著用Maven的角度來介紹Gradle，最後也會試著比較相同功能，兩種不同的寫法。

本文原本是以Gradle 4.1版做為基礎所寫，但Gradle 5後更動不少，雖配合修正，但仍恐有未盡完善確認之處，還請見諒。

## 零、總結

重點寫在最前面，廢話不是我專長。

目的 | Gradle | Maven
-----|-----|-----
安裝 | brew install gradle | brew install maven
建立空專案 | gradle init --type java-application | mvn archetype:generate
可用的建構階段 | gradle tasks --all | mvn help:describe -Dcmd=install
執行建構 | gradle build | mvn package
檢視Dependency Tree | gradle dependencies | mvn dependency:tree

## 一、安裝

使用Homebrew最方便，不過要先確認有**```JAVA_HOME```**的設定

```cmd
brew install gradle
```

將**```GRADLE_HOME```**加入環境變數

```cmd
export GRADLE_HOME=`brew --prefix gradle`/libexec
```

確認安裝結果

```cmd
$ gradle -v

------------------------------------------------------------
Gradle 6.4.1
------------------------------------------------------------

Build time:   2020-05-15 19:43:40 UTC
Revision:     1a04183c502614b5c80e33d603074e0b4a2777c5

Kotlin:       1.3.71
Groovy:       2.5.10
Ant:          Apache Ant(TM) version 1.10.7 compiled on September 1 2019
JVM:          14.0.1 (Oracle Corporation 14.0.1+7)
OS:           Mac OS X 10.15.3 x86_64

```

一開始，其中的Groovy及Ant會是用安裝包裡內建的，與外部環境沒有關連。

## 二、建立基本Java Project

執行**```gradle init --type java-application --dsl kotlin```**，可以產生基本的Java Project，可以把**```gradle init --type ＊＊＊```**當作是**```mvn archetype:generate```**，```--type```後面的參數可使用不同類型的範本名稱。

```cmd
$ gradle init --type java-application --dsl kotlin
```

產生的檔案如下

```cmd
-rw-r--r--   1 elliot  staff   103 Sep 12 08:21 .gitignore
drwxr-xr-x   4 elliot  staff   128 Sep 12 08:20 .gradle
-rw-r--r--   1 elliot  staff  1154 Sep 12 08:21 build.gradle.kts
drwxr-xr-x   3 elliot  staff    96 Sep 12 08:20 gradle
-rwxr-xr-x   1 elliot  staff  5960 Sep 12 08:20 gradlew
-rw-r--r--   1 elliot  staff  2942 Sep 12 08:20 gradlew.bat
-rw-r--r--   1 elliot  staff   351 Sep 12 08:21 settings.gradle.kts
drwxr-xr-x   4 elliot  staff   128 Sep 12 08:21 src
```

重點放在**```build.gradle.kts```**及**```settings.gradle.kts```**上面，Gradle會參照**```build.gradle.kts```**裡的描述來建構專案，而會將**```settings.gradle.kts```**裡的設定引入**```build.gradle.kts```**之中。

ps: Maven可用的完整指令 

```
mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-quickstart
```

其他可用的type可以參考 [Gradle Build Types](https://docs.gradle.org/current/userguide/build_init_plugin.html#supported_gradle_build_types)

## 三、build.gradle

先看看**```build.gradle`.kts``**

```gradle
plugins {
    java
    application
}

repositories {
    jcenter()
}

dependencies {
    // This dependency is used by the application.
    implementation("com.google.guava:guava:29.0-jre")
    
    // Use JUnit Jupiter API for testing.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    
    // Use JUnit Jupiter Engine for testing.
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

application {
    // Define the main class for the application
    mainClassName = "tw.elliot.App"
}
```

### 1. plugin: id 'java'
Gradle透過各種Plugin，來支援各種不同類型的程式語言建構工作，所以當看到**```plugins { id 'java'}```**時，可以知道，這個Project是一個Java Project.

目前直接支援的程式語言有```java```、```groovy```、```scala```、```antlr```。

Gradle的Plugin也像Maven一樣有分build或report類型，但表示法比較簡單，以Maven來說需要設定```<build><plugins></plugins></build>``` 、 ```<reporting><plugins></plugins></reporting>```兩處的plugin，Gradle就統一只用```plugins```即可

reference : [Standard Gradle plugins](https://docs.gradle.org/current/userguide/standard_plugins.html)

### 2. repositories

跟Maven相同，Gradle裡也能使用公用的library repository；基本上可以使用的設定有

設定值 | 說明
-----|-----
mavenCentral() | 公開的Maven Repository
jcenter() | Bintray’s JCenter
google() | Goole Maven Repository
mavenLocal() | Local 端的Maven Repositoy

其他ivy或自建的就先不提了。


### 3. dependencies

跟Maven一樣，也要區分是測試用的還是執行時用的，分類跟使用的Plugin有關，如果是Java的話，可以參考[Grale Java Plugin: Dependency Management](https://docs.gradle.org/current/userguide/java_plugin.html#sec:java_plugin_and_dependency_management)。

至於Maven有哪些Scope，可以參考這裡[Maven Dependency Scope](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)

設定值 | 說明
-----|-----
implementation | 同Maven compile，編譯，執行時使用
testImplementation | 同Maven test，編譯，執行Test時使用
runtimeOnly | 同Maven runtime，執行時使用

### 4. src

範本裡已有指定基本的src

```
└── src
    ├── main
    │   ├── java
    │   │   └── tw
    │   │       └── elliot
    │   │           └── App.java
    │   └── resources
    └── test
        ├── java
        │   └── tw
        │       └── elliot
        │           └── AppTest.java
        └── resources
```

跟用Maven建出來的相同，可以有預設的package名稱。

需要修改的話，Gradle可以用```sourceSets```來指定

```
sourceSets {
    main {
        java {
            srcDirs("src/main/java")
        }
        resources {
            srcDirs("src/main/resources")
        }
    }
    test {
        java.srcDirs("src/test/java")
        resources.srcDirs("src/tes/resources")
    }
}
```

可用的```sourceSets```可以參考[Java Project Layout](https://docs.gradle.org/current/userguide/java_plugin.html#sec:java_project_layout)，注意的是，這是```java project```的layout.

### 5. build

Maven是依據著Lifecycle跟Phase，還有pom.xml中設定的```package```類型來做建構，有哪些Lifecycle可參考[Maven Lifecycles](http://maven.apache.org/ref/3.5.0/maven-core/lifecycles.html)及[Maven Package Binding](http://maven.apache.org/ref/3.5.0/maven-core/default-bindings.html)，實際執行上比較大的影響是```package```類型，無論是```jar```、```war```還是```ear```，要完整建構的話可以只要下```mvn package```即可。。

而Gradle也有Lifecycle，但Gradle實際執行的是所謂的```task```，會依據```apply plugin```而改變，所以要如何來build，是要參考plugin裡的說明，例如[Gradle Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html)裡，就會有註明相關的lifecycle task有哪些，像是有```build ```、```test```、```jar```等，所以```jar```、```war```或是```ear```，要建構可能會有區分，實際上全部用```gradle build```基本上也沒問題。

比較快的方式是執行***```gradle tasks --all```***，可以馬上看到現有可執行的tasks

```
$ gradle tasks --all

> Task :tasks

------------------------------------------------------------
All tasks runnable from root project
------------------------------------------------------------

Application tasks
-----------------
run - Runs this project as a JVM application

Build tasks
-----------
assemble - Assembles the outputs of this project.
build - Assembles and tests this project.
classes - Assembles main classes.
clean - Deletes the build directory.
jar - Assembles a jar archive containing the main classes.
testClasses - Assembles test classes.

.....

Verification tasks
------------------
check - Runs all checks.
test - Runs the unit tests.

```

實際執行***```gradle -i build```***看看，多一個```-i```的參數是為了可以看到實際過程中執行的各個task

```
> Configure project :
> Task :compileJava
> Task :processResources
> Task :classes
> Task :jar
> Task :startScripts
> Task :distTar
> Task :distZip
> Task :assemble
> Task :compileTestJava
> Task :processTestResources
> Task :testClasses
> Task :test
> Task :check
> Task :build
BUILD SUCCESSFUL in 0s
```


至於想瞭解Maven有哪些phase可用，可以執行***```mvn help:describe -Dcmd=install```***

```
$ mvn help:describe -Dcmd=install
....
It is a part of the lifecycle for the POM packaging 'jar'. This lifecycle includes the following phases:
* validate: Not defined
* initialize: Not defined
* generate-sources: Not defined
* process-sources: Not defined
* generate-resources: Not defined
* process-resources: org.apache.maven.plugins:maven-resources-plugin:2.6:resources
* compile: org.apache.maven.plugins:maven-compiler-plugin:3.1:compile
* process-classes: Not defined
* generate-test-sources: Not defined
* process-test-sources: Not defined
* generate-test-resources: Not defined
* process-test-resources: org.apache.maven.plugins:maven-resources-plugin:2.6:testResources
* test-compile: org.apache.maven.plugins:maven-compiler-plugin:3.1:testCompile
* process-test-classes: Not defined
* test: org.apache.maven.plugins:maven-surefire-plugin:2.12.4:test
* prepare-package: Not defined
* package: org.apache.maven.plugins:maven-jar-plugin:2.4:jar
* pre-integration-test: Not defined
* integration-test: Not defined
* post-integration-test: Not defined
* verify: Not defined
* install: org.apache.maven.plugins:maven-install-plugin:2.4:install
* deploy: org.apache.maven.plugins:maven-deploy-plugin:2.7:deploy
```

 ## Reference
 
 [4.6 中文版使用指南](https://doc.yonyoucloud.com/doc/wiki/project/GradleUserGuide-Wiki/index.html)