package org.skypro.skyshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.service.StorageService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @InjectMocks
    private BasketService basketService;

    @Mock
    private ProductBasket productBasket;

    @Mock
    private StorageService storageService;

    private UUID existingId;
    private UUID missingId;
    private SimpleProduct existingProduct;

    @BeforeEach
    void setUp() {
        existingId = UUID.randomUUID();
        missingId = UUID.randomUUID();

        existingProduct = new SimpleProduct(existingId, "Хлеб", 50.0);
    }

    // Сценарий: добавление несуществующего товара → выбрасывается NoSuchProductException
    @Test
    void addToBasket_whenProductDoesNotExist_throwsNoSuchProductException() {
        when(storageService.getProductById(missingId)).thenReturn(Optional.empty());

        assertThrows(NoSuchProductException.class, () -> {
            basketService.addToBasket(missingId);
        }, "Должен выбрасываться NoSuchProductException при отсутствии товара");

        verify(storageService).getProductById(missingId);
        verify(productBasket, never()).addProduct(missingId); // метод корзины не должен вызываться
    }

    // Сценарий: добавление существующего товара → вызывается addProduct у ProductBasket
    @Test
    void addToBasket_whenProductExists_callsProductBasketAddProduct() {
        when(storageService.getProductById(existingId)).thenReturn(Optional.of(existingProduct));

        basketService.addToBasket(existingId);

        verify(storageService).getProductById(existingId);
        verify(productBasket).addProduct(existingId);
    }

    // Сценарий: getUserBasket возвращает пустую корзину, если ProductBasket пуст
    @Test
    void getUserBasket_whenBasketIsEmpty_returnsEmptyUserBasket() {
        Map<UUID, Integer> emptyMap = Map.of();
        when(productBasket.getItems()).thenReturn(emptyMap);

        UserBasket result = basketService.getUserBasket();

        assertNotNull(result, "Результат не должен быть null");
        assertTrue(result.getItems().isEmpty(), "Список товаров должен быть пустым");
        assertEquals(0, result.getTotal(), 0.01, "Total должен быть равен 0");
        verify(productBasket).getItems();
    }

    // Сценарий: getUserBasket возвращает корзину с товарами и правильным total
    @Test
    void getUserBasket_whenBasketHasItems_returnsCorrectUserBasket() {
        UUID itemId = UUID.randomUUID();
        // Продукт, который мы ожидаем получить из StorageService
        SimpleProduct itemProduct = new SimpleProduct(itemId, "Молоко", 80.0);

        Map<UUID, Integer> basketMap = new HashMap<>();
        basketMap.put(itemId, 2); // 2 штуки

        when(productBasket.getItems()).thenReturn(basketMap);
        // Важно: мок должен возвращать тот же самый объект (или с такими же данными), что мы создали
        when(storageService.getProductById(itemId)).thenReturn(Optional.of(itemProduct));

        UserBasket result = basketService.getUserBasket();

        assertNotNull(result, "Результат не должен быть null");
        assertEquals(1, result.getItems().size(), "В корзине должен быть ровно 1 товар");

        BasketItem item = result.getItems().get(0);

        // ИСПРАВЛЕНИЕ: сравниваем не объекты, а их данные, чтобы избежать проблем с equals()
        assertEquals(itemId, item.getProduct().getId(), "ID продукта должен совпадать");
        assertEquals("Молоко", item.getProduct().getName(), "Название продукта должно совпадать");
        assertEquals(80.0, item.getProduct().getPrice(), 0.01, "Цена должна совпадать");

        assertEquals(2, item.getQuantity(), "Количество должно быть равно 2");
        assertEquals(160.0, result.getTotal(), 0.01, "Total должен быть 80 * 2 = 160");

        verify(productBasket).getItems();
        verify(storageService).getProductById(itemId);
    }

    // Дополнительный сценарий: проверка, что при отсутствии товара в хранилище во время getUserBasket выбрасывается исключение
    @Test
    void getUserBasket_whenProductMissingInStorage_throwsNoSuchProductException() {
        UUID missingItemId = UUID.randomUUID();
        Map<UUID, Integer> basketMap = new HashMap<>();
        basketMap.put(missingItemId, 1);

        when(productBasket.getItems()).thenReturn(basketMap);
        when(storageService.getProductById(missingItemId)).thenReturn(Optional.empty());

        assertThrows(NoSuchProductException.class, () -> {
            basketService.getUserBasket();
        }, "Должен выбрасываться NoSuchProductException, если товара нет в хранилище");

        verify(productBasket).getItems();
        verify(storageService).getProductById(missingItemId);
    }
}
