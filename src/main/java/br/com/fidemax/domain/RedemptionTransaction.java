package br.com.fidemax.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A RedemptionTransaction.
 */
@Entity
@Table(name = "redemption_transaction")
public class RedemptionTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_date")
    private LocalDate date;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "balance", precision = 10, scale = 2, nullable = false)
    private BigDecimal balance;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private ItemProduct itemProduct;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("redemptionTransactions")
    private Portfolio portfolio;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("redemptionTransactions")
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public RedemptionTransaction date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public RedemptionTransaction balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public ItemProduct getItemProduct() {
        return itemProduct;
    }

    public RedemptionTransaction itemProduct(ItemProduct itemProduct) {
        this.itemProduct = itemProduct;
        return this;
    }

    public void setItemProduct(ItemProduct itemProduct) {
        this.itemProduct = itemProduct;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public RedemptionTransaction portfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
        return this;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Company getCompany() {
        return company;
    }

    public RedemptionTransaction company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RedemptionTransaction redemptionTransaction = (RedemptionTransaction) o;
        if (redemptionTransaction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), redemptionTransaction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RedemptionTransaction{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", balance=" + getBalance() +
            "}";
    }
}
