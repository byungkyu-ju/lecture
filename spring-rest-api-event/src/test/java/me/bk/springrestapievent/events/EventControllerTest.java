package me.bk.springrestapievent.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.bk.springrestapievent.common.RestDocsConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@Import(RestDocsConfiguration.class)
class EventControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventRepository eventRepository;

    @Test
    @DisplayName("입력값 전달시 JSON 응답으로 201이 나오는지 확인")
    void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("description")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 11, 10, 9, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 11, 10, 18, 0))
                .beginEventDateTime(LocalDateTime.of(2019, 11, 11, 9, 0))
                .endEventDateTime(LocalDateTime.of(2019, 11, 11, 18, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남")
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document(
                        "create-event",
                        links(halLinks(),
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query-events"),
                                linkWithRel("update-event").description("link to update-event")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("contents type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("name of new event"),
                                fieldWithPath("description").description("name of new description"),
                                fieldWithPath("beginEnrollmentDateTime").description("name of new beginEnrollmentDateTime"),
                                fieldWithPath("closeEnrollmentDateTime").description("name of new closeEnrollmentDateTime"),
                                fieldWithPath("beginEventDateTime").description("name of new beginEventDateTime"),
                                fieldWithPath("endEventDateTime").description("name of new endEventDateTime"),
                                fieldWithPath("location").description("name of new location"),
                                fieldWithPath("basePrice").description("name of new basePrice"),
                                fieldWithPath("maxPrice").description("name of new maxPrice"),
                                fieldWithPath("limitOfEnrollment").description("name of new limitOfEnrollment"),
                                fieldWithPath("location").description("name of new location")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("name of new event"),
                                fieldWithPath("description").description("name of new description"),
                                fieldWithPath("beginEnrollmentDateTime").description("name of new beginEnrollmentDateTime"),
                                fieldWithPath("closeEnrollmentDateTime").description("name of new closeEnrollmentDateTime"),
                                fieldWithPath("beginEventDateTime").description("name of new beginEventDateTime"),
                                fieldWithPath("endEventDateTime").description("name of new endEventDateTime"),
                                fieldWithPath("location").description("name of new location"),
                                fieldWithPath("basePrice").description("name of new basePrice"),
                                fieldWithPath("maxPrice").description("name of new maxPrice"),
                                fieldWithPath("limitOfEnrollment").description("name of new limitOfEnrollment"),
                                fieldWithPath("location").description("name of new location"),
                                fieldWithPath("free").description("this event is free or not"),
                                fieldWithPath("offline").description("this event is offline meeting or not"),
                                fieldWithPath("eventStatus").description("this event is eventStatus"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query-events"),
                                fieldWithPath("_links.update-event.href").description("link to update-event")
                        )
                ));
    }

    @Test
    @DisplayName("id같은 항목은 user로 부터 요청받으면 안되기 때문에 bad case로 변경함")
    void createEvent_bad_reqeust() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("description")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 11, 10, 9, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 11, 10, 18, 0))
                .beginEventDateTime(LocalDateTime.of(2019, 11, 11, 9, 0))
                .endEventDateTime(LocalDateTime.of(2019, 11, 11, 18, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFree() {
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        event.update();
        assertThat(event.isFree()).isTrue();

        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();

        event.update();
        assertThat(event.isFree()).isFalse();
    }

    @Test
    void testOffline() {
        Event event = Event.builder()
                .location("종로3가")
                .build();

        event.update();
        assertThat(event.isOffline()).isTrue();

        event = Event.builder()
                .location("")
                .build();

        event.update();
        assertThat(event.isOffline()).isFalse();
    }

    @Test
    @DisplayName("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    void queryEvents() throws Exception {
        IntStream.range(0, 30).forEach(this::generateEvent);

        this.mockMvc.perform(get("/api/events")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("query-events"))

        ;
    }

    @Test
    @Description("기존의 이벤트 하나를 조회하기")
    void getEvent() throws Exception {
        Event event = this.generateEvent(100);
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists());
    }

    @Test
    @Description("없는 이벤트는 조회했을 때 404")
    void getEvent404() throws Exception {
        this.mockMvc.perform(get("/api/events/9999453"))
                .andExpect(status().isNotFound());
    }

    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event " + index)
                .description("test event")
                .build();

        return this.eventRepository.save(event);
    }


}