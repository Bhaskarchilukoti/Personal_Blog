package org.example.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Article {
    private int id;
    private String title;
    private String content;
    private LocalDate publicationDate;
}
