package ru.javaboys.vibejson.view.component;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.List;

/**
 * Превращает ответ ассистента из Markdown в HTML.
 * <p>
 * Сырой HTML в тексте экранируется, а небезопасные ссылки вычищаются: ответ
 * модели - внешние данные, и вставлять его в страницу как есть нельзя.
 */
public final class MarkdownRenderer {

    private static final List<Extension> EXTENSIONS = List.of(TablesExtension.create());

    private static final Parser PARSER = Parser.builder()
            .extensions(EXTENSIONS)
            .build();

    private static final HtmlRenderer RENDERER = HtmlRenderer.builder()
            .extensions(EXTENSIONS)
            .escapeHtml(true)
            .sanitizeUrls(true)
            .softbreak("<br>")
            .build();

    private MarkdownRenderer() {
    }

    /**
     * Возвращает HTML с единственным корневым элементом - этого требует
     * компонент {@link com.vaadin.flow.component.Html}.
     */
    public static String toHtml(String markdown) {
        String body = markdown == null ? "" : RENDERER.render(PARSER.parse(markdown));
        return "<div class=\"vj-md\">" + body + "</div>";
    }
}
