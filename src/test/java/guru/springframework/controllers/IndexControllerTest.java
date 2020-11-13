package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import javax.persistence.criteria.CriteriaBuilder;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IndexControllerTest {

    IndexController indexController;

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        indexController = new IndexController(recipeService);
    }

    @Test
    void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }

    @Test
    void getIndexPage() {
        //given
        Set<Recipe> recipeSet = new HashSet<>();
        Recipe recipe1 = new Recipe();
        recipe1.setDescription("Stoofvlees");
        recipeSet.add(recipe1);
        Recipe recipe2 = new Recipe();
        recipe2.setDescription("Spaghetti");
        recipeSet.add(recipe2);
        when(recipeService.getRecipes()).thenReturn(recipeSet);
        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);
        //when
        String viewName = indexController.getIndexPage(model);

        //then
        assertEquals("index",viewName);
        verify(recipeService,times(1)).getRecipes();
        verify(model,times(1)).addAttribute(eq("ATTR_RECIPES"),argumentCaptor.capture());
        Set<Recipe> setInController = argumentCaptor.getValue();
        assertEquals(2,setInController.size());
    }
}