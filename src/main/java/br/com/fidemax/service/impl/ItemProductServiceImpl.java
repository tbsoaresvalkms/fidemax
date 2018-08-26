package br.com.fidemax.service.impl;

import br.com.fidemax.service.ItemProductService;
import br.com.fidemax.domain.ItemProduct;
import br.com.fidemax.repository.ItemProductRepository;
import br.com.fidemax.service.dto.ItemProductDTO;
import br.com.fidemax.service.mapper.ItemProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
/**
 * Service Implementation for managing ItemProduct.
 */
@Service
@Transactional
public class ItemProductServiceImpl implements ItemProductService {

    private final Logger log = LoggerFactory.getLogger(ItemProductServiceImpl.class);

    private final ItemProductRepository itemProductRepository;

    private final ItemProductMapper itemProductMapper;

    public ItemProductServiceImpl(ItemProductRepository itemProductRepository, ItemProductMapper itemProductMapper) {
        this.itemProductRepository = itemProductRepository;
        this.itemProductMapper = itemProductMapper;
    }

    /**
     * Save a itemProduct.
     *
     * @param itemProductDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ItemProductDTO save(ItemProductDTO itemProductDTO) {
        log.debug("Request to save ItemProduct : {}", itemProductDTO);
        ItemProduct itemProduct = itemProductMapper.toEntity(itemProductDTO);
        itemProduct = itemProductRepository.save(itemProduct);
        return itemProductMapper.toDto(itemProduct);
    }

    /**
     * Get all the itemProducts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ItemProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ItemProducts");
        return itemProductRepository.findAll(pageable)
            .map(itemProductMapper::toDto);
    }



    /**
     *  get all the itemProducts where RedemptionTransaction is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ItemProductDTO> findAllWhereRedemptionTransactionIsNull() {
        log.debug("Request to get all itemProducts where RedemptionTransaction is null");
        return StreamSupport
            .stream(itemProductRepository.findAll().spliterator(), false)
            .filter(itemProduct -> itemProduct.getRedemptionTransaction() == null)
            .map(itemProductMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one itemProduct by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ItemProductDTO> findOne(Long id) {
        log.debug("Request to get ItemProduct : {}", id);
        return itemProductRepository.findById(id)
            .map(itemProductMapper::toDto);
    }

    /**
     * Delete the itemProduct by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ItemProduct : {}", id);
        itemProductRepository.deleteById(id);
    }
}
