package com.enigmacamp.presenter;

import com.enigmacamp.model.Product;
import com.enigmacamp.model.Transaction;
import com.enigmacamp.model.TransactionDetail;
import com.enigmacamp.presenter.interfaces.ITransactionPresenter;
import com.enigmacamp.service.ProductService;
import com.enigmacamp.service.TransactionService;
import com.enigmacamp.shared.utils.InputHelper;
import com.enigmacamp.shared.utils.StringHelper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class TransactionPresenter implements ITransactionPresenter {
    private final TransactionService transactionService;
    private final ProductService productService;
    private final Scanner scanner;
    private final String title;

    public TransactionPresenter(
            TransactionService transactionService, ProductService productService, Scanner scanner, String title
    ) {
        this.transactionService = transactionService;
        this.productService = productService;
        this.scanner = scanner;
        this.title = title;
    }

    public void mainMenu() {
        int menuInput = -1;
        while (menuInput != 0) {
            StringHelper.printHeader(title);
            System.out.println("1. Create " + title);
            System.out.println("2. Get " + title);
            System.out.println("3. Delete " + title);
            System.out.println("0. Exit");
            StringHelper.printInputPrompt("Select Feature Menu");
            menuInput = InputHelper.inputInt(scanner);
            switch (menuInput) {
                case 1:
                    StringHelper.printHeader("Create " + title);
                    createMenu();
                    break;
                case 2:
                    StringHelper.printHeader(title + " Detail");
                    getAll(this::getDetailMenu);
                    break;
                case 3:
                    StringHelper.printHeader("Delete " + title);
                    getAll(this::removeMenu);
                    break;
                case 0:
                    break;
            }
        }
    }

    public void createMenu() {
        Transaction newTransaction = new Transaction();
        StringHelper.printInputPrompt(title + " Date (yyyy-mm-dd)");
        newTransaction.transaction_date = Date.valueOf(scanner.nextLine());

        List<Product> products = new ArrayList<>();
        try {
            products = productService.getAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        boolean stop = false;
        int i = 0;
        while (!stop) {
            TransactionDetail newDetail = new TransactionDetail();
            StringHelper.printHeader(++i + ". ");
            for (int j = 0; j < products.size(); j++) {
                System.out.printf("%d\t| %s\n", (j + 1), products.get(j));
            }
            StringHelper.printInputPrompt("Select Product index");
            newDetail.productPriceId =
                    products.get(InputHelper.inputInt(scanner, 1, products.size()) - 1).productPrice.id;
            StringHelper.printInputPrompt("Quantity");
            newDetail.quantity = InputHelper.inputFloat(scanner);

            newTransaction.transactionDetails.add(newDetail);

            stop = !InputHelper.confirmation("Add another item?", scanner);
        }

        try {
            Transaction insertedProduct = transactionService.create(newTransaction);
            System.out.println("New " + title + " Created");
            System.out.println(insertedProduct);
        } catch (Exception e) {
            System.out.println("Error while creating new " + title);
            System.out.println(e.getMessage());
        }
    }

    public void getAll(Consumer<Integer> nextAction) {
        try {
            List<Transaction> transactions = transactionService.getAll();
            if (transactions.size() == 0) {
                System.out.println(title + " is empty");
                return;
            }
            for (int i = 0; i < transactions.size(); i++) {
                Transaction transaction = transactions.get(i);
                System.out.printf(
                        "%d. Date : %s\t Items : %d\n", (i + 1),
                        transaction.transaction_date,
                        transaction.transactionDetails.size()
                );
            }
            StringHelper.printInputPrompt("Select transaction index");
            int selectedIndex = InputHelper.inputInt(scanner, 1, transactions.size()) - 1;
            nextAction.accept(transactions.get(selectedIndex).id);
        } catch (Exception e) {
            System.out.println("Error While Getting " + title + "s");
            System.out.println(e.getMessage());
        }
    }

    public void getDetailMenu(Integer id) {
        try {
            Transaction transaction = transactionService.getById(id);
            System.out.println("ID\t\t" + transaction.id);
            System.out.println("Date\t" + transaction.transaction_date);
            System.out.println("Items\t");
            System.out.println("| Product\t\t Price\t Quantity");
            System.out.println("|---------------------------------------");
            transaction.transactionDetails.forEach(
                    td -> System.out.println(
                            "| " + td.productPrice.product.productName +
                                    "\t " + td.productPrice.price +
                                    "\t " + td.quantity)
            );
        } catch (Exception e) {
            System.out.println("Error While Getting " + title + " Detail");
            System.out.println(e.getMessage());
        }
    }

    public void removeMenu(Integer id) {
        if (!InputHelper.confirmation("Are you sure you want to remove transaction?", scanner)) {

            return;
        }
        try {
            Integer removedId = transactionService.remove(id);
            if (removedId != -1) {
                System.out.println(title + " With ID " + removedId + " Removed Successfully");
            } else {
                System.out.println(title + " Not Found");
            }
        } catch (Exception e) {
            System.out.println("Error While Updating " + title);
            System.out.println(e.getMessage());
        }
    }
}
