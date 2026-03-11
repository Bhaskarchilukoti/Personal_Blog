package org.example.controllers;


import lombok.RequiredArgsConstructor;
import org.example.model.Article;
import org.example.service.ArticleService;
import org.example.service.MarkdownService;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static java.lang.Integer.parseInt;

@Controller
public class GuestController {
    private final ArticleService articleService;
    private final MarkdownService markdownService; // Inject the new service

    public GuestController(ArticleService articleService, MarkdownService markdownService) {
        this.articleService = articleService;
        this.markdownService = markdownService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return "home"; // Ensure you create a home.html template
    }


    @GetMapping("/article/{id}")
    public String viewArticle(@PathVariable int id, Model model) {
        Article article = articleService.getArticleById(id);

        if (article != null) {
            // Convert the Markdown content to HTML string
            String htmlContent = markdownService.renderMarkdownToHtml(article.getContent());
            model.addAttribute("article", article);
            model.addAttribute("htmlContent",htmlContent); // Pass the HTML to the view
        }

        return "article";
    }

}
