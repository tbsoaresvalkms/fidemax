package br.com.fidemax.service.impl;

import br.com.fidemax.service.RedemptionTransactionService;
import br.com.fidemax.domain.RedemptionTransaction;
import br.com.fidemax.repository.RedemptionTransactionRepository;
import br.com.fidemax.service.dto.RedemptionTransactionDTO;
import br.com.fidemax.service.mapper.RedemptionTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing RedemptionTransaction.
 */
@Service
@Transactional
public class RedemptionTransactionServiceImpl implements RedemptionTransactionService {

    private final Logger log = LoggerFactory.getLogger(RedemptionTransactionServiceImpl.class);

    private final RedemptionTransactionRepository redemptionTransactionRepository;

    private final RedemptionTransactionMapper redemptionTransactionMapper;

    public RedemptionTransactionServiceImpl(RedemptionTransactionRepository redemptionTransactionRepository, RedemptionTransactionMapper redemptionTransactionMapper) {
        this.redemptionTransactionRepository = redemptionTransactionRepository;
        this.redemptionTransactionMapper = redemptionTransactionMapper;
    }

    /**
     * Save a redemptionTransaction.
     *
     * @param redemptionTransactionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RedemptionTransactionDTO save(RedemptionTransactionDTO redemptionTransactionDTO) {
        log.debug("Request to save RedemptionTransaction : {}", redemptionTransactionDTO);
        RedemptionTransaction redemptionTransaction = redemptionTransactionMapper.toEntity(redemptionTransactionDTO);
        redemptionTransaction = redemptionTransactionRepository.save(redemptionTransaction);
        return redemptionTransactionMapper.toDto(redemptionTransaction);
    }

    /**
     * Get all the redemptionTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RedemptionTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RedemptionTransactions");
        return redemptionTransactionRepository.findAll(pageable)
            .map(redemptionTransactionMapper::toDto);
    }


    /**
     * Get one redemptionTransaction by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RedemptionTransactionDTO> findOne(Long id) {
        log.debug("Request to get RedemptionTransaction : {}", id);
        return redemptionTransactionRepository.findById(id)
            .map(redemptionTransactionMapper::toDto);
    }

    /**
     * Delete the redemptionTransaction by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RedemptionTransaction : {}", id);
        redemptionTransactionRepository.deleteById(id);
    }
}
