// Copyright 2024 Brad Stancel. Licensed under Apache 2.0.
package com.github.stancel.rstpreview.editor;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class RstPreviewConfigurable implements Configurable {
    private TextFieldWithBrowseButton myRst2HtmlPathField;
    private JBRadioButton myJcefRadioButton;
    private JBRadioButton mySwingRadioButton;

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "ReStructuredText Preview";
    }

    @Override
    public @Nullable JComponent createComponent() {
        myRst2HtmlPathField = new TextFieldWithBrowseButton();
        myRst2HtmlPathField.addBrowseFolderListener(
                "Select rst2html Executable",
                "Select the path to rst2html or rst2html.py",
                null,
                FileChooserDescriptorFactory.createSingleFileDescriptor()
        );

        myJcefRadioButton = new JBRadioButton("JCEF (Chromium-based, recommended)");
        mySwingRadioButton = new JBRadioButton("Swing (basic HTML support)");

        ButtonGroup panelGroup = new ButtonGroup();
        panelGroup.add(myJcefRadioButton);
        panelGroup.add(mySwingRadioButton);

        if (!JBCefApp.isSupported()) {
            myJcefRadioButton.setEnabled(false);
            myJcefRadioButton.setText("JCEF (not available on this platform)");
        }

        JPanel panelTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTypePanel.add(myJcefRadioButton);
        panelTypePanel.add(Box.createHorizontalStrut(20));
        panelTypePanel.add(mySwingRadioButton);

        return FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("rst2html executable:"), myRst2HtmlPathField)
                .addComponent(new JBLabel("<html><small>Path to rst2html command. Install with: <code>pip install docutils</code></small></html>"))
                .addVerticalGap(10)
                .addLabeledComponent(new JBLabel("Preview panel type:"), panelTypePanel)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    @Override
    public boolean isModified() {
        RstPreviewSettings settings = RstPreviewSettings.getInstance();
        return !myRst2HtmlPathField.getText().equals(settings.getRst2HtmlCommand()) ||
                !getSelectedPanel().equals(settings.getCurrentPanel());
    }

    @Override
    public void apply() {
        RstPreviewSettings settings = RstPreviewSettings.getInstance();
        settings.setRst2HtmlCommand(myRst2HtmlPathField.getText());
        settings.setCurrentPanel(getSelectedPanel());
    }

    @Override
    public void reset() {
        RstPreviewSettings settings = RstPreviewSettings.getInstance();
        myRst2HtmlPathField.setText(settings.getRst2HtmlCommand());

        String currentPanel = settings.getCurrentPanel();
        if (RstPreviewSettings.JCEF.equals(currentPanel) && JBCefApp.isSupported()) {
            myJcefRadioButton.setSelected(true);
        } else {
            mySwingRadioButton.setSelected(true);
        }
    }

    private String getSelectedPanel() {
        return myJcefRadioButton.isSelected() ? RstPreviewSettings.JCEF : RstPreviewSettings.SWING;
    }
}
