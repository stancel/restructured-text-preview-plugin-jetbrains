// Copyright 2024 Brad Stancel. Licensed under Apache 2.0.
package com.github.stancel.rstpreview.editor;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditorWithPreview;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class RstPrintPreviewAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        RstPreviewFileEditor previewEditor = findPreviewEditor(project);
        if (previewEditor != null) {
            previewEditor.getPanel().print();
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null && findPreviewEditor(project) != null);
    }

    private static RstPreviewFileEditor findPreviewEditor(Project project) {
        FileEditor[] editors = FileEditorManager.getInstance(project).getSelectedEditors();
        for (FileEditor editor : editors) {
            if (editor instanceof TextEditorWithPreview composite) {
                FileEditor preview = composite.getPreviewEditor();
                if (preview instanceof RstPreviewFileEditor rstPreview) {
                    return rstPreview;
                }
            }
        }
        return null;
    }
}
