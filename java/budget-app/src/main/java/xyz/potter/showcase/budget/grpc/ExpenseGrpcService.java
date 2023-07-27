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
import xyz.potter.showcase.budget.proto.Expense;
import xyz.potter.showcase.budget.proto.ExpenseServiceGrpc.ExpenseServiceImplBase;
import xyz.potter.showcase.budget.service.ExpenseService;

@GrpcService
public class ExpenseGrpcService extends ExpenseServiceImplBase {

  private Logger logger = LoggerFactory.getLogger(ExpenseGrpcService.class);

  private ExpenseService expenseService;

  public ExpenseGrpcService(ExpenseService expenseService) {
    this.expenseService = expenseService;
  }

  @Override
  @Timed("grpc.expense.findById")
  public void findById(StringValue id, StreamObserver<Expense> responseObserver) {
    logger.debug("findById: " + id.getValue());

    try {
      Optional<Expense> optionalProto = expenseService.findById(id.getValue());
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
  @Timed("grpc.expense.findByCategoryId")
  public void findByCategoryId(StringValue categoryId, StreamObserver<Expense> responseObserver) {
    logger.debug("findByCategoryId");

    try {
      Iterator<Expense> protoIterator = expenseService.findByCategoryId(categoryId.getValue());
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
  @Timed("grpc.expense.create")
  public void create(Expense proto, StreamObserver<Expense> responseObserver) {
    try {
      logger.debug("create: " + JsonFormat.printer().print(proto));
    } catch (InvalidProtocolBufferException e) {
      logger.error(e.toString(), e);
    }

    try {
      proto = expenseService.create(proto);
      responseObserver.onNext(proto);
      responseObserver.onCompleted();
    } catch (Exception e) {
      logger.error(e.toString(), e);
      responseObserver.onError(Status.INTERNAL.withCause(e).asException());
    }
  }

  // @Override
  @Timed("grpc.expense.update")
  public void update(Expense proto, StreamObserver<Expense> responseObserver) {
    try {
      logger.debug("update: " + JsonFormat.printer().print(proto));
    } catch (InvalidProtocolBufferException e) {
      logger.error(e.toString(), e);
    }

    try {
      Optional<Expense> optionalProto = expenseService.update(proto);
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
  @Timed("grpc.expense.delete")
  public void delete(StringValue id, StreamObserver<Empty> responseObserver) {
    logger.debug("delete: " + id.getValue());

    try {
      expenseService.delete(id.getValue());
      responseObserver.onNext(Empty.getDefaultInstance());
      responseObserver.onCompleted();
    } catch (Exception e) {
      logger.error(e.toString(), e);
      responseObserver.onError(Status.INTERNAL.withCause(e).asException());
    }
  }
}
