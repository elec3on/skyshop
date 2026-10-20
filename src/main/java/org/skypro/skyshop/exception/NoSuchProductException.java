package org.skypro.skyshop.exception;

import java.util.UUID;

public class NoSuchProductException extends RuntimeException {

    public NoSuchProductException(UUID productId) {
        super("Товар с ID " + productId + " не найден");
    }

    public NoSuchProductException(String message) {
        super(message);
    }
}