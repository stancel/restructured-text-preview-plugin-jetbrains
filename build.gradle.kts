import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.2.1"
}

group = "com.github.stancel"
version = "1.0.3"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        // Use IntelliJ IDEA Community as the base
        intellijIdeaCommunity("2024.3")
        // ReStructuredText plugin from marketplace
        plugin("org.jetbrains.plugins.rest", "243.21565.122")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

intellijPlatform {
    pluginConfiguration {
        id = "com.github.stancel.rst-preview-standalone"
        name = "ReStructuredText Preview (Standalone)"
        version = project.version.toString()
        description = """
            Adds standalone HTML preview for ReStructuredText files without requiring the Python plugin.
            Uses your system's rst2html command (from docutils) to render previews inside the IDE.

            <b>Features:</b>
            <ul>
              <li>Live HTML preview inside the IDE (split-pane view)</li>
              <li>Uses your system's rst2html command from docutils</li>
              <li>Works without the Python plugin</li>
              <li>Supports both light and dark themes</li>
              <li>Configurable rst2html path in settings</li>
            </ul>

            <b>Requirements:</b>
            <ul>
              <li>docutils installed: <code>pip install docutils</code></li>
              <li>rst2html in your PATH or configured in settings</li>
            </ul>
        """.trimIndent()
        vendor {
            name = "Brad Stancel"
            url = "https://github.com/stancel"
        }
        ideaVersion {
            sinceBuild = "243"
            untilBuild = "253.*"
        }
    }

    // Publishing configuration for JetBrains Marketplace
    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
    }

    // Plugin verification configuration
    pluginVerification {
        ides {
            // Verify against current and previous major versions
            ide(IntelliJPlatformType.IntellijIdeaCommunity, "2024.3")
        }
    }
}

tasks {
    buildSearchableOptions {
        enabled = false
    }
}
