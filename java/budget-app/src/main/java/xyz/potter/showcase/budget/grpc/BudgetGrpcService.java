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
import xyz.potter.showcase.budget.proto.Budget;
import xyz.potter.showcase.budget.proto.BudgetServiceGrpc.BudgetServiceImplBase;
import xyz.potter.showcase.budget.service.BudgetService;

@GrpcService
public class BudgetGrpcService extends BudgetServiceImplBase {

  private Logger logger = LoggerFactory.getLogger(BudgetGrpcService.class);

  private BudgetService budgetService;

  public BudgetGrpcService(BudgetService budgetService) {
    this.budgetService = budgetService;
  }

  @Override
  @Timed("grpc.budget.findById")
  public void findById(StringValue id, StreamObserver<Budget> responseObserver) {
    logger.debug("findById: " + id.getValue());

    try {
      Optional<Budget> optionalProto = budgetService.findById(id.getValue());
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
  @Timed("grpc.budget.findAll")
  public void findAll(Empty empty, StreamObserver<Budget> responseObserver) {
    logger.debug("findAll");

    try {
      Iterator<Budget> protoIterator = budgetService.findAll();
      while (protoIterator.hasNext()) {
        responseObserver.onNext(protoIterator.next());
      }
      responseObserver.onCompleted();
    } catch (Exception e) {
      logger.error(e.toString(), e);
      responseObserver.onError(Status.INTERNAL.withCause(e).asException());
    }
  }

  @Override
  @Timed("grpc.budget.create")
  public void create(Budget proto, StreamObserver<Budget> responseObserver) {
    try {
      logger.debug("create: " + JsonFormat.printer().print(proto));
    } catch (InvalidProtocolBufferException e) {
      logger.error(e.toString(), e);
    }

    try {
      proto = budgetService.create(proto);
      responseObserver.onNext(proto);
      responseObserver.onCompleted();
    } catch (Exception e) {
      logger.error(e.toString(), e);
      responseObserver.onError(Status.INTERNAL.withCause(e).asException());
    }
  }

  @Override
  @Timed("grpc.budget.update")
  public void update(Budget proto, StreamObserver<Budget> responseObserver) {
    try {
      logger.debug("update: " + JsonFormat.printer().print(proto));
    } catch (InvalidProtocolBufferException e) {
      logger.error(e.toString(), e);
    }

    try {
      Optional<Budget> optionalProto = budgetService.update(proto);
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
  @Timed("grpc.budget.delete")
  public void delete(StringValue id, StreamObserver<Empty> responseObserver) {
    logger.debug("delete: " + id.getValue());

    try {
      budgetService.delete(id.getValue());
      responseObserver.onNext(Empty.getDefaultInstance());
      responseObserver.onCompleted();
    } catch (Exception e) {
      logger.error(e.toString(), e);
      responseObserver.onError(Status.INTERNAL.withCause(e).asException());
    }
  }
}
