package co.com.ancas.playground.sec05.mapper;


import co.com.ancas.playground.sec05.dto.CustomerDTO;
import co.com.ancas.playground.sec05.entity.CustomerEntity;

public class EntityDTOMapper {
    public static CustomerEntity toEntity(CustomerDTO customerDTO) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customerDTO.id());
        customerEntity.setName(customerDTO.name());
        customerEntity.setEmail(customerDTO.email());
        return customerEntity;
    }

    public static CustomerDTO toDTO(CustomerEntity customerEntity) {
        return new CustomerDTO(customerEntity.getId(), customerEntity.getName(), customerEntity.getEmail());
    }
}
