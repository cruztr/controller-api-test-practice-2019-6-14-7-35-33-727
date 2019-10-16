package com.tw.api.unit.test.controller;

import com.tw.api.unit.test.domain.todo.Todo;
import com.tw.api.unit.test.domain.todo.TodoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoController.class)
@ActiveProfiles(profiles = "test")
public class TodoControllerTest {

    @Autowired
    private TodoController todoController;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoRepository todoRepository;

    @Test
    public void test_getAll() throws Exception {
        when(todoRepository.getAll()).thenReturn(singletonList(new Todo("Title", false)));

        ResultActions result = mvc.perform(get("/todos"));

        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Title")))
                .andExpect(jsonPath("$[0].completed", is(false)));
    }

    @Test
    public void test_get_single_ToDo() throws Exception {
        Todo todo = new Todo(123, "Title", false, 1);
        when(todoRepository.findById(1L)).thenReturn(java.util.Optional.of(todo));

        ResultActions resultActions = mvc.perform(get("/todos/1"));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", instanceOf(Todo.class)))
                .andExpect(jsonPath("$.id", is(123)))
                .andExpect(jsonPath("$.title", is("Title")))
                .andExpect(jsonPath("$.completed", is(false)))
                .andExpect(jsonPath("$.order", is(1)));
    }

}