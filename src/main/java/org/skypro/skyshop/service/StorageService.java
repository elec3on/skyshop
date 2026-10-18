package org.skypro.skyshop.service;

import org.skypro.skyshop.model.product.*;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.UUID;

@Service
public class StorageService {
    private final Map<UUID, Product> products;
    private final Map<UUID, Article> articles;

    public StorageService() {
        this.products = new HashMap<>();
        this.articles = new HashMap<>();
        initData();
    }

    private void initData() {
        // --- Товары как в App.java ---
        UUID idMilk = UUID.randomUUID();
        UUID idBread = UUID.randomUUID();
        UUID idSugar = UUID.randomUUID();
        UUID idJuice = UUID.randomUUID();
        UUID idSalt = UUID.randomUUID();

        products.put(idMilk, new SimpleProduct(idMilk, "Молоко", 89.0));
        products.put(idBread, new DiscountedProduct(idBread, "Хлеб", 50.0, 20));      // скидка 20%
        products.put(idSugar, new FixPriceProduct(idSugar, "Сахар"));
        products.put(idJuice, new DiscountedProduct(idJuice, "Сок", 120.0, 15));    // скидка 15%
        products.put(idSalt, new FixPriceProduct(idSalt, "Соль"));

        // --- Статьи как в App.java ---
        UUID art1 = UUID.randomUUID();
        UUID art2 = UUID.randomUUID();
        UUID art3 = UUID.randomUUID();

        articles.put(art1, new Article(art1, "О молоке", "Молоко — полезный напиток, богатый кальцием."));
        articles.put(art2, new Article(art2, "Как выбрать хлеб", "Свежий хлеб должен иметь ароматную корочку."));
        articles.put(art3, new Article(art3, "Сахар и соль", "Сахар и соль — самые популярные приправы на кухне."));
    }

    public Collection<Product> getAllProducts() {
        return Collections.unmodifiableCollection(products.values());
    }

    public Collection<Article> getAllArticles() {
        return Collections.unmodifiableCollection(articles.values());
    }

    public Collection<? extends Searchable> getAllSearchable() {
        return Stream.concat(
                products.values().stream(),
                articles.values().stream()
        ).collect(Collectors.toList());
    }
}