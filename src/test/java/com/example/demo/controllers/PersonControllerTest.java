package com.example.demo.controllers;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.model.Person;
import com.example.demo.services.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService service;


    private Person person;

    @BeforeEach
    public void setup() {
        person = new Person("any", "any", "any", "any", "any@mail.com");
    }

    @Test
    public void testGivenPersonObject_whenCreatePerson_thenReturnSavedPerson() throws Exception {
        // given / arrange
        given(service.create(any(Person.class))).willReturn(person);

        // When / act
        ResultActions response = mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstNome").value("any"));
    }

    @Test
    public void testGivenPersonLidt_whenFindAllPersons_thenReturnPersonsList() throws Exception {
        // given / arrange
        List<Person> persons = new ArrayList<>();
        persons.add(person);

        given(service.findAll()).willReturn(persons);

        // When / act
        ResultActions response = mockMvc.perform(get("/person"));

        // then
        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testGivenPersonId_whenFindById_thenReturnPersonObject() throws Exception {
        long personId = 1L;
        // given / arrange
        given(service.findById(personId)).willReturn(person);

        // When / act
        ResultActions response = mockMvc.perform(get("/person/{id}", personId));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstNome").value("any"));
    }

    @Test
    public void testGivenInvalidPersonId_whenFindById_thenReturnNotFound() throws Exception {
        long personId = 1L;
        // given / arrange
        given(service.findById(personId)).willThrow(ResourceNotFoundException.class);

        // When / act
        ResultActions response = mockMvc.perform(get("/person/{id}", personId));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGivenPerson_whenUpdate_thenReturnPersonObject() throws Exception {
        long personId = 1L;
        // given / arrange
        given(service.findById(personId)).willReturn(person);
        given(service.update(any(Person.class))).willReturn(person);

        // When / act
        Person updatedPerson = new Person("any", "any11", "any11", "any11", "any@mail.com");
        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPerson)));

        // then
        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGivenInvalidPerson_whenUpdate_thenReturnNotFound() throws Exception {
        long personId = 1L;
        // given / arrange
        given(service.findById(personId)).willThrow(ResourceNotFoundException.class);
        given(service.update(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(1));

        // When / act
        Person updatedPerson = new Person("any", "any11", "any11", "any11", "any@mail.com");
        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPerson)));

        // then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGivenPersonId_whenDelete_thenReturnNoContent() throws Exception {
        long personId = 1L;
        // given / arrange
        willDoNothing().given(service).delete(personId);

        // When / act
        ResultActions response = mockMvc.perform(delete("/person/{id}", personId));

        // then
        response
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}