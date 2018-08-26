package br.com.fidemax.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import br.com.fidemax.domain.ItemProduct;
import br.com.fidemax.domain.*; // for static metamodels
import br.com.fidemax.repository.ItemProductRepository;
import br.com.fidemax.service.dto.ItemProductCriteria;

import br.com.fidemax.service.dto.ItemProductDTO;
import br.com.fidemax.service.mapper.ItemProductMapper;

/**
 * Service for executing complex queries for ItemProduct entities in the database.
 * The main input is a {@link ItemProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ItemProductDTO} or a {@link Page} of {@link ItemProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItemProductQueryService extends QueryService<ItemProduct> {

    private final Logger log = LoggerFactory.getLogger(ItemProductQueryService.class);

    private final ItemProductRepository itemProductRepository;

    private final ItemProductMapper itemProductMapper;

    public ItemProductQueryService(ItemProductRepository itemProductRepository, ItemProductMapper itemProductMapper) {
        this.itemProductRepository = itemProductRepository;
        this.itemProductMapper = itemProductMapper;
    }

    /**
     * Return a {@link List} of {@link ItemProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ItemProductDTO> findByCriteria(ItemProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ItemProduct> specification = createSpecification(criteria);
        return itemProductMapper.toDto(itemProductRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ItemProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemProductDTO> findByCriteria(ItemProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ItemProduct> specification = createSpecification(criteria);
        return itemProductRepository.findAll(specification, page)
            .map(itemProductMapper::toDto);
    }

    /**
     * Function to convert ItemProductCriteria to a {@link Specification}
     */
    private Specification<ItemProduct> createSpecification(ItemProductCriteria criteria) {
        Specification<ItemProduct> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ItemProduct_.id));
            }
            if (criteria.getCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCount(), ItemProduct_.count));
            }
            if (criteria.getUnitPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnitPrice(), ItemProduct_.unitPrice));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProductId(), ItemProduct_.product, Product_.id));
            }
            if (criteria.getRedemptionTransactionId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getRedemptionTransactionId(), ItemProduct_.redemptionTransaction, RedemptionTransaction_.id));
            }
        }
        return specification;
    }

}
