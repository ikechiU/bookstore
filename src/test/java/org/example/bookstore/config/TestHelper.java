package org.example.bookstore.config;


import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.dto.ResponseDTO;
import org.example.bookstore.dto.auth.LoginRequestDTO;
import org.example.bookstore.dto.auth.LoginResponse;
import org.example.bookstore.util.AppUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 * N/B: To run test do the following
 * Set active profile to 'bookstore' and 'test'
 */

@Component
@Slf4j
@Profile("test")
public class TestHelper {
    private final MockMvc mockMvc;

    public TestHelper(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private static String getAccessToken(MvcResult result) throws UnsupportedEncodingException {
        LoginResponse loginResponse = getLoginResponse(result);
        return "Bearer ".concat(loginResponse.getAccessToken());
    }

    private static LoginResponse getLoginResponse(MvcResult result) throws UnsupportedEncodingException {
        ResponseDTO<?> response = getApiResponseJson(result);
        String loginResponseInString = AppUtil.getGsonMapper().toJson(response.getData());
        log.info(loginResponseInString);
        Map<String, Object> linkedTreeMap = (Map<String, Object>) response.getData().iterator().next();
        String accessToken = (String) linkedTreeMap.get("accessToken");
        Map<String, Object> additionalInformation = (Map<String, Object>) linkedTreeMap.get("additionalInformation");
        return new LoginResponse(accessToken, additionalInformation);
    }


    private static ResponseDTO<?> getApiResponseJson(MvcResult result) throws UnsupportedEncodingException {
        ResponseDTO<?> response =
                AppUtil.getGsonMapper()
                        .fromJson(result.getResponse().getContentAsString().trim(), ResponseDTO.class);
        assertEquals("200", response.getStatusCode());
        return response;
    }

    /**
     * This method is useful for getting  a valid jwt for integration test
     **/
    public String retrieveValidSuperAdminAccessToken() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setPassword("B00kSt0r3Df@Pwd");
        loginRequestDTO.setEmail("superadmin@bookstore.com");

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/login")
                                .content(AppUtil.getGsonMapper().toJson(loginRequestDTO))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return getAccessToken(result);
    }

    public String retrieveReaderValidAccessToken() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setPassword("B00kSt0r3Df@Pwd");
        loginRequestDTO.setEmail("reader@bookstore.com");

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/login")
                                .content(AppUtil.getGsonMapper().toJson(loginRequestDTO))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return getAccessToken(result);
    }

}
