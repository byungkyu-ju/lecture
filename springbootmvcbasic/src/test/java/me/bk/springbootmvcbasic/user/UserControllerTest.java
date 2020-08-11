package me.bk.springbootmvcbasic.user;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  MockMvc mockMvc; //WebMvcTest를 사용해서 자동으로 bean을 등록해 주기 때문에 bean에 등록된걸 바로 사용할 수 있음

  @Test
  public void hello() throws Exception {
    mockMvc.perform(get("/hello"))
        .andExpect(status().isOk())
        .andExpect(content().string("hello"));
  }

  @Test
  public void createUser_JSON() throws Exception {
    String userJson = "{\"username\":\"bk\", \"password\":\"1234\"}";
    mockMvc.perform(post("/users/create")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(userJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username", is(equalTo("bk"))))
        .andExpect(jsonPath("$.password", is(equalTo("1234")))
        );
  }

  @Test
  public void createUser_XML() throws Exception {
    String userJson = "{\"username\":\"bk\", \"password\":\"1234\"}";
    mockMvc.perform(post("/users/create")
        .contentType(MediaType.APPLICATION_JSON) //요청은 JSON
        .accept(MediaType.APPLICATION_XML) //응답은 XML
        .content(userJson))
        .andExpect(status().isOk())
        .andExpect(xpath("/User/username").string("bk"))
        .andExpect(xpath("/User/password").string("1234")
        );
  }
  //실행시 HttpMediaTypeNotAcceptableException 메시지가 나타난다면, MediaType을 처리할 HttpMessageConverter가 없는것.
  //기본은 HttpMessageConvertersAutoConfiguration이 처리함
  //https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-xml dependency 추가


}
