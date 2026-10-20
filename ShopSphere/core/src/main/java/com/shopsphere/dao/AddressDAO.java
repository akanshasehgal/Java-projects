package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopsphere.model.Address;

public class AddressDAO {

    public List<Address> findByUser(int userId) {
        List<Address> list = new ArrayList<>();
        String sql = "SELECT * FROM addresses WHERE user_id = ? ORDER BY address_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Could not load addresses", e);
        }
        return list;
    }

    /** Returns the address only if it belongs to the given user. */
    public Address findByIdForUser(int addressId, int userId) {
        String sql = "SELECT * FROM addresses WHERE address_id = ? AND user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, addressId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new DAOException("Could not load address", e);
        }
    }

    public void insert(Address a) {
        String sql = "INSERT INTO addresses (user_id, address_line, city, state, pincode, address_type) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getUserId());
            ps.setString(2, a.getAddressLine());
            ps.setString(3, a.getCity());
            ps.setString(4, a.getState());
            ps.setString(5, a.getPincode());
            ps.setString(6, a.getAddressType());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Could not save address", e);
        }
    }

    public void delete(int addressId, int userId) {
        String sql = "DELETE FROM addresses WHERE address_id = ? AND user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, addressId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Could not delete address", e);
        }
    }

    private Address map(ResultSet rs) throws SQLException {
        Address a = new Address();
        a.setAddressId(rs.getInt("address_id"));
        a.setUserId(rs.getInt("user_id"));
        a.setAddressLine(rs.getString("address_line"));
        a.setCity(rs.getString("city"));
        a.setState(rs.getString("state"));
        a.setPincode(rs.getString("pincode"));
        a.setAddressType(rs.getString("address_type"));
        return a;
    }
}
