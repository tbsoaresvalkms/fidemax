package br.com.fidemax.web.rest;

import br.com.fidemax.FidemaxApp;

import br.com.fidemax.domain.RedemptionTransaction;
import br.com.fidemax.domain.ItemProduct;
import br.com.fidemax.domain.Portfolio;
import br.com.fidemax.domain.Company;
import br.com.fidemax.repository.RedemptionTransactionRepository;
import br.com.fidemax.service.RedemptionTransactionService;
import br.com.fidemax.service.dto.RedemptionTransactionDTO;
import br.com.fidemax.service.mapper.RedemptionTransactionMapper;
import br.com.fidemax.web.rest.errors.ExceptionTranslator;
import br.com.fidemax.service.dto.RedemptionTransactionCriteria;
import br.com.fidemax.service.RedemptionTransactionQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static br.com.fidemax.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RedemptionTransactionResource REST controller.
 *
 * @see RedemptionTransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FidemaxApp.class)
public class RedemptionTransactionResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(0);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(1);

    @Autowired
    private RedemptionTransactionRepository redemptionTransactionRepository;


    @Autowired
    private RedemptionTransactionMapper redemptionTransactionMapper;
    

    @Autowired
    private RedemptionTransactionService redemptionTransactionService;

    @Autowired
    private RedemptionTransactionQueryService redemptionTransactionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRedemptionTransactionMockMvc;

    private RedemptionTransaction redemptionTransaction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RedemptionTransactionResource redemptionTransactionResource = new RedemptionTransactionResource(redemptionTransactionService, redemptionTransactionQueryService);
        this.restRedemptionTransactionMockMvc = MockMvcBuilders.standaloneSetup(redemptionTransactionResource)
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
    public static RedemptionTransaction createEntity(EntityManager em) {
        RedemptionTransaction redemptionTransaction = new RedemptionTransaction()
            .date(DEFAULT_DATE)
            .balance(DEFAULT_BALANCE);
        // Add required entity
        ItemProduct itemProduct = ItemProductResourceIntTest.createEntity(em);
        em.persist(itemProduct);
        em.flush();
        redemptionTransaction.setItemProduct(itemProduct);
        // Add required entity
        Portfolio portfolio = PortfolioResourceIntTest.createEntity(em);
        em.persist(portfolio);
        em.flush();
        redemptionTransaction.setPortfolio(portfolio);
        // Add required entity
        Company company = CompanyResourceIntTest.createEntity(em);
        em.persist(company);
        em.flush();
        redemptionTransaction.setCompany(company);
        return redemptionTransaction;
    }

    @Before
    public void initTest() {
        redemptionTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createRedemptionTransaction() throws Exception {
        int databaseSizeBeforeCreate = redemptionTransactionRepository.findAll().size();

        // Create the RedemptionTransaction
        RedemptionTransactionDTO redemptionTransactionDTO = redemptionTransactionMapper.toDto(redemptionTransaction);
        restRedemptionTransactionMockMvc.perform(post("/api/redemption-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(redemptionTransactionDTO)))
            .andExpect(status().isCreated());

        // Validate the RedemptionTransaction in the database
        List<RedemptionTransaction> redemptionTransactionList = redemptionTransactionRepository.findAll();
        assertThat(redemptionTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        RedemptionTransaction testRedemptionTransaction = redemptionTransactionList.get(redemptionTransactionList.size() - 1);
        assertThat(testRedemptionTransaction.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRedemptionTransaction.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createRedemptionTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = redemptionTransactionRepository.findAll().size();

        // Create the RedemptionTransaction with an existing ID
        redemptionTransaction.setId(1L);
        RedemptionTransactionDTO redemptionTransactionDTO = redemptionTransactionMapper.toDto(redemptionTransaction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRedemptionTransactionMockMvc.perform(post("/api/redemption-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(redemptionTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RedemptionTransaction in the database
        List<RedemptionTransaction> redemptionTransactionList = redemptionTransactionRepository.findAll();
        assertThat(redemptionTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = redemptionTransactionRepository.findAll().size();
        // set the field null
        redemptionTransaction.setBalance(null);

        // Create the RedemptionTransaction, which fails.
        RedemptionTransactionDTO redemptionTransactionDTO = redemptionTransactionMapper.toDto(redemptionTransaction);

        restRedemptionTransactionMockMvc.perform(post("/api/redemption-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(redemptionTransactionDTO)))
            .andExpect(status().isBadRequest());

        List<RedemptionTransaction> redemptionTransactionList = redemptionTransactionRepository.findAll();
        assertThat(redemptionTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRedemptionTransactions() throws Exception {
        // Initialize the database
        redemptionTransactionRepository.saveAndFlush(redemptionTransaction);

        // Get all the redemptionTransactionList
        restRedemptionTransactionMockMvc.perform(get("/api/redemption-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(redemptionTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));
    }
    

    @Test
    @Transactional
    public void getRedemptionTransaction() throws Exception {
        // Initialize the database
        redemptionTransactionRepository.saveAndFlush(redemptionTransaction);

        // Get the redemptionTransaction
        restRedemptionTransactionMockMvc.perform(get("/api/redemption-transactions/{id}", redemptionTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(redemptionTransaction.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()));
    }

    @Test
    @Transactional
    public void getAllRedemptionTransactionsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        redemptionTransactionRepository.saveAndFlush(redemptionTransaction);

        // Get all the redemptionTransactionList where date equals to DEFAULT_DATE
        defaultRedemptionTransactionShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the redemptionTransactionList where date equals to UPDATED_DATE
        defaultRedemptionTransactionShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRedemptionTransactionsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        redemptionTransactionRepository.saveAndFlush(redemptionTransaction);

        // Get all the redemptionTransactionList where date in DEFAULT_DATE or UPDATED_DATE
        defaultRedemptionTransactionShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the redemptionTransactionList where date equals to UPDATED_DATE
        defaultRedemptionTransactionShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRedemptionTransactionsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        redemptionTransactionRepository.saveAndFlush(redemptionTransaction);

        // Get all the redemptionTransactionList where date is not null
        defaultRedemptionTransactionShouldBeFound("date.specified=true");

        // Get all the redemptionTransactionList where date is null
        defaultRedemptionTransactionShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllRedemptionTransactionsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        redemptionTransactionRepository.saveAndFlush(redemptionTransaction);

        // Get all the redemptionTransactionList where date greater than or equals to DEFAULT_DATE
        defaultRedemptionTransactionShouldBeFound("date.greaterOrEqualThan=" + DEFAULT_DATE);

        // Get all the redemptionTransactionList where date greater than or equals to UPDATED_DATE
        defaultRedemptionTransactionShouldNotBeFound("date.greaterOrEqualThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRedemptionTransactionsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        redemptionTransactionRepository.saveAndFlush(redemptionTransaction);

        // Get all the redemptionTransactionList where date less than or equals to DEFAULT_DATE
        defaultRedemptionTransactionShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the redemptionTransactionList where date less than or equals to UPDATED_DATE
        defaultRedemptionTransactionShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }


    @Test
    @Transactional
    public void getAllRedemptionTransactionsByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        redemptionTransactionRepository.saveAndFlush(redemptionTransaction);

        // Get all the redemptionTransactionList where balance equals to DEFAULT_BALANCE
        defaultRedemptionTransactionShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the redemptionTransactionList where balance equals to UPDATED_BALANCE
        defaultRedemptionTransactionShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllRedemptionTransactionsByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        redemptionTransactionRepository.saveAndFlush(redemptionTransaction);

        // Get all the redemptionTransactionList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultRedemptionTransactionShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the redemptionTransactionList where balance equals to UPDATED_BALANCE
        defaultRedemptionTransactionShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllRedemptionTransactionsByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        redemptionTransactionRepository.saveAndFlush(redemptionTransaction);

        // Get all the redemptionTransactionList where balance is not null
        defaultRedemptionTransactionShouldBeFound("balance.specified=true");

        // Get all the redemptionTransactionList where balance is null
        defaultRedemptionTransactionShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllRedemptionTransactionsByItemProductIsEqualToSomething() throws Exception {
        // Initialize the database
        ItemProduct itemProduct = ItemProductResourceIntTest.createEntity(em);
        em.persist(itemProduct);
        em.flush();
        redemptionTransaction.setItemProduct(itemProduct);
        redemptionTransactionRepository.saveAndFlush(redemptionTransaction);
        Long itemProductId = itemProduct.getId();

        // Get all the redemptionTransactionList where itemProduct equals to itemProductId
        defaultRedemptionTransactionShouldBeFound("itemProductId.equals=" + itemProductId);

        // Get all the redemptionTransactionList where itemProduct equals to itemProductId + 1
        defaultRedemptionTransactionShouldNotBeFound("itemProductId.equals=" + (itemProductId + 1));
    }


    @Test
    @Transactional
    public void getAllRedemptionTransactionsByPortfolioIsEqualToSomething() throws Exception {
        // Initialize the database
        Portfolio portfolio = PortfolioResourceIntTest.createEntity(em);
        em.persist(portfolio);
        em.flush();
        redemptionTransaction.setPortfolio(portfolio);
        redemptionTransactionRepository.saveAndFlush(redemptionTransaction);
        Long portfolioId = portfolio.getId();

        // Get all the redemptionTransactionList where portfolio equals to portfolioId
        defaultRedemptionTransactionShouldBeFound("portfolioId.equals=" + portfolioId);

        // Get all the redemptionTransactionList where portfolio equals to portfolioId + 1
        defaultRedemptionTransactionShouldNotBeFound("portfolioId.equals=" + (portfolioId + 1));
    }


    @Test
    @Transactional
    public void getAllRedemptionTransactionsByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        Company company = CompanyResourceIntTest.createEntity(em);
        em.persist(company);
        em.flush();
        redemptionTransaction.setCompany(company);
        redemptionTransactionRepository.saveAndFlush(redemptionTransaction);
        Long companyId = company.getId();

        // Get all the redemptionTransactionList where company equals to companyId
        defaultRedemptionTransactionShouldBeFound("companyId.equals=" + companyId);

        // Get all the redemptionTransactionList where company equals to companyId + 1
        defaultRedemptionTransactionShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRedemptionTransactionShouldBeFound(String filter) throws Exception {
        restRedemptionTransactionMockMvc.perform(get("/api/redemption-transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(redemptionTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRedemptionTransactionShouldNotBeFound(String filter) throws Exception {
        restRedemptionTransactionMockMvc.perform(get("/api/redemption-transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingRedemptionTransaction() throws Exception {
        // Get the redemptionTransaction
        restRedemptionTransactionMockMvc.perform(get("/api/redemption-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRedemptionTransaction() throws Exception {
        // Initialize the database
        redemptionTransactionRepository.saveAndFlush(redemptionTransaction);

        int databaseSizeBeforeUpdate = redemptionTransactionRepository.findAll().size();

        // Update the redemptionTransaction
        RedemptionTransaction updatedRedemptionTransaction = redemptionTransactionRepository.findById(redemptionTransaction.getId()).get();
        // Disconnect from session so that the updates on updatedRedemptionTransaction are not directly saved in db
        em.detach(updatedRedemptionTransaction);
        updatedRedemptionTransaction
            .date(UPDATED_DATE)
            .balance(UPDATED_BALANCE);
        RedemptionTransactionDTO redemptionTransactionDTO = redemptionTransactionMapper.toDto(updatedRedemptionTransaction);

        restRedemptionTransactionMockMvc.perform(put("/api/redemption-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(redemptionTransactionDTO)))
            .andExpect(status().isOk());

        // Validate the RedemptionTransaction in the database
        List<RedemptionTransaction> redemptionTransactionList = redemptionTransactionRepository.findAll();
        assertThat(redemptionTransactionList).hasSize(databaseSizeBeforeUpdate);
        RedemptionTransaction testRedemptionTransaction = redemptionTransactionList.get(redemptionTransactionList.size() - 1);
        assertThat(testRedemptionTransaction.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRedemptionTransaction.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingRedemptionTransaction() throws Exception {
        int databaseSizeBeforeUpdate = redemptionTransactionRepository.findAll().size();

        // Create the RedemptionTransaction
        RedemptionTransactionDTO redemptionTransactionDTO = redemptionTransactionMapper.toDto(redemptionTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restRedemptionTransactionMockMvc.perform(put("/api/redemption-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(redemptionTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RedemptionTransaction in the database
        List<RedemptionTransaction> redemptionTransactionList = redemptionTransactionRepository.findAll();
        assertThat(redemptionTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRedemptionTransaction() throws Exception {
        // Initialize the database
        redemptionTransactionRepository.saveAndFlush(redemptionTransaction);

        int databaseSizeBeforeDelete = redemptionTransactionRepository.findAll().size();

        // Get the redemptionTransaction
        restRedemptionTransactionMockMvc.perform(delete("/api/redemption-transactions/{id}", redemptionTransaction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RedemptionTransaction> redemptionTransactionList = redemptionTransactionRepository.findAll();
        assertThat(redemptionTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RedemptionTransaction.class);
        RedemptionTransaction redemptionTransaction1 = new RedemptionTransaction();
        redemptionTransaction1.setId(1L);
        RedemptionTransaction redemptionTransaction2 = new RedemptionTransaction();
        redemptionTransaction2.setId(redemptionTransaction1.getId());
        assertThat(redemptionTransaction1).isEqualTo(redemptionTransaction2);
        redemptionTransaction2.setId(2L);
        assertThat(redemptionTransaction1).isNotEqualTo(redemptionTransaction2);
        redemptionTransaction1.setId(null);
        assertThat(redemptionTransaction1).isNotEqualTo(redemptionTransaction2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RedemptionTransactionDTO.class);
        RedemptionTransactionDTO redemptionTransactionDTO1 = new RedemptionTransactionDTO();
        redemptionTransactionDTO1.setId(1L);
        RedemptionTransactionDTO redemptionTransactionDTO2 = new RedemptionTransactionDTO();
        assertThat(redemptionTransactionDTO1).isNotEqualTo(redemptionTransactionDTO2);
        redemptionTransactionDTO2.setId(redemptionTransactionDTO1.getId());
        assertThat(redemptionTransactionDTO1).isEqualTo(redemptionTransactionDTO2);
        redemptionTransactionDTO2.setId(2L);
        assertThat(redemptionTransactionDTO1).isNotEqualTo(redemptionTransactionDTO2);
        redemptionTransactionDTO1.setId(null);
        assertThat(redemptionTransactionDTO1).isNotEqualTo(redemptionTransactionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(redemptionTransactionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(redemptionTransactionMapper.fromId(null)).isNull();
    }
}
