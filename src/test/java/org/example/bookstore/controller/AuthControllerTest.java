package org.example.bookstore.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.config.H2TestConfiguration;
import org.example.bookstore.config.properties.AppPropertyConfig;
import org.example.bookstore.dto.ResponseDTO;
import org.example.bookstore.dto.auth.LoginRequestDTO;
import org.example.bookstore.util.AppUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Slf4j
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppPropertyConfig propertyConfig;

    @BeforeEach
    void checkup() {
        new H2TestConfiguration(propertyConfig);
    }

    @Test
    void getAccessToken() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setPassword("B00kSt0r3Df@Pwd");
        loginRequestDTO.setEmail("reader@bookstore.com");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
                        .content(AppUtil.getGsonMapper().toJson(loginRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        ResponseDTO<?> response = AppUtil.getGsonMapper().fromJson(result.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals("200", response.getStatusCode());
    }

}
