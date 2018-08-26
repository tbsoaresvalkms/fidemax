package br.com.fidemax.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.fidemax.service.ConsumerTransactionService;
import br.com.fidemax.web.rest.errors.BadRequestAlertException;
import br.com.fidemax.web.rest.util.HeaderUtil;
import br.com.fidemax.web.rest.util.PaginationUtil;
import br.com.fidemax.service.dto.ConsumerTransactionDTO;
import br.com.fidemax.service.dto.ConsumerTransactionCriteria;
import br.com.fidemax.service.ConsumerTransactionQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ConsumerTransaction.
 */
@RestController
@RequestMapping("/api")
public class ConsumerTransactionResource {

    private final Logger log = LoggerFactory.getLogger(ConsumerTransactionResource.class);

    private static final String ENTITY_NAME = "consumerTransaction";

    private final ConsumerTransactionService consumerTransactionService;

    private final ConsumerTransactionQueryService consumerTransactionQueryService;

    public ConsumerTransactionResource(ConsumerTransactionService consumerTransactionService, ConsumerTransactionQueryService consumerTransactionQueryService) {
        this.consumerTransactionService = consumerTransactionService;
        this.consumerTransactionQueryService = consumerTransactionQueryService;
    }

    /**
     * POST  /consumer-transactions : Create a new consumerTransaction.
     *
     * @param consumerTransactionDTO the consumerTransactionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new consumerTransactionDTO, or with status 400 (Bad Request) if the consumerTransaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/consumer-transactions")
    @Timed
    public ResponseEntity<ConsumerTransactionDTO> createConsumerTransaction(@Valid @RequestBody ConsumerTransactionDTO consumerTransactionDTO) throws URISyntaxException {
        log.debug("REST request to save ConsumerTransaction : {}", consumerTransactionDTO);
        if (consumerTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new consumerTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConsumerTransactionDTO result = consumerTransactionService.save(consumerTransactionDTO);
        return ResponseEntity.created(new URI("/api/consumer-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /consumer-transactions : Updates an existing consumerTransaction.
     *
     * @param consumerTransactionDTO the consumerTransactionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated consumerTransactionDTO,
     * or with status 400 (Bad Request) if the consumerTransactionDTO is not valid,
     * or with status 500 (Internal Server Error) if the consumerTransactionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/consumer-transactions")
    @Timed
    public ResponseEntity<ConsumerTransactionDTO> updateConsumerTransaction(@Valid @RequestBody ConsumerTransactionDTO consumerTransactionDTO) throws URISyntaxException {
        log.debug("REST request to update ConsumerTransaction : {}", consumerTransactionDTO);
        if (consumerTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConsumerTransactionDTO result = consumerTransactionService.save(consumerTransactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, consumerTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /consumer-transactions : get all the consumerTransactions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of consumerTransactions in body
     */
    @GetMapping("/consumer-transactions")
    @Timed
    public ResponseEntity<List<ConsumerTransactionDTO>> getAllConsumerTransactions(ConsumerTransactionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ConsumerTransactions by criteria: {}", criteria);
        Page<ConsumerTransactionDTO> page = consumerTransactionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/consumer-transactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /consumer-transactions/:id : get the "id" consumerTransaction.
     *
     * @param id the id of the consumerTransactionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the consumerTransactionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/consumer-transactions/{id}")
    @Timed
    public ResponseEntity<ConsumerTransactionDTO> getConsumerTransaction(@PathVariable Long id) {
        log.debug("REST request to get ConsumerTransaction : {}", id);
        Optional<ConsumerTransactionDTO> consumerTransactionDTO = consumerTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consumerTransactionDTO);
    }

    /**
     * DELETE  /consumer-transactions/:id : delete the "id" consumerTransaction.
     *
     * @param id the id of the consumerTransactionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/consumer-transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteConsumerTransaction(@PathVariable Long id) {
        log.debug("REST request to delete ConsumerTransaction : {}", id);
        consumerTransactionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
