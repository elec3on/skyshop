package org.skypro.skyshop.service;

import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasketService {

    private final ProductBasket basket;
    private final StorageService storageService;

    // Конструктор, private final, внедрение через конструктор
    public BasketService(ProductBasket basket, StorageService storageService) {
        this.basket = basket;
        this.storageService = storageService;
    }

    // orElseThrow для выброса исключения
    public void addToBasket(UUID productId) {
        storageService.getProductById(productId)
                .orElseThrow(() -> new NoSuchProductException(productId)); // передаём UUID
        basket.addProduct(productId);
    }

    public UserBasket getUserBasket() {
        var basketMap = basket.getItems();

        var items = basketMap.entrySet().stream()
                .map(e -> {
                    Product p = storageService.getProductById(e.getKey())
                            .orElseThrow(() -> new IllegalStateException("Товар исчез из хранилища: " + e.getKey()));
                    return new BasketItem(p, e.getValue());
                })
                .collect(Collectors.toList());

        return new UserBasket(items);
    }
}