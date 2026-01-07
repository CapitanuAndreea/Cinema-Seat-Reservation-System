package com.example.service;

import com.example.exceptions.NotFoundException;
import com.example.model.Customer;
import com.example.model.dto.CreateCustomerRequest;
import com.example.model.dto.CustomerResponse;
import com.example.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CustomerResponse findById(Long id) {
        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id=" + id));
        return toResponse(c);
    }

    @Transactional
    public CustomerResponse add(CreateCustomerRequest request) {
        Customer c = new Customer(request.fullName(), request.email());
        Customer saved = customerRepository.save(c);
        return toResponse(saved);
    }

    private CustomerResponse toResponse(Customer c) {
        return new CustomerResponse(c.getId(), c.getFullName(), c.getEmail());
    }
}
