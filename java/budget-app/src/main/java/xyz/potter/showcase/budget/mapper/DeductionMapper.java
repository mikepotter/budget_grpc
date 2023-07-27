package xyz.potter.showcase.budget.mapper;

import java.util.UUID;
import xyz.potter.showcase.budget.entity.DeductionEntity;
import xyz.potter.showcase.budget.proto.Deduction;

public class DeductionMapper {

  public static void entityToProto(DeductionEntity entity, Deduction.Builder protoBuilder) {
    if (entity.getId() != null) {
      protoBuilder.setId(entity.getId().toString());
    }
    protoBuilder.setPaycheckId(entity.getPaycheckId().toString());
    protoBuilder.setTitle(entity.getTitle());
    protoBuilder.setAmount(entity.getAmount());
    protoBuilder.setCalculateOn(entity.getCalculateOn());
    protoBuilder.setTaxableType(entity.getTaxableType());
    protoBuilder.setEnabled(entity.getEnabled());
    protoBuilder.setPosition(entity.getPosition());
  }

  public static void protoToEntity(Deduction proto, DeductionEntity entity) {
    if (proto.getId() != null && !proto.getId().isEmpty()) {
      entity.setId(UUID.fromString(proto.getId()));
    }
    entity.setPaycheckId(UUID.fromString(proto.getPaycheckId()));
    entity.setAmount(proto.getAmount());
    entity.setCalculateOn(proto.getCalculateOn());
    entity.setTaxableType(proto.getTaxableType());
    entity.setEnabled(proto.getEnabled());
    entity.setPosition(proto.getPosition());
    entity.setTitle(proto.getTitle());

  }
}
