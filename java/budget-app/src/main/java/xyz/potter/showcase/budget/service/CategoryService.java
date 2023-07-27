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
import xyz.potter.showcase.budget.entity.CategoryEntity;
import xyz.potter.showcase.budget.mapper.BudgetMapper;
import xyz.potter.showcase.budget.mapper.CategoryMapper;
import xyz.potter.showcase.budget.proto.Category;
import xyz.potter.showcase.budget.repository.jpa.CategoryJpaRepository;

@Service
public class CategoryService {

  private Logger logger = LoggerFactory.getLogger(CategoryService.class);

  private CategoryJpaRepository categoryJpaRepository;

  public CategoryService(CategoryJpaRepository categoryJpaRepository) {
    this.categoryJpaRepository = categoryJpaRepository;
  }

  @Cacheable(value = "category", key = "#id")
  public Optional<Category> findById(String id) {
    Optional<CategoryEntity> optionalEntity = categoryJpaRepository.findById(UUID.fromString(id));
    if (optionalEntity.isEmpty()) {
      logger.debug("category " + id + " not found in db");
      return Optional.empty();
    } else {
      logger.debug("category " + id + " found in db");
      Category.Builder protoBuilder = Category.newBuilder();
      CategoryMapper.entityToProto(optionalEntity.get(), protoBuilder);
      Category proto = protoBuilder.build();
      Optional<Category> optionalProto = Optional.of(proto);
      return optionalProto;
    }
  }

 
  public Iterator<Category> findByBudgetId(String budgetId) {
    Iterator<CategoryEntity> entityIterator = categoryJpaRepository.findByBudgetId(UUID.fromString(budgetId)).iterator();

    Iterable<Category> protoIterable = new Iterable<Category>() {
      @Override
      public Iterator<Category> iterator() {
        return new Iterator<Category>() {

          @Override
          public boolean hasNext() {
            return entityIterator.hasNext();
          }

          @Override
          public Category next() {
            Category.Builder protoBuilder = Category.newBuilder();
            CategoryMapper.entityToProto(entityIterator.next(), protoBuilder);
            return protoBuilder.build();
          }

        };
      }

    };

    return protoIterable.iterator();
  }

  @CachePut(value = "category", key = "#result.id")
  public Category create(Category proto) {
    CategoryEntity entity = new CategoryEntity();
    CategoryMapper.protoToEntity(proto, entity);
    entity = categoryJpaRepository.save(entity);
    logger.debug("category " + entity.getId() + " saved to db");
    Category.Builder protoBuilder = Category.newBuilder();
    CategoryMapper.entityToProto(entity, protoBuilder);
    proto = protoBuilder.build();
    return proto;
  }

  @CachePut(value = "category", key = "#result.id")
  public Optional<Category> update(Category proto) {
    String id = proto.getId();
    Optional<CategoryEntity> optionalEntity = categoryJpaRepository.findById(UUID.fromString(id));
    if (optionalEntity.isEmpty()) {
      logger.debug("category " + id + " not found in db");
      return Optional.empty();
    } else {
      logger.debug("category " + id + " found in db");
      CategoryEntity entity = optionalEntity.get();
      CategoryMapper.protoToEntity(proto, entity);
      categoryJpaRepository.save(entity);
      logger.debug("category " + id + " saved to db");

      Category.Builder protoBuilder = Category.newBuilder();
      CategoryMapper.entityToProto(entity, protoBuilder);
      proto = protoBuilder.build();

      return Optional.of(proto);
    }
  }

  @CacheEvict(value = "category", key = "#id")
  public void delete(String id) {
    categoryJpaRepository.deleteById(UUID.fromString(id));
    logger.debug("category " + id + " deleted from db");
  }

}
