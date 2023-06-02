package com.ali.IDM.controller;

import com.ali.IDM.Utility.APIResponse;
import com.ali.IDM.model.Website;
import com.ali.IDM.services.WebsiteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
class WebsiteControllerTest {
    @Mock
    private WebsiteService websiteService;
    @InjectMocks
    private WebsiteController websiteController;
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    @BeforeEach
    public void setup() {
        mockMvc =
                MockMvcBuilders.standaloneSetup(websiteController).build();
    }

    @Test
    void getAll_Success() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss");
        List<Website> websites = Arrays.asList(
                new Website(UUID.fromString("0002fc26-9b61-4e09-8d2d-1cb32eadb5fc"),"IGIHE", new URL("https://igihe.com"),
                        LocalDateTime.parse("2021-11-23 09:07:21",formatter),
                        LocalDateTime.parse("2021-11-23 09:10:21",formatter),
                        1L,2L),
                new Website(UUID.fromString("f1f2a692-af84-466e-b495-e1c02c067784"),"WIKIPEDIA", new URL("https://wikipedia.com"),
                        LocalDateTime.parse("2021-12-2 09:07:21",formatter),
                        LocalDateTime.parse("2021-12-2 09:10:21",formatter),
                        2L,3L)
                );

        when(websiteService.all()).thenReturn(websites);

        ResultActions result= mockMvc.perform(get("/site").accept(MediaType.APPLICATION_JSON));
        result.andExpectAll(
                status().isOk(),
                content().json(objectMapper.writeValueAsString(websites))
        ).andReturn();
    }

    @Test
    void getById_Success() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss");
        Website website = new Website(UUID.fromString("0002fc26-9b61-4e09-8d2d-1cb32eadb5fc"),"IGIHE",
                new URL("https://igihe.com"), LocalDateTime.parse("2021-11-23 09:07:21",formatter),
                LocalDateTime.parse("2021-11-23 09:10:21",formatter), 1L,2L);

        when(websiteService.getById(website.getId())).thenReturn(website);

        ResultActions result= mockMvc.perform(get("/site/"+website.getId())
                .accept(MediaType.APPLICATION_JSON));

        result.andExpectAll(
                status().isOk(),
                content().json(objectMapper.writeValueAsString(website))
        ).andReturn();
    }

    @Test
    void getById_NotFound() throws Exception {
        UUID id = UUID.fromString("0002fc26-9b61-4e09-8d2d-1cb32eadb5fc");
        when(websiteService.getById(id)).thenReturn(null);

        ResultActions result= mockMvc.perform(get("/site/"+id)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpectAll(
                status().isNotFound(),
                content().json(objectMapper.writeValueAsString(new APIResponse(false,"Website not found")))
        ).andReturn();
    }

    //TODO add expected content
    @Test
    void create_Success() throws Exception {

        String _url = "https://igihe.com";


        ResultActions result = mockMvc.perform(post("/site/add")
                .content(url)
                .contentType(MediaType.APPLICATION_JSON)
        );

        verify(websiteService, atLeastOnce()).create(url);

        result.andExpect(status().isOk());
    }

    //TODO add expected content
    @Test
    void create_RequiredUrl() throws Exception {
        String url ="";

        ResultActions result = mockMvc.perform(post("/site/add")
                .content(url)
                .contentType(MediaType.APPLICATION_JSON)
        );

        verify(websiteService, never()).create(url);

        result.andExpectAll(
                status().isBadRequest()
        ).andReturn();
    }

}