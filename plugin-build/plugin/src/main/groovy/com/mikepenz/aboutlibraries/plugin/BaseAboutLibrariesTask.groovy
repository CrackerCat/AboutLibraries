package com.mikepenz.aboutlibraries.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject
import java.util.regex.Pattern

abstract class BaseAboutLibrariesTask extends DefaultTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseAboutLibrariesTask.class);

    private def rootDir = project.rootDir
    private def extension = project.extensions.aboutLibraries

    private static final Boolean TEST_CONFIG_CACHE = true

    @Inject
    abstract DependencyHandler getDependencyHandler()

    /**
     * holds the collected set of dependencies
     */
    @Input
    protected Map<String, HashSet<String>> collectedDependencies = new HashMap<>()

    /**
     * Collect the dependencies via the available configurations for the current project
     */
    void loadCollectedDependencies(final String variant = null) {
        if (TEST_CONFIG_CACHE) {
            collectedDependencies = new DependencyCollector(variant).collect(project)
        }
    }

    void loadCollectedDependenciesTask(final String variant = null) {
        if (!TEST_CONFIG_CACHE) {
            collectedDependencies = new DependencyCollector(variant).collect(project)
        }
    }

    @org.gradle.api.tasks.Optional
    @PathSensitive(value = PathSensitivity.RELATIVE)
    @InputDirectory
    File getConfigPath() {
        final String path = extension.configPath
        if (path != null) {
            final File inputFile = new File(path)
            final File absoluteFile = new File(rootDir, path)
            if (inputFile.isAbsolute() && inputFile.exists()) {
                return inputFile
            } else if (absoluteFile.exists()) {
                return absoluteFile
            } else {
                LOGGER.warn("Couldn't find provided path in: '${inputFile.absolutePath}' or '${absoluteFile.absolutePath}'")
            }
        }
        return null
    }

    @Input
    final Boolean fetchRemoteLicense = extension.fetchRemoteLicense

    @Input
    final Boolean getAsStringResource() {
        def value = extension.asStringResource
        if (value == null) return true else return value
    }

    @Input
    final List<Pattern> exclusionPatterns = extension.exclusionPatterns ?: new ArrayList<>()

    @Input
    final Boolean includeAllLicenses = extension.includeAllLicenses

    @Input
    HashSet<String> getAdditionalLicenses() {
        HashSet<String> licenses = new HashSet<>()
        extension.additionalLicenses.all {
            licenses.add(it.name)
        }
        return licenses
    }
}
