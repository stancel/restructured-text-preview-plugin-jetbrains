// Copyright 2024 Brad Stancel. Licensed under Apache 2.0.
package com.github.stancel.rstpreview.editor;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.CapturingProcessHandler;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class RstPreviewProvider {
    private static final Logger LOG = Logger.getInstance(RstPreviewProvider.class);
    private static final int TIMEOUT_MS = 10000;

    private RstPreviewProvider() {}

    public static @Nullable Pair<String, String> toHtml(@NotNull String text, @NotNull VirtualFile virtualFile) {
        String rst2htmlPath = RstPreviewSettings.getInstance().getRst2HtmlCommand();

        if (rst2htmlPath.isEmpty()) {
            return Pair.create("", "<p><b>Error:</b> rst2html not found. Please install docutils " +
                    "(<code>pip install docutils</code>) and configure the path in " +
                    "Settings → Languages & Frameworks → ReStructuredText Preview.</p>");
        }

        if (!isExecutable(rst2htmlPath)) {
            return Pair.create("", "<p><b>Error:</b> rst2html not found at: <code>" + rst2htmlPath + "</code></p>" +
                    "<p>Please install docutils (<code>pip install docutils</code>) or configure the correct path.</p>");
        }

        try {
            GeneralCommandLine commandLine = new GeneralCommandLine();

            if (rst2htmlPath.endsWith(".py") || needsPythonInterpreter(rst2htmlPath)) {
                String pythonPath = findPython();
                if (pythonPath != null) {
                    commandLine.setExePath(pythonPath);
                    commandLine.addParameter(rst2htmlPath);
                } else {
                    commandLine.setExePath(rst2htmlPath);
                }
            } else {
                commandLine.setExePath(rst2htmlPath);
            }

            commandLine.addParameter("--no-generator");
            commandLine.addParameter("--no-source-link");
            commandLine.addParameter("--no-datestamp");

            VirtualFile parent = virtualFile.getParent();
            if (parent != null) {
                commandLine.setWorkDirectory(parent.getPath());
            }

            commandLine.setCharset(StandardCharsets.UTF_8);

            CapturingProcessHandler handler = new CapturingProcessHandler(commandLine);
            Process process = handler.getProcess();

            byte[] inputBytes = text.getBytes(StandardCharsets.UTF_8);
            Thread stdinWriter = new Thread(() -> {
                try (OutputStream stdin = process.getOutputStream()) {
                    stdin.write(inputBytes);
                    stdin.flush();
                } catch (Exception e) {
                    LOG.debug("Error writing to stdin", e);
                }
            });
            stdinWriter.start();

            ProcessOutput output = handler.runProcess(TIMEOUT_MS);

            if (output.isTimeout()) {
                return Pair.create("", "<p><b>Error:</b> rst2html timed out after " + (TIMEOUT_MS / 1000) + " seconds.</p>");
            }

            if (output.getExitCode() != 0) {
                String stderr = output.getStderr();
                if (!stderr.isEmpty()) {
                    return Pair.create("", "<p><b>rst2html error:</b></p><pre>" + escapeHtml(stderr) + "</pre>");
                }
                return Pair.create("", "<p><b>Error:</b> rst2html exited with code " + output.getExitCode() + "</p>");
            }

            String html = output.getStdout();
            String warnings = output.getStderr();

            if (html.isEmpty() && !warnings.isEmpty()) {
                return Pair.create("", "<p><b>rst2html warnings:</b></p><pre>" + escapeHtml(warnings) + "</pre>");
            }

            String errorSection = "";
            if (!warnings.isEmpty()) {
                errorSection = "<details><summary>Warnings</summary><pre>" + escapeHtml(warnings) + "</pre></details>";
            }

            return Pair.create(html, errorSection);

        } catch (Exception e) {
            LOG.warn("Failed to run rst2html", e);
            return Pair.create("", "<p><b>Error running rst2html:</b> " + escapeHtml(e.getMessage()) + "</p>");
        }
    }

    private static boolean isExecutable(@NotNull String path) {
        if (!path.contains(File.separator) && !path.contains("/")) {
            return true;
        }
        Path p = Paths.get(path);
        return Files.exists(p) && (Files.isExecutable(p) || SystemInfo.isWindows);
    }

    private static boolean needsPythonInterpreter(@NotNull String path) {
        try {
            Path p = Paths.get(path);
            if (Files.exists(p)) {
                byte[] bytes = Files.readAllBytes(p);
                String content = new String(bytes, StandardCharsets.UTF_8);
                return content.startsWith("#!") && content.contains("python");
            }
        } catch (Exception ignored) {}
        return false;
    }

    private static @Nullable String findPython() {
        String[] candidates = SystemInfo.isWindows
                ? new String[]{"python.exe", "python3.exe", "py.exe"}
                : new String[]{"python3", "python"};

        String pathEnv = System.getenv("PATH");
        if (pathEnv != null) {
            for (String dir : pathEnv.split(File.pathSeparator)) {
                for (String candidate : candidates) {
                    Path p = Paths.get(dir, candidate);
                    if (Files.exists(p) && Files.isExecutable(p)) {
                        return p.toString();
                    }
                }
            }
        }

        String homeDir = System.getProperty("user.home");
        if (homeDir != null) {
            Path pyenvPython = Paths.get(homeDir, ".pyenv", "shims", "python3");
            if (Files.exists(pyenvPython)) return pyenvPython.toString();
            pyenvPython = Paths.get(homeDir, ".pyenv", "shims", "python");
            if (Files.exists(pyenvPython)) return pyenvPython.toString();
        }

        for (String loc : new String[]{"/usr/bin/python3", "/usr/local/bin/python3", "/usr/bin/python"}) {
            if (Files.exists(Paths.get(loc))) return loc;
        }

        return null;
    }

    private static @NotNull String escapeHtml(@NotNull String text) {
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
