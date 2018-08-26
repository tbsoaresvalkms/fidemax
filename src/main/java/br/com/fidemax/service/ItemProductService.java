package br.com.fidemax.service;

import br.com.fidemax.service.dto.ItemProductDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing ItemProduct.
 */
public interface ItemProductService {

    /**
     * Save a itemProduct.
     *
     * @param itemProductDTO the entity to save
     * @return the persisted entity
     */
    ItemProductDTO save(ItemProductDTO itemProductDTO);

    /**
     * Get all the itemProducts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ItemProductDTO> findAll(Pageable pageable);
    /**
     * Get all the ItemProductDTO where RedemptionTransaction is null.
     *
     * @return the list of entities
     */
    List<ItemProductDTO> findAllWhereRedemptionTransactionIsNull();


    /**
     * Get the "id" itemProduct.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ItemProductDTO> findOne(Long id);

    /**
     * Delete the "id" itemProduct.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
