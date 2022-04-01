const fs = require("fs/promises");

const argv = process.argv.slice(2)

void async function init() {
  for (let i = 0; i < argv.length; i++) {
    const arg = argv[i].split(" ");
    const name = arg.join(" ");
    const archiveName = arg.join("-").toLowerCase();
    const artifact = arg.join("").toLowerCase();
    console.log(`Creating mod ${name} (archive name: ${archiveName}, id: ${artifact})`);
    main(name, archiveName, artifact);
    console.log(`${name}: Reading settings.gradle`);
    let settings = await fs.readFile("settings.gradle", "utf8");
    console.log(`${name}: Parsing settings.gradle`);
    settings = settings.split("\n").map(line => {
      if (line.startsWith("include ")) {
        line = line.replace("include ", "");
        artifacts = line.split(",");
        artifacts = artifacts.map(artifact => artifact.trim());
        artifacts.push(`'${archiveName}'`);
        line = artifacts.sort().join(", ");
        line = `include ${line}`;
      }
      return line;
    });
  
    console.log(`${name}: Writing to settings.gradle`);
    await fs.writeFile("settings.gradle", settings.join("\n"));
  }
}();

async function main(name, archiveName, artifact) {
  console.log(`${name}: Creating necessary directories`);
  await fs.mkdir(`./${archiveName}`, { recursive: true });
  await fs.mkdir(`./${archiveName}/src/main`, { recursive: true });
  await fs.mkdir(`./${archiveName}/src/main/java/org/infernalstudios/${artifact}`, { recursive: true });
  await fs.mkdir(`./${archiveName}/src/main/java/org/infernalstudios/${artifact}/mixin`, { recursive: true });
  await fs.mkdir(`./${archiveName}/src/main/resources`, { recursive: true });
  await fs.mkdir(`./${archiveName}/src/main/resources/META-INF`, { recursive: true });
  console.log(`${name}: Writing mods.toml`);
  fs.writeFile(`./${archiveName}/src/main/resources/META-INF/mods.toml`, `modLoader="javafml"
loaderVersion="[40,)"
license="Apache-2.0"
issueTrackerURL="https://infernalstudios.org"

[[mods]]
modId = "${artifact}"
version="\${file.jarVersion}"
displayName = "${name}"
# updateJSONURL="https://infernalstudios.org/api/mods/${artifact}/forge"
displayURL="https://infernalstudios.org"
# logoFile = ""
credits = "Infernal Studios"
authors = "Infernal Studios"
description = '''
Lorem ipsum dolor sit amet, consectetur adipiscing elit.
'''

[[dependencies.${artifact}]]
  modId="forge"
  mandatory=true
  versionRange="[40,)"
  ordering="NONE"
  side="BOTH"

[[dependencies.${artifact}]]
  modId="minecraft"
  mandatory = true
  versionRange = "[1.18.2,1.19)"
  ordering = "NONE"
  side="BOTH"`);
  console.log(`${name}: Writing pack.mcmeta`);
  fs.writeFile(`./${archiveName}/src/main/resources/pack.mcmeta`, `{
  "pack": {
    "description": "${artifact} resources",
    "pack_format": 6
  }
}`);

  console.log(`${name}: Writing mixin configuration`);
  fs.writeFile(`./${archiveName}/src/main/resources/${archiveName}.mixins.json`, `{
  "required": true,
  "minVersion": "0.8",
  "package": "org.infernalstudios.${artifact}.mixin",
  "refmap": "${archiveName}.refmap.json",
  "compatibilityLevel": "JAVA_17",
  "injectors": {
    "defaultRequire": 1
  },
  "mixins": [
  ]
}`);
  
  console.log(`${name}: Writing main java class`);
  const javaName = name.replace(/\s/g, "");
  fs.writeFile(`./${archiveName}/src/main/java/org/infernalstudios/${artifact}/${javaName}.java`, `package org.infernalstudios.${artifact};

import net.minecraftforge.fml.common.Mod;

@Mod(${javaName}.MOD_ID)
public class ${javaName} {
  public static final String MOD_ID = "${artifact}";

  public ${javaName}() {
  }
}`);

  console.log(`${name}: Writing build.gradle`);
  fs.writeFile(`./${archiveName}/build.gradle`, `buildscript {
  repositories {
    maven { url = 'https://files.minecraftforge.net/maven' }
    maven { url = 'https://repo.spongepowered.org/maven/' }

    jcenter()
    mavenCentral()
  }

  dependencies {
    classpath group: 'net.minecraftforge.gradle', name: 'ForgeGradle', version: '5.1+', changing: true
    classpath group: 'org.spongepowered',         name: 'mixingradle', version: '0.7-SNAPSHOT'
  }
}

plugins { id 'com.github.johnrengelman.shadow' version '7.1.2' }
apply plugin: 'net.minecraftforge.gradle'
apply plugin: 'eclipse'
apply plugin: 'org.spongepowered.mixin'

ext.defaultConfig = parseConfig(file('../build.properties'))
ext.config = parseConfig(file('build.properties'))
ext.config.MINECRAFT_VERSION = defaultConfig.MINECRAFT_VERSION
ext.config.FORGE_VERSION = defaultConfig.FORGE_VERSION
ext.config.MAPPINGS_CHANNEL = defaultConfig.MAPPINGS_CHANNEL
ext.config.MAPPINGS_VERSION = defaultConfig.MAPPINGS_VERSION
ext.config.CONFIG_LIB_VERSION = defaultConfig.CONFIG_LIB_VERSION
ext.config.GROUP = defaultConfig.GROUP
ext.config.AUTHOR = defaultConfig.AUTHOR

project.version = config.VERSION
project.group = "\${config.GROUP}.\${config.ARTIFACT}"
project.archivesBaseName = "\${config.ARCHIVES_BASE_NAME}-\${config.MINECRAFT_VERSION}"

java.toolchain.languageVersion = JavaLanguageVersion.of(17)

minecraft {
  mappings channel: config.MAPPINGS_CHANNEL, version: config.MAPPINGS_VERSION

  runs {
    client {
      workingDirectory project.file('run')
      arg '-mixin.config=${archiveName}.mixins.json'
      property 'forge.logging.markers', 'SCAN,REGISTRIES,REGISTRYDUMP'
      property 'forge.logging.console.level', 'debug'
      mods {
        mod {
          source sourceSets.main
        }
      }
    }

    server {
      workingDirectory project.file('run')
      arg '-mixin.config=${archiveName}.mixins.json'
      property 'forge.logging.markers', 'SCAN,REGISTRIES,REGISTRYDUMP'
      property 'forge.logging.console.level', 'debug'
      mods {
        mod {
          source sourceSets.main
        }
      }
    }

    data {
      workingDirectory project.file('run')
      property 'forge.logging.markers', 'SCAN,REGISTRIES,REGISTRYDUMP'
      property 'forge.logging.console.level', 'debug'
      args '--mod', 'mod', '--all', '--output', file('src/generated/resources/'), '--existing', file('src/main/resources/')
      mods {
        mod {
          source sourceSets.main
        }
      }
    }
  }
}

mixin {
  add sourceSets.main, "${archiveName}.refmap.json"
  config "${archiveName}.mixins.json"

  debug.verbose = true
  debug.export = true
  dumpTargetOnFailure = true
}

configurations {
  packIntoJar {
    transitive = false
  }
}

repositories {
  maven {
    url = config.CONFIG_LIB_VERSION.endsWith('SNAPSHOT') ? 'https://maven.infernalstudios.org/snapshots' : 'https://maven.infernalstudios.org/releases' 
  }
}

dependencies {
  minecraft "net.minecraftforge:forge:\${config.MINECRAFT_VERSION}-\${config.FORGE_VERSION}"
  annotationProcessor 'org.spongepowered:mixin:0.8.5:processor'
  implementation "org.infernalstudios:config:\${config.CONFIG_LIB_VERSION}"
  packIntoJar "org.infernalstudios:config:\${config.CONFIG_LIB_VERSION}"

  if (project.name != 'common') {
    implementation project(':common')
    packIntoJar project(':common')
  }
}

shadowJar {
  classifier ''
  configurations = [project.configurations.packIntoJar]
  relocate 'org.infernalstudios.config', "\${config.GROUP}.\${config.ARTIFACT}.config.library"
  if (project.name != 'common') {
    relocate 'org.infernalstudios.clusterfuck', "\${config.GROUP}.\${config.ARTIFACT}.clusterfuck"
  }
}

reobf {
  shadowJar {}
}

jar {
  classifier 'base'
  finalizedBy 'reobfJar'

  manifest {
    attributes([
      'Specification-Title'   : config.TITLE,
      'Specification-Vendor'  : config.AUTHOR,
      'Specification-Version' : '1',
      'Implementation-Title'  : config.TITLE,
      'Implementation-Version': config.VERSION,
      'Implementation-Vendor' : config.AUTHOR,
      'MixinConfigs'          : '${archiveName}.mixins.json'
    ])
  }
}

task sourcesJar(type: Jar) {
  archiveClassifier.set('sources')
  from sourceSets.main.allJava
}

tasks.build.dependsOn sourcesJar
tasks.build.dependsOn shadowJar
tasks.build.dependsOn reobfShadowJar

def parseConfig(File config) {
  config.withReader {
    def prop = new Properties()
    prop.load(it)
    return (new ConfigSlurper().parse(prop))
  }
}`);

  console.log(`${name}: Writing build.properties`);
  fs.writeFile(`./${archiveName}/build.properties`, `VERSION=1.0.0
ARTIFACT=${artifact}
ARCHIVES_BASE_NAME=${archiveName}
TITLE=${artifact}`);
}