package me.bk.springrestapievent.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
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
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 11, 11, 9, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 11, 11, 18, 0))
                .beginEventDateTime(LocalDateTime.of(2019, 11, 12, 9, 0))
                .endEventDateTime(LocalDateTime.of(2019, 11, 12, 18, 0))
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
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE));
    }

    @Test
    @DisplayName("id같은 항목은 user로 부터 요청받으면 안되기 때문에 bad case로 변경함")
    void createEvent_bad_reqeust() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("description")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 11, 11, 9, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 11, 11, 18, 0))
                .beginEventDateTime(LocalDateTime.of(2019, 11, 12, 9, 0))
                .endEventDateTime(LocalDateTime.of(2019, 11, 12, 18, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남")
                .free(true)
                .offline(false)
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createEvent_Bad_Request_Empty_input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }
}