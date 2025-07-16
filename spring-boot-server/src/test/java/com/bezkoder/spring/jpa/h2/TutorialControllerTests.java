package com.bezkoder.spring.jpa.h2;

import com.bezkoder.spring.jpa.h2.controller.TutorialController;
import com.bezkoder.spring.jpa.h2.model.Tutorial;
import com.bezkoder.spring.jpa.h2.repository.TutorialRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(TutorialController.class)
class TutorialControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TutorialRepository tutorialRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/tutorials returns list of tutorials")
    void testGetAllTutorials() throws Exception {
        Tutorial t1 = new Tutorial("Java", "Learn Java", true);
        Tutorial t2 = new Tutorial("Spring", "Learn Spring", false);
        Mockito.when(tutorialRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tutorials"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /api/tutorials?title=Java returns filtered list")
    void testGetTutorialsByTitle() throws Exception {
        Tutorial t = new Tutorial("Java Basics", "Introduction", true);
        Mockito.when(tutorialRepository.findByTitleContainingIgnoreCase("Java"))
                .thenReturn(Collections.singletonList(t));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tutorials")
                        .param("title", "Java"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Java Basics"));
    }

    @Test
    @DisplayName("GET /api/tutorials/{id} returns tutorial when found")
    void testGetTutorialByIdFound() throws Exception {
        Tutorial t = new Tutorial("Java", "Learn Java", true);
        Mockito.when(tutorialRepository.findById(1L)).thenReturn(Optional.of(t));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tutorials/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Java"));
    }

    @Test
    @DisplayName("GET /api/tutorials/{id} returns 404 when not found")
    void testGetTutorialByIdNotFound() throws Exception {
        Mockito.when(tutorialRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tutorials/99"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/tutorials creates a new tutorial")
    void testCreateTutorial() throws Exception {
        Tutorial t = new Tutorial("Test", "Desc", false);
        Tutorial saved = new Tutorial("Test", "Desc", false);
        Mockito.when(tutorialRepository.save(any(Tutorial.class))).thenReturn(saved);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tutorials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(t)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test"));
    }

    @Test
    @DisplayName("PUT /api/tutorials/{id} updates tutorial when found")
    void testUpdateTutorialFound() throws Exception {
        Tutorial existing = new Tutorial("Old", "OldDesc", false);
        Mockito.when(tutorialRepository.findById(1L)).thenReturn(Optional.of(existing));
        Mockito.when(tutorialRepository.save(any(Tutorial.class))).thenReturn(existing);

        existing.setTitle("New");
        existing.setDescription("NewDesc");
        existing.setPublished(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tutorials/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existing)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("New"));
    }

    @Test
    @DisplayName("DELETE /api/tutorials/{id} deletes tutorial")
    void testDeleteTutorial() throws Exception {
        Mockito.doNothing().when(tutorialRepository).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tutorials/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/tutorials deletes all tutorials")
    void testDeleteAllTutorials() throws Exception {
        Mockito.doNothing().when(tutorialRepository).deleteAll();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tutorials"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/tutorials/published returns published tutorials")
    void testFindByPublished() throws Exception {
        Tutorial t = new Tutorial("Pub", "Desc", true);
        Mockito.when(tutorialRepository.findByPublished(true))
                .thenReturn(Collections.singletonList(t));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tutorials/published"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].published").value(true));
    }

}
