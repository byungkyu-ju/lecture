package me.bk.springbootmvcbasic.sample;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

  @CrossOrigin(origins = "http://localhost:18080")
  @GetMapping("/helloSample")
  public EntityModel helloSample() {
    Hello hello = new Hello();
    hello.setPrefix("hey, ");
    hello.setName("bk");

    EntityModel helloEntityModel = new EntityModel<>(hello);
    helloEntityModel.add(linkTo(methodOn(SampleController.class).helloSample()).withSelfRel());

    return helloEntityModel;
  }

}
