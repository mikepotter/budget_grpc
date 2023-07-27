package xyz.potter.showcase.budget.repository.jpa;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.potter.showcase.budget.entity.DeductionEntity;

@Repository
public interface DeductionJpaRepository extends CrudRepository<DeductionEntity, UUID> {
    Iterable<DeductionEntity> findByPaycheckId(UUID paycheckId);
}
