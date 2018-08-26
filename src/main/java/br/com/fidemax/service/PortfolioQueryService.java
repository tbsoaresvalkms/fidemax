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

import br.com.fidemax.domain.Portfolio;
import br.com.fidemax.domain.*; // for static metamodels
import br.com.fidemax.repository.PortfolioRepository;
import br.com.fidemax.service.dto.PortfolioCriteria;

import br.com.fidemax.service.dto.PortfolioDTO;
import br.com.fidemax.service.mapper.PortfolioMapper;

/**
 * Service for executing complex queries for Portfolio entities in the database.
 * The main input is a {@link PortfolioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PortfolioDTO} or a {@link Page} of {@link PortfolioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PortfolioQueryService extends QueryService<Portfolio> {

    private final Logger log = LoggerFactory.getLogger(PortfolioQueryService.class);

    private final PortfolioRepository portfolioRepository;

    private final PortfolioMapper portfolioMapper;

    public PortfolioQueryService(PortfolioRepository portfolioRepository, PortfolioMapper portfolioMapper) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioMapper = portfolioMapper;
    }

    /**
     * Return a {@link List} of {@link PortfolioDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PortfolioDTO> findByCriteria(PortfolioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Portfolio> specification = createSpecification(criteria);
        return portfolioMapper.toDto(portfolioRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PortfolioDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PortfolioDTO> findByCriteria(PortfolioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Portfolio> specification = createSpecification(criteria);
        return portfolioRepository.findAll(specification, page)
            .map(portfolioMapper::toDto);
    }

    /**
     * Function to convert PortfolioCriteria to a {@link Specification}
     */
    private Specification<Portfolio> createSpecification(PortfolioCriteria criteria) {
        Specification<Portfolio> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Portfolio_.id));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), Portfolio_.balance));
            }
            if (criteria.getConsumerTransactionId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getConsumerTransactionId(), Portfolio_.consumerTransactions, ConsumerTransaction_.id));
            }
            if (criteria.getRedemptionTransactionId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getRedemptionTransactionId(), Portfolio_.redemptionTransactions, RedemptionTransaction_.id));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCustomerId(), Portfolio_.customer, Customer_.id));
            }
        }
        return specification;
    }

}
