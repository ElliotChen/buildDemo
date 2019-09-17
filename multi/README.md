# Gradle － (2) Multiple Projects

## 說明

當今的專案已經少有單一模組的，如何建立多模組的專案已經是必要的。

### Structure

以專案程式碼結構而言，Maven與Gradle是可以共用的，所以用下列的結構做為說明。

1. ```multi```為parent project：做為root project，共用的設定都放在此處。
2. ```core```為library project：使用```multi```做為parent project。
3. ```frontend```為war project：使用```multi```做為parent project，並使用```core```做為library。


```
├── multi
│   ├── core
│   │   ├── build.gradle
│   │   ├── pom.xml
│   │   └── src
│   │       ├── main
│   │       │   ├── java
│   │       │   └── resources
│   │       └── test
│   │           ├── java
│   │           └── resources
│   ├── frontend
│   │   ├── build.gradle
│   │   ├── pom.xml
│   │   └── src
│   │       ├── main
│   │       │   ├── java
│   │       │   └── resources
│   │       └── test
│   │           ├── java
│   │           └── resources
│   ├── build.gradle
│   ├── pom.xml
│   └── settings.gradle
```

## Maven

1. Create Root Parent Project

```cmd
mvn archetype:generate -DarchetypeGroupId=org.codehaus.mojo.archetypes -DarchetypeArtifactId=pom-root -DgroupId=tw.elliot -DartifactId=mps -DinteractiveMode=false
```

2. Create Sub Projects

```cmd
cd parent_folder

mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-quickstart -DgroupId=tw.elliot -DartifactId=core -DinteractiveMode=false

mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-quickstart -DgroupId=tw.elliot -DartifactId=frontend -DinteractiveMode=false

```

3. Edit ```multi/pom.xml```

非重點部份就點點帶過，主要是下列兩項要注意
```packaging```跟```modules```

```
<?xml version="1.0" encoding="UTF-8"?>
<project>
	....
	<packaging>pom</packaging>
	....
	<modules>
		<module>core</module>
		<module>frontend</module>
	</modules>
</project>
```

3. Edit ```multi/core/pom.xml```

```parent```要加入```relativePath```

```
<?xml version="1.0" encoding="UTF-8"?>
<project>
	<parent>
		<artifactId>multi</artifactId>
		<version>${global.version}</version>
		<relativePath>..</relativePath>
	</parent>

	<artifactId>core</artifactId>
	<version>${parent.version}</version>
	<packaging>jar</packaging>
	....

</project>
```

4. Edit ```multi/frontend/pom.xml```

要將```core```加入```dependency```中

```
<?xml version="1.0" encoding="UTF-8"?>
<project>
	<parent>
		<artifactId>multi</artifactId>
		<version>${global.version}</version>
		<relativePath>..</relativePath>
	</parent>

	<artifactId>frontend</artifactId>
	<version>${parent.version}</version>
	<packaging>war</packaging>
	....
	
	<dependencies>
		<dependency>
			<groupId>tw.elliot</groupId>
			<artifactId>core</artifactId>
			<version>${parent.version}</version>
		</dependency>
		....
	</dependencies>
</project>
```

## Gradle

1. Create Root Parent Project

```
mkdir multi
cd multi

gradle init
```

2. Create Sub Projects

```
mkdir core
mkdir frontend

cd ./core
gradle init

cd ../frontend
gradle init
```

3. Edit ```multi/build.gradle```

跟Maven相同，將使用到相同的library記在此處

而```allprojects```跟```subprojects```區塊的差異，在```allprojects```的設定對所有的project有效，包含multi自己本身，而```subprojects```則是對其他project有效

```
allprojects {
    repositories {
        jcenter()
    }
}

subprojects {
    apply plugin: 'java'

    version = '1.0'

    dependencies {
        ....
    }
}
```

4. Edit ```multi/settings.gradle```

跟Maven不同的，是```modules```的設定，Gradle要另外放在```settings.gradle```中

用include指出有哪些subprojects

```
include 'core'
include 'frontend'
```

5. Edit ```multi/core/build.gradle```

由於要使用的library已在parent宣告，所以只有簡單地指出建構型態

```
plugins {
    id 'java-library'
}
```

6. Edit ```multi/frontend/build.gradle```

同```core```，指出建構型態並加入```core```做為library

```
plugins {
    id 'war'
}

dependencies {
    implementation project(':core')
}
```