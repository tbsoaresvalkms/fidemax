package br.com.fidemax.service.dto;

import java.io.Serializable;
import br.com.fidemax.domain.enumeration.Office;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Employee entity. This class is used in EmployeeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /employees?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmployeeCriteria implements Serializable {
    /**
     * Class for filtering Office
     */
    public static class OfficeFilter extends Filter<Office> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private OfficeFilter office;

    private LongFilter userId;

    private LongFilter consumerTransactionId;

    private LongFilter companyId;

    public EmployeeCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public OfficeFilter getOffice() {
        return office;
    }

    public void setOffice(OfficeFilter office) {
        this.office = office;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getConsumerTransactionId() {
        return consumerTransactionId;
    }

    public void setConsumerTransactionId(LongFilter consumerTransactionId) {
        this.consumerTransactionId = consumerTransactionId;
    }

    public LongFilter getCompanyId() {
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "EmployeeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (office != null ? "office=" + office + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (consumerTransactionId != null ? "consumerTransactionId=" + consumerTransactionId + ", " : "") +
                (companyId != null ? "companyId=" + companyId + ", " : "") +
            "}";
    }

}
