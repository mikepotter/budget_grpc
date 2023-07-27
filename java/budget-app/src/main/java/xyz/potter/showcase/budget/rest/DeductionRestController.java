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
import xyz.potter.showcase.budget.proto.Deduction;
import xyz.potter.showcase.budget.proto.Deductions;
import xyz.potter.showcase.budget.service.DeductionService;
import reactor.core.publisher.Mono;

@RestController
public class DeductionRestController {

  private Logger logger = LoggerFactory.getLogger(DeductionRestController.class);

  private DeductionService deductionService;

  public DeductionRestController(DeductionService deductionService) {
    this.deductionService = deductionService;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/api/deduction/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<?>> findById(@PathVariable String id) {
    logger.debug("findById: " + id);

    try {
      Optional<Deduction> optionalProto = deductionService.findById(id);
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

  @RequestMapping(method = RequestMethod.GET, value = "/api/paycheck/{paycheckId}/deductions", produces = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<?>> findByPaycheckId(@PathVariable String paycheckId) {
    logger.debug("findByPaycheckId: " + paycheckId);

    try {
      Deductions.Builder deductionsBuilder = Deductions.newBuilder();
      Iterator<Deduction> protoIterator = deductionService.findByPaycheckId(paycheckId);
      while (protoIterator.hasNext()) {
        deductionsBuilder.addDeduction(protoIterator.next());
      }
      return Mono.just(ResponseEntity.ok().body(JsonFormat.printer().print(deductionsBuilder.build())));
    } catch (Exception e) {
      logger.error(e.toString(), e);
      return Mono.just(ResponseEntity.internalServerError().body(e.toString()));
    }
  }

  @RequestMapping(method = RequestMethod.POST, value = "/api/deductions", produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<?>> create(@RequestBody String body) {
    logger.debug("create: " + body);

    try {
      Deduction.Builder builder = Deduction.newBuilder();
      JsonFormat.parser().ignoringUnknownFields().merge(body, builder);
      Deduction proto = deductionService.create(builder.build());
      return Mono.just(ResponseEntity.ok().body(JsonFormat.printer().print(proto)));
    } catch (Exception e) {
      logger.error(e.toString(), e);
      return Mono.just(ResponseEntity.internalServerError().body(e.toString()));
    }
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/api/deductions/{id}", produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<?>> update(@RequestBody String body) {
    logger.debug("update: " + body);

    try {
      Deduction.Builder builder = Deduction.newBuilder();
      JsonFormat.parser().ignoringUnknownFields().merge(body, builder);
      Optional<Deduction> optionalProto = deductionService.update(builder.build());
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

  @RequestMapping(method = RequestMethod.DELETE, value = "/api/deductions/{id}")
  public Mono<ResponseEntity<?>> deleteById(@PathVariable String id) {
    logger.debug("delete: " + id);

    try {
      deductionService.delete(id);
      return Mono.just(ResponseEntity.noContent().build());
    } catch (Exception e) {
      logger.error(e.toString(), e);
      return Mono.just(ResponseEntity.internalServerError().body(e.toString()));
    }
  }

}
