package br.com.fidemax.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the ItemProduct entity.
 */
public class ItemProductDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    private Integer count;

    private BigDecimal unitPrice;

    private Long productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ItemProductDTO itemProductDTO = (ItemProductDTO) o;
        if (itemProductDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), itemProductDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ItemProductDTO{" +
            "id=" + getId() +
            ", count=" + getCount() +
            ", unitPrice=" + getUnitPrice() +
            ", product=" + getProductId() +
            "}";
    }
}
