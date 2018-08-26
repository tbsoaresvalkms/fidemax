package br.com.fidemax.service.mapper;

import br.com.fidemax.domain.*;
import br.com.fidemax.service.dto.PortfolioDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Portfolio and its DTO PortfolioDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PortfolioMapper extends EntityMapper<PortfolioDTO, Portfolio> {


    @Mapping(target = "consumerTransactions", ignore = true)
    @Mapping(target = "redemptionTransactions", ignore = true)
    @Mapping(target = "customer", ignore = true)
    Portfolio toEntity(PortfolioDTO portfolioDTO);

    default Portfolio fromId(Long id) {
        if (id == null) {
            return null;
        }
        Portfolio portfolio = new Portfolio();
        portfolio.setId(id);
        return portfolio;
    }
}
