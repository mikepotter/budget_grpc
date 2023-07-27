package xyz.potter.showcase.budget.mapper;

import java.util.UUID;
import xyz.potter.showcase.budget.entity.ExpenseEntity;
import xyz.potter.showcase.budget.proto.Expense;

public class ExpenseMapper {

  public static void entityToProto(ExpenseEntity entity, Expense.Builder protoBuilder) {
    if (entity.getId() != null) {
      protoBuilder.setId(entity.getId().toString());
    }
    protoBuilder.setCategoryId(entity.getCategoryId().toString());
    protoBuilder.setQuadrant(entity.getQuadrant());
    protoBuilder.setFixed(entity.getFixed());
    protoBuilder.setTitle(entity.getTitle());
    protoBuilder.setLow(entity.getLow());
    protoBuilder.setHigh(entity.getHigh());
    protoBuilder.setAmount(entity.getAmount());
    protoBuilder.setCalculateOn(entity.getCalculateOn());
    protoBuilder.setEnabled(entity.getEnabled());
    protoBuilder.setPosition(entity.getPosition());
  }

  public static void protoToEntity(Expense proto, ExpenseEntity entity) {
    if (proto.getId() != null && !proto.getId().isEmpty()) {
      entity.setId(UUID.fromString(proto.getId()));
    }
    entity.setCategoryId(UUID.fromString(proto.getCategoryId()));
    entity.setQuadrant(proto.getQuadrant());
    entity.setFixed(proto.getFixed());
    entity.setTitle(proto.getTitle());
    entity.setLow(proto.getLow());
    entity.setHigh(proto.getHigh());
    entity.setAmount(proto.getAmount());
    entity.setCalculateOn(proto.getCalculateOn());
    entity.setEnabled(proto.getEnabled());
    entity.setPosition(proto.getPosition());

  }
}
