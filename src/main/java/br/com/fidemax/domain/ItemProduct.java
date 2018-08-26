package br.com.fidemax.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A ItemProduct.
 */
@Entity
@Table(name = "item_product")
public class ItemProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Product product;

    @OneToOne(mappedBy = "itemProduct")
    @JsonIgnore
    private RedemptionTransaction redemptionTransaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public ItemProduct count(Integer count) {
        this.count = count;
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public ItemProduct unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Product getProduct() {
        return product;
    }

    public ItemProduct product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public RedemptionTransaction getRedemptionTransaction() {
        return redemptionTransaction;
    }

    public ItemProduct redemptionTransaction(RedemptionTransaction redemptionTransaction) {
        this.redemptionTransaction = redemptionTransaction;
        return this;
    }

    public void setRedemptionTransaction(RedemptionTransaction redemptionTransaction) {
        this.redemptionTransaction = redemptionTransaction;
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
        ItemProduct itemProduct = (ItemProduct) o;
        if (itemProduct.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), itemProduct.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ItemProduct{" +
            "id=" + getId() +
            ", count=" + getCount() +
            ", unitPrice=" + getUnitPrice() +
            "}";
    }
}
