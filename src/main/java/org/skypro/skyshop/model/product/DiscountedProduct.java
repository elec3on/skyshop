package org.skypro.skyshop.model.product;

import java.util.UUID;

public class DiscountedProduct extends Product {
    private final double basePrice;
    private final int discountPercent;

    public DiscountedProduct(UUID id, String name, double basePrice, int discountPercent) {
        super(id, name);

        if (basePrice <= 0) {
            throw new IllegalArgumentException(
                    "Базовая цена должна быть больше 0."
            );
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException(
                    "Процент скидки должен быть между 0 и 100 (включительно)."
            );
        }

        this.basePrice = basePrice;
        this.discountPercent = discountPercent;
    }

    @Override
    public double getPrice() {
        return basePrice * (1 - discountPercent / 100.0);
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public String toString() {
        return getName() + ": " + getPrice() + " (" + discountPercent + "%)";
    }
}
