package xyz.potter.showcase.budget.mapper;

import java.util.UUID;
import xyz.potter.showcase.budget.entity.BudgetEntity;
import xyz.potter.showcase.budget.proto.Budget;

public class BudgetMapper {

  public static void entityToProto(BudgetEntity entity, Budget.Builder protoBuilder) {
    if (entity.getId() != null) {
      protoBuilder.setId(entity.getId().toString());
    }

    protoBuilder.setTitle(entity.getTitle());
  }

  public static void protoToEntity(Budget proto, BudgetEntity entity) {
    if (proto.getId() != null && !proto.getId().isEmpty()) {
      entity.setId(UUID.fromString(proto.getId()));
    }

    entity.setTitle(proto.getTitle());
  }
}
