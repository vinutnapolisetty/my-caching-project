package com.cache.usecase.usecase2;

import com.cache.usecase.Product;


import java.util.Random;

public class DataGenerator {
    private static final Random random = new Random();

    public static Product generateProduct(String id) {
        return new Product(id, "Product " + id, 10 + random.nextDouble() * 990);
    }

    public static String generateRandomId(int maxId) {
        return "PROD" + random.nextInt(maxId);
    }
}
