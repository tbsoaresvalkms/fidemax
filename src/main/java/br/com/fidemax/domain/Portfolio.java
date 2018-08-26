package br.com.fidemax.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Portfolio.
 */
@Entity
@Table(name = "portfolio")
public class Portfolio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "balance", precision = 10, scale = 2, nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "portfolio")
    private Set<ConsumerTransaction> consumerTransactions = new HashSet<>();

    @OneToMany(mappedBy = "portfolio")
    private Set<RedemptionTransaction> redemptionTransactions = new HashSet<>();

    @OneToOne(mappedBy = "portfolio")
    @JsonIgnore
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Portfolio balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Set<ConsumerTransaction> getConsumerTransactions() {
        return consumerTransactions;
    }

    public Portfolio consumerTransactions(Set<ConsumerTransaction> consumerTransactions) {
        this.consumerTransactions = consumerTransactions;
        return this;
    }

    public Portfolio addConsumerTransaction(ConsumerTransaction consumerTransaction) {
        this.consumerTransactions.add(consumerTransaction);
        consumerTransaction.setPortfolio(this);
        return this;
    }

    public Portfolio removeConsumerTransaction(ConsumerTransaction consumerTransaction) {
        this.consumerTransactions.remove(consumerTransaction);
        consumerTransaction.setPortfolio(null);
        return this;
    }

    public void setConsumerTransactions(Set<ConsumerTransaction> consumerTransactions) {
        this.consumerTransactions = consumerTransactions;
    }

    public Set<RedemptionTransaction> getRedemptionTransactions() {
        return redemptionTransactions;
    }

    public Portfolio redemptionTransactions(Set<RedemptionTransaction> redemptionTransactions) {
        this.redemptionTransactions = redemptionTransactions;
        return this;
    }

    public Portfolio addRedemptionTransaction(RedemptionTransaction redemptionTransaction) {
        this.redemptionTransactions.add(redemptionTransaction);
        redemptionTransaction.setPortfolio(this);
        return this;
    }

    public Portfolio removeRedemptionTransaction(RedemptionTransaction redemptionTransaction) {
        this.redemptionTransactions.remove(redemptionTransaction);
        redemptionTransaction.setPortfolio(null);
        return this;
    }

    public void setRedemptionTransactions(Set<RedemptionTransaction> redemptionTransactions) {
        this.redemptionTransactions = redemptionTransactions;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Portfolio customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
        Portfolio portfolio = (Portfolio) o;
        if (portfolio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), portfolio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Portfolio{" +
            "id=" + getId() +
            ", balance=" + getBalance() +
            "}";
    }
}
