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

import br.com.fidemax.domain.ConsumerTransaction;
import br.com.fidemax.domain.*; // for static metamodels
import br.com.fidemax.repository.ConsumerTransactionRepository;
import br.com.fidemax.service.dto.ConsumerTransactionCriteria;

import br.com.fidemax.service.dto.ConsumerTransactionDTO;
import br.com.fidemax.service.mapper.ConsumerTransactionMapper;

/**
 * Service for executing complex queries for ConsumerTransaction entities in the database.
 * The main input is a {@link ConsumerTransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ConsumerTransactionDTO} or a {@link Page} of {@link ConsumerTransactionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConsumerTransactionQueryService extends QueryService<ConsumerTransaction> {

    private final Logger log = LoggerFactory.getLogger(ConsumerTransactionQueryService.class);

    private final ConsumerTransactionRepository consumerTransactionRepository;

    private final ConsumerTransactionMapper consumerTransactionMapper;

    public ConsumerTransactionQueryService(ConsumerTransactionRepository consumerTransactionRepository, ConsumerTransactionMapper consumerTransactionMapper) {
        this.consumerTransactionRepository = consumerTransactionRepository;
        this.consumerTransactionMapper = consumerTransactionMapper;
    }

    /**
     * Return a {@link List} of {@link ConsumerTransactionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ConsumerTransactionDTO> findByCriteria(ConsumerTransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ConsumerTransaction> specification = createSpecification(criteria);
        return consumerTransactionMapper.toDto(consumerTransactionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ConsumerTransactionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConsumerTransactionDTO> findByCriteria(ConsumerTransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ConsumerTransaction> specification = createSpecification(criteria);
        return consumerTransactionRepository.findAll(specification, page)
            .map(consumerTransactionMapper::toDto);
    }

    /**
     * Function to convert ConsumerTransactionCriteria to a {@link Specification}
     */
    private Specification<ConsumerTransaction> createSpecification(ConsumerTransactionCriteria criteria) {
        Specification<ConsumerTransaction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ConsumerTransaction_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), ConsumerTransaction_.date));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), ConsumerTransaction_.balance));
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getEmployeeId(), ConsumerTransaction_.employee, Employee_.id));
            }
            if (criteria.getPortfolioId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPortfolioId(), ConsumerTransaction_.portfolio, Portfolio_.id));
            }
            if (criteria.getCompanyId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCompanyId(), ConsumerTransaction_.company, Company_.id));
            }
        }
        return specification;
    }

}
