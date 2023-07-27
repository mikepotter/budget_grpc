package xyz.potter.showcase.budget.grpc;

import java.util.Iterator;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.protobuf.Empty;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.StringValue;
import com.google.protobuf.util.JsonFormat;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.micrometer.core.annotation.Timed;
import net.devh.boot.grpc.server.service.GrpcService;
import xyz.potter.showcase.budget.proto.Category;
import xyz.potter.showcase.budget.proto.CategoryServiceGrpc.CategoryServiceImplBase;
import xyz.potter.showcase.budget.service.CategoryService;

@GrpcService
public class CategoryGrpcService extends CategoryServiceImplBase {

  private Logger logger = LoggerFactory.getLogger(CategoryGrpcService.class);

  private CategoryService categoryService;

  public CategoryGrpcService(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @Override
  @Timed("grpc.category.findById")
  public void findById(StringValue id, StreamObserver<Category> responseObserver) {
    logger.debug("findById: " + id.getValue());

    try {
      Optional<Category> optionalProto = categoryService.findById(id.getValue());
      if (optionalProto.isEmpty()) {
        responseObserver.onError(Status.NOT_FOUND.asException());
      } else {
        responseObserver.onNext(optionalProto.get());
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      logger.error(e.toString(), e);
      responseObserver.onError(Status.INTERNAL.withCause(e).asException());
    }
  }

  @Override
  @Timed("grpc.category.findByBudgetId")
  public void findByBudgetId(StringValue budgetId, StreamObserver<Category> responseObserver) {
    logger.debug("findByBudgetId");

    try {
      Iterator<Category> protoIterator = categoryService.findByBudgetId(budgetId.getValue());
      while (protoIterator.hasNext()) {
        responseObserver.onNext(protoIterator.next());
      }
      responseObserver.onCompleted();
    } catch (Exception e) {
      logger.error(e.toString(), e);
      responseObserver.onError(Status.INTERNAL.withCause(e).asException());
    }
  }

  // @Override
  @Timed("grpc.category.create")
  public void create(Category proto, StreamObserver<Category> responseObserver) {
    try {
      logger.debug("create: " + JsonFormat.printer().print(proto));
    } catch (InvalidProtocolBufferException e) {
      logger.error(e.toString(), e);
    }

    try {
      proto = categoryService.create(proto);
      responseObserver.onNext(proto);
      responseObserver.onCompleted();
    } catch (Exception e) {
      logger.error(e.toString(), e);
      responseObserver.onError(Status.INTERNAL.withCause(e).asException());
    }
  }

  // @Override
  @Timed("grpc.category.update")
  public void update(Category proto, StreamObserver<Category> responseObserver) {
    try {
      logger.debug("update: " + JsonFormat.printer().print(proto));
    } catch (InvalidProtocolBufferException e) {
      logger.error(e.toString(), e);
    }

    try {
      Optional<Category> optionalProto = categoryService.update(proto);
      if (optionalProto.isEmpty()) {
        responseObserver.onError(Status.NOT_FOUND.asException());
      } else {
        responseObserver.onNext(proto);
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      logger.error(e.toString(), e);
      responseObserver.onError(Status.INTERNAL.withCause(e).asException());
    }
  }

  @Override
  @Timed("grpc.category.delete")
  public void delete(StringValue id, StreamObserver<Empty> responseObserver) {
    logger.debug("delete: " + id.getValue());

    try {
      categoryService.delete(id.getValue());
      responseObserver.onNext(Empty.getDefaultInstance());
      responseObserver.onCompleted();
    } catch (Exception e) {
      logger.error(e.toString(), e);
      responseObserver.onError(Status.INTERNAL.withCause(e).asException());
    }
  }
}
