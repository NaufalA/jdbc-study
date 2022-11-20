package com.enigmacamp.repository;

import com.enigmacamp.model.Transaction;
import com.enigmacamp.model.TransactionDetail;
import com.enigmacamp.repository.interfaces.ITransactionRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionRepository implements ITransactionRepository<Transaction> {
    Connection conn;
    String tableName;

    public TransactionRepository(Connection conn, String tableName) {
        this.conn = conn;
        this.tableName = tableName;
    }

    @Override
    public Transaction insert(Transaction newItem) throws SQLException {
        Transaction insertedItem = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            String productQuery = String.format("INSERT INTO %s(transaction_date) VALUES(?) RETURNING *", tableName);

            st = conn.prepareStatement(productQuery);
            st.setDate(1, new Date(newItem.transaction_date.getTime()));

            rs = st.executeQuery();
            while (rs.next()) {
                insertedItem = parse(rs);
            }

            if (insertedItem != null) {
                for (TransactionDetail detail : newItem.transactionDetails) {
                    String priceQuery =
                            "INSERT INTO transaction_details(quantity,product_price_id,transaction_id) " +
                                    "VALUES (?,?,?) " +
                                    "RETURNING id as detail_id, quantity, product_price_id, transaction_id";
                    st = conn.prepareStatement(priceQuery);
                    st.setFloat(1, detail.quantity);
                    st.setInt(2, detail.productPriceId);
                    st.setInt(3, insertedItem.id);

                    rs = st.executeQuery();
                    while (rs.next()) {
                        insertedItem.transactionDetails.add(parseDetail(rs, false));
                    }
                }
            }
            conn.commit();
            return insertedItem;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public List<Transaction> findAll() throws SQLException {
        String query = String.format(
                "SELECT " +
                        "t.id, t.transaction_date, " +
                        "r1.id AS detail_id, r1.quantity, r1.product_price_id, " +
                        "r2.price, r3.product_name " +
                        "FROM %s t " +
                        "INNER JOIN transaction_details r1 ON t.id = r1.transaction_id " +
                        "INNER JOIN product_prices r2 ON r1.product_price_id = r2.id " +
                        "INNER JOIN products r3 ON r2.product_id = r3.id " +
                        "WHERE t.is_deleted=false",
                tableName
        );
        List<Transaction> list = new ArrayList<>();
        Transaction item = null;

        PreparedStatement st = conn.prepareStatement(query);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            Integer itemId = rs.getInt("id");
            if (item == null) {
                item = parse(rs);
                item.transactionDetails.add(parseDetail(rs, true));
            } else if (Objects.equals(item.id, itemId)) {
                item.transactionDetails.add(parseDetail(rs, true));
            } else {
                list.add(item);
                item = parse(rs);
                item.transactionDetails.add(parseDetail(rs, true));
            }
        }

        rs.close();
        st.close();

        return list;
    }

    @Override
    public Transaction findById(Integer id) throws SQLException {
        String query = String.format(
                "SELECT " +
                        "t.id, t.transaction_date, " +
                        "r1.id AS detail_id, r1.quantity, r1.product_price_id, " +
                        "r2.price, r3.product_name " +
                        "FROM %s t " +
                        "INNER JOIN transaction_details r1 ON t.id = r1.transaction_id " +
                        "INNER JOIN product_prices r2 ON r1.product_price_id = r2.id " +
                        "INNER JOIN products r3 ON r2.product_id = r3.id " +
                        "WHERE t.id=%d AND t.is_deleted=false",
                tableName, id
        );

        Transaction item = null;

        PreparedStatement st = conn.prepareStatement(query);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            if (item == null) {
                item = parse(rs);
            }
            item.transactionDetails.add(parseDetail(rs, true));
        }

        rs.close();
        st.close();

        return item;
    }

    @Override
    public Integer delete(Integer id) throws SQLException {
        Transaction existingItem = findById(id);
        if (existingItem == null) {
            return -1;
        }

        PreparedStatement st = null;
        try {
            String productQuery = String.format("UPDATE %s SET is_deleted=true WHERE id=%d", tableName, existingItem.id);

            st = conn.prepareStatement(productQuery);

            st.executeUpdate();

            conn.commit();
            return id;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public Transaction parse(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getInt("id"),
                rs.getDate("transaction_date")
        );
    }

    private TransactionDetail parseDetail(ResultSet rs, boolean withPrice) throws SQLException {
        if (withPrice) {
            return new TransactionDetail(
                    rs.getInt("detail_id"),
                    rs.getFloat("quantity"),
                    rs.getInt("product_price_id")
            );
        }
        return new TransactionDetail(
                rs.getInt("detail_id"),
                rs.getFloat("quantity"),
                rs.getInt("product_price_id")
        );
    }
}
