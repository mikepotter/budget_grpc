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

import xyz.potter.showcase.budget.entity.ExpenseEntity;
import xyz.potter.showcase.budget.mapper.ExpenseMapper;
import xyz.potter.showcase.budget.proto.Expense;
import xyz.potter.showcase.budget.repository.jpa.ExpenseJpaRepository;

@Service
public class ExpenseService {

  private Logger logger = LoggerFactory.getLogger(ExpenseService.class);

  private ExpenseJpaRepository expenseJpaRepository;

  public ExpenseService(ExpenseJpaRepository r) {
    expenseJpaRepository = r;
  }

  @Cacheable(value = "expense", key = "#id")
  public Optional<Expense> findById(String id) {
    Optional<ExpenseEntity> optionalEntity = expenseJpaRepository.findById(UUID.fromString(id));
    if (optionalEntity.isEmpty()) {
      logger.debug("expense " + id + " not found in db");
      return Optional.empty();
    } else {
      logger.debug("expense " + id + " found in db");
      Expense.Builder protoBuilder = Expense.newBuilder();
      ExpenseMapper.entityToProto(optionalEntity.get(), protoBuilder);
      Expense proto = protoBuilder.build();
      Optional<Expense> optionalProto = Optional.of(proto);
      return optionalProto;
    }
  }

 
  public Iterator<Expense> findByCategoryId(String categoryId) {
    Iterator<ExpenseEntity> entityIterator = expenseJpaRepository.findByCategoryId(UUID.fromString(categoryId)).iterator();

    Iterable<Expense> protoIterable = new Iterable<Expense>() {
      @Override
      public Iterator<Expense> iterator() {
        return new Iterator<Expense>() {

          @Override
          public boolean hasNext() {
            return entityIterator.hasNext();
          }

          @Override
          public Expense next() {
            Expense.Builder protoBuilder = Expense.newBuilder();
            ExpenseMapper.entityToProto(entityIterator.next(), protoBuilder);
            return protoBuilder.build();
          }

        };
      }

    };

    return protoIterable.iterator();
  }

  @CachePut(value = "expense", key = "#result.id")
  public Expense create(Expense proto) {
    ExpenseEntity entity = new ExpenseEntity();
    ExpenseMapper.protoToEntity(proto, entity);
    entity = expenseJpaRepository.save(entity);
    logger.debug("expense " + entity.getId() + " saved to db");
    Expense.Builder protoBuilder = Expense.newBuilder();
    ExpenseMapper.entityToProto(entity, protoBuilder);
    proto = protoBuilder.build();
    return proto;
  }

  @CachePut(value = "expense", key = "#result.id")
  public Optional<Expense> update(Expense proto) {
    String id = proto.getId();
    Optional<ExpenseEntity> optionalEntity = expenseJpaRepository.findById(UUID.fromString(id));
    if (optionalEntity.isEmpty()) {
      logger.debug("expense " + id + " not found in db");
      return Optional.empty();
    } else {
      logger.debug("expense " + id + " found in db");
      ExpenseEntity entity = optionalEntity.get();
      ExpenseMapper.protoToEntity(proto, entity);
      expenseJpaRepository.save(entity);
      logger.debug("expense " + id + " saved to db");

      Expense.Builder protoBuilder = Expense.newBuilder();
      ExpenseMapper.entityToProto(entity, protoBuilder);
      proto = protoBuilder.build();

      return Optional.of(proto);
    }
  }

  @CacheEvict(value = "expense", key = "#id")
  public void delete(String id) {
    expenseJpaRepository.deleteById(UUID.fromString(id));
    logger.debug("expense " + id + " deleted from db");
  }

}
