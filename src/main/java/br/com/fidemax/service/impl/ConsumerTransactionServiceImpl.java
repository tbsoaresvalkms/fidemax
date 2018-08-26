package br.com.fidemax.service.impl;

import br.com.fidemax.service.ConsumerTransactionService;
import br.com.fidemax.domain.ConsumerTransaction;
import br.com.fidemax.repository.ConsumerTransactionRepository;
import br.com.fidemax.service.dto.ConsumerTransactionDTO;
import br.com.fidemax.service.mapper.ConsumerTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing ConsumerTransaction.
 */
@Service
@Transactional
public class ConsumerTransactionServiceImpl implements ConsumerTransactionService {

    private final Logger log = LoggerFactory.getLogger(ConsumerTransactionServiceImpl.class);

    private final ConsumerTransactionRepository consumerTransactionRepository;

    private final ConsumerTransactionMapper consumerTransactionMapper;

    public ConsumerTransactionServiceImpl(ConsumerTransactionRepository consumerTransactionRepository, ConsumerTransactionMapper consumerTransactionMapper) {
        this.consumerTransactionRepository = consumerTransactionRepository;
        this.consumerTransactionMapper = consumerTransactionMapper;
    }

    /**
     * Save a consumerTransaction.
     *
     * @param consumerTransactionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ConsumerTransactionDTO save(ConsumerTransactionDTO consumerTransactionDTO) {
        log.debug("Request to save ConsumerTransaction : {}", consumerTransactionDTO);
        ConsumerTransaction consumerTransaction = consumerTransactionMapper.toEntity(consumerTransactionDTO);
        consumerTransaction = consumerTransactionRepository.save(consumerTransaction);
        return consumerTransactionMapper.toDto(consumerTransaction);
    }

    /**
     * Get all the consumerTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConsumerTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConsumerTransactions");
        return consumerTransactionRepository.findAll(pageable)
            .map(consumerTransactionMapper::toDto);
    }


    /**
     * Get one consumerTransaction by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConsumerTransactionDTO> findOne(Long id) {
        log.debug("Request to get ConsumerTransaction : {}", id);
        return consumerTransactionRepository.findById(id)
            .map(consumerTransactionMapper::toDto);
    }

    /**
     * Delete the consumerTransaction by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConsumerTransaction : {}", id);
        consumerTransactionRepository.deleteById(id);
    }
}
