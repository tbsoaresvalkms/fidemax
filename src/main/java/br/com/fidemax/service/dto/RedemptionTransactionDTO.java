package br.com.fidemax.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the RedemptionTransaction entity.
 */
public class RedemptionTransactionDTO implements Serializable {

    private Long id;

    private LocalDate date;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal balance;

    private Long itemProductId;

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

    public Long getItemProductId() {
        return itemProductId;
    }

    public void setItemProductId(Long itemProductId) {
        this.itemProductId = itemProductId;
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

        RedemptionTransactionDTO redemptionTransactionDTO = (RedemptionTransactionDTO) o;
        if (redemptionTransactionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), redemptionTransactionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RedemptionTransactionDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", balance=" + getBalance() +
            ", itemProduct=" + getItemProductId() +
            ", portfolio=" + getPortfolioId() +
            ", company=" + getCompanyId() +
            "}";
    }
}
