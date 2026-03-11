package org.example.service;

import org.example.model.Article;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private static final String FILE_PATH = "articles.json";

    private final ObjectMapper mapper;
    private List<Article> articles = new ArrayList<>();

    public ArticleService() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        loadFromJson();
    }


    private void loadFromJson() {
        File file = new File(FILE_PATH);

        try {
            // 1. Check if it's actually a directory by mistake
            if (file.exists() && file.isDirectory()) {
                throw new IOException("Path 'articles.json' is a directory, not a file!");
            }

            // 2. If file doesn't exist or is empty, initialize with empty list
            if (!file.exists() || file.length() == 0) {
                this.articles = new ArrayList<>();
                // Optional: Create the file with [] so Jackson doesn't crash next time
                // mapper.writeValue(file, this.articles);
                return;
            }

            // 3. Normal loading logic
            articles = mapper.readValue(
                    file,
                    mapper.getTypeFactory().constructCollectionType(List.class, Article.class)
            );
        } catch (IOException e) {
            System.err.println("Critical Error loading JSON: " + e.getMessage());
            e.printStackTrace();
            this.articles = new ArrayList<>(); // Fallback to prevent NullPointerException
        }
    }

    private void saveToJson() {
        try {
            mapper.writeValue(new File(FILE_PATH), articles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Article> getAllArticles() {
        return articles.stream()
                .sorted(Comparator.comparing(Article::getPublicationDate).reversed())
                .collect(Collectors.toList());
    }

    public void saveArticle(Article article) {
        if (article.getId() == 0) { // New article
            int maxId = articles.stream().mapToInt(Article::getId).max().orElse(0);
            article.setId(maxId + 1);
        }
        articles.removeIf(a -> a.getId() == article.getId());
        articles.add(article);
        saveToJson();
    }

    public Article getArticleById(int id) {
        return articles.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void deleteArticle(int id) {
        articles.removeIf(a -> a.getId() == id);
        saveToJson();
    }
}