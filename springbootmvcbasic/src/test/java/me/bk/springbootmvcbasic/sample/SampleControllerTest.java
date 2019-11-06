package me.bk.springbootmvcbasic.sample;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SampleController.class)
public class SampleControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  public void helloSample() throws Exception {
    mockMvc.perform(get("/helloSample"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._links.self").exists());
  }

}
