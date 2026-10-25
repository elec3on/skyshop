package org.skypro.skyshop.service;

import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
        Map<UUID, Integer> basketMap = basket.getItems();

        List<BasketItem> items = basketMap.entrySet().stream()
                .map(e -> {
                    UUID productId = e.getKey();
                    int quantity = e.getValue();

                    // ИСПРАВЛЕНИЕ: используем NoSuchProductException вместо IllegalStateException
                    Product p = storageService.getProductById(productId)
                            .orElseThrow(() -> new NoSuchProductException(productId));

                    return new BasketItem(p, quantity);
                })
                .collect(Collectors.toList());

        return new UserBasket(items);
    }
}