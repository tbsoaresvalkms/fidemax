package br.com.fidemax.web.rest;

import br.com.fidemax.FidemaxApp;

import br.com.fidemax.domain.Portfolio;
import br.com.fidemax.domain.ConsumerTransaction;
import br.com.fidemax.domain.RedemptionTransaction;
import br.com.fidemax.domain.Customer;
import br.com.fidemax.repository.PortfolioRepository;
import br.com.fidemax.service.PortfolioService;
import br.com.fidemax.service.dto.PortfolioDTO;
import br.com.fidemax.service.mapper.PortfolioMapper;
import br.com.fidemax.web.rest.errors.ExceptionTranslator;
import br.com.fidemax.service.dto.PortfolioCriteria;
import br.com.fidemax.service.PortfolioQueryService;

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
 * Test class for the PortfolioResource REST controller.
 *
 * @see PortfolioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FidemaxApp.class)
public class PortfolioResourceIntTest {

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(0);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(1);

    @Autowired
    private PortfolioRepository portfolioRepository;


    @Autowired
    private PortfolioMapper portfolioMapper;
    

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private PortfolioQueryService portfolioQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPortfolioMockMvc;

    private Portfolio portfolio;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PortfolioResource portfolioResource = new PortfolioResource(portfolioService, portfolioQueryService);
        this.restPortfolioMockMvc = MockMvcBuilders.standaloneSetup(portfolioResource)
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
    public static Portfolio createEntity(EntityManager em) {
        Portfolio portfolio = new Portfolio()
            .balance(DEFAULT_BALANCE);
        // Add required entity
        Customer customer = CustomerResourceIntTest.createEntity(em);
        em.persist(customer);
        em.flush();
        portfolio.setCustomer(customer);
        return portfolio;
    }

    @Before
    public void initTest() {
        portfolio = createEntity(em);
    }

    @Test
    @Transactional
    public void createPortfolio() throws Exception {
        int databaseSizeBeforeCreate = portfolioRepository.findAll().size();

        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);
        restPortfolioMockMvc.perform(post("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(portfolioDTO)))
            .andExpect(status().isCreated());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeCreate + 1);
        Portfolio testPortfolio = portfolioList.get(portfolioList.size() - 1);
        assertThat(testPortfolio.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createPortfolioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = portfolioRepository.findAll().size();

        // Create the Portfolio with an existing ID
        portfolio.setId(1L);
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPortfolioMockMvc.perform(post("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(portfolioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = portfolioRepository.findAll().size();
        // set the field null
        portfolio.setBalance(null);

        // Create the Portfolio, which fails.
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        restPortfolioMockMvc.perform(post("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(portfolioDTO)))
            .andExpect(status().isBadRequest());

        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPortfolios() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        // Get all the portfolioList
        restPortfolioMockMvc.perform(get("/api/portfolios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(portfolio.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));
    }
    

    @Test
    @Transactional
    public void getPortfolio() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        // Get the portfolio
        restPortfolioMockMvc.perform(get("/api/portfolios/{id}", portfolio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(portfolio.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()));
    }

    @Test
    @Transactional
    public void getAllPortfoliosByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        // Get all the portfolioList where balance equals to DEFAULT_BALANCE
        defaultPortfolioShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the portfolioList where balance equals to UPDATED_BALANCE
        defaultPortfolioShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPortfoliosByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        // Get all the portfolioList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultPortfolioShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the portfolioList where balance equals to UPDATED_BALANCE
        defaultPortfolioShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void getAllPortfoliosByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        // Get all the portfolioList where balance is not null
        defaultPortfolioShouldBeFound("balance.specified=true");

        // Get all the portfolioList where balance is null
        defaultPortfolioShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    public void getAllPortfoliosByConsumerTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        ConsumerTransaction consumerTransaction = ConsumerTransactionResourceIntTest.createEntity(em);
        em.persist(consumerTransaction);
        em.flush();
        portfolio.addConsumerTransaction(consumerTransaction);
        portfolioRepository.saveAndFlush(portfolio);
        Long consumerTransactionId = consumerTransaction.getId();

        // Get all the portfolioList where consumerTransaction equals to consumerTransactionId
        defaultPortfolioShouldBeFound("consumerTransactionId.equals=" + consumerTransactionId);

        // Get all the portfolioList where consumerTransaction equals to consumerTransactionId + 1
        defaultPortfolioShouldNotBeFound("consumerTransactionId.equals=" + (consumerTransactionId + 1));
    }


    @Test
    @Transactional
    public void getAllPortfoliosByRedemptionTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        RedemptionTransaction redemptionTransaction = RedemptionTransactionResourceIntTest.createEntity(em);
        em.persist(redemptionTransaction);
        em.flush();
        portfolio.addRedemptionTransaction(redemptionTransaction);
        portfolioRepository.saveAndFlush(portfolio);
        Long redemptionTransactionId = redemptionTransaction.getId();

        // Get all the portfolioList where redemptionTransaction equals to redemptionTransactionId
        defaultPortfolioShouldBeFound("redemptionTransactionId.equals=" + redemptionTransactionId);

        // Get all the portfolioList where redemptionTransaction equals to redemptionTransactionId + 1
        defaultPortfolioShouldNotBeFound("redemptionTransactionId.equals=" + (redemptionTransactionId + 1));
    }


    @Test
    @Transactional
    public void getAllPortfoliosByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        Customer customer = CustomerResourceIntTest.createEntity(em);
        em.persist(customer);
        em.flush();
        portfolio.setCustomer(customer);
        customer.setPortfolio(portfolio);
        portfolioRepository.saveAndFlush(portfolio);
        Long customerId = customer.getId();

        // Get all the portfolioList where customer equals to customerId
        defaultPortfolioShouldBeFound("customerId.equals=" + customerId);

        // Get all the portfolioList where customer equals to customerId + 1
        defaultPortfolioShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPortfolioShouldBeFound(String filter) throws Exception {
        restPortfolioMockMvc.perform(get("/api/portfolios?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(portfolio.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPortfolioShouldNotBeFound(String filter) throws Exception {
        restPortfolioMockMvc.perform(get("/api/portfolios?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingPortfolio() throws Exception {
        // Get the portfolio
        restPortfolioMockMvc.perform(get("/api/portfolios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePortfolio() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        int databaseSizeBeforeUpdate = portfolioRepository.findAll().size();

        // Update the portfolio
        Portfolio updatedPortfolio = portfolioRepository.findById(portfolio.getId()).get();
        // Disconnect from session so that the updates on updatedPortfolio are not directly saved in db
        em.detach(updatedPortfolio);
        updatedPortfolio
            .balance(UPDATED_BALANCE);
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(updatedPortfolio);

        restPortfolioMockMvc.perform(put("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(portfolioDTO)))
            .andExpect(status().isOk());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeUpdate);
        Portfolio testPortfolio = portfolioList.get(portfolioList.size() - 1);
        assertThat(testPortfolio.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingPortfolio() throws Exception {
        int databaseSizeBeforeUpdate = portfolioRepository.findAll().size();

        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restPortfolioMockMvc.perform(put("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(portfolioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePortfolio() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        int databaseSizeBeforeDelete = portfolioRepository.findAll().size();

        // Get the portfolio
        restPortfolioMockMvc.perform(delete("/api/portfolios/{id}", portfolio.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Portfolio.class);
        Portfolio portfolio1 = new Portfolio();
        portfolio1.setId(1L);
        Portfolio portfolio2 = new Portfolio();
        portfolio2.setId(portfolio1.getId());
        assertThat(portfolio1).isEqualTo(portfolio2);
        portfolio2.setId(2L);
        assertThat(portfolio1).isNotEqualTo(portfolio2);
        portfolio1.setId(null);
        assertThat(portfolio1).isNotEqualTo(portfolio2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PortfolioDTO.class);
        PortfolioDTO portfolioDTO1 = new PortfolioDTO();
        portfolioDTO1.setId(1L);
        PortfolioDTO portfolioDTO2 = new PortfolioDTO();
        assertThat(portfolioDTO1).isNotEqualTo(portfolioDTO2);
        portfolioDTO2.setId(portfolioDTO1.getId());
        assertThat(portfolioDTO1).isEqualTo(portfolioDTO2);
        portfolioDTO2.setId(2L);
        assertThat(portfolioDTO1).isNotEqualTo(portfolioDTO2);
        portfolioDTO1.setId(null);
        assertThat(portfolioDTO1).isNotEqualTo(portfolioDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(portfolioMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(portfolioMapper.fromId(null)).isNull();
    }
}
