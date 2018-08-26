package br.com.fidemax.service;

import br.com.fidemax.service.dto.RedemptionTransactionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing RedemptionTransaction.
 */
public interface RedemptionTransactionService {

    /**
     * Save a redemptionTransaction.
     *
     * @param redemptionTransactionDTO the entity to save
     * @return the persisted entity
     */
    RedemptionTransactionDTO save(RedemptionTransactionDTO redemptionTransactionDTO);

    /**
     * Get all the redemptionTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RedemptionTransactionDTO> findAll(Pageable pageable);


    /**
     * Get the "id" redemptionTransaction.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RedemptionTransactionDTO> findOne(Long id);

    /**
     * Delete the "id" redemptionTransaction.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
