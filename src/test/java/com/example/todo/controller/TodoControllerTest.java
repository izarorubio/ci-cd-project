// Pruebas unitarias simples para TodoController
package com.example.todo.controller;

import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TodoControllerTest {

    @InjectMocks
    private TodoController todoController;

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testAddTodo() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");

        String viewName = todoController.addTodo("Nueva tarea", authentication);
        assertEquals("redirect:/", viewName);
        verify(todoRepository).save(any(Todo.class));
    }


    @Test
    void testDeleteTodo() {
        String viewName = todoController.deleteTodo(1L);
        assertEquals("redirect:/", viewName);
        verify(todoRepository).deleteById(1L);
    }
}
