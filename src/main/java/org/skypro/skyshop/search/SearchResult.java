package org.skypro.skyshop.search;

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
        String name;
        if (s instanceof Product p) {
            name = p.getName();
        } else if (s instanceof Article a) {
            name = a.getTitle();
        } else {
            name = "Unknown";
        }
        return new SearchResult(idStr, name, s.getContentType());
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getContentType() { return contentType; }
}