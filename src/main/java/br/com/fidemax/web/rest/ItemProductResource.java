package br.com.fidemax.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.fidemax.service.ItemProductService;
import br.com.fidemax.web.rest.errors.BadRequestAlertException;
import br.com.fidemax.web.rest.util.HeaderUtil;
import br.com.fidemax.web.rest.util.PaginationUtil;
import br.com.fidemax.service.dto.ItemProductDTO;
import br.com.fidemax.service.dto.ItemProductCriteria;
import br.com.fidemax.service.ItemProductQueryService;
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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing ItemProduct.
 */
@RestController
@RequestMapping("/api")
public class ItemProductResource {

    private final Logger log = LoggerFactory.getLogger(ItemProductResource.class);

    private static final String ENTITY_NAME = "itemProduct";

    private final ItemProductService itemProductService;

    private final ItemProductQueryService itemProductQueryService;

    public ItemProductResource(ItemProductService itemProductService, ItemProductQueryService itemProductQueryService) {
        this.itemProductService = itemProductService;
        this.itemProductQueryService = itemProductQueryService;
    }

    /**
     * POST  /item-products : Create a new itemProduct.
     *
     * @param itemProductDTO the itemProductDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new itemProductDTO, or with status 400 (Bad Request) if the itemProduct has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/item-products")
    @Timed
    public ResponseEntity<ItemProductDTO> createItemProduct(@Valid @RequestBody ItemProductDTO itemProductDTO) throws URISyntaxException {
        log.debug("REST request to save ItemProduct : {}", itemProductDTO);
        if (itemProductDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemProductDTO result = itemProductService.save(itemProductDTO);
        return ResponseEntity.created(new URI("/api/item-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /item-products : Updates an existing itemProduct.
     *
     * @param itemProductDTO the itemProductDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated itemProductDTO,
     * or with status 400 (Bad Request) if the itemProductDTO is not valid,
     * or with status 500 (Internal Server Error) if the itemProductDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/item-products")
    @Timed
    public ResponseEntity<ItemProductDTO> updateItemProduct(@Valid @RequestBody ItemProductDTO itemProductDTO) throws URISyntaxException {
        log.debug("REST request to update ItemProduct : {}", itemProductDTO);
        if (itemProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ItemProductDTO result = itemProductService.save(itemProductDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, itemProductDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /item-products : get all the itemProducts.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of itemProducts in body
     */
    @GetMapping("/item-products")
    @Timed
    public ResponseEntity<List<ItemProductDTO>> getAllItemProducts(ItemProductCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ItemProducts by criteria: {}", criteria);
        Page<ItemProductDTO> page = itemProductQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/item-products");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /item-products/:id : get the "id" itemProduct.
     *
     * @param id the id of the itemProductDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the itemProductDTO, or with status 404 (Not Found)
     */
    @GetMapping("/item-products/{id}")
    @Timed
    public ResponseEntity<ItemProductDTO> getItemProduct(@PathVariable Long id) {
        log.debug("REST request to get ItemProduct : {}", id);
        Optional<ItemProductDTO> itemProductDTO = itemProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemProductDTO);
    }

    /**
     * DELETE  /item-products/:id : delete the "id" itemProduct.
     *
     * @param id the id of the itemProductDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/item-products/{id}")
    @Timed
    public ResponseEntity<Void> deleteItemProduct(@PathVariable Long id) {
        log.debug("REST request to delete ItemProduct : {}", id);
        itemProductService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
