// Copyright 2024 Brad Stancel. Licensed under Apache 2.0.
package com.github.stancel.rstpreview.editor;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public final class RstSwingPreviewPanel implements RstPreviewPanel {
    private final @NotNull JEditorPane myEditorPane;
    private final @NotNull JBScrollPane myScrollPane;

    public RstSwingPreviewPanel() {
        myEditorPane = new JEditorPane();
        myEditorPane.setEditable(false);
        myEditorPane.setContentType("text/html");

        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body { font-family: sans-serif; font-size: 14px; padding: 10px; }");
        styleSheet.addRule("h1, h2, h3 { color: #333; }");
        styleSheet.addRule("code { background-color: #f5f5f5; padding: 2px 4px; }");
        styleSheet.addRule("pre { background-color: #f5f5f5; padding: 10px; }");
        styleSheet.addRule("a { color: #0366d6; }");
        myEditorPane.setEditorKit(kit);

        myScrollPane = new JBScrollPane(myEditorPane);
        myScrollPane.setBorder(JBUI.Borders.empty());
    }

    @Override
    public void setHtml(@NotNull String html) {
        myEditorPane.setText(html);
        myEditorPane.setCaretPosition(0);
    }

    @Override
    public void render() {
        myEditorPane.repaint();
    }

    @Override
    public @NotNull JComponent getComponent() {
        return myScrollPane;
    }

    @Override
    public void dispose() {
    }
}
