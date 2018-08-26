package br.com.fidemax.web.rest;

import br.com.fidemax.FidemaxApp;

import br.com.fidemax.domain.ItemProduct;
import br.com.fidemax.domain.Product;
import br.com.fidemax.domain.RedemptionTransaction;
import br.com.fidemax.repository.ItemProductRepository;
import br.com.fidemax.service.ItemProductService;
import br.com.fidemax.service.dto.ItemProductDTO;
import br.com.fidemax.service.mapper.ItemProductMapper;
import br.com.fidemax.web.rest.errors.ExceptionTranslator;
import br.com.fidemax.service.dto.ItemProductCriteria;
import br.com.fidemax.service.ItemProductQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;


import static br.com.fidemax.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ItemProductResource REST controller.
 *
 * @see ItemProductResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FidemaxApp.class)
public class ItemProductResourceIntTest {

    private static final Integer DEFAULT_COUNT = 0;
    private static final Integer UPDATED_COUNT = 1;

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(2);

    @Autowired
    private ItemProductRepository itemProductRepository;


    @Autowired
    private ItemProductMapper itemProductMapper;
    

    @Autowired
    private ItemProductService itemProductService;

    @Autowired
    private ItemProductQueryService itemProductQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restItemProductMockMvc;

    private ItemProduct itemProduct;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItemProductResource itemProductResource = new ItemProductResource(itemProductService, itemProductQueryService);
        this.restItemProductMockMvc = MockMvcBuilders.standaloneSetup(itemProductResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemProduct createEntity(EntityManager em) {
        ItemProduct itemProduct = new ItemProduct()
            .count(DEFAULT_COUNT)
            .unitPrice(DEFAULT_UNIT_PRICE);
        // Add required entity
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        itemProduct.setProduct(product);
        return itemProduct;
    }

    @Before
    public void initTest() {
        itemProduct = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemProduct() throws Exception {
        int databaseSizeBeforeCreate = itemProductRepository.findAll().size();

        // Create the ItemProduct
        ItemProductDTO itemProductDTO = itemProductMapper.toDto(itemProduct);
        restItemProductMockMvc.perform(post("/api/item-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemProductDTO)))
            .andExpect(status().isCreated());

        // Validate the ItemProduct in the database
        List<ItemProduct> itemProductList = itemProductRepository.findAll();
        assertThat(itemProductList).hasSize(databaseSizeBeforeCreate + 1);
        ItemProduct testItemProduct = itemProductList.get(itemProductList.size() - 1);
        assertThat(testItemProduct.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testItemProduct.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void createItemProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemProductRepository.findAll().size();

        // Create the ItemProduct with an existing ID
        itemProduct.setId(1L);
        ItemProductDTO itemProductDTO = itemProductMapper.toDto(itemProduct);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemProductMockMvc.perform(post("/api/item-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemProductDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemProduct in the database
        List<ItemProduct> itemProductList = itemProductRepository.findAll();
        assertThat(itemProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemProductRepository.findAll().size();
        // set the field null
        itemProduct.setCount(null);

        // Create the ItemProduct, which fails.
        ItemProductDTO itemProductDTO = itemProductMapper.toDto(itemProduct);

        restItemProductMockMvc.perform(post("/api/item-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemProductDTO)))
            .andExpect(status().isBadRequest());

        List<ItemProduct> itemProductList = itemProductRepository.findAll();
        assertThat(itemProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemProducts() throws Exception {
        // Initialize the database
        itemProductRepository.saveAndFlush(itemProduct);

        // Get all the itemProductList
        restItemProductMockMvc.perform(get("/api/item-products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())));
    }
    

    @Test
    @Transactional
    public void getItemProduct() throws Exception {
        // Initialize the database
        itemProductRepository.saveAndFlush(itemProduct);

        // Get the itemProduct
        restItemProductMockMvc.perform(get("/api/item-products/{id}", itemProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itemProduct.getId().intValue()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getAllItemProductsByCountIsEqualToSomething() throws Exception {
        // Initialize the database
        itemProductRepository.saveAndFlush(itemProduct);

        // Get all the itemProductList where count equals to DEFAULT_COUNT
        defaultItemProductShouldBeFound("count.equals=" + DEFAULT_COUNT);

        // Get all the itemProductList where count equals to UPDATED_COUNT
        defaultItemProductShouldNotBeFound("count.equals=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void getAllItemProductsByCountIsInShouldWork() throws Exception {
        // Initialize the database
        itemProductRepository.saveAndFlush(itemProduct);

        // Get all the itemProductList where count in DEFAULT_COUNT or UPDATED_COUNT
        defaultItemProductShouldBeFound("count.in=" + DEFAULT_COUNT + "," + UPDATED_COUNT);

        // Get all the itemProductList where count equals to UPDATED_COUNT
        defaultItemProductShouldNotBeFound("count.in=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void getAllItemProductsByCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemProductRepository.saveAndFlush(itemProduct);

        // Get all the itemProductList where count is not null
        defaultItemProductShouldBeFound("count.specified=true");

        // Get all the itemProductList where count is null
        defaultItemProductShouldNotBeFound("count.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemProductsByCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        itemProductRepository.saveAndFlush(itemProduct);

        // Get all the itemProductList where count greater than or equals to DEFAULT_COUNT
        defaultItemProductShouldBeFound("count.greaterOrEqualThan=" + DEFAULT_COUNT);

        // Get all the itemProductList where count greater than or equals to UPDATED_COUNT
        defaultItemProductShouldNotBeFound("count.greaterOrEqualThan=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void getAllItemProductsByCountIsLessThanSomething() throws Exception {
        // Initialize the database
        itemProductRepository.saveAndFlush(itemProduct);

        // Get all the itemProductList where count less than or equals to DEFAULT_COUNT
        defaultItemProductShouldNotBeFound("count.lessThan=" + DEFAULT_COUNT);

        // Get all the itemProductList where count less than or equals to UPDATED_COUNT
        defaultItemProductShouldBeFound("count.lessThan=" + UPDATED_COUNT);
    }


    @Test
    @Transactional
    public void getAllItemProductsByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        itemProductRepository.saveAndFlush(itemProduct);

        // Get all the itemProductList where unitPrice equals to DEFAULT_UNIT_PRICE
        defaultItemProductShouldBeFound("unitPrice.equals=" + DEFAULT_UNIT_PRICE);

        // Get all the itemProductList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultItemProductShouldNotBeFound("unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemProductsByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        itemProductRepository.saveAndFlush(itemProduct);

        // Get all the itemProductList where unitPrice in DEFAULT_UNIT_PRICE or UPDATED_UNIT_PRICE
        defaultItemProductShouldBeFound("unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE);

        // Get all the itemProductList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultItemProductShouldNotBeFound("unitPrice.in=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllItemProductsByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        itemProductRepository.saveAndFlush(itemProduct);

        // Get all the itemProductList where unitPrice is not null
        defaultItemProductShouldBeFound("unitPrice.specified=true");

        // Get all the itemProductList where unitPrice is null
        defaultItemProductShouldNotBeFound("unitPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllItemProductsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        itemProduct.setProduct(product);
        itemProductRepository.saveAndFlush(itemProduct);
        Long productId = product.getId();

        // Get all the itemProductList where product equals to productId
        defaultItemProductShouldBeFound("productId.equals=" + productId);

        // Get all the itemProductList where product equals to productId + 1
        defaultItemProductShouldNotBeFound("productId.equals=" + (productId + 1));
    }


    @Test
    @Transactional
    public void getAllItemProductsByRedemptionTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        RedemptionTransaction redemptionTransaction = RedemptionTransactionResourceIntTest.createEntity(em);
        em.persist(redemptionTransaction);
        em.flush();
        itemProduct.setRedemptionTransaction(redemptionTransaction);
        redemptionTransaction.setItemProduct(itemProduct);
        itemProductRepository.saveAndFlush(itemProduct);
        Long redemptionTransactionId = redemptionTransaction.getId();

        // Get all the itemProductList where redemptionTransaction equals to redemptionTransactionId
        defaultItemProductShouldBeFound("redemptionTransactionId.equals=" + redemptionTransactionId);

        // Get all the itemProductList where redemptionTransaction equals to redemptionTransactionId + 1
        defaultItemProductShouldNotBeFound("redemptionTransactionId.equals=" + (redemptionTransactionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultItemProductShouldBeFound(String filter) throws Exception {
        restItemProductMockMvc.perform(get("/api/item-products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultItemProductShouldNotBeFound(String filter) throws Exception {
        restItemProductMockMvc.perform(get("/api/item-products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingItemProduct() throws Exception {
        // Get the itemProduct
        restItemProductMockMvc.perform(get("/api/item-products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemProduct() throws Exception {
        // Initialize the database
        itemProductRepository.saveAndFlush(itemProduct);

        int databaseSizeBeforeUpdate = itemProductRepository.findAll().size();

        // Update the itemProduct
        ItemProduct updatedItemProduct = itemProductRepository.findById(itemProduct.getId()).get();
        // Disconnect from session so that the updates on updatedItemProduct are not directly saved in db
        em.detach(updatedItemProduct);
        updatedItemProduct
            .count(UPDATED_COUNT)
            .unitPrice(UPDATED_UNIT_PRICE);
        ItemProductDTO itemProductDTO = itemProductMapper.toDto(updatedItemProduct);

        restItemProductMockMvc.perform(put("/api/item-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemProductDTO)))
            .andExpect(status().isOk());

        // Validate the ItemProduct in the database
        List<ItemProduct> itemProductList = itemProductRepository.findAll();
        assertThat(itemProductList).hasSize(databaseSizeBeforeUpdate);
        ItemProduct testItemProduct = itemProductList.get(itemProductList.size() - 1);
        assertThat(testItemProduct.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testItemProduct.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingItemProduct() throws Exception {
        int databaseSizeBeforeUpdate = itemProductRepository.findAll().size();

        // Create the ItemProduct
        ItemProductDTO itemProductDTO = itemProductMapper.toDto(itemProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restItemProductMockMvc.perform(put("/api/item-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemProductDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemProduct in the database
        List<ItemProduct> itemProductList = itemProductRepository.findAll();
        assertThat(itemProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteItemProduct() throws Exception {
        // Initialize the database
        itemProductRepository.saveAndFlush(itemProduct);

        int databaseSizeBeforeDelete = itemProductRepository.findAll().size();

        // Get the itemProduct
        restItemProductMockMvc.perform(delete("/api/item-products/{id}", itemProduct.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ItemProduct> itemProductList = itemProductRepository.findAll();
        assertThat(itemProductList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemProduct.class);
        ItemProduct itemProduct1 = new ItemProduct();
        itemProduct1.setId(1L);
        ItemProduct itemProduct2 = new ItemProduct();
        itemProduct2.setId(itemProduct1.getId());
        assertThat(itemProduct1).isEqualTo(itemProduct2);
        itemProduct2.setId(2L);
        assertThat(itemProduct1).isNotEqualTo(itemProduct2);
        itemProduct1.setId(null);
        assertThat(itemProduct1).isNotEqualTo(itemProduct2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemProductDTO.class);
        ItemProductDTO itemProductDTO1 = new ItemProductDTO();
        itemProductDTO1.setId(1L);
        ItemProductDTO itemProductDTO2 = new ItemProductDTO();
        assertThat(itemProductDTO1).isNotEqualTo(itemProductDTO2);
        itemProductDTO2.setId(itemProductDTO1.getId());
        assertThat(itemProductDTO1).isEqualTo(itemProductDTO2);
        itemProductDTO2.setId(2L);
        assertThat(itemProductDTO1).isNotEqualTo(itemProductDTO2);
        itemProductDTO1.setId(null);
        assertThat(itemProductDTO1).isNotEqualTo(itemProductDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(itemProductMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(itemProductMapper.fromId(null)).isNull();
    }
}
