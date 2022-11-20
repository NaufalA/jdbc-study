package com.enigmacamp;

import com.enigmacamp.model.Product;
import com.enigmacamp.model.ProductPrice;
import com.enigmacamp.presenter.ProductPresenter;
import com.enigmacamp.repository.ProductRepository;
import com.enigmacamp.service.ProductService;
import com.enigmacamp.shared.utils.DBManager;

import java.sql.SQLException;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws Exception {
        try {
            DBManager.createConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Scanner scanner = new Scanner(System.in);

        ProductRepository productRepository = new ProductRepository(DBManager.getConnection(), "products");
        ProductService productService = new ProductService(productRepository);
        ProductPresenter productPresenter = new ProductPresenter(productService, scanner, "Product");

        try {
            Product kecap = new Product("Kecap", new ProductPrice(5000f));
            Product beras = new Product("Beras", new ProductPrice(10000f));
            kecap = productService.create(kecap);
            beras = productService.create(beras);

            productService.getAll().forEach(System.out::println);

            System.out.println(productService.getById(kecap.id));

            productService.update(beras.id, new Product("beras2", new ProductPrice(12000f)));

            System.out.println(productService.getById(beras.id));

            productService.remove(kecap.id);

            productService.getAll().forEach(System.out::println);
        } catch (Exception e) {
            DBManager.getConnection().rollback();
            throw e;
        }

        DBManager.closeConnection();
    }
}
