package org.example.service;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

@Service
public class MarkdownService {

    public String renderMarkdownToHtml(String markdown) {
        if (markdown == null) return "";

        Parser parser = Parser.builder().build();
        org.commonmark.node.Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        return renderer.render(document);
    }
}
