package br.com.fidemax.service.mapper;

import br.com.fidemax.domain.*;
import br.com.fidemax.service.dto.ConsumerTransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ConsumerTransaction and its DTO ConsumerTransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, PortfolioMapper.class, CompanyMapper.class})
public interface ConsumerTransactionMapper extends EntityMapper<ConsumerTransactionDTO, ConsumerTransaction> {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "portfolio.id", target = "portfolioId")
    @Mapping(source = "company.id", target = "companyId")
    ConsumerTransactionDTO toDto(ConsumerTransaction consumerTransaction);

    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "portfolioId", target = "portfolio")
    @Mapping(source = "companyId", target = "company")
    ConsumerTransaction toEntity(ConsumerTransactionDTO consumerTransactionDTO);

    default ConsumerTransaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConsumerTransaction consumerTransaction = new ConsumerTransaction();
        consumerTransaction.setId(id);
        return consumerTransaction;
    }
}
