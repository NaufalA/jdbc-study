package com.enigmacamp;

import com.enigmacamp.presenter.ProductPresenter;
import com.enigmacamp.presenter.TransactionPresenter;
import com.enigmacamp.repository.ProductRepository;
import com.enigmacamp.repository.TransactionRepository;
import com.enigmacamp.service.ProductService;
import com.enigmacamp.service.TransactionService;
import com.enigmacamp.shared.utils.DBManager;
import com.enigmacamp.shared.utils.InputHelper;
import com.enigmacamp.shared.utils.StringHelper;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
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

        TransactionRepository transactionRepository = new TransactionRepository(DBManager.getConnection(), "transactions");
        TransactionService transactionService = new TransactionService(transactionRepository);
        TransactionPresenter transactionPresenter = new TransactionPresenter(transactionService, productService, scanner, "Transaction");

        int menuInput = -1;
        while (menuInput != 0) {
            StringHelper.printHeader("Enigma Mart");
            System.out.println("1. Product");
            System.out.println("2. Transaction");
            System.out.println("3. Report");
            System.out.println("0. Exit");
            StringHelper.printInputPrompt("Select Feature Menu");
            menuInput = InputHelper.inputInt(scanner);
            switch (menuInput) {
                case 1:
                    productPresenter.mainMenu();
                    break;
                case 2:
                    transactionPresenter.mainMenu();
                    break;
                case 3:
                    break;
                case 0:
                    break;
            }
        }

        DBManager.closeConnection();
    }
}