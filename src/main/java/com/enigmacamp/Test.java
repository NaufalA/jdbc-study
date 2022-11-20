package com.enigmacamp;

import com.enigmacamp.model.Transaction;
import com.enigmacamp.model.TransactionDetail;
import com.enigmacamp.repository.ProductRepository;
import com.enigmacamp.repository.TransactionRepository;
import com.enigmacamp.service.ProductService;
import com.enigmacamp.shared.utils.DBManager;

import java.sql.SQLException;
import java.util.*;

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

        TransactionRepository transactionRepository =
                new TransactionRepository(DBManager.getConnection(), "transactions");

//        try {
//            Product kecap = new Product("Kecap", new ProductPrice(5000f));
//            Product beras = new Product("Beras", new ProductPrice(10000f));
//            kecap = productService.create(kecap);
//            beras = productService.create(beras);
//
//            productService.getAll().forEach(System.out::println);
//
//            System.out.println(productService.getById(kecap.id));
//
//            productService.update(beras.id, new Product("beras2", new ProductPrice(12000f)));
//
//            System.out.println(productService.getById(beras.id));
//
//            productService.remove(kecap.id);
//
//            productService.getAll().forEach(System.out::println);
//        } catch (Exception e) {
//            DBManager.getConnection().rollback();
//            throw e;
//        }

        try {
            List<TransactionDetail> trxDetails = new ArrayList<>(
                    Arrays.asList(
                            new TransactionDetail(5f, 51),
                            new TransactionDetail(2f, 49)
                    )
            );
            long millis = System.currentTimeMillis();
            Transaction trx1 = new Transaction(new Date(millis), trxDetails);
            millis = System.currentTimeMillis();
            Transaction trx2 = new Transaction(new Date(millis), trxDetails);
            System.out.println("insert");
            trx1 = transactionRepository.insert(trx1);
            trx2 = transactionRepository.insert(trx2);

            System.out.println("findall");
            transactionRepository.findAll().forEach(System.out::println);
            System.out.println("find by id");
            System.out.println(transactionRepository.findById(trx1.id));
            System.out.println("delete" +
                    transactionRepository.delete(trx2.id)
            );
            transactionRepository.findAll().forEach(System.out::println);

        } catch (Exception e) {
            DBManager.getConnection().rollback();
            throw e;
        }

        DBManager.closeConnection();
    }
}
