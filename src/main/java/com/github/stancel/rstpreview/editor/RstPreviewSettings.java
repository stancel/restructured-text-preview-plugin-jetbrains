// Copyright 2024 Brad Stancel. Licensed under Apache 2.0.
package com.github.stancel.rstpreview.editor;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.RoamingType;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@State(name = "RstPreviewSettings", storages = @Storage(value = "rstPreviewStandalone.xml", roamingType = RoamingType.DISABLED))
public class RstPreviewSettings implements PersistentStateComponent<RstPreviewSettings> {
    public static final String JCEF = "JCEF";
    public static final String SWING = "Swing";

    private @NotNull String myCurrentPanel = JBCefApp.isSupported() ? JCEF : SWING;
    private @NotNull String myRst2HtmlCommand = "";

    public @NotNull String getCurrentPanel() {
        return myCurrentPanel;
    }

    public void setCurrentPanel(@NotNull String currentPanel) {
        myCurrentPanel = currentPanel;
    }

    public @NotNull String getRst2HtmlCommand() {
        if (myRst2HtmlCommand.isEmpty()) {
            myRst2HtmlCommand = findRst2Html();
        }
        return myRst2HtmlCommand;
    }

    public void setRst2HtmlCommand(@NotNull String rst2HtmlCommand) {
        myRst2HtmlCommand = rst2HtmlCommand;
    }

    public static RstPreviewSettings getInstance() {
        return ApplicationManager.getApplication().getService(RstPreviewSettings.class);
    }

    @Override
    public RstPreviewSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull RstPreviewSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    private static @NotNull String findRst2Html() {
        List<String> candidates = new ArrayList<>();

        String pathEnv = System.getenv("PATH");
        if (pathEnv != null) {
            for (String dir : pathEnv.split(File.pathSeparator)) {
                candidates.add(Paths.get(dir, "rst2html").toString());
                candidates.add(Paths.get(dir, "rst2html.py").toString());
            }
        }

        String homeDir = System.getProperty("user.home");
        if (homeDir != null) {
            // pyenv
            candidates.add(Paths.get(homeDir, ".pyenv", "shims", "rst2html").toString());
            // Local bin
            candidates.add(Paths.get(homeDir, ".local", "bin", "rst2html").toString());
            // pyenv versions
            try {
                Path pyenvVersions = Paths.get(homeDir, ".pyenv", "versions");
                if (Files.exists(pyenvVersions) && Files.isDirectory(pyenvVersions)) {
                    Files.list(pyenvVersions).forEach(version -> {
                        candidates.add(version.resolve("bin/rst2html").toString());
                    });
                }
            } catch (Exception ignored) {}
        }

        if (!SystemInfo.isWindows) {
            candidates.add("/usr/bin/rst2html");
            candidates.add("/usr/bin/rst2html.py");
            candidates.add("/usr/local/bin/rst2html");
        }

        for (String candidate : candidates) {
            Path path = Paths.get(candidate);
            if (Files.exists(path) && Files.isExecutable(path)) {
                return candidate;
            }
        }

        return "rst2html";
    }
}
