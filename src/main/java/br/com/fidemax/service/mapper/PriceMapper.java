package br.com.fidemax.service.mapper;

import br.com.fidemax.domain.*;
import br.com.fidemax.service.dto.PriceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Price and its DTO PriceDTO.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface PriceMapper extends EntityMapper<PriceDTO, Price> {

    @Mapping(source = "product.id", target = "productId")
    PriceDTO toDto(Price price);

    @Mapping(source = "productId", target = "product")
    Price toEntity(PriceDTO priceDTO);

    default Price fromId(Long id) {
        if (id == null) {
            return null;
        }
        Price price = new Price();
        price.setId(id);
        return price;
    }
}
