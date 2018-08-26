package br.com.fidemax.repository;

import br.com.fidemax.domain.ConsumerTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ConsumerTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsumerTransactionRepository extends JpaRepository<ConsumerTransaction, Long>, JpaSpecificationExecutor<ConsumerTransaction> {

}
