package br.com.fidemax.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @OneToMany(mappedBy = "company")
    private Set<ConsumerTransaction> consumerTransactions = new HashSet<>();

    @OneToMany(mappedBy = "company")
    private Set<RedemptionTransaction> redemptionTransactions = new HashSet<>();

    @OneToMany(mappedBy = "company")
    private Set<Employee> employees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Company name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnpj() {
        return cnpj;
    }

    public Company cnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Set<ConsumerTransaction> getConsumerTransactions() {
        return consumerTransactions;
    }

    public Company consumerTransactions(Set<ConsumerTransaction> consumerTransactions) {
        this.consumerTransactions = consumerTransactions;
        return this;
    }

    public Company addConsumerTransaction(ConsumerTransaction consumerTransaction) {
        this.consumerTransactions.add(consumerTransaction);
        consumerTransaction.setCompany(this);
        return this;
    }

    public Company removeConsumerTransaction(ConsumerTransaction consumerTransaction) {
        this.consumerTransactions.remove(consumerTransaction);
        consumerTransaction.setCompany(null);
        return this;
    }

    public void setConsumerTransactions(Set<ConsumerTransaction> consumerTransactions) {
        this.consumerTransactions = consumerTransactions;
    }

    public Set<RedemptionTransaction> getRedemptionTransactions() {
        return redemptionTransactions;
    }

    public Company redemptionTransactions(Set<RedemptionTransaction> redemptionTransactions) {
        this.redemptionTransactions = redemptionTransactions;
        return this;
    }

    public Company addRedemptionTransaction(RedemptionTransaction redemptionTransaction) {
        this.redemptionTransactions.add(redemptionTransaction);
        redemptionTransaction.setCompany(this);
        return this;
    }

    public Company removeRedemptionTransaction(RedemptionTransaction redemptionTransaction) {
        this.redemptionTransactions.remove(redemptionTransaction);
        redemptionTransaction.setCompany(null);
        return this;
    }

    public void setRedemptionTransactions(Set<RedemptionTransaction> redemptionTransactions) {
        this.redemptionTransactions = redemptionTransactions;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public Company employees(Set<Employee> employees) {
        this.employees = employees;
        return this;
    }

    public Company addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setCompany(this);
        return this;
    }

    public Company removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setCompany(null);
        return this;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
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
        Company company = (Company) o;
        if (company.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), company.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            "}";
    }
}
