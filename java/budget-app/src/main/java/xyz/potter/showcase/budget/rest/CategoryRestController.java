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
import xyz.potter.showcase.budget.proto.Category;
import xyz.potter.showcase.budget.proto.Categories;
import xyz.potter.showcase.budget.service.CategoryService;
import reactor.core.publisher.Mono;

@RestController
public class CategoryRestController {

  private Logger logger = LoggerFactory.getLogger(CategoryRestController.class);

  private CategoryService categoryService;

  public CategoryRestController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/api/category/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<?>> findById(@PathVariable String id) {
    logger.debug("findById: " + id);

    try {
      Optional<Category> optionalProto = categoryService.findById(id);
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

  @RequestMapping(method = RequestMethod.GET, value = "/api/budget/{budgetId}/categories", produces = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<?>> findByBudgetId(@PathVariable String budgetId) {
    logger.debug("findByBudgetId: " + budgetId);

    try {
      Categories.Builder categoriesBuilder = Categories.newBuilder();
      Iterator<Category> protoIterator = categoryService.findByBudgetId(budgetId);
      while (protoIterator.hasNext()) {
        categoriesBuilder.addCategory(protoIterator.next());
      }
      return Mono.just(ResponseEntity.ok().body(JsonFormat.printer().print(categoriesBuilder.build())));
    } catch (Exception e) {
      logger.error(e.toString(), e);
      return Mono.just(ResponseEntity.internalServerError().body(e.toString()));
    }
  }

  @RequestMapping(method = RequestMethod.POST, value = "/api/categories", produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<?>> create(@RequestBody String body) {
    logger.debug("create: " + body);

    try {
      Category.Builder builder = Category.newBuilder();
      JsonFormat.parser().ignoringUnknownFields().merge(body, builder);
      Category proto = categoryService.create(builder.build());
      return Mono.just(ResponseEntity.ok().body(JsonFormat.printer().print(proto)));
    } catch (Exception e) {
      logger.error(e.toString(), e);
      return Mono.just(ResponseEntity.internalServerError().body(e.toString()));
    }
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/api/categories/{id}", produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<ResponseEntity<?>> update(@RequestBody String body) {
    logger.debug("update: " + body);

    try {
      Category.Builder builder = Category.newBuilder();
      JsonFormat.parser().ignoringUnknownFields().merge(body, builder);
      Optional<Category> optionalProto = categoryService.update(builder.build());
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

  @RequestMapping(method = RequestMethod.DELETE, value = "/api/categories/{id}")
  public Mono<ResponseEntity<?>> deleteById(@PathVariable String id) {
    logger.debug("delete: " + id);

    try {
      categoryService.delete(id);
      return Mono.just(ResponseEntity.noContent().build());
    } catch (Exception e) {
      logger.error(e.toString(), e);
      return Mono.just(ResponseEntity.internalServerError().body(e.toString()));
    }
  }

}
