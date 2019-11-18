package me.bk.springrestapievent.events;

import me.bk.springrestapievent.common.ErrorEntityModel;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        if (errors.hasErrors()) {
            //return ResponseEntity.badRequest().body(errors);
            return badRequest(errors);
        }
        if (errors.hasErrors()) {
            return badRequest(errors);
        }
        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        Event newEvent = this.eventRepository.save(event);
        Link selfLink = linkTo(EventController.class).withRel("query-events");
        URI createdUri = selfLink.toUri();
        EventRepresentationModel eventRepresentationModel = new EventRepresentationModel(event);
        eventRepresentationModel.add(linkTo(EventController.class).withRel("query-events"));
        //eventRepresentationModel.add(selfLink.withSelfRel());
        eventRepresentationModel.add(selfLink.withRel("update-event"));
        return ResponseEntity.created(createdUri).body(eventRepresentationModel);
    }

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorEntityModel(errors));
    }

    @GetMapping
    private ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> page = this.eventRepository.findAll(pageable);
        PagedModel<EntityModel<Event>> pagedResources = assembler.toModel(page);
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id){
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if(optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Event event = optionalEvent.get();
        EventRepresentationModel eventRepresentationModel = new EventRepresentationModel(event);
        return ResponseEntity.ok(eventRepresentationModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateevent(@PathVariable Integer id,
                                      @RequestBody @Valid EventDto eventDto,
                                      Errors errors) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if(optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        if(errors.hasErrors()){
            return badRequest(errors);
        }

        Event existingEvent = optionalEvent.get();
        this.modelMapper.map(eventDto,existingEvent);
        Event savedEvent = this.eventRepository.save(existingEvent);

        EventRepresentationModel eventRepresentationModel = new EventRepresentationModel(savedEvent);
        //eventRepresentationModel.add(profile); //프로필링크 추가
        return ResponseEntity.ok(eventRepresentationModel);
    }
}
