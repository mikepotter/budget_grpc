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
import xyz.potter.showcase.budget.entity.BudgetEntity;
import xyz.potter.showcase.budget.mapper.BudgetMapper;
import xyz.potter.showcase.budget.proto.Budget;
import xyz.potter.showcase.budget.repository.jpa.BudgetJpaRepository;

@Service
public class BudgetService {

  private Logger logger = LoggerFactory.getLogger(BudgetService.class);

  private BudgetJpaRepository budgetJpaRepository;

  public BudgetService(BudgetJpaRepository budgetJpaRepository) {
    this.budgetJpaRepository = budgetJpaRepository;
  }

  @Cacheable(value = "budget", key = "#id")
  public Optional<Budget> findById(String id) {
    Optional<BudgetEntity> optionalEntity = budgetJpaRepository.findById(UUID.fromString(id));
    if (optionalEntity.isEmpty()) {
      logger.debug("budget " + id + " not found in db");
      return Optional.empty();
    } else {
      logger.debug("budget " + id + " found in db");
      Budget.Builder protoBuilder = Budget.newBuilder();
      BudgetMapper.entityToProto(optionalEntity.get(), protoBuilder);
      Budget proto = protoBuilder.build();
      Optional<Budget> optionalProto = Optional.of(proto);
      return optionalProto;
    }
  }

  public Iterator<Budget> findAll() {
    Iterator<BudgetEntity> entityIterator = budgetJpaRepository.findAll().iterator();

    Iterable<Budget> protoIterable = new Iterable<Budget>() {

      @Override
      public Iterator<Budget> iterator() {
        return new Iterator<Budget>() {

          @Override
          public boolean hasNext() {
            return entityIterator.hasNext();
          }

          @Override
          public Budget next() {
            Budget.Builder protoBuilder = Budget.newBuilder();
            BudgetMapper.entityToProto(entityIterator.next(), protoBuilder);
            return protoBuilder.build();
          }

        };
      }

    };

    return protoIterable.iterator();
  }

  @CachePut(value = "budget", key = "#result.id")
  public Budget create(Budget proto) {
    BudgetEntity entity = new BudgetEntity();
    BudgetMapper.protoToEntity(proto, entity);
    entity = budgetJpaRepository.save(entity);
    logger.debug("budget " + entity.getId() + " saved to db");
    Budget.Builder protoBuilder = Budget.newBuilder();
    BudgetMapper.entityToProto(entity, protoBuilder);
    proto = protoBuilder.build();
    return proto;
  }

  @CachePut(value = "budget", key = "#result.id")
  public Optional<Budget> update(Budget proto) {
    String id = proto.getId();
    Optional<BudgetEntity> optionalEntity = budgetJpaRepository.findById(UUID.fromString(id));
    if (optionalEntity.isEmpty()) {
      logger.debug("budget " + id + " not found in db");
      return Optional.empty();
    } else {
      logger.debug("budget " + id + " found in db");
      BudgetEntity entity = optionalEntity.get();
      BudgetMapper.protoToEntity(proto, entity);
      budgetJpaRepository.save(entity);
      logger.debug("budget " + id + " saved to db");

      Budget.Builder protoBuilder = Budget.newBuilder();
      BudgetMapper.entityToProto(entity, protoBuilder);
      proto = protoBuilder.build();

      return Optional.of(proto);
    }
  }

  @CacheEvict(value = "budget", key = "#id")
  public void delete(String id) {
    budgetJpaRepository.deleteById(UUID.fromString(id));
    logger.debug("budget " + id + " deleted from db");
  }

}
