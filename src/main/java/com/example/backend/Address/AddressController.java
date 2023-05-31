package com.example.backend.Address;

import com.example.backend.Exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:3000", "https://starlit-bienenstitch-282c7d.netlify.app"})
@RestController
@RequestMapping("api/v1/addresses")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping
    public List<Address> findAll() {
        return addressService.findAll();
    }

    @GetMapping("/user/{id}")
    public List<Address> findAddressesByUserId(@PathVariable UUID id) {
        return addressService.findAddressesByUserId(id);
    }

    @GetMapping("/{id}")
    public Address findById(@PathVariable UUID id) {
        return addressService.findById(id);
    }

    @PostMapping
    public Address createOne(@RequestBody Address address) {
        return addressService.createOne(address);
    }

    @PutMapping
    public Address updateOne(@RequestBody Address address) {
        return addressService.updateOne(address);
    }

    @DeleteMapping("/{id}")
    public  void deleteOne(@PathVariable UUID id) {
        addressService.deleteById(id);
    }


}
