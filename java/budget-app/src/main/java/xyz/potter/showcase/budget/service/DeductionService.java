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

import xyz.potter.showcase.budget.entity.DeductionEntity;
import xyz.potter.showcase.budget.mapper.DeductionMapper;
import xyz.potter.showcase.budget.proto.Deduction;
import xyz.potter.showcase.budget.repository.jpa.DeductionJpaRepository;

@Service
public class DeductionService {

  private Logger logger = LoggerFactory.getLogger(DeductionService.class);

  private DeductionJpaRepository deductionJpaRepository;

  public DeductionService(DeductionJpaRepository r) {
    deductionJpaRepository = r;
  }

  @Cacheable(value = "deduction", key = "#id")
  public Optional<Deduction> findById(String id) {
    Optional<DeductionEntity> optionalEntity = deductionJpaRepository.findById(UUID.fromString(id));
    if (optionalEntity.isEmpty()) {
      logger.debug("deduction " + id + " not found in db");
      return Optional.empty();
    } else {
      logger.debug("deduction " + id + " found in db");
      Deduction.Builder protoBuilder = Deduction.newBuilder();
      DeductionMapper.entityToProto(optionalEntity.get(), protoBuilder);
      Deduction proto = protoBuilder.build();
      Optional<Deduction> optionalProto = Optional.of(proto);
      return optionalProto;
    }
  }

 
  public Iterator<Deduction> findByPaycheckId(String categoryId) {
    Iterator<DeductionEntity> entityIterator = deductionJpaRepository.findByPaycheckId(UUID.fromString(categoryId)).iterator();

    Iterable<Deduction> protoIterable = new Iterable<Deduction>() {
      @Override
      public Iterator<Deduction> iterator() {
        return new Iterator<Deduction>() {

          @Override
          public boolean hasNext() {
            return entityIterator.hasNext();
          }

          @Override
          public Deduction next() {
            Deduction.Builder protoBuilder = Deduction.newBuilder();
            DeductionMapper.entityToProto(entityIterator.next(), protoBuilder);
            return protoBuilder.build();
          }

        };
      }

    };

    return protoIterable.iterator();
  }

  @CachePut(value = "deduction", key = "#result.id")
  public Deduction create(Deduction proto) {
    DeductionEntity entity = new DeductionEntity();
    DeductionMapper.protoToEntity(proto, entity);
    entity = deductionJpaRepository.save(entity);
    logger.debug("deduction " + entity.getId() + " saved to db");
    Deduction.Builder protoBuilder = Deduction.newBuilder();
    DeductionMapper.entityToProto(entity, protoBuilder);
    proto = protoBuilder.build();
    return proto;
  }

  @CachePut(value = "deduction", key = "#result.id")
  public Optional<Deduction> update(Deduction proto) {
    String id = proto.getId();
    Optional<DeductionEntity> optionalEntity = deductionJpaRepository.findById(UUID.fromString(id));
    if (optionalEntity.isEmpty()) {
      logger.debug("deduction " + id + " not found in db");
      return Optional.empty();
    } else {
      logger.debug("deduction " + id + " found in db");
      DeductionEntity entity = optionalEntity.get();
      DeductionMapper.protoToEntity(proto, entity);
      deductionJpaRepository.save(entity);
      logger.debug("deduction " + id + " saved to db");

      Deduction.Builder protoBuilder = Deduction.newBuilder();
      DeductionMapper.entityToProto(entity, protoBuilder);
      proto = protoBuilder.build();

      return Optional.of(proto);
    }
  }

  @CacheEvict(value = "deduction", key = "#id")
  public void delete(String id) {
    deductionJpaRepository.deleteById(UUID.fromString(id));
    logger.debug("deduction " + id + " deleted from db");
  }

}
