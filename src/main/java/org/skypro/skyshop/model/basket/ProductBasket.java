package org.skypro.skyshop.model.basket;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@SessionScope
public class ProductBasket {

    private final Map<UUID, Integer> items;

    public ProductBasket() {
        this.items = new HashMap<>();
    }

    /**
     * Добавляет товар в корзину. Если товар уже есть — увеличивает количество на 1.
     */
    public void addProduct(UUID productId) {
        items.computeIfAbsent(productId, k -> 0);
        items.put(productId, items.get(productId) + 1);
    }

    /**
     * Возвращает неизменяемое представление содержимого корзины.
     * Это защищает внутреннюю мапу от внешних изменений.
     */
    public Map<UUID, Integer> getItems() {
        return Collections.unmodifiableMap(items);
    }
}
