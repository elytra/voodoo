// Generated by delombok at Sat Jul 14 01:46:55 CEST 2018
/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */
package com.skcraft.launcher.builder;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import java.io.File;

public class BuilderOptions {
    public static final String DEFAULT_CONFIG_FILENAME = "modpack.json";
    public static final String DEFAULT_VERSION_FILENAME = "version.json";
    public static final String DEFAULT_SRC_DIRNAME = "src";
    public static final String DEFAULT_LOADERS_DIRNAME = "loaders";
    // Configuration
    // Override config
    @Parameter(names = "--name")
    private String name;
    @Parameter(names = "--title")
    private String title;
    @Parameter(names = "--mc-version")
    private String gameVersion;
    // Required
    @Parameter(names = "--version", required = true)
    private String version;
    @Parameter(names = "--manifest-dest", required = true)
    private File manifestPath;
    // Overall paths
    @Parameter(names = {"--input", "-i"})
    private File inputPath;
    @Parameter(names = {"--output", "-o"})
    private File outputPath;
    // Input paths
    @Parameter(names = "--config")
    private File configPath;
    @Parameter(names = "--version-file")
    private File versionManifestPath;
    @Parameter(names = "--files")
    private File filesDir;
    @Parameter(names = "--loaders")
    private File loadersDir;
    // Output paths
    @Parameter(names = "--objects-dest")
    private File objectsDir;
    @Parameter(names = "--libraries-dest")
    private File librariesDir;
    @Parameter(names = "--libs-url")
    private String librariesLocation = "libraries";
    @Parameter(names = "--objects-url")
    private String objectsLocation = "objects";
    // Misc
    @Parameter(names = "--pretty-print")
    private boolean prettyPrinting;

    public void choosePaths() throws ParameterException {
        if (configPath == null) {
            requireInputPath("--config");
            configPath = new File(inputPath, DEFAULT_CONFIG_FILENAME);
        }
        if (versionManifestPath == null) {
            requireInputPath("--version");
            versionManifestPath = new File(inputPath, DEFAULT_VERSION_FILENAME);
        }
        if (filesDir == null) {
            requireInputPath("--files");
            filesDir = new File(inputPath, DEFAULT_SRC_DIRNAME);
        }
        if (loadersDir == null) {
            requireInputPath("--loaders");
            loadersDir = new File(inputPath, DEFAULT_LOADERS_DIRNAME);
        }
        if (objectsDir == null) {
            requireOutputPath("--objects-dest");
            objectsDir = new File(outputPath, objectsLocation);
        }
        if (librariesDir == null) {
            requireOutputPath("--libs-dest");
            librariesDir = new File(outputPath, librariesLocation);
        }
    }

    private void requireOutputPath(String name) throws ParameterException {
        if (outputPath == null) {
            throw new ParameterException("Because " + name + " was not specified, --output needs to be specified as the output directory and then " + name + " will be default to a pre-set path within the output directory");
        }
    }

    private void requireInputPath(String name) throws ParameterException {
        if (inputPath == null) {
            throw new ParameterException("Because " + name + " was not specified, --input needs to be specified as the project directory and then " + name + " will be default to a pre-set path within the project directory");
        }
    }

    @java.lang.SuppressWarnings("all")
    public BuilderOptions() {
    }

    @java.lang.SuppressWarnings("all")
    public String getName() {
        return this.name;
    }

    @java.lang.SuppressWarnings("all")
    public String getTitle() {
        return this.title;
    }

    @java.lang.SuppressWarnings("all")
    public String getGameVersion() {
        return this.gameVersion;
    }

    @java.lang.SuppressWarnings("all")
    public String getVersion() {
        return this.version;
    }

    @java.lang.SuppressWarnings("all")
    public File getManifestPath() {
        return this.manifestPath;
    }

    @java.lang.SuppressWarnings("all")
    public File getInputPath() {
        return this.inputPath;
    }

    @java.lang.SuppressWarnings("all")
    public File getOutputPath() {
        return this.outputPath;
    }

    @java.lang.SuppressWarnings("all")
    public File getConfigPath() {
        return this.configPath;
    }

    @java.lang.SuppressWarnings("all")
    public File getVersionManifestPath() {
        return this.versionManifestPath;
    }

    @java.lang.SuppressWarnings("all")
    public File getFilesDir() {
        return this.filesDir;
    }

    @java.lang.SuppressWarnings("all")
    public File getLoadersDir() {
        return this.loadersDir;
    }

    @java.lang.SuppressWarnings("all")
    public File getObjectsDir() {
        return this.objectsDir;
    }

    @java.lang.SuppressWarnings("all")
    public File getLibrariesDir() {
        return this.librariesDir;
    }

    @java.lang.SuppressWarnings("all")
    public String getLibrariesLocation() {
        return this.librariesLocation;
    }

    @java.lang.SuppressWarnings("all")
    public String getObjectsLocation() {
        return this.objectsLocation;
    }

    @java.lang.SuppressWarnings("all")
    public boolean isPrettyPrinting() {
        return this.prettyPrinting;
    }

    @java.lang.SuppressWarnings("all")
    public void setName(final String name) {
        this.name = name;
    }

    @java.lang.SuppressWarnings("all")
    public void setTitle(final String title) {
        this.title = title;
    }

    @java.lang.SuppressWarnings("all")
    public void setGameVersion(final String gameVersion) {
        this.gameVersion = gameVersion;
    }

    @java.lang.SuppressWarnings("all")
    public void setVersion(final String version) {
        this.version = version;
    }

    @java.lang.SuppressWarnings("all")
    public void setManifestPath(final File manifestPath) {
        this.manifestPath = manifestPath;
    }

    @java.lang.SuppressWarnings("all")
    public void setInputPath(final File inputPath) {
        this.inputPath = inputPath;
    }

    @java.lang.SuppressWarnings("all")
    public void setOutputPath(final File outputPath) {
        this.outputPath = outputPath;
    }

    @java.lang.SuppressWarnings("all")
    public void setConfigPath(final File configPath) {
        this.configPath = configPath;
    }

    @java.lang.SuppressWarnings("all")
    public void setVersionManifestPath(final File versionManifestPath) {
        this.versionManifestPath = versionManifestPath;
    }

    @java.lang.SuppressWarnings("all")
    public void setFilesDir(final File filesDir) {
        this.filesDir = filesDir;
    }

    @java.lang.SuppressWarnings("all")
    public void setLoadersDir(final File loadersDir) {
        this.loadersDir = loadersDir;
    }

    @java.lang.SuppressWarnings("all")
    public void setObjectsDir(final File objectsDir) {
        this.objectsDir = objectsDir;
    }

    @java.lang.SuppressWarnings("all")
    public void setLibrariesDir(final File librariesDir) {
        this.librariesDir = librariesDir;
    }

    @java.lang.SuppressWarnings("all")
    public void setLibrariesLocation(final String librariesLocation) {
        this.librariesLocation = librariesLocation;
    }

    @java.lang.SuppressWarnings("all")
    public void setObjectsLocation(final String objectsLocation) {
        this.objectsLocation = objectsLocation;
    }

    @java.lang.SuppressWarnings("all")
    public void setPrettyPrinting(final boolean prettyPrinting) {
        this.prettyPrinting = prettyPrinting;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof BuilderOptions)) return false;
        final BuilderOptions other = (BuilderOptions) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$name = this.getName();
        final java.lang.Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final java.lang.Object this$title = this.getTitle();
        final java.lang.Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        final java.lang.Object this$gameVersion = this.getGameVersion();
        final java.lang.Object other$gameVersion = other.getGameVersion();
        if (this$gameVersion == null ? other$gameVersion != null : !this$gameVersion.equals(other$gameVersion)) return false;
        final java.lang.Object this$version = this.getVersion();
        final java.lang.Object other$version = other.getVersion();
        if (this$version == null ? other$version != null : !this$version.equals(other$version)) return false;
        final java.lang.Object this$manifestPath = this.getManifestPath();
        final java.lang.Object other$manifestPath = other.getManifestPath();
        if (this$manifestPath == null ? other$manifestPath != null : !this$manifestPath.equals(other$manifestPath)) return false;
        final java.lang.Object this$inputPath = this.getInputPath();
        final java.lang.Object other$inputPath = other.getInputPath();
        if (this$inputPath == null ? other$inputPath != null : !this$inputPath.equals(other$inputPath)) return false;
        final java.lang.Object this$outputPath = this.getOutputPath();
        final java.lang.Object other$outputPath = other.getOutputPath();
        if (this$outputPath == null ? other$outputPath != null : !this$outputPath.equals(other$outputPath)) return false;
        final java.lang.Object this$configPath = this.getConfigPath();
        final java.lang.Object other$configPath = other.getConfigPath();
        if (this$configPath == null ? other$configPath != null : !this$configPath.equals(other$configPath)) return false;
        final java.lang.Object this$versionManifestPath = this.getVersionManifestPath();
        final java.lang.Object other$versionManifestPath = other.getVersionManifestPath();
        if (this$versionManifestPath == null ? other$versionManifestPath != null : !this$versionManifestPath.equals(other$versionManifestPath)) return false;
        final java.lang.Object this$filesDir = this.getFilesDir();
        final java.lang.Object other$filesDir = other.getFilesDir();
        if (this$filesDir == null ? other$filesDir != null : !this$filesDir.equals(other$filesDir)) return false;
        final java.lang.Object this$loadersDir = this.getLoadersDir();
        final java.lang.Object other$loadersDir = other.getLoadersDir();
        if (this$loadersDir == null ? other$loadersDir != null : !this$loadersDir.equals(other$loadersDir)) return false;
        final java.lang.Object this$objectsDir = this.getObjectsDir();
        final java.lang.Object other$objectsDir = other.getObjectsDir();
        if (this$objectsDir == null ? other$objectsDir != null : !this$objectsDir.equals(other$objectsDir)) return false;
        final java.lang.Object this$librariesDir = this.getLibrariesDir();
        final java.lang.Object other$librariesDir = other.getLibrariesDir();
        if (this$librariesDir == null ? other$librariesDir != null : !this$librariesDir.equals(other$librariesDir)) return false;
        final java.lang.Object this$librariesLocation = this.getLibrariesLocation();
        final java.lang.Object other$librariesLocation = other.getLibrariesLocation();
        if (this$librariesLocation == null ? other$librariesLocation != null : !this$librariesLocation.equals(other$librariesLocation)) return false;
        final java.lang.Object this$objectsLocation = this.getObjectsLocation();
        final java.lang.Object other$objectsLocation = other.getObjectsLocation();
        if (this$objectsLocation == null ? other$objectsLocation != null : !this$objectsLocation.equals(other$objectsLocation)) return false;
        if (this.isPrettyPrinting() != other.isPrettyPrinting()) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof BuilderOptions;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final java.lang.Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        final java.lang.Object $gameVersion = this.getGameVersion();
        result = result * PRIME + ($gameVersion == null ? 43 : $gameVersion.hashCode());
        final java.lang.Object $version = this.getVersion();
        result = result * PRIME + ($version == null ? 43 : $version.hashCode());
        final java.lang.Object $manifestPath = this.getManifestPath();
        result = result * PRIME + ($manifestPath == null ? 43 : $manifestPath.hashCode());
        final java.lang.Object $inputPath = this.getInputPath();
        result = result * PRIME + ($inputPath == null ? 43 : $inputPath.hashCode());
        final java.lang.Object $outputPath = this.getOutputPath();
        result = result * PRIME + ($outputPath == null ? 43 : $outputPath.hashCode());
        final java.lang.Object $configPath = this.getConfigPath();
        result = result * PRIME + ($configPath == null ? 43 : $configPath.hashCode());
        final java.lang.Object $versionManifestPath = this.getVersionManifestPath();
        result = result * PRIME + ($versionManifestPath == null ? 43 : $versionManifestPath.hashCode());
        final java.lang.Object $filesDir = this.getFilesDir();
        result = result * PRIME + ($filesDir == null ? 43 : $filesDir.hashCode());
        final java.lang.Object $loadersDir = this.getLoadersDir();
        result = result * PRIME + ($loadersDir == null ? 43 : $loadersDir.hashCode());
        final java.lang.Object $objectsDir = this.getObjectsDir();
        result = result * PRIME + ($objectsDir == null ? 43 : $objectsDir.hashCode());
        final java.lang.Object $librariesDir = this.getLibrariesDir();
        result = result * PRIME + ($librariesDir == null ? 43 : $librariesDir.hashCode());
        final java.lang.Object $librariesLocation = this.getLibrariesLocation();
        result = result * PRIME + ($librariesLocation == null ? 43 : $librariesLocation.hashCode());
        final java.lang.Object $objectsLocation = this.getObjectsLocation();
        result = result * PRIME + ($objectsLocation == null ? 43 : $objectsLocation.hashCode());
        result = result * PRIME + (this.isPrettyPrinting() ? 79 : 97);
        return result;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
        return "BuilderOptions(name=" + this.getName() + ", title=" + this.getTitle() + ", gameVersion=" + this.getGameVersion() + ", version=" + this.getVersion() + ", manifestPath=" + this.getManifestPath() + ", inputPath=" + this.getInputPath() + ", outputPath=" + this.getOutputPath() + ", configPath=" + this.getConfigPath() + ", versionManifestPath=" + this.getVersionManifestPath() + ", filesDir=" + this.getFilesDir() + ", loadersDir=" + this.getLoadersDir() + ", objectsDir=" + this.getObjectsDir() + ", librariesDir=" + this.getLibrariesDir() + ", librariesLocation=" + this.getLibrariesLocation() + ", objectsLocation=" + this.getObjectsLocation() + ", prettyPrinting=" + this.isPrettyPrinting() + ")";
    }
}