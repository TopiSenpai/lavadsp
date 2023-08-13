# lavadsp

A bunch of lavaplayer audio filters implemented with native code forked from https://github.com/natanbc/lavadsp with mac m1 natives fixes

## Getting Started

### Installing

Replace `VERSION` with the version you want to use. The latest version can be found in the badge above.

#### Gradle

```gradle
repositories {
    maven{ "http://maven.arbjerg.dev/releases" }
}

dependencies {
    compile "dev.arbjerg:lavadsp:VERSION"
}
```

#### Maven

```xml
<repositories>
    <repository>
        <id>arbjerg</id>
        <name>arbjerg</name>
        <url>http://maven.arbjerg.dev/releases</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>dev.arbjerg</groupId>
        <artifactId>lavadsp</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```

### Basic Usage

```java
AudioPlayer player = manager.createPlayer();
player.setFilterFactory((track, format, output)->{
    TimescalePcmAudioFilter audioFilter = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
    audioFilter.setSpeed(1.5); //1.5x normal speed
    return Collections.singletonList(audioFilter);
});
```

### Chaining

Filters may be chained to merge their effects:
```java
AudioPlayer player = manager.createPlayer();
player.setFilterFactory((track, format, output)->{
    TremoloPcmAudioFilter tremolo = new TremoloPcmAudioFilter(output, format.channelCount, format.sampleRate);
    tremolo.setDepth(0.75);
    TimescalePcmAudioFilter timescale = new TimescalePcmAudioFilter(tremolo, format.channelCount, format.sampleRate);
    timescale.setSpeed(1.5);
    return Arrays.asList(timescale, tremolo);
});
```

To dynamically choose which filters to use, you can see [this example](https://github.com/natanbc/andesite/blob/0ee816125c99ca0921a1cb4280f30398ac520a9d/api/src/main/java/andesite/player/filter/FilterChainConfiguration.java#L197-L218)
