package com.app.publishsubscribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static com.app.publishsubscribe.StaticTestData.*;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PublishSubscribeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PubSubControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void TestACreateSubscriber() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_ADD_SUBSCRIBER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(StaticTestData.addSubscriber())))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void TestBAddMessageToQueue() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_ADD_MESSAGE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(API_KEY, API_KEY_VALUE)
                        .content(asJsonString(addPayload1())))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        Thread.sleep(5000);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_ADD_MESSAGE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(API_KEY, API_KEY_VALUE)
                        .content(asJsonString(addPayload2())))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        Thread.sleep(5000);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_ADD_MESSAGE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(API_KEY, API_KEY_VALUE)
                        .content(asJsonString(addPayload3())))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        Thread.sleep(5000);
    }

    @Test
    public void TestCBroadcastMessages() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_ADD_BROADCAST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(API_KEY, API_KEY_VALUE)
                        .content(asJsonString(StaticTestData.addSubscriber())))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void TestDGetMessagesForSubscriber() throws Exception {
        this.mockMvc
                .perform( MockMvcRequestBuilders
                        .get(getSubscriberUrl("1"))
                        .header(API_KEY, API_KEY_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    public void TestERemoveSubscriber() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete(getRemoveSubscriberUrl("1"))
                        .header(API_KEY, API_KEY_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
