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
import xyz.potter.showcase.budget.proto.Deduction;
import xyz.potter.showcase.budget.proto.DeductionServiceGrpc.DeductionServiceImplBase;
import xyz.potter.showcase.budget.service.DeductionService;

@GrpcService
public class DeductionGrpcService extends DeductionServiceImplBase {

  private Logger logger = LoggerFactory.getLogger(DeductionGrpcService.class);

  private DeductionService deductionService;

  public DeductionGrpcService(DeductionService deductionService) {
    this.deductionService = deductionService;
  }

  @Override
  @Timed("grpc.deduction.findById")
  public void findById(StringValue id, StreamObserver<Deduction> responseObserver) {
    logger.debug("findById: " + id.getValue());

    try {
      Optional<Deduction> optionalProto = deductionService.findById(id.getValue());
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
  @Timed("grpc.deduction.findByPaycheckId")
  public void findByPaycheckId(StringValue paycheckId, StreamObserver<Deduction> responseObserver) {
    logger.debug("findByPaycheckId");

    try {
      Iterator<Deduction> protoIterator = deductionService.findByPaycheckId(paycheckId.getValue());
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
  @Timed("grpc.deduction.create")
  public void create(Deduction proto, StreamObserver<Deduction> responseObserver) {
    try {
      logger.debug("create: " + JsonFormat.printer().print(proto));
    } catch (InvalidProtocolBufferException e) {
      logger.error(e.toString(), e);
    }

    try {
      proto = deductionService.create(proto);
      responseObserver.onNext(proto);
      responseObserver.onCompleted();
    } catch (Exception e) {
      logger.error(e.toString(), e);
      responseObserver.onError(Status.INTERNAL.withCause(e).asException());
    }
  }

  // @Override
  @Timed("grpc.deduction.update")
  public void update(Deduction proto, StreamObserver<Deduction> responseObserver) {
    try {
      logger.debug("update: " + JsonFormat.printer().print(proto));
    } catch (InvalidProtocolBufferException e) {
      logger.error(e.toString(), e);
    }

    try {
      Optional<Deduction> optionalProto = deductionService.update(proto);
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
  @Timed("grpc.deduction.delete")
  public void delete(StringValue id, StreamObserver<Empty> responseObserver) {
    logger.debug("delete: " + id.getValue());

    try {
      deductionService.delete(id.getValue());
      responseObserver.onNext(Empty.getDefaultInstance());
      responseObserver.onCompleted();
    } catch (Exception e) {
      logger.error(e.toString(), e);
      responseObserver.onError(Status.INTERNAL.withCause(e).asException());
    }
  }
}
