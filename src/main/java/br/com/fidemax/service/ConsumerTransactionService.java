package br.com.fidemax.service;

import br.com.fidemax.service.dto.ConsumerTransactionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ConsumerTransaction.
 */
public interface ConsumerTransactionService {

    /**
     * Save a consumerTransaction.
     *
     * @param consumerTransactionDTO the entity to save
     * @return the persisted entity
     */
    ConsumerTransactionDTO save(ConsumerTransactionDTO consumerTransactionDTO);

    /**
     * Get all the consumerTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ConsumerTransactionDTO> findAll(Pageable pageable);


    /**
     * Get the "id" consumerTransaction.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ConsumerTransactionDTO> findOne(Long id);

    /**
     * Delete the "id" consumerTransaction.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
