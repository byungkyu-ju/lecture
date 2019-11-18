package me.bk.springrestapievent.events;

import me.bk.springrestapievent.common.ErrorEntityModel;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

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

    private ResponseEntity badRequest(Errors errors){
        return ResponseEntity.badRequest().body(new ErrorEntityModel(errors));
    }
}
