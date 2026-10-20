package org.skypro.skyshop.controller;


import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.service.BasketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/basket")
public class BasketController {

    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    /**
     * Добавление продукта в корзину через путь URL.
     */
    @GetMapping("/{id}")
    public String addProduct(@PathVariable("id") UUID id) {
        basketService.addToBasket(id);
        return "Продукт успешно добавлен";
    }

    /**
     * Получение содержимого корзины.
     * Возвращает UserBasket с товарами и итоговой суммой.
     */
    @GetMapping
    public UserBasket getUserBasket() {
        return basketService.getUserBasket();
    }
}