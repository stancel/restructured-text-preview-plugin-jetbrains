// Copyright 2024 Brad Stancel. Licensed under Apache 2.0.
package com.github.stancel.rstpreview.editor;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.TextEditorWithPreview;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.restructuredtext.RestFileType;
import org.jetbrains.annotations.NotNull;

/**
 * Editor provider that creates a split editor with text editing and HTML preview for RST files.
 */
public class RstPreviewEditorProvider implements FileEditorProvider, DumbAware {

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return file.getFileType() instanceof RestFileType;
    }

    @Override
    public boolean acceptRequiresReadAction() {
        return false;
    }

    @Override
    public @NotNull FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        TextEditor textEditor = (TextEditor) TextEditorProvider.getInstance().createEditor(project, file);
        RstPreviewFileEditor previewEditor = new RstPreviewFileEditor(file, project);
        return new TextEditorWithPreview(textEditor, previewEditor, "RST Editor with Preview",
                TextEditorWithPreview.Layout.SHOW_EDITOR_AND_PREVIEW);
    }

    @Override
    public @NotNull String getEditorTypeId() {
        return "rst-preview-standalone-editor";
    }

    @Override
    public @NotNull FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_OTHER_EDITORS;
    }
}
