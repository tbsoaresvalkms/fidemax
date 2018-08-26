package br.com.fidemax.service.mapper;

import br.com.fidemax.domain.*;
import br.com.fidemax.service.dto.ItemProductDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ItemProduct and its DTO ItemProductDTO.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ItemProductMapper extends EntityMapper<ItemProductDTO, ItemProduct> {

    @Mapping(source = "product.id", target = "productId")
    ItemProductDTO toDto(ItemProduct itemProduct);

    @Mapping(source = "productId", target = "product")
    @Mapping(target = "redemptionTransaction", ignore = true)
    ItemProduct toEntity(ItemProductDTO itemProductDTO);

    default ItemProduct fromId(Long id) {
        if (id == null) {
            return null;
        }
        ItemProduct itemProduct = new ItemProduct();
        itemProduct.setId(id);
        return itemProduct;
    }
}
