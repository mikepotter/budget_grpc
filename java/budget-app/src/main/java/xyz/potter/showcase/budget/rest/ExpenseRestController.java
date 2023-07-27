package xyz.potter.showcase.budget.rest;

import java.util.Iterator;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.protobuf.util.JsonFormat;
import xyz.potter.showcase.budget.proto.Expense;
import xyz.potter.showcase.budget.proto.Expenses;
import xyz.potter.showcase.budget.service.ExpenseService;
import reactor.core.publisher.Mono;

@RestController
public class ExpenseRestController {

  private Logger logger = LoggerFactory.getLogger(ExpenseRestController.class);

  private ExpenseService expenseService;

  public ExpenseRestController(ExpenseService expenseService) {
    this.expenseService = expenseService;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/api/expense/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<?>> findById(@PathVariable String id) {
    logger.debug("findById: " + id);

    try {
      Optional<Expense> optionalProto = expenseService.findById(id);
      if (optionalProto.isEmpty()) {
        return Mono.just(ResponseEntity.notFound().build());
      } else {
        return Mono.just(ResponseEntity.ok().body(JsonFormat.printer().print(optionalProto.get())));
      }
    } catch (Exception e) {
      logger.error(e.toString(), e);
      return Mono.just(ResponseEntity.internalServerError().body(e.toString()));
    }
  }

  @RequestMapping(method = RequestMethod.GET, value = "/api/category/{categoryId}/expenses", produces = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<?>> findByCategoryId(@PathVariable String categoryId) {
    logger.debug("findByCategoryId: " + categoryId);

    try {
      Expenses.Builder expensesBuilder = Expenses.newBuilder();
      Iterator<Expense> protoIterator = expenseService.findByCategoryId(categoryId);
      while (protoIterator.hasNext()) {
        expensesBuilder.addExpense(protoIterator.next());
      }
      return Mono.just(ResponseEntity.ok().body(JsonFormat.printer().print(expensesBuilder.build())));
    } catch (Exception e) {
      logger.error(e.toString(), e);
      return Mono.just(ResponseEntity.internalServerError().body(e.toString()));
    }
  }

  @RequestMapping(method = RequestMethod.POST, value = "/api/expenses", produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<?>> create(@RequestBody String body) {
    logger.debug("create: " + body);

    try {
      Expense.Builder builder = Expense.newBuilder();
      JsonFormat.parser().ignoringUnknownFields().merge(body, builder);
      Expense proto = expenseService.create(builder.build());
      return Mono.just(ResponseEntity.ok().body(JsonFormat.printer().print(proto)));
    } catch (Exception e) {
      logger.error(e.toString(), e);
      return Mono.just(ResponseEntity.internalServerError().body(e.toString()));
    }
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/api/expenses/{id}", produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<?>> update(@RequestBody String body) {
    logger.debug("update: " + body);

    try {
      Expense.Builder builder = Expense.newBuilder();
      JsonFormat.parser().ignoringUnknownFields().merge(body, builder);
      Optional<Expense> optionalProto = expenseService.update(builder.build());
      if (optionalProto.isEmpty()) {
        return Mono.just(ResponseEntity.noContent().build());
      } else {
        return Mono.just(ResponseEntity.ok().body(JsonFormat.printer().print(optionalProto.get())));
      }
    } catch (Exception e) {
      logger.error(e.toString(), e);
      return Mono.just(ResponseEntity.internalServerError().body(e.toString()));
    }
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/api/expenses/{id}")
  public Mono<ResponseEntity<?>> deleteById(@PathVariable String id) {
    logger.debug("delete: " + id);

    try {
      expenseService.delete(id);
      return Mono.just(ResponseEntity.noContent().build());
    } catch (Exception e) {
      logger.error(e.toString(), e);
      return Mono.just(ResponseEntity.internalServerError().body(e.toString()));
    }
  }

}
