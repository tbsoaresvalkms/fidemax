package br.com.fidemax.web.rest;

import br.com.fidemax.FidemaxApp;

import br.com.fidemax.domain.ConsumerTransaction;
import br.com.fidemax.domain.Employee;
import br.com.fidemax.domain.Portfolio;
import br.com.fidemax.domain.Company;
import br.com.fidemax.repository.ConsumerTransactionRepository;
import br.com.fidemax.service.ConsumerTransactionService;
import br.com.fidemax.service.dto.ConsumerTransactionDTO;
import br.com.fidemax.service.mapper.ConsumerTransactionMapper;
import br.com.fidemax.web.rest.errors.ExceptionTranslator;
import br.com.fidemax.service.dto.ConsumerTransactionCriteria;
import br.com.fidemax.service.ConsumerTransactionQueryService;

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
 * Test class for the ConsumerTransactionResource REST controller.
 *
 * @see ConsumerTransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FidemaxApp.class)
public class ConsumerTransactionResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(0);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(1);

    @Autowired
    private ConsumerTransactionRepository consumerTransactionRepository;


    @Autowired
    private ConsumerTransactionMapper consumerTransactionMapper;
    

    @Autowired
    private ConsumerTransactionService consumerTransactionService;

    @Autowired
    private ConsumerTransactionQueryService consumerTransactionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restConsumerTransactionMockMvc;

    private ConsumerTransaction consumerTransaction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConsumerTransactionResource consumerTransactionResource = new ConsumerTransactionResource(consumerTransactionService, consumerTransactionQueryService);
        this.restConsumerTransactionMockMvc = MockMvcBuilders.standaloneSetup(consumerTransactionResource)
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
    public static ConsumerTransaction createEntity(EntityManager em) {
        ConsumerTransaction consumerTransaction = new ConsumerTransaction()
            .date(DEFAULT_DATE)
            .balance(DEFAULT_BALANCE);
        // Add required entity
        Employee employee = EmployeeResourceIntTest.createEntity(em);
        em.persist(employee);
        em.flush();
        consumerTransaction.setEmployee(employee);
        // Add required entity
        Portfolio portfolio = PortfolioResourceIntTest.createEntity(em);
        em.persist(portfolio);
        em.flush();
        consumerTransaction.setPortfolio(portfolio);
        // Add required entity
        Company company = CompanyResourceIntTest.createEntity(em);
        em.persist(company);
        em.flush();
        consumerTransaction.setCompany(company);
        return consumerTransaction;
    }

    @Before
    public void initTest() {
        consumerTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createConsumerTransaction() throws Exception {
        int databaseSizeBeforeCreate = consumerTransactionRepository.findAll().size();

        // Create the ConsumerTransaction
        ConsumerTransactionDTO consumerTransactionDTO = consumerTransactionMapper.toDto(consumerTransaction);
        restConsumerTransactionMockMvc.perform(post("/api/consumer-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consumerTransactionDTO)))
            .andExpect(status().isCreated());

        // Validate the ConsumerTransaction in the database
        List<ConsumerTransaction> consumerTransactionList = consumerTransactionRepository.findAll();
        assertThat(consumerTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        ConsumerTransaction testConsumerTransaction = consumerTransactionList.get(consumerTransactionList.size() - 1);
        assertThat(testConsumerTransaction.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testConsumerTransaction.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createConsumerTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = consumerTransactionRepository.findAll().size();

        // Create the ConsumerTransaction with an existing ID
        consumerTransaction.setId(1L);
        ConsumerTransactionDTO consumerTransactionDTO = consumerTransactionMapper.toDto(consumerTransaction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsumerTransactionMockMvc.perform(post("/api/consumer-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consumerTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConsumerTransaction in the database
        List<ConsumerTransaction> consumerTransactionList = consumerTransactionRepository.findAll();
        assertThat(consumerTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = consumerTransactionRepository.findAll().size();
        // set the field null
        consumerTransaction.setBalance(null);

        // Create the ConsumerTransaction, which fails.
        ConsumerTransactionDTO consumerTransactionDTO = consumerTransactionMapper.toDto(consumerTransaction);

        restConsumerTransactionMockMvc.perform(post("/api/consumer-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consumerTransactionDTO)))
            .andExpect(status().isBadRequest());

        List<ConsumerTransaction> consumerTransactionList = consumerTransactionRepository.findAll();
        assertThat(consumerTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConsumerTransactions() throws Exception {
        // Initialize the database
        consumerTransactionRepository.saveAndFlush(consumerTransaction);

        // Get all the consumerTransactionList
        restConsumerTransactionMockMvc.perform(get("/api/consumer-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consumerTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));
    }
    

    @Test
    @Transactional
    public void getConsumerTransaction() throws Exception {
        // Initialize the database
        consumerTransactionRepository.saveAndFlush(consumerTransaction);

        // Get the consumerTransaction
        restConsumerTransactionMockMvc.perform(get("/api/consumer-transactions/{id}", consumerTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(consumerTransaction.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()));
    }

    @Test
    @Transactional
    public void getAllConsumerTransactionsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        consumerTransactionRepository.saveAndFlush(consumerTransaction);

        // Get all the consumerTransactionList where date equals to DEFAULT_DATE
        defaultConsumerTransactionShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the consumerTransactionList where date equals to UPDATED_DATE
        defaultConsumerTransactionShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllConsumerTransactionsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        consumerTransactionRepository.saveAndFlush(consumerTransaction);

        // Get all the consumerTransactionList where date in DEFAULT_DATE or UPDATED_DATE
        defaultConsumerTransactionShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the consumerTransactionList where date equals to UPDATED_DATE
        defaultConsumerTransactionShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllConsumerTransactionsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        consumerTransactionRepository.saveAndFlush(consumerTransaction);

        // Get all the consumerTransactionList where date is not null
        defaultConsumerTransactionShouldBeFound("date.specified=true");

        // Get all the consumerTransactionList where date is null
        defaultConsumerTransactionShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllConsumerTransactionsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consumerTransactionRepository.saveAndFlush(consumerTransaction);

        // Get all the consumerTransactionList where date greater than or equals to DEFAULT_DATE
        defaultConsumerTransactionShouldBeFound("date.greaterOrEqualThan=" + DEFAULT_DATE);

        // Get all the consumerTransactionList where date greater than or equals to UPDATED_DATE
        defaultConsumerTransactionShouldNotBeFound("date.greaterOrEqualThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllConsumerTransactionsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        consumerTransactionRepository.saveAndFlush(consumerTransaction);

        // Get all the consumerTransactionList where date less than or equals to DEFAULT_DATE
        defaultConsumerTransactionShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the consumerTransactionList where date less than or equals to UPDATED_DATE
        defaultConsumerTransactionShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }


    @Test
    @Transactional
    public void getAllConsumerTransactionsByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        consumerTransactionRepository.saveAndFlush(consumerTransaction);

        // Get all the consumerTransactionList where balance equals to DEFAULT_BALANCE
        defaultConsumerTransactionShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the consumerTransactionList where balance equals to UPDATED_BALANCE
        defaultConsumerTransactionShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllConsumerTransactionsByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        consumerTransactionRepository.saveAndFlush(consumerTransaction);

        // Get all the consumerTransactionList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultConsumerTransactionShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the consumerTransactionList where balance equals to UPDATED_BALANCE
        defaultConsumerTransactionShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllConsumerTransactionsByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        consumerTransactionRepository.saveAndFlush(consumerTransaction);

        // Get all the consumerTransactionList where balance is not null
        defaultConsumerTransactionShouldBeFound("balance.specified=true");

        // Get all the consumerTransactionList where balance is null
        defaultConsumerTransactionShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllConsumerTransactionsByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        Employee employee = EmployeeResourceIntTest.createEntity(em);
        em.persist(employee);
        em.flush();
        consumerTransaction.setEmployee(employee);
        consumerTransactionRepository.saveAndFlush(consumerTransaction);
        Long employeeId = employee.getId();

        // Get all the consumerTransactionList where employee equals to employeeId
        defaultConsumerTransactionShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the consumerTransactionList where employee equals to employeeId + 1
        defaultConsumerTransactionShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }


    @Test
    @Transactional
    public void getAllConsumerTransactionsByPortfolioIsEqualToSomething() throws Exception {
        // Initialize the database
        Portfolio portfolio = PortfolioResourceIntTest.createEntity(em);
        em.persist(portfolio);
        em.flush();
        consumerTransaction.setPortfolio(portfolio);
        consumerTransactionRepository.saveAndFlush(consumerTransaction);
        Long portfolioId = portfolio.getId();

        // Get all the consumerTransactionList where portfolio equals to portfolioId
        defaultConsumerTransactionShouldBeFound("portfolioId.equals=" + portfolioId);

        // Get all the consumerTransactionList where portfolio equals to portfolioId + 1
        defaultConsumerTransactionShouldNotBeFound("portfolioId.equals=" + (portfolioId + 1));
    }


    @Test
    @Transactional
    public void getAllConsumerTransactionsByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        Company company = CompanyResourceIntTest.createEntity(em);
        em.persist(company);
        em.flush();
        consumerTransaction.setCompany(company);
        consumerTransactionRepository.saveAndFlush(consumerTransaction);
        Long companyId = company.getId();

        // Get all the consumerTransactionList where company equals to companyId
        defaultConsumerTransactionShouldBeFound("companyId.equals=" + companyId);

        // Get all the consumerTransactionList where company equals to companyId + 1
        defaultConsumerTransactionShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultConsumerTransactionShouldBeFound(String filter) throws Exception {
        restConsumerTransactionMockMvc.perform(get("/api/consumer-transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consumerTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultConsumerTransactionShouldNotBeFound(String filter) throws Exception {
        restConsumerTransactionMockMvc.perform(get("/api/consumer-transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingConsumerTransaction() throws Exception {
        // Get the consumerTransaction
        restConsumerTransactionMockMvc.perform(get("/api/consumer-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConsumerTransaction() throws Exception {
        // Initialize the database
        consumerTransactionRepository.saveAndFlush(consumerTransaction);

        int databaseSizeBeforeUpdate = consumerTransactionRepository.findAll().size();

        // Update the consumerTransaction
        ConsumerTransaction updatedConsumerTransaction = consumerTransactionRepository.findById(consumerTransaction.getId()).get();
        // Disconnect from session so that the updates on updatedConsumerTransaction are not directly saved in db
        em.detach(updatedConsumerTransaction);
        updatedConsumerTransaction
            .date(UPDATED_DATE)
            .balance(UPDATED_BALANCE);
        ConsumerTransactionDTO consumerTransactionDTO = consumerTransactionMapper.toDto(updatedConsumerTransaction);

        restConsumerTransactionMockMvc.perform(put("/api/consumer-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consumerTransactionDTO)))
            .andExpect(status().isOk());

        // Validate the ConsumerTransaction in the database
        List<ConsumerTransaction> consumerTransactionList = consumerTransactionRepository.findAll();
        assertThat(consumerTransactionList).hasSize(databaseSizeBeforeUpdate);
        ConsumerTransaction testConsumerTransaction = consumerTransactionList.get(consumerTransactionList.size() - 1);
        assertThat(testConsumerTransaction.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testConsumerTransaction.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingConsumerTransaction() throws Exception {
        int databaseSizeBeforeUpdate = consumerTransactionRepository.findAll().size();

        // Create the ConsumerTransaction
        ConsumerTransactionDTO consumerTransactionDTO = consumerTransactionMapper.toDto(consumerTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restConsumerTransactionMockMvc.perform(put("/api/consumer-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consumerTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConsumerTransaction in the database
        List<ConsumerTransaction> consumerTransactionList = consumerTransactionRepository.findAll();
        assertThat(consumerTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConsumerTransaction() throws Exception {
        // Initialize the database
        consumerTransactionRepository.saveAndFlush(consumerTransaction);

        int databaseSizeBeforeDelete = consumerTransactionRepository.findAll().size();

        // Get the consumerTransaction
        restConsumerTransactionMockMvc.perform(delete("/api/consumer-transactions/{id}", consumerTransaction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ConsumerTransaction> consumerTransactionList = consumerTransactionRepository.findAll();
        assertThat(consumerTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsumerTransaction.class);
        ConsumerTransaction consumerTransaction1 = new ConsumerTransaction();
        consumerTransaction1.setId(1L);
        ConsumerTransaction consumerTransaction2 = new ConsumerTransaction();
        consumerTransaction2.setId(consumerTransaction1.getId());
        assertThat(consumerTransaction1).isEqualTo(consumerTransaction2);
        consumerTransaction2.setId(2L);
        assertThat(consumerTransaction1).isNotEqualTo(consumerTransaction2);
        consumerTransaction1.setId(null);
        assertThat(consumerTransaction1).isNotEqualTo(consumerTransaction2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsumerTransactionDTO.class);
        ConsumerTransactionDTO consumerTransactionDTO1 = new ConsumerTransactionDTO();
        consumerTransactionDTO1.setId(1L);
        ConsumerTransactionDTO consumerTransactionDTO2 = new ConsumerTransactionDTO();
        assertThat(consumerTransactionDTO1).isNotEqualTo(consumerTransactionDTO2);
        consumerTransactionDTO2.setId(consumerTransactionDTO1.getId());
        assertThat(consumerTransactionDTO1).isEqualTo(consumerTransactionDTO2);
        consumerTransactionDTO2.setId(2L);
        assertThat(consumerTransactionDTO1).isNotEqualTo(consumerTransactionDTO2);
        consumerTransactionDTO1.setId(null);
        assertThat(consumerTransactionDTO1).isNotEqualTo(consumerTransactionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(consumerTransactionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(consumerTransactionMapper.fromId(null)).isNull();
    }
}
