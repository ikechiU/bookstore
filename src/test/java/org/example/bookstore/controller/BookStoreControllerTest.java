package org.example.bookstore.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.config.H2TestConfiguration;
import org.example.bookstore.config.TestHelper;
import org.example.bookstore.config.properties.AppPropertyConfig;
import org.example.bookstore.dto.ResponseDTO;
import org.example.bookstore.dto.bookstore.BookDTO;
import org.example.bookstore.dto.bookstore.UpdateBookDTO;
import org.example.bookstore.enums.AvailabilityStatus;
import org.example.bookstore.enums.BookGenre;
import org.example.bookstore.util.AppUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Sat)-2024
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Slf4j
class BookStoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppPropertyConfig propertyConfig;


    @BeforeEach
    void checkup() {
        new H2TestConfiguration(propertyConfig);
    }

    @Test
    void retrieveAllBooks_With_Authorization() throws Exception {
        TestHelper testHelper = new TestHelper(mockMvc);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testHelper.retrieveReaderValidAccessToken()))
                .andExpect(status().isOk()).andReturn();

        ResponseDTO<?> response = AppUtil.getGsonMapper().fromJson(result.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals("200", response.getStatusCode());
    }

    @Test
    void retrieveOneBook_With_Authorization() throws Exception {
        TestHelper testHelper = new TestHelper(mockMvc);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books-d89b58c4-c9d4-4a55-8d41-9803d051875d")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testHelper.retrieveReaderValidAccessToken()))
                .andExpect(status().isOk()).andReturn();

        ResponseDTO<?> response = AppUtil.getGsonMapper().fromJson(result.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertFalse(synopsisResult(response));
        Assertions.assertEquals("200", response.getStatusCode());
    }

    @Test
    void retrieveOneBook_Without_Authorization() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books-d89b58c4-c9d4-4a55-8d41-9803d051875d")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        ResponseDTO<?> response = AppUtil.getGsonMapper().fromJson(result.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertTrue(synopsisResult(response));
        Assertions.assertEquals("200", response.getStatusCode());
    }

    @Test
    void createBook_Successful() throws Exception {
        TestHelper testHelper = new TestHelper(mockMvc);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books")
                        .content(AppUtil.getGsonMapper().toJson(bookDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testHelper.retrieveValidSuperAdminAccessToken()))
                .andExpect(status().isOk()).andReturn();

        ResponseDTO<?> response = AppUtil.getGsonMapper().fromJson(result.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals("200", response.getStatusCode());
    }

    @Test
    void createBook_Failed_Reader_Permission() throws Exception {
        TestHelper testHelper = new TestHelper(mockMvc);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books")
                        .content(AppUtil.getGsonMapper().toJson(bookDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testHelper.retrieveReaderValidAccessToken()))
                .andExpect(status().is4xxClientError()).andReturn();

        ResponseDTO<?> response = AppUtil.getGsonMapper().fromJson(result.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals("403", response.getStatusCode());
        Assertions.assertEquals(Collections.singletonList("You are not authorized to make this request"), response.getErrors());
    }

    @Test
    void updateBook() throws Exception {
        UpdateBookDTO updateBookDTO = new UpdateBookDTO();
        updateBookDTO.setBookYear("1960");
        updateBookDTO.setSynopsis("A sample synopsis");
        updateBookDTO.setContent("A sample content");
        updateBookDTO.setTitle("Example title");
        updateBookDTO.setGenre(BookGenre.FANTASY.getDisplayName());
        updateBookDTO.setAuthor("Example Auth");

        TestHelper testHelper = new TestHelper(mockMvc);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books-d89b58c4-c9d4-4a55-8d41-9803d051875d")
                        .content(AppUtil.getGsonMapper().toJson(updateBookDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testHelper.retrieveValidSuperAdminAccessToken()))
                .andExpect(status().isOk()).andReturn();

        ResponseDTO<?> response = AppUtil.getGsonMapper().fromJson(result.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals("200", response.getStatusCode());
    }

    @Test
    void manageAvailabilityStatus_Success() throws Exception {
        TestHelper testHelper = new TestHelper(mockMvc);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books-8f51f916-ba02-4e08-b3c1-0db29b6661e2")
                        .param("availabilityStatus", AvailabilityStatus.OUT_OF_STOCK.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testHelper.retrieveValidSuperAdminAccessToken()))
                .andExpect(status().isOk()).andReturn();

        ResponseDTO<?> response = AppUtil.getGsonMapper().fromJson(result.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals("200", response.getStatusCode());
        Assertions.assertEquals(AvailabilityStatus.OUT_OF_STOCK, availabilityStatus(response));
    }

    @Test
    void manageAvailabilityStatus_Failed_Same_Availability_Status() throws Exception {
        TestHelper testHelper = new TestHelper(mockMvc);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books-e35a1d2f-77f8-4b9b-86f9-31b9f82ef358")
                        .param("availabilityStatus", AvailabilityStatus.OUT_OF_STOCK.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testHelper.retrieveValidSuperAdminAccessToken()))
                .andExpect(status().is4xxClientError()).andReturn();

        ResponseDTO<?> response = AppUtil.getGsonMapper().fromJson(result.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals("400", response.getStatusCode());
        Assertions.assertEquals(Collections.singletonList("No change made to book availability status."), response.getErrors());
    }

    @Test
    void manageAvailabilityStatus_Failed_Only_PreOder_Books_In_Out_Of_Stock() throws Exception {
        TestHelper testHelper = new TestHelper(mockMvc);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books-8f51f916-ba02-4e08-b3c1-0db29b6661e2")
                        .param("availabilityStatus", AvailabilityStatus.PRE_ORDER.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testHelper.retrieveValidSuperAdminAccessToken()))
                .andExpect(status().is4xxClientError()).andReturn();

        ResponseDTO<?> response = AppUtil.getGsonMapper().fromJson(result.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals("400", response.getStatusCode());
        Assertions.assertEquals(Collections.singletonList("Only preorder books that are out of stock."), response.getErrors());
    }

    @Test
    void manageAvailabilityStatus_Failed_Cannot_Change_Discontinued_Availability_Status() throws Exception {
        TestHelper testHelper = new TestHelper(mockMvc);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books-49a5c614-58e4-4975-af4d-796e0d94e4f7")
                        .param("availabilityStatus", AvailabilityStatus.IN_STOCK.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testHelper.retrieveValidSuperAdminAccessToken()))
                .andExpect(status().is4xxClientError()).andReturn();

        ResponseDTO<?> response = AppUtil.getGsonMapper().fromJson(result.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals("400", response.getStatusCode());
        String expected = String.format("Cannot mark a discontinued book as %s.", AvailabilityStatus.IN_STOCK.getDisplayName().toLowerCase());
        Assertions.assertEquals(Collections.singletonList(expected), response.getErrors());
    }


    @Test
    void deleteBook() throws Exception {
        TestHelper testHelper = new TestHelper(mockMvc);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books-2c92c166-87bc-4c9e-9b2a-27b3e69edff6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testHelper.retrieveValidSuperAdminAccessToken()))
                .andExpect(status().isOk()).andReturn();

        ResponseDTO<?> response = AppUtil.getGsonMapper().fromJson(result.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals("200", response.getStatusCode());

        MvcResult confirmingDeletedResult = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books-2c92c166-87bc-4c9e-9b2a-27b3e69edff6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", testHelper.retrieveValidSuperAdminAccessToken()))
                .andExpect(status().is4xxClientError()).andReturn();

        ResponseDTO<?> errorResponse = AppUtil.getGsonMapper().fromJson(confirmingDeletedResult.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals("400", errorResponse.getStatusCode());
        Assertions.assertEquals(Collections.singletonList("Book with id 2c92c166-87bc-4c9e-9b2a-27b3e69edff6 does not exist"), errorResponse.getErrors());
    }


    private BookDTO bookDTO() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookYear("2000");
        bookDTO.setSynopsis("A sample synopsis");
        bookDTO.setContent("A sample content");
        bookDTO.setTitle("A sample title");
        bookDTO.setAvailabilityStatus(AvailabilityStatus.IN_STOCK);
        bookDTO.setGenre(BookGenre.ADVENTURE.getDisplayName());
        bookDTO.setAuthor("A sample author");
        return bookDTO;
    }

    private boolean synopsisResult(ResponseDTO<?> response) {
        Map<String, Object> map = (Map<String, Object>) response.getData().iterator().next();
        String synopsis = (String) map.get("synopsis");
        return synopsis.contains("\n\n[SIGN UP ... to read complete story]");
    }

    private AvailabilityStatus availabilityStatus(ResponseDTO<?> response) {
        Map<String, Object> map = (Map<String, Object>) response.getData().iterator().next();
        String availabilityStatus = (String) map.get("availabilityStatus");
        return  AvailabilityStatus.valueOf(availabilityStatus);
    }

}
