package xyz.potter.showcase.budget.mapper;

import java.util.UUID;
import xyz.potter.showcase.budget.entity.CategoryEntity;
import xyz.potter.showcase.budget.proto.Category;

public class CategoryMapper {

  public static void entityToProto(CategoryEntity entity, Category.Builder protoBuilder) {
    if (entity.getId() != null) {
      protoBuilder.setId(entity.getId().toString());
    }

    protoBuilder.setTitle(entity.getTitle());
  }

  public static void protoToEntity(Category proto, CategoryEntity entity) {
    if (proto.getId() != null && !proto.getId().isEmpty()) {
      entity.setId(UUID.fromString(proto.getId()));
    }
    if (proto.getBudgetId() != null && !proto.getBudgetId().isEmpty()) {
      entity.setBudgetId(UUID.fromString(proto.getBudgetId()));
    }

    entity.setTitle(proto.getTitle());
  }
}
