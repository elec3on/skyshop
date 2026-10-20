package org.skypro.skyshop.model.basket;

import java.util.List;
import java.util.Objects;

public class UserBasket {

    private final List<BasketItem> items;
    private final int total; // считаем как int

    // Конструктор принимает List<BasketItem>, total вычисляется в конструкторе
    public UserBasket(List<BasketItem> items) {
        Objects.requireNonNull(items, "Список товаров не может быть null");
        this.items = List.copyOf(items); // неизменяемый список

        // Stream API + mapToInt(...).sum()
        this.total = items.stream()
                .mapToInt(item -> (int) (item.getProduct().getPrice() * item.getQuantity()))
                .sum();
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public int getTotal() {
        return total;
    }
}
