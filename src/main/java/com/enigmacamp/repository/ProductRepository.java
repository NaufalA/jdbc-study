package com.enigmacamp.repository;

import com.enigmacamp.model.Product;
import com.enigmacamp.model.ProductPrice;
import com.enigmacamp.shared.interfaces.IRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductRepository implements IRepository<Product> {
    Connection conn;
    String tableName;

    public ProductRepository(Connection conn, String tableName) {
        this.conn = conn;
        this.tableName = tableName;
    }

    @Override
    public Product insert(Product newItem) throws SQLException {
        Product insertedItem = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            String productQuery = String.format("INSERT INTO %s(product_name) VALUES(?) RETURNING *", tableName);

            st = conn.prepareStatement(productQuery);
            st.setString(1, newItem.productName);

            rs = st.executeQuery();
            while (rs.next()) {
                insertedItem = parse(rs);
            }

            if (insertedItem != null) {
                String priceQuery = "INSERT INTO product_prices(price,product_id) VALUES (?,?) RETURNING *";
                st = conn.prepareStatement(priceQuery);
                st.setFloat(1, newItem.productPrice.price);
                st.setInt(2, insertedItem.id);

                rs = st.executeQuery();
                while (rs.next()) {
                    insertedItem.productPrice = parsePrice(rs, false);
                }
            }

            conn.commit();
            return insertedItem;
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
    public List<Product> findAll() throws SQLException {
        String query = String.format(
                "SELECT t.id, t.product_name, r1.id AS price_id, r1.price " +
                        "FROM %s t " +
                        "INNER JOIN product_prices r1 ON t.id = r1.product_id " +
                        "WHERE t.is_deleted=false AND r1.is_active=true",
                tableName
        );
        List<Product> list = new ArrayList<>();

        PreparedStatement st = conn.prepareStatement(query);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            Product item = parse(rs);
            item.productPrice = parsePrice(rs, true);
            list.add(item);
        }

        rs.close();
        st.close();

        return list;
    }

    @Override
    public Product findById(Integer id) throws SQLException {
        String query = String.format(
                "SELECT t.id, t.product_name, r1.id AS price_id, r1.price " +
                        "FROM %s t " +
                        "INNER JOIN product_prices r1 ON t.id = r1.product_id " +
                        "WHERE t.id=%d AND t.is_deleted=false AND r1.is_active=true",
                tableName, id
        );
        Product item = null;

        PreparedStatement st = conn.prepareStatement(query);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            item = parse(rs);
            item.productPrice = parsePrice(rs, true);
        }

        rs.close();
        st.close();

        return item;
    }

    @Override
    public Product update(Integer id, Product updatedItem) throws SQLException {
        Product existingItem = findById(id);
        if (existingItem == null) {
            return null;
        }

        Product savedItem;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            String productQuery = String.format(
                    "UPDATE %s SET product_name=? WHERE id=%d RETURNING *",
                    tableName, existingItem.id
            );

            st = conn.prepareStatement(productQuery);
            st.setString(1, updatedItem.productName.equals("") ? existingItem.productName : updatedItem.productName);

            rs = st.executeQuery();

            savedItem = null;
            while (rs.next()) {
                savedItem = parse(rs);
            }
            if (
                    updatedItem.productPrice.price != -1 &&
                    !Objects.equals(updatedItem.productPrice.price, existingItem.productPrice.price)
            ) {
                String updateLastPriceQuery = String.format(
                        "UPDATE product_prices SET is_active=false WHERE id=%s",
                        existingItem.productPrice.id
                );

                st = conn.prepareStatement(updateLastPriceQuery);

                st.executeUpdate();

                String createPriceQuery = "INSERT INTO product_prices(price,product_id) VALUES (?,?) RETURNING *";
                st = conn.prepareStatement(createPriceQuery);
                st.setFloat(1, updatedItem.productPrice.price);
                st.setInt(2, existingItem.id);

                rs = st.executeQuery();

                while (rs.next()) {
                    if (savedItem != null) {
                        savedItem.productPrice = parsePrice(rs, false);
                    }
                }
            }
            conn.commit();
            return savedItem;
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

    private ProductPrice getPrice(Integer productId) throws SQLException {
        String query = String.format("SELECT * FROM product_prices WHERE product_id=%d AND is_active=true", productId);
        ProductPrice price = null;

        PreparedStatement st = conn.prepareStatement(query);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            price = parsePrice(rs, false);
        }

        rs.close();
        st.close();

        return price;
    }

    @Override
    public Integer delete(Integer id) throws SQLException {
        Product existingItem = findById(id);
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
    public Product parse(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("product_name")
        );
    }

    private ProductPrice parsePrice(ResultSet rs, Boolean fromRelation) throws SQLException {
        if (fromRelation) {
            return new ProductPrice(
                    rs.getInt("price_id"),
                    rs.getFloat("price")
            );
        }
        return new ProductPrice(
                rs.getInt("id"),
                rs.getFloat("price")
        );
    }
}
