// Copyright 2024 Brad Stancel. Licensed under Apache 2.0.
package com.github.stancel.rstpreview.editor;

import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.util.Alarm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeListener;

public class RstPreviewFileEditor extends UserDataHolderBase implements FileEditor {
    private static final long PARSING_CALL_TIMEOUT_MS = 50L;
    private static final String NO_PREVIEW = "<h2>No preview available.</h2><br/><br/>";
    private static final long RENDERING_DELAY_MS = 20L;

    private final @NotNull RstPreviewPanel myPanel;
    private final @NotNull VirtualFile myFile;
    private final @NotNull Project myProject;
    private final @Nullable Document myDocument;
    private final @NotNull Alarm myPooledAlarm = new Alarm(Alarm.ThreadToUse.POOLED_THREAD, this);
    private final @NotNull Alarm mySwingAlarm = new Alarm(Alarm.ThreadToUse.SWING_THREAD, this);

    private final Object REQUESTS_LOCK = new Object();
    private @Nullable Runnable myLastRequest = null;
    private @NotNull String myLastRenderedHtml = "";
    private volatile boolean myDisposed = false;

    public RstPreviewFileEditor(@NotNull VirtualFile file, @NotNull Project project) {
        myFile = file;
        myProject = project;
        myDocument = FileDocumentManager.getInstance().getDocument(myFile);

        boolean useJcef = JBCefApp.isSupported() &&
                RstPreviewSettings.JCEF.equals(RstPreviewSettings.getInstance().getCurrentPanel());
        myPanel = useJcef ? new RstJcefPreviewPanel(myProject) : new RstSwingPreviewPanel();

        if (myDocument != null) {
            myDocument.addDocumentListener(new DocumentListener() {
                @Override
                public void beforeDocumentChange(@NotNull DocumentEvent e) {
                    myPooledAlarm.cancelAllRequests();
                }

                @Override
                public void documentChanged(final @NotNull DocumentEvent e) {
                    myPooledAlarm.addRequest(() -> updateHtml(), PARSING_CALL_TIMEOUT_MS);
                }
            }, this);
        }
    }

    @Override
    public @NotNull JComponent getComponent() {
        return myPanel.getComponent();
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return myPanel.getComponent();
    }

    @Override
    public @NotNull String getName() {
        return "RST Preview";
    }

    @Override
    public void setState(@NotNull FileEditorState state) {
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void selectNotify() {
        myPooledAlarm.cancelAllRequests();
        myPooledAlarm.addRequest(() -> updateHtml(), 0);
    }

    private void updateHtml() {
        if (!myFile.isValid() || myDocument == null || isDisposed()) {
            return;
        }

        final Pair<String, String> htmlAndError = RstPreviewProvider.toHtml(myDocument.getText(), myFile);
        if (htmlAndError == null) return;

        String html = htmlAndError.getFirst();
        if (html.isEmpty()) {
            html = NO_PREVIEW + htmlAndError.getSecond();
        }

        if (!myFile.isValid() || isDisposed()) {
            return;
        }

        synchronized (REQUESTS_LOCK) {
            if (myLastRequest != null) {
                mySwingAlarm.cancelRequest(myLastRequest);
            }
            String finalHtml = html;
            myLastRequest = () -> {
                if (!finalHtml.equals(myLastRenderedHtml)) {
                    myLastRenderedHtml = finalHtml;
                    myPanel.setHtml(myLastRenderedHtml);
                }
                myPanel.render();
                synchronized (REQUESTS_LOCK) {
                    myLastRequest = null;
                }
            };
            mySwingAlarm.addRequest(myLastRequest, RENDERING_DELAY_MS, ModalityState.stateForComponent(getComponent()));
        }
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    @Override
    public @NotNull VirtualFile getFile() {
        return myFile;
    }

    private boolean isDisposed() {
        return myDisposed;
    }

    @Override
    public void dispose() {
        myDisposed = true;
        Disposer.dispose(myPanel);
    }
}
