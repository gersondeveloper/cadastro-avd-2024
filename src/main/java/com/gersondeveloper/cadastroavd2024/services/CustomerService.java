package com.gersondeveloper.cadastroavd2024.services;

import com.gersondeveloper.cadastroavd2024.domain.dtos.CustomerDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.Customer;
import com.gersondeveloper.cadastroavd2024.mappers.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerMapper mapper;

    @Autowired
    public CustomerService(CustomerMapper mapper) {
        this.mapper = mapper;
    }

    public CustomerDto getCustomerDto(Customer customer) {
        return mapper.customerToCustomerDto(customer);
    }

    public Customer getCustomer(CustomerDto customerDto) {
        return mapper.customerDtoToCustomer(customerDto);
    }

    public List<CustomerDto> getAllCustomers(List<Customer> customersList){
        return mapper.customerListToCustomerDtoList(customersList);
    }

}
