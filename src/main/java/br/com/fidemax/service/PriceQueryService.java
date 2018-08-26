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

import br.com.fidemax.domain.Price;
import br.com.fidemax.domain.*; // for static metamodels
import br.com.fidemax.repository.PriceRepository;
import br.com.fidemax.service.dto.PriceCriteria;

import br.com.fidemax.service.dto.PriceDTO;
import br.com.fidemax.service.mapper.PriceMapper;

/**
 * Service for executing complex queries for Price entities in the database.
 * The main input is a {@link PriceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PriceDTO} or a {@link Page} of {@link PriceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PriceQueryService extends QueryService<Price> {

    private final Logger log = LoggerFactory.getLogger(PriceQueryService.class);

    private final PriceRepository priceRepository;

    private final PriceMapper priceMapper;

    public PriceQueryService(PriceRepository priceRepository, PriceMapper priceMapper) {
        this.priceRepository = priceRepository;
        this.priceMapper = priceMapper;
    }

    /**
     * Return a {@link List} of {@link PriceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PriceDTO> findByCriteria(PriceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Price> specification = createSpecification(criteria);
        return priceMapper.toDto(priceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PriceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PriceDTO> findByCriteria(PriceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Price> specification = createSpecification(criteria);
        return priceRepository.findAll(specification, page)
            .map(priceMapper::toDto);
    }

    /**
     * Function to convert PriceCriteria to a {@link Specification}
     */
    private Specification<Price> createSpecification(PriceCriteria criteria) {
        Specification<Price> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Price_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Price_.date));
            }
            if (criteria.getUnitPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnitPrice(), Price_.unitPrice));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProductId(), Price_.product, Product_.id));
            }
        }
        return specification;
    }

}
