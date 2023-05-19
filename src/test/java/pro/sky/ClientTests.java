package pro.sky;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ClientTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void givenNoClientsInDatabase_whenGetClients_thenEmptyJsonArray() throws Exception {
        mockMvc.perform(get("/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void givenNoClientsInDatabase_whenClientAdded_thenItExistsInList() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "testName");
        jsonObject.put("phoneNumber", "testPhoneNumber");
        jsonObject.put("address", "testAddress");

        mockMvc.perform(post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.phoneNumber").value("testPhoneNumber"))
                .andExpect(jsonPath("$.address").value("testAddress"));

        mockMvc.perform(get("/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("testName"))
                .andExpect(jsonPath("$[0].phoneNumber").value("testPhoneNumber"))
                .andExpect(jsonPath("$[0].address").value("testAddress"));
    }
}
