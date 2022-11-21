package com.enigmacamp.presenter;

import com.enigmacamp.model.Product;
import com.enigmacamp.model.ProductPrice;
import com.enigmacamp.presenter.interfaces.IProductPresenter;
import com.enigmacamp.service.ProductService;
import com.enigmacamp.shared.utils.InputHelper;
import com.enigmacamp.shared.utils.StringHelper;

import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class ProductPresenter implements IProductPresenter {
    private final ProductService service;
    private final Scanner scanner;
    private final String title;

    public ProductPresenter(ProductService service, Scanner scanner, String title) {
        this.service = service;
        this.scanner = scanner;
        this.title = title;
    }

    public void mainMenu() {
        int menuInput = -1;
        while (menuInput != 0) {
            StringHelper.printHeader(title);
            System.out.println("1. Create " + title);
            System.out.println("2. Get " + title);
            System.out.println("3. Update " + title);
            System.out.println("4. Delete " + title);
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
                    StringHelper.printHeader("Update " + title);
                    getAll(this::updateMenu);
                    break;
                case 4:
                    StringHelper.printHeader("Delete " + title);
                    getAll(this::removeMenu);
                    break;
                case 0:
                    break;
            }
        }
    }

    public void createMenu() {
        Product newProduct = new Product();

        StringHelper.printInputPrompt(title + " Name");
        newProduct.productName = scanner.nextLine();
        StringHelper.printInputPrompt(title + " Price");
        newProduct.productPrice = new ProductPrice(InputHelper.inputFloat(scanner));
        try {
            Product insertedProduct = service.create(newProduct);
            System.out.println("New " + title + " Created");
            System.out.println(insertedProduct);
        } catch (Exception e) {
            System.out.println("Error while creating new " + title);
            System.out.println(e.getMessage());
        }
    }

    public void getAll(Consumer<Integer> nextAction) {
        try {
            List<Product> products = service.getAll();
            if (products.size() == 0) {
                System.out.println(title + " is empty");
                return;
            }
            for (int i = 0; i < products.size(); i++) {
                System.out.printf("%d\t| %s\n", (i + 1), products.get(i));
            }
            StringHelper.printInputPrompt("Select product index");
            int selectedIndex = InputHelper.inputInt(scanner);
            nextAction.accept(products.get(selectedIndex - 1).id);
        } catch (Exception e) {
            System.out.println("Error While Setting " + title + "s");
            System.out.println(e.getMessage());
        }
    }

    public void getDetailMenu(Integer id) {
        try {
            Product product = service.getById(id);
            System.out.println("ID\t\t" + product.id);
            System.out.println("Name\t" + product.productName);
            System.out.println("Price\t" + product.productPrice.price);
        } catch (Exception e) {
            System.out.println("Error While Getting " + title + " Detail");
            System.out.println(e.getMessage());
        }
    }

    public void updateMenu(Integer id) {
        try {
            Product newProduct = new Product();

            StringHelper.printInputPrompt(title + " Name");
            newProduct.productName = scanner.nextLine();
            StringHelper.printInputPrompt(title + " Price");
            newProduct.productPrice = new ProductPrice(InputHelper.inputFloat(scanner));
            Product updatedProduct = service.update(id, newProduct);
            if (updatedProduct != null) {
                System.out.println(title + " Updated Successfully");
                System.out.println(updatedProduct);
            } else {
                System.out.println(title + " Not Found");
            }
        } catch (Exception e) {
            System.out.println("Error While Updating " + title);
            System.out.println(e.getMessage());
        }
    }

    public void removeMenu(Integer id) {
        try {
            Integer removedId = service.remove(id);
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
