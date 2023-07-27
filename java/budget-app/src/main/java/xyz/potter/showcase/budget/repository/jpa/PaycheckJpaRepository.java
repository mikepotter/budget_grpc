package xyz.potter.showcase.budget.repository.jpa;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.potter.showcase.budget.entity.PaycheckEntity;

@Repository
public interface PaycheckJpaRepository extends CrudRepository<PaycheckEntity, UUID> {
    Iterable<PaycheckEntity> findByBudgetId(UUID budgetId);
}
