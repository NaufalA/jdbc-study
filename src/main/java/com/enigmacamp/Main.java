package com.enigmacamp;

import com.enigmacamp.model.Product;
import com.enigmacamp.model.ProductPrice;
import com.enigmacamp.repository.ProductRepository;
import com.enigmacamp.shared.utils.DBManager;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        try {
            DBManager.createConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ProductRepository productRepository = new ProductRepository(DBManager.getConnection(), "products");

        try {
            Product kecap = new Product("Kecap", new ProductPrice(5000f));
            Product beras = new Product("Beras", new ProductPrice(10000f));
            kecap = productRepository.insert(kecap);
            beras = productRepository.insert(beras);

            productRepository.findAll().forEach(System.out::println);

            System.out.println(productRepository.findById(kecap.id));

            productRepository.update(beras.id, new Product("beras2", new ProductPrice(12000f)));

            System.out.println(productRepository.findById(beras.id));

            productRepository.delete(kecap.id);

            productRepository.findAll().forEach(System.out::println);
        } catch (Exception e) {
            DBManager.getConnection().rollback();
            throw e;
        }
    }
}