package com.shopsphere.service;

import java.util.Arrays;
import java.util.List;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.model.Address;

/** Delivery address rules. */
public class AddressService {

    public static final List<String> TYPES = Arrays.asList("HOME", "WORK", "OTHER");

    private final AddressDAO addressDAO = new AddressDAO();

    public List<Address> getAddresses(int userId) {
        return addressDAO.findByUser(userId);
    }

    public void addAddress(Address a) {
        a.setAddressLine(AuthService.trim(a.getAddressLine()));
        a.setCity(AuthService.trim(a.getCity()));
        a.setState(AuthService.trim(a.getState()));
        a.setPincode(AuthService.trim(a.getPincode()));
        if (a.getAddressLine().length() < 5) throw new ServiceException("Please enter the full street address.");
        if (a.getCity().isEmpty() || a.getState().isEmpty()) throw new ServiceException("City and state are required.");
        if (!a.getPincode().matches("[1-9]\\d{5}")) throw new ServiceException("Please enter a valid 6-digit PIN code.");
        if (!TYPES.contains(a.getAddressType())) a.setAddressType("HOME");
        addressDAO.insert(a);
    }

    public void deleteAddress(int addressId, int userId) {
        addressDAO.delete(addressId, userId);
    }
}
