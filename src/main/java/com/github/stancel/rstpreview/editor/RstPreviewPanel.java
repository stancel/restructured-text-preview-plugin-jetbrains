// Copyright 2024 Brad Stancel. Licensed under Apache 2.0.
package com.github.stancel.rstpreview.editor;

import com.intellij.openapi.Disposable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public interface RstPreviewPanel extends Disposable {
    void setHtml(@NotNull String html);
    void render();
    @NotNull JComponent getComponent();
    default void print() {}
    default void scrollToRatio(double ratio) {}
    default void setScrollListener(@Nullable ScrollListener listener) {}

    @FunctionalInterface
    interface ScrollListener {
        void onScroll(double ratio);
    }
}
