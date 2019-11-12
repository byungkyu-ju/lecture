package me.bk.springrestapievent.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.bk.springrestapievent.common.RestDocsConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class EventControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

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

}