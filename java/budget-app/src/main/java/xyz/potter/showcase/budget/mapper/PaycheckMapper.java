package xyz.potter.showcase.budget.mapper;

import java.util.UUID;
import xyz.potter.showcase.budget.entity.PaycheckEntity;
import xyz.potter.showcase.budget.proto.Paycheck;

public class PaycheckMapper {

  public static void entityToProto(PaycheckEntity entity, Paycheck.Builder protoBuilder) {
    if (entity.getId() != null) {
      protoBuilder.setId(entity.getId().toString());
    }
    protoBuilder.setBudgetId(entity.getBudgetId().toString());
    protoBuilder.setTitle(entity.getTitle());
    protoBuilder.setAnnualGross(entity.getAnnualGross());
    protoBuilder.setFrequency(entity.getFrequency());
  }

  public static void protoToEntity(Paycheck proto, PaycheckEntity entity) {
    if (proto.getId() != null && !proto.getId().isEmpty()) {
      entity.setId(UUID.fromString(proto.getId()));
    }
    entity.setBudgetId(UUID.fromString(proto.getBudgetId()));
    entity.setTitle(proto.getTitle());
    entity.setAnnualGross(proto.getAnnualGross());
    entity.setFrequency(proto.getFrequency());
  }
}
