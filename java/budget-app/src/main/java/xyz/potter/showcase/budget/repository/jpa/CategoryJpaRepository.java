package xyz.potter.showcase.budget.repository.jpa;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.potter.showcase.budget.entity.CategoryEntity;

@Repository
public interface CategoryJpaRepository extends CrudRepository<CategoryEntity, UUID> {
    Iterable<CategoryEntity> findByBudgetId(UUID budgetId);
}
