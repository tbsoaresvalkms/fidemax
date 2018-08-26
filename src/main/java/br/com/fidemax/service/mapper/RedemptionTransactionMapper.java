package br.com.fidemax.service.mapper;

import br.com.fidemax.domain.*;
import br.com.fidemax.service.dto.RedemptionTransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RedemptionTransaction and its DTO RedemptionTransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {ItemProductMapper.class, PortfolioMapper.class, CompanyMapper.class})
public interface RedemptionTransactionMapper extends EntityMapper<RedemptionTransactionDTO, RedemptionTransaction> {

    @Mapping(source = "itemProduct.id", target = "itemProductId")
    @Mapping(source = "portfolio.id", target = "portfolioId")
    @Mapping(source = "company.id", target = "companyId")
    RedemptionTransactionDTO toDto(RedemptionTransaction redemptionTransaction);

    @Mapping(source = "itemProductId", target = "itemProduct")
    @Mapping(source = "portfolioId", target = "portfolio")
    @Mapping(source = "companyId", target = "company")
    RedemptionTransaction toEntity(RedemptionTransactionDTO redemptionTransactionDTO);

    default RedemptionTransaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        RedemptionTransaction redemptionTransaction = new RedemptionTransaction();
        redemptionTransaction.setId(id);
        return redemptionTransaction;
    }
}
