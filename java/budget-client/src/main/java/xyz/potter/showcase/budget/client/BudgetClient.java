package xyz.potter.showcase.budget.client;

import java.util.Iterator;
import java.util.concurrent.Future;
import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import xyz.potter.showcase.budget.proto.Budget;
import xyz.potter.showcase.budget.proto.BudgetServiceGrpc;
import xyz.potter.showcase.budget.proto.BudgetServiceGrpc.BudgetServiceBlockingStub;
import xyz.potter.showcase.budget.proto.BudgetServiceGrpc.BudgetServiceFutureStub;

public class BudgetClient {

  private BudgetServiceBlockingStub blockingStub;
  private BudgetServiceFutureStub futureStub;

  public BudgetClient(String host, Integer port) {
    ManagedChannel channel =
        ManagedChannelBuilder.forAddress(host, port).usePlaintext().defaultLoadBalancingPolicy("round_robin").build();

    blockingStub = BudgetServiceGrpc.newBlockingStub(channel);
    futureStub = BudgetServiceGrpc.newFutureStub(channel);
  }

  public Future<Budget> findById(String id) {
    return futureStub.findById(StringValue.of(id));
  }

  public Iterator<Budget> findAll() {
    return blockingStub.findAll(Empty.getDefaultInstance());
  }

  public Future<Budget> create(Budget budget) {
    return futureStub.create(budget);
  }

  public Future<Budget> update(Budget budget) {
    return futureStub.update(budget);
  }

  public Future<Empty> delete(String id) {
    return futureStub.delete(StringValue.of(id));
  }
}
