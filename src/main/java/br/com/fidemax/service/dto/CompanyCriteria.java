package br.com.fidemax.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Company entity. This class is used in CompanyResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /companies?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CompanyCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private StringFilter cnpj;

    private LongFilter consumerTransactionId;

    private LongFilter redemptionTransactionId;

    private LongFilter employeeId;

    public CompanyCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCnpj() {
        return cnpj;
    }

    public void setCnpj(StringFilter cnpj) {
        this.cnpj = cnpj;
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

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String toString() {
        return "CompanyCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (cnpj != null ? "cnpj=" + cnpj + ", " : "") +
                (consumerTransactionId != null ? "consumerTransactionId=" + consumerTransactionId + ", " : "") +
                (redemptionTransactionId != null ? "redemptionTransactionId=" + redemptionTransactionId + ", " : "") +
                (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            "}";
    }

}
