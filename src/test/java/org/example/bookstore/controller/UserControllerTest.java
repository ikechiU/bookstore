package org.example.bookstore.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.bookstore.config.H2TestConfiguration;
import org.example.bookstore.config.TestHelper;
import org.example.bookstore.config.properties.AppPropertyConfig;
import org.example.bookstore.dto.ResponseDTO;
import org.example.bookstore.dto.auth.LoginRequestDTO;
import org.example.bookstore.dto.auth.UserRegistrationDTO;
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
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppPropertyConfig propertyConfig;
    private String email;

    @BeforeEach
    void checkup() throws Exception {
        new H2TestConfiguration(propertyConfig);
        createTestUser();
    }

    public UserRegistrationDTO createUserProfile() {
        email = RandomStringUtils.randomAlphabetic(8).concat("@bookstore.com");
        UserRegistrationDTO user = new UserRegistrationDTO();
        user.setEmail(email);
        user.setFirstName("USER");
        user.setLastName("USER");
        user.setPassword("B00kSt0r3Df@Pwd");
        return user;
    }

    public void createTestUser() throws Exception {
        TestHelper testHelper = new TestHelper(mockMvc);
        UserRegistrationDTO userRegistrationDTO = createUserProfile();
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/users")
                                .content(AppUtil.getGsonMapper().toJson(userRegistrationDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", testHelper.retrieveValidSuperAdminAccessToken()))
                .andExpect(status().isOk())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
        ResponseDTO<?> response = AppUtil.getGsonMapper().fromJson(result.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals("200", response.getStatusCode());
    }

    @Test
    void getAccessToken() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setPassword("B00kSt0r3Df@Pwd");
        loginRequestDTO.setEmail(email);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
                        .content(AppUtil.getGsonMapper().toJson(loginRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        ResponseDTO<?> response = AppUtil.getGsonMapper().fromJson(result.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals("200", response.getStatusCode());
    }

    @Test
    void retrieveAllUsers() throws Exception {
        TestHelper testHelper = new TestHelper(mockMvc);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testHelper.retrieveValidSuperAdminAccessToken()))
                .andExpect(status().isOk()).andReturn();

        ResponseDTO<?> response = AppUtil.getGsonMapper().fromJson(result.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals("200", response.getStatusCode());
    }

}
