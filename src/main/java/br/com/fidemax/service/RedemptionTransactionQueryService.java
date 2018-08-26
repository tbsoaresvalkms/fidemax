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

import br.com.fidemax.domain.RedemptionTransaction;
import br.com.fidemax.domain.*; // for static metamodels
import br.com.fidemax.repository.RedemptionTransactionRepository;
import br.com.fidemax.service.dto.RedemptionTransactionCriteria;

import br.com.fidemax.service.dto.RedemptionTransactionDTO;
import br.com.fidemax.service.mapper.RedemptionTransactionMapper;

/**
 * Service for executing complex queries for RedemptionTransaction entities in the database.
 * The main input is a {@link RedemptionTransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RedemptionTransactionDTO} or a {@link Page} of {@link RedemptionTransactionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RedemptionTransactionQueryService extends QueryService<RedemptionTransaction> {

    private final Logger log = LoggerFactory.getLogger(RedemptionTransactionQueryService.class);

    private final RedemptionTransactionRepository redemptionTransactionRepository;

    private final RedemptionTransactionMapper redemptionTransactionMapper;

    public RedemptionTransactionQueryService(RedemptionTransactionRepository redemptionTransactionRepository, RedemptionTransactionMapper redemptionTransactionMapper) {
        this.redemptionTransactionRepository = redemptionTransactionRepository;
        this.redemptionTransactionMapper = redemptionTransactionMapper;
    }

    /**
     * Return a {@link List} of {@link RedemptionTransactionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RedemptionTransactionDTO> findByCriteria(RedemptionTransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RedemptionTransaction> specification = createSpecification(criteria);
        return redemptionTransactionMapper.toDto(redemptionTransactionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RedemptionTransactionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RedemptionTransactionDTO> findByCriteria(RedemptionTransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RedemptionTransaction> specification = createSpecification(criteria);
        return redemptionTransactionRepository.findAll(specification, page)
            .map(redemptionTransactionMapper::toDto);
    }

    /**
     * Function to convert RedemptionTransactionCriteria to a {@link Specification}
     */
    private Specification<RedemptionTransaction> createSpecification(RedemptionTransactionCriteria criteria) {
        Specification<RedemptionTransaction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), RedemptionTransaction_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), RedemptionTransaction_.date));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), RedemptionTransaction_.balance));
            }
            if (criteria.getItemProductId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getItemProductId(), RedemptionTransaction_.itemProduct, ItemProduct_.id));
            }
            if (criteria.getPortfolioId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPortfolioId(), RedemptionTransaction_.portfolio, Portfolio_.id));
            }
            if (criteria.getCompanyId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCompanyId(), RedemptionTransaction_.company, Company_.id));
            }
        }
        return specification;
    }

}
