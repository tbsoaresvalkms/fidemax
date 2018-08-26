package br.com.fidemax.repository;

import br.com.fidemax.domain.RedemptionTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RedemptionTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RedemptionTransactionRepository extends JpaRepository<RedemptionTransaction, Long>, JpaSpecificationExecutor<RedemptionTransaction> {

}
