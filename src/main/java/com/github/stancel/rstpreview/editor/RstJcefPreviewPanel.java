// Copyright 2024 Brad Stancel. Licensed under Apache 2.0.
package com.github.stancel.rstpreview.editor;

import com.intellij.ide.BrowserUtil;
import com.intellij.ide.ui.LafManagerListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.jcef.JBCefBrowserBase;
import com.intellij.ui.jcef.JBCefJSQuery;
import com.intellij.ui.jcef.JCEFHtmlPanel;
import com.intellij.ui.JBColor;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefLoadHandler;
import org.cef.handler.CefLoadHandlerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class RstJcefPreviewPanel extends JCEFHtmlPanel implements RstPreviewPanel {

    private static final AtomicInteger ourCounter = new AtomicInteger(-1);
    private static final @NotNull String ourClassUrl = RstJcefPreviewPanel.class.getResource(
            RstJcefPreviewPanel.class.getSimpleName() + ".class").toExternalForm();

    private enum Style { DARCULA, DEFAULT }
    private static final EnumMap<Style, String> ourLoadedStylesCache = new EnumMap<>(Style.class);

    private final JBCefJSQuery myJSQueryOpenInBrowser = JBCefJSQuery.create((JBCefBrowserBase) this);
    private final CefLoadHandler myCefLoadHandler;
    private final @NotNull Project myProject;
    private @Nullable String myLastHtml;

    private static final @NotNull String JS_CODE =
            "window.__IntelliJTools = {};\n" +
            "window.onclick = function(e) {\n" +
            "  if (e.target.tagName !== 'A') return true;\n" +
            "  e.stopPropagation();\n" +
            "  e.preventDefault();\n" +
            "  var href = e.target.href;\n" +
            "  if (href.indexOf('#') !== -1 && !/^https?:\\/\\//i.test(href)) {\n" +
            "    var elementId = href.split('#')[1];\n" +
            "    var elementById = document.getElementById(elementId);\n" +
            "    if (elementById) elementById.scrollIntoView();\n" +
            "    return;\n" +
            "  }\n" +
            "  if (window.__IntelliJTools.openInBrowserCallback !== undefined) {\n" +
            "    window.__IntelliJTools.openInBrowserCallback(href);\n" +
            "  }\n" +
            "};\n";

    public RstJcefPreviewPanel(@NotNull Project project) {
        super(generateUniqueUrl());
        myProject = project;

        getJBCefClient().addLoadHandler(myCefLoadHandler = new CefLoadHandlerAdapter() {
            @Override
            public void onLoadingStateChange(CefBrowser browser, boolean isLoading, boolean canGoBack, boolean canGoForward) {
                browser.executeJavaScript(JS_CODE, getCefBrowser().getURL(), 0);
                browser.executeJavaScript("window.__IntelliJTools.openInBrowserCallback = function(link) {"
                        + myJSQueryOpenInBrowser.inject("link") + "}",
                        getCefBrowser().getURL(), 0);
            }
        }, getCefBrowser());

        myJSQueryOpenInBrowser.addHandler(link -> {
            if (!link.isEmpty()) BrowserUtil.browse(link);
            return null;
        });

        Disposer.register(this, myJSQueryOpenInBrowser);

        ApplicationManager.getApplication().getMessageBus().connect(this)
                .subscribe(LafManagerListener.TOPIC, source -> this.render());
    }

    @Override
    protected @NotNull String prepareHtml(@NotNull String html) {
        return html.replace("<head>", "<head>" + getCssStyleCodeToInject());
    }

    @Override
    public void setHtml(@NotNull String html) {
        String basePath = myProject.getBasePath();
        if (basePath != null) {
            html = makeImageUrlsAbsolute(html, basePath);
        }
        myLastHtml = html;
        super.setHtml(html);
    }

    @Override
    public void render() {
        if (myLastHtml != null) setHtml(myLastHtml);
    }

    @Override
    public @NotNull JComponent getComponent() {
        return super.getComponent();
    }

    @Override
    public void dispose() {
        super.dispose();
        getJBCefClient().removeLoadHandler(myCefLoadHandler, getCefBrowser());
    }

    private static @NotNull String generateUniqueUrl() {
        return ourClassUrl + "@" + ourCounter.incrementAndGet();
    }

    private static @NotNull String getCssStyleCodeToInject() {
        boolean isDarkTheme = !JBColor.isBright();
        Style style = isDarkTheme ? Style.DARCULA : Style.DEFAULT;
        String cssCodeToInject = ourLoadedStylesCache.get(style);
        if (cssCodeToInject != null) return cssCodeToInject;

        cssCodeToInject = "<style>" + getBuiltInCss(isDarkTheme) + "</style>";
        ourLoadedStylesCache.put(style, cssCodeToInject);
        return cssCodeToInject;
    }

    private static @NotNull String getBuiltInCss(boolean isDarcula) {
        if (isDarcula) {
            return "body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Helvetica, Arial, sans-serif; " +
                   "font-size: 14px; line-height: 1.6; padding: 20px; max-width: 900px; margin: 0 auto; " +
                   "background-color: #2b2b2b; color: #a9b7c6; }\n" +
                   "h1, h2, h3, h4, h5, h6 { color: #ffc66d; margin-top: 24px; margin-bottom: 16px; font-weight: 600; }\n" +
                   "h1 { font-size: 2em; border-bottom: 1px solid #3c3f41; padding-bottom: 0.3em; }\n" +
                   "h2 { font-size: 1.5em; border-bottom: 1px solid #3c3f41; padding-bottom: 0.3em; }\n" +
                   "a { color: #6897bb; text-decoration: none; }\n" +
                   "a:hover { text-decoration: underline; }\n" +
                   "code { background-color: #3c3f41; padding: 0.2em 0.4em; border-radius: 3px; font-family: 'JetBrains Mono', Consolas, monospace; }\n" +
                   "pre { background-color: #3c3f41; padding: 16px; overflow: auto; border-radius: 6px; }\n" +
                   "pre code { background: none; padding: 0; }\n" +
                   "blockquote { border-left: 4px solid #4a4a4a; margin: 0; padding-left: 16px; color: #808080; }\n" +
                   "table { border-collapse: collapse; width: 100%; }\n" +
                   "th, td { border: 1px solid #3c3f41; padding: 8px 12px; }\n" +
                   "th { background-color: #3c3f41; }\n" +
                   "img { max-width: 100%; height: auto; }\n";
        } else {
            return "body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Helvetica, Arial, sans-serif; " +
                   "font-size: 14px; line-height: 1.6; padding: 20px; max-width: 900px; margin: 0 auto; " +
                   "background-color: #ffffff; color: #24292e; }\n" +
                   "h1, h2, h3, h4, h5, h6 { color: #24292e; margin-top: 24px; margin-bottom: 16px; font-weight: 600; }\n" +
                   "h1 { font-size: 2em; border-bottom: 1px solid #eaecef; padding-bottom: 0.3em; }\n" +
                   "h2 { font-size: 1.5em; border-bottom: 1px solid #eaecef; padding-bottom: 0.3em; }\n" +
                   "a { color: #0366d6; text-decoration: none; }\n" +
                   "a:hover { text-decoration: underline; }\n" +
                   "code { background-color: #f6f8fa; padding: 0.2em 0.4em; border-radius: 3px; font-family: 'JetBrains Mono', Consolas, monospace; }\n" +
                   "pre { background-color: #f6f8fa; padding: 16px; overflow: auto; border-radius: 6px; }\n" +
                   "pre code { background: none; padding: 0; }\n" +
                   "blockquote { border-left: 4px solid #dfe2e5; margin: 0; padding-left: 16px; color: #6a737d; }\n" +
                   "table { border-collapse: collapse; width: 100%; }\n" +
                   "th, td { border: 1px solid #dfe2e5; padding: 8px 12px; }\n" +
                   "th { background-color: #f6f8fa; }\n" +
                   "img { max-width: 100%; height: auto; }\n";
        }
    }

    private static @NotNull String makeImageUrlsAbsolute(@NotNull String html, @NotNull String basePath) {
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByTag("img");

        for (Element element : elements) {
            String src = element.attr("src");
            URI uri = null;
            try {
                uri = new URI(src);
                if (uri.getScheme() != null && !uri.getScheme().equals("file")) continue;
            } catch (URISyntaxException e) {
                // Assume file scheme for malformed URIs
            }

            Path originalPath = Paths.get((uri != null && uri.getPath() != null) ? uri.getPath() : src);
            if (originalPath.isAbsolute()) continue;

            element.attr("src", Paths.get(basePath, originalPath.toString()).toString());
        }

        return document.outerHtml();
    }
}
