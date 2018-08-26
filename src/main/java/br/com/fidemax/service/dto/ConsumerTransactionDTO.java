package br.com.fidemax.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the ConsumerTransaction entity.
 */
public class ConsumerTransactionDTO implements Serializable {

    private Long id;

    private LocalDate date;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal balance;

    private Long employeeId;

    private Long portfolioId;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConsumerTransactionDTO consumerTransactionDTO = (ConsumerTransactionDTO) o;
        if (consumerTransactionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), consumerTransactionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConsumerTransactionDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", balance=" + getBalance() +
            ", employee=" + getEmployeeId() +
            ", portfolio=" + getPortfolioId() +
            ", company=" + getCompanyId() +
            "}";
    }
}
