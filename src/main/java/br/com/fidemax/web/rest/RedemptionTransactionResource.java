package br.com.fidemax.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.fidemax.service.RedemptionTransactionService;
import br.com.fidemax.web.rest.errors.BadRequestAlertException;
import br.com.fidemax.web.rest.util.HeaderUtil;
import br.com.fidemax.web.rest.util.PaginationUtil;
import br.com.fidemax.service.dto.RedemptionTransactionDTO;
import br.com.fidemax.service.dto.RedemptionTransactionCriteria;
import br.com.fidemax.service.RedemptionTransactionQueryService;
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
 * REST controller for managing RedemptionTransaction.
 */
@RestController
@RequestMapping("/api")
public class RedemptionTransactionResource {

    private final Logger log = LoggerFactory.getLogger(RedemptionTransactionResource.class);

    private static final String ENTITY_NAME = "redemptionTransaction";

    private final RedemptionTransactionService redemptionTransactionService;

    private final RedemptionTransactionQueryService redemptionTransactionQueryService;

    public RedemptionTransactionResource(RedemptionTransactionService redemptionTransactionService, RedemptionTransactionQueryService redemptionTransactionQueryService) {
        this.redemptionTransactionService = redemptionTransactionService;
        this.redemptionTransactionQueryService = redemptionTransactionQueryService;
    }

    /**
     * POST  /redemption-transactions : Create a new redemptionTransaction.
     *
     * @param redemptionTransactionDTO the redemptionTransactionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new redemptionTransactionDTO, or with status 400 (Bad Request) if the redemptionTransaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/redemption-transactions")
    @Timed
    public ResponseEntity<RedemptionTransactionDTO> createRedemptionTransaction(@Valid @RequestBody RedemptionTransactionDTO redemptionTransactionDTO) throws URISyntaxException {
        log.debug("REST request to save RedemptionTransaction : {}", redemptionTransactionDTO);
        if (redemptionTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new redemptionTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RedemptionTransactionDTO result = redemptionTransactionService.save(redemptionTransactionDTO);
        return ResponseEntity.created(new URI("/api/redemption-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /redemption-transactions : Updates an existing redemptionTransaction.
     *
     * @param redemptionTransactionDTO the redemptionTransactionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated redemptionTransactionDTO,
     * or with status 400 (Bad Request) if the redemptionTransactionDTO is not valid,
     * or with status 500 (Internal Server Error) if the redemptionTransactionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/redemption-transactions")
    @Timed
    public ResponseEntity<RedemptionTransactionDTO> updateRedemptionTransaction(@Valid @RequestBody RedemptionTransactionDTO redemptionTransactionDTO) throws URISyntaxException {
        log.debug("REST request to update RedemptionTransaction : {}", redemptionTransactionDTO);
        if (redemptionTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RedemptionTransactionDTO result = redemptionTransactionService.save(redemptionTransactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, redemptionTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /redemption-transactions : get all the redemptionTransactions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of redemptionTransactions in body
     */
    @GetMapping("/redemption-transactions")
    @Timed
    public ResponseEntity<List<RedemptionTransactionDTO>> getAllRedemptionTransactions(RedemptionTransactionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RedemptionTransactions by criteria: {}", criteria);
        Page<RedemptionTransactionDTO> page = redemptionTransactionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/redemption-transactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /redemption-transactions/:id : get the "id" redemptionTransaction.
     *
     * @param id the id of the redemptionTransactionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the redemptionTransactionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/redemption-transactions/{id}")
    @Timed
    public ResponseEntity<RedemptionTransactionDTO> getRedemptionTransaction(@PathVariable Long id) {
        log.debug("REST request to get RedemptionTransaction : {}", id);
        Optional<RedemptionTransactionDTO> redemptionTransactionDTO = redemptionTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(redemptionTransactionDTO);
    }

    /**
     * DELETE  /redemption-transactions/:id : delete the "id" redemptionTransaction.
     *
     * @param id the id of the redemptionTransactionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/redemption-transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteRedemptionTransaction(@PathVariable Long id) {
        log.debug("REST request to delete RedemptionTransaction : {}", id);
        redemptionTransactionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
