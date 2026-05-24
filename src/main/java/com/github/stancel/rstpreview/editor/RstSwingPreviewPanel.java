// Copyright 2024 Brad Stancel. Licensed under Apache 2.0.
package com.github.stancel.rstpreview.editor;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public final class RstSwingPreviewPanel implements RstPreviewPanel {
    private static final Logger LOG = Logger.getInstance(RstSwingPreviewPanel.class);
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
        styleSheet.addRule("div.note, div.tip, div.hint, div.warning, div.caution, div.attention, " +
                "div.danger, div.error, div.important, .admonition " +
                "{ border-left: 4px solid #0366d6; background-color: #f1f8ff; padding: 8px 12px; margin: 12px 0; }");
        styleSheet.addRule("div.warning, div.caution, div.attention { border-left-color: #e36209; background-color: #fff8f0; }");
        styleSheet.addRule("div.danger, div.error { border-left-color: #d73a49; background-color: #ffeef0; }");
        styleSheet.addRule("div.tip, div.hint { border-left-color: #28a745; background-color: #f0fff4; }");
        styleSheet.addRule("div.important { border-left-color: #6f42c1; background-color: #f5f0ff; }");
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
    public void print() {
        try {
            myEditorPane.print();
        } catch (PrinterException e) {
            LOG.warn("Failed to print RST preview", e);
        }
    }

    @Override
    public void dispose() {
    }
}
