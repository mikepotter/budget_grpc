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
import xyz.potter.showcase.budget.proto.Paycheck;
import xyz.potter.showcase.budget.proto.PaycheckServiceGrpc.PaycheckServiceImplBase;
import xyz.potter.showcase.budget.service.PaycheckService;

@GrpcService
public class PaycheckGrpcService extends PaycheckServiceImplBase {

  private Logger logger = LoggerFactory.getLogger(PaycheckGrpcService.class);

  private PaycheckService paycheckService;

  public PaycheckGrpcService(PaycheckService paycheckService) {
    this.paycheckService = paycheckService;
  }

  @Override
  @Timed("grpc.paycheck.findById")
  public void findById(StringValue id, StreamObserver<Paycheck> responseObserver) {
    logger.debug("findById: " + id.getValue());

    try {
      Optional<Paycheck> optionalProto = paycheckService.findById(id.getValue());
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
  @Timed("grpc.paycheck.findByBudgetId")
  public void findByBudgetId(StringValue budgetId, StreamObserver<Paycheck> responseObserver) {
    logger.debug("findByBudgetId");

    try {
      Iterator<Paycheck> protoIterator = paycheckService.findByBudgetId(budgetId.getValue());
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
  @Timed("grpc.paycheck.create")
  public void create(Paycheck proto, StreamObserver<Paycheck> responseObserver) {
    try {
      logger.debug("create: " + JsonFormat.printer().print(proto));
    } catch (InvalidProtocolBufferException e) {
      logger.error(e.toString(), e);
    }

    try {
      proto = paycheckService.create(proto);
      responseObserver.onNext(proto);
      responseObserver.onCompleted();
    } catch (Exception e) {
      logger.error(e.toString(), e);
      responseObserver.onError(Status.INTERNAL.withCause(e).asException());
    }
  }

  // @Override
  @Timed("grpc.paycheck.update")
  public void update(Paycheck proto, StreamObserver<Paycheck> responseObserver) {
    try {
      logger.debug("update: " + JsonFormat.printer().print(proto));
    } catch (InvalidProtocolBufferException e) {
      logger.error(e.toString(), e);
    }

    try {
      Optional<Paycheck> optionalProto = paycheckService.update(proto);
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
  @Timed("grpc.paycheck.delete")
  public void delete(StringValue id, StreamObserver<Empty> responseObserver) {
    logger.debug("delete: " + id.getValue());

    try {
      paycheckService.delete(id.getValue());
      responseObserver.onNext(Empty.getDefaultInstance());
      responseObserver.onCompleted();
    } catch (Exception e) {
      logger.error(e.toString(), e);
      responseObserver.onError(Status.INTERNAL.withCause(e).asException());
    }
  }
}
