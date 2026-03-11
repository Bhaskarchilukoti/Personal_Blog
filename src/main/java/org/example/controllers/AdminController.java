package org.example.controllers;

import org.example.model.Article;
import org.example.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ArticleService articleService;

    public AdminController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) throws IOException {
        model.addAttribute("articles", articleService.getAllArticles());
        return "dashboard";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("article", new Article());
        return "admin-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "admin-form"; // This will now work for both add and edit
    }

    @PostMapping("/save")
    public String saveArticle(@ModelAttribute Article article) {
        if (article.getPublicationDate() == null) {
            article.setPublicationDate(LocalDate.now());
        }
        articleService.saveArticle(article);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        articleService.deleteArticle(id);
        return "redirect:/admin/dashboard";
    }

}
