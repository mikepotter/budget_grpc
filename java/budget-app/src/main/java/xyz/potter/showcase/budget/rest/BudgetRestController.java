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
import xyz.potter.showcase.budget.proto.Budget;
import xyz.potter.showcase.budget.proto.Budgets;
import xyz.potter.showcase.budget.service.BudgetService;
import reactor.core.publisher.Mono;

@RestController
public class BudgetRestController {

  private Logger logger = LoggerFactory.getLogger(BudgetRestController.class);

  private BudgetService budgetService;

  public BudgetRestController(BudgetService budgetService) {
    this.budgetService = budgetService;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/api/budget/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<?>> findById(@PathVariable String id) {
    logger.debug("findById: " + id);

    try {
      Optional<Budget> optionalProto = budgetService.findById(id);
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

  @RequestMapping(method = RequestMethod.GET, value = "/api/budgets", produces = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<?>> findAll() {
    logger.debug("findAll");

    try {
      Budgets.Builder budgetsBuilder = Budgets.newBuilder();
      Iterator<Budget> protoIterator = budgetService.findAll();
      while (protoIterator.hasNext()) {
        budgetsBuilder.addBudget(protoIterator.next());
      }
      return Mono.just(ResponseEntity.ok().body(JsonFormat.printer().print(budgetsBuilder.build())));
    } catch (Exception e) {
      logger.error(e.toString(), e);
      return Mono.just(ResponseEntity.internalServerError().body(e.toString()));
    }
  }

  @RequestMapping(method = RequestMethod.POST, value = "/api/budgets", produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<?>> create(@RequestBody String body) {
    logger.debug("create: " + body);

    try {
      Budget.Builder builder = Budget.newBuilder();
      JsonFormat.parser().ignoringUnknownFields().merge(body, builder);
      Budget proto = budgetService.create(builder.build());
      return Mono.just(ResponseEntity.ok().body(JsonFormat.printer().print(proto)));
    } catch (Exception e) {
      logger.error(e.toString(), e);
      return Mono.just(ResponseEntity.internalServerError().body(e.toString()));
    }
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/api/budgets/{id}", produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<?>> update(@RequestBody String body) {
    logger.debug("update: " + body);

    try {
      Budget.Builder builder = Budget.newBuilder();
      JsonFormat.parser().ignoringUnknownFields().merge(body, builder);
      Optional<Budget> optionalProto = budgetService.update(builder.build());
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

  @RequestMapping(method = RequestMethod.DELETE, value = "/api/budgets/{id}")
  public Mono<ResponseEntity<?>> deleteById(@PathVariable String id) {
    logger.debug("delete: " + id);

    try {
      budgetService.delete(id);
      return Mono.just(ResponseEntity.noContent().build());
    } catch (Exception e) {
      logger.error(e.toString(), e);
      return Mono.just(ResponseEntity.internalServerError().body(e.toString()));
    }
  }

}
