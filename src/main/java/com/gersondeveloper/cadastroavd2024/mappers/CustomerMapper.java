package com.gersondeveloper.cadastroavd2024.mappers;

import com.gersondeveloper.cadastroavd2024.domain.dtos.CustomerDto;
import com.gersondeveloper.cadastroavd2024.domain.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDto customerToCustomerDto (Customer customer);
    Customer customerDtoToCustomer (CustomerDto customerDto);

    List<CustomerDto> customerListToCustomerDtoList (List<Customer> customerList);

}
