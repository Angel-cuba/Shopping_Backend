package com.example.backend.Address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public Address findById(UUID id) {
        return addressRepository.findById(id).orElse(null);
    }

    public Address createOne(Address address) {
        return addressRepository.save(address);
    }

    public List<Address> findAddressesByUserId(UUID id) {
        return addressRepository.findAddressesByUserId(id);
    }
    public Address updateOne(Address address) {
        Address addressToUpdate = addressRepository.findById(address.getId()).orElse((null));
        if (addressToUpdate == null) {
            return null;
        }
        addressToUpdate.setAddress(address.getAddress());
        addressToUpdate.setCity(address.getCity());
        addressToUpdate.setPostalCode(address.getPostalCode());
        addressToUpdate.setCountry(address.getCountry());
        addressToUpdate.setUser(address.getUser());
        return addressRepository.save(addressToUpdate);
    }

    public void deleteById(UUID id) {
        addressRepository.deleteById(id);
    }
}