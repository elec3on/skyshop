package org.skypro.skyshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @InjectMocks
    private SearchService searchService;

    @Mock
    private StorageService storageService;

    @Test
    void search_whenStorageIsEmpty_returnsEmptyList() {
        List<Searchable> empty = new ArrayList<>();

        when(storageService.getAllSearchable())
                .thenAnswer(invocation -> empty);

        List<SearchResult> result = searchService.search("любой запрос");

        assertTrue(result.isEmpty(), "При пустом хранилище поиск должен вернуть пустой список");
        verify(storageService).getAllSearchable();
    }

    @Test
    void search_whenNoMatchingObjects_returnsEmptyList() {
        Searchable milk = new SimpleProduct(UUID.randomUUID(), "Молоко", 89.0);
        Searchable article = new Article(UUID.randomUUID(), "О молоке", "Текст про молоко");

        List<Searchable> all = new ArrayList<>();
        all.add(milk);
        all.add(article);

        when(storageService.getAllSearchable())
                .thenAnswer(invocation -> all);

        String query = "Хлеб"; // запроса нет в названиях
        List<SearchResult> result = searchService.search(query);

        assertTrue(result.isEmpty(), "Поиск по несуществующему слову должен вернуть пустой список");
        verify(storageService).getAllSearchable();
    }

    // Этот тест теперь строго соответствует критерию: имя "TestProduct", поиск по "Test"
    @Test
    void search_whenMatchingObjectExists_returnsIt_withTestProduct() {
        UUID productId = UUID.randomUUID();
        Searchable product = new SimpleProduct(productId, "TestProduct", 99.0);

        List<Searchable> all = new ArrayList<>();
        all.add(product);

        when(storageService.getAllSearchable())
                .thenAnswer(invocation -> all);

        String query = "Test";
        List<SearchResult> result = searchService.search(query);

        assertEquals(1, result.size(), "Поиск должен найти ровно один подходящий объект");

        SearchResult found = result.get(0);
        assertNotNull(found, "Найденный результат не должен быть null");

        assertEquals(productId.toString(), found.getId());
        assertEquals("TestProduct", found.getName());
        assertEquals("PRODUCT", found.getContentType());

        verify(storageService).getAllSearchable();
    }

    // Дополнительный тест: проверка, что поиск чувствителен к регистру (или нечувствителен, если так реализовано)
    // Если в searchService есть toLowerCase() — этот тест тоже должен пройти
    @Test
    void search_caseInsensitive_match_returnsIt() {
        UUID productId = UUID.randomUUID();
        // Название в верхнем регистре
        Searchable product = new SimpleProduct(productId, "TESTPRODUCT", 150.0);

        List<Searchable> all = new ArrayList<>();
        all.add(product);

        when(storageService.getAllSearchable())
                .thenAnswer(invocation -> all);

        String query = "test"; // запрос в нижнем регистре
        List<SearchResult> result = searchService.search(query);

        assertEquals(1, result.size(), "Поиск должен находить товар даже при разном регистре");
        assertEquals(productId.toString(), result.get(0).getId());
    }
}
