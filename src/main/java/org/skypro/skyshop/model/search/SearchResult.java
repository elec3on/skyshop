package org.skypro.skyshop.model.search;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;

public class SearchResult {
    private final String id;
    private final String name;
    private final String contentType;

    public SearchResult(String id, String name, String contentType) {
        this.id = id;
        this.name = name;
        this.contentType = contentType;
    }

    public static SearchResult fromSearchable(Searchable s) {
        String idStr = s.getId().toString();
        String name = s.getName();          // полиморфно: у Product это name, у Article — title
        String contentType = s.getContentType();

        return new SearchResult(idStr, name, contentType);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getContentType() { return contentType; }
}