package br.com.fidemax.service;

import br.com.fidemax.service.dto.PortfolioDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Portfolio.
 */
public interface PortfolioService {

    /**
     * Save a portfolio.
     *
     * @param portfolioDTO the entity to save
     * @return the persisted entity
     */
    PortfolioDTO save(PortfolioDTO portfolioDTO);

    /**
     * Get all the portfolios.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PortfolioDTO> findAll(Pageable pageable);
    /**
     * Get all the PortfolioDTO where Customer is null.
     *
     * @return the list of entities
     */
    List<PortfolioDTO> findAllWhereCustomerIsNull();


    /**
     * Get the "id" portfolio.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PortfolioDTO> findOne(Long id);

    /**
     * Delete the "id" portfolio.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
