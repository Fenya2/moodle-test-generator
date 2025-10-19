package ru.moodle.testgenerator.moodletestgenerator.ui.previewform;

import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Компонент, отображающий вопрос с поддержкой LaTeX (MathJax, SVG-режим)
 */
public class QuestionAreaPreviewView extends StackPane {

    private static final String UPDATE_CONTENT_SCRIPT = """
            document.getElementById('content').innerHTML = '%s';
            MathJax.typesetPromise();
            """;
    private final WebEngine engine;

    public QuestionAreaPreviewView() {
        WebView webView = new WebView();
        this.engine = webView.getEngine();

        this.getChildren().add(webView);

        String html = """
                <html>
                    <head>
                        <script>
                            window.MathJax = {
                                tex: { inlineMath: [['$', '$'], ['\\\\(', '\\\\)']] },
                                options: { skipHtmlTags: ['script', 'style', 'noscript'] },
                                svg: { fontCache: 'global' }
                            };
                        </script>
                        <script defer src="https://cdn.jsdelivr.net/npm/mathjax@4/tex-svg.js"></script>
                        <style>
                            body { margin: 0; padding: 10px; font-family: sans-serif; }
                            #content { padding-bottom: 20px; }
                        </style>
                    </head>
                    <body>
                        <div id="content"></div>
                    </body>
                </html>
                """;

        engine.loadContent(html);
    }

    /**
     * Устанавливает текст вопроса с LaTeX.
     *
     * @param question Текст с формулами в формате \( ... \) или $$ ... $$
     */
    public void setText(String question) {
        String safeQuestion = question
                .replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\n", "<br>");
        String script = UPDATE_CONTENT_SCRIPT.formatted(safeQuestion);
        engine.executeScript(script);
    }
}
