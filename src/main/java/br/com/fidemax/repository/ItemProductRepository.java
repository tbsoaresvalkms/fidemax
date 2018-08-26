package br.com.fidemax.repository;

import br.com.fidemax.domain.ItemProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ItemProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemProductRepository extends JpaRepository<ItemProduct, Long>, JpaSpecificationExecutor<ItemProduct> {

}
