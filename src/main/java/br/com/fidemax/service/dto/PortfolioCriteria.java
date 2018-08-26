package br.com.fidemax.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;





/**
 * Criteria class for the Portfolio entity. This class is used in PortfolioResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /portfolios?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PortfolioCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private BigDecimalFilter balance;

    private LongFilter consumerTransactionId;

    private LongFilter redemptionTransactionId;

    private LongFilter customerId;

    public PortfolioCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getBalance() {
        return balance;
    }

    public void setBalance(BigDecimalFilter balance) {
        this.balance = balance;
    }

    public LongFilter getConsumerTransactionId() {
        return consumerTransactionId;
    }

    public void setConsumerTransactionId(LongFilter consumerTransactionId) {
        this.consumerTransactionId = consumerTransactionId;
    }

    public LongFilter getRedemptionTransactionId() {
        return redemptionTransactionId;
    }

    public void setRedemptionTransactionId(LongFilter redemptionTransactionId) {
        this.redemptionTransactionId = redemptionTransactionId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "PortfolioCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (balance != null ? "balance=" + balance + ", " : "") +
                (consumerTransactionId != null ? "consumerTransactionId=" + consumerTransactionId + ", " : "") +
                (redemptionTransactionId != null ? "redemptionTransactionId=" + redemptionTransactionId + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
            "}";
    }

}
