package xyz.potter.showcase.budget.service;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import xyz.potter.showcase.budget.entity.PaycheckEntity;
import xyz.potter.showcase.budget.mapper.PaycheckMapper;
import xyz.potter.showcase.budget.proto.Paycheck;
import xyz.potter.showcase.budget.repository.jpa.PaycheckJpaRepository;

@Service
public class PaycheckService {

  private Logger logger = LoggerFactory.getLogger(PaycheckService.class);

  private PaycheckJpaRepository paycheckJpaRepository;

  public PaycheckService(PaycheckJpaRepository paycheckJpaRepository) {
    this.paycheckJpaRepository = paycheckJpaRepository;
  }

  @Cacheable(value = "paycheck", key = "#id")
  public Optional<Paycheck> findById(String id) {
    Optional<PaycheckEntity> optionalEntity = paycheckJpaRepository.findById(UUID.fromString(id));
    if (optionalEntity.isEmpty()) {
      logger.debug("paycheck " + id + " not found in db");
      return Optional.empty();
    } else {
      logger.debug("paycheck " + id + " found in db");
      Paycheck.Builder protoBuilder = Paycheck.newBuilder();
      PaycheckMapper.entityToProto(optionalEntity.get(), protoBuilder);
      Paycheck proto = protoBuilder.build();
      Optional<Paycheck> optionalProto = Optional.of(proto);
      return optionalProto;
    }
  }

 
  public Iterator<Paycheck> findByBudgetId(String budgetId) {
    Iterator<PaycheckEntity> entityIterator = paycheckJpaRepository.findByBudgetId(UUID.fromString(budgetId)).iterator();

    Iterable<Paycheck> protoIterable = new Iterable<Paycheck>() {
      @Override
      public Iterator<Paycheck> iterator() {
        return new Iterator<Paycheck>() {

          @Override
          public boolean hasNext() {
            return entityIterator.hasNext();
          }

          @Override
          public Paycheck next() {
            Paycheck.Builder protoBuilder = Paycheck.newBuilder();
            PaycheckMapper.entityToProto(entityIterator.next(), protoBuilder);
            return protoBuilder.build();
          }

        };
      }

    };

    return protoIterable.iterator();
  }

  @CachePut(value = "paycheck", key = "#result.id")
  public Paycheck create(Paycheck proto) {
    PaycheckEntity entity = new PaycheckEntity();
    PaycheckMapper.protoToEntity(proto, entity);
    entity = paycheckJpaRepository.save(entity);
    logger.debug("paycheck " + entity.getId() + " saved to db");
    Paycheck.Builder protoBuilder = Paycheck.newBuilder();
    PaycheckMapper.entityToProto(entity, protoBuilder);
    proto = protoBuilder.build();
    return proto;
  }

  @CachePut(value = "paycheck", key = "#result.id")
  public Optional<Paycheck> update(Paycheck proto) {
    String id = proto.getId();
    Optional<PaycheckEntity> optionalEntity = paycheckJpaRepository.findById(UUID.fromString(id));
    if (optionalEntity.isEmpty()) {
      logger.debug("paycheck " + id + " not found in db");
      return Optional.empty();
    } else {
      logger.debug("paycheck " + id + " found in db");
      PaycheckEntity entity = optionalEntity.get();
      PaycheckMapper.protoToEntity(proto, entity);
      paycheckJpaRepository.save(entity);
      logger.debug("paycheck " + id + " saved to db");

      Paycheck.Builder protoBuilder = Paycheck.newBuilder();
      PaycheckMapper.entityToProto(entity, protoBuilder);
      proto = protoBuilder.build();

      return Optional.of(proto);
    }
  }

  @CacheEvict(value = "paycheck", key = "#id")
  public void delete(String id) {
    paycheckJpaRepository.deleteById(UUID.fromString(id));
    logger.debug("paycheck " + id + " deleted from db");
  }

}
