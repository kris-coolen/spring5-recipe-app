package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.DisabledIf;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class IngredientServiceImplTest {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    IngredientService ingredientService;

    //init converters
    public IngredientServiceImplTest() {
        this.ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        this.ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ingredientService = new IngredientServiceImpl(ingredientToIngredientCommand, ingredientCommandToIngredient,
                recipeRepository, unitOfMeasureRepository);
    }

    @Test
    public void findByRecipeIdAndId() throws Exception {
    }

    @Test
    public void findByRecipeIdAndRecipeIdHappyPath() throws Exception {
        //given a valid recipe with 3 ingredients
        Optional<Recipe> recipeOptional = getValidTestRecipe();

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        //then
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(1L, 3L);

        //when
        assertEquals(Long.valueOf(3L), ingredientCommand.getId());
        assertEquals(Long.valueOf(1L), ingredientCommand.getRecipeId());
        verify(recipeRepository, times(1)).findById(anyLong());
    }





    @Test
    public void testSaveRecipeCommand() throws Exception {
        //given
        IngredientCommand command = new IngredientCommand();
        command.setId(3L);
        command.setRecipeId(2L);

        Optional<Recipe> recipeOptional = Optional.of(new Recipe());

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId(3L);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);

        //when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        //then
        assertEquals(Long.valueOf(3L), savedCommand.getId());
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, times(1)).save(any(Recipe.class));

    }

    @Test
    public void removeIngredientOfRecipe_Recipe_not_found() {
        //given
        Optional<Recipe> emptyRecipeOptional = Optional.empty();

        //when no recipe of id is found
        when(recipeRepository.findById(anyLong())).thenReturn(emptyRecipeOptional);
        ingredientService.removeIngredientOfRecipe(3L,anyLong());
        //then
        //todo add an assertThrows when we add exception handling

    }

    @Test
    public void removeIngredientOfRecipe_Ingredient_not_found() {
        //given a valid recipe with id 1 with 3 ingredients (id 1, 2 and 3)
        Optional<Recipe> recipeOptional = getValidTestRecipe();


        //when
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        ingredientService.removeIngredientOfRecipe(1L,4L);

        //then
        assertTrue(recipeOptional.isPresent());
        assertEquals(3,recipeOptional.get().getIngredients().size()); //all 3 ingredients still present
        //todo add an assertThrows when we add exception handling

    }

    @Test
    public void removeIngredientOfRecipe_HappyPath() {
        //given a valid recipe with id 1 with 3 ingredients (id 1, 2 and 3)
        Optional<Recipe> recipeOptional = getValidTestRecipe();


        //when
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        ingredientService.removeIngredientOfRecipe(1L,2L);

        //then
        assertTrue(recipeOptional.isPresent());
        assertEquals(2,recipeOptional.get().getIngredients().size()); //One ingredient should be removed
        //check that ingredient with id 2 is indeed removed
        assertTrue(recipeOptional
                .get()
                .getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(2L))
                .findAny()
                .isEmpty()
        );
        verify(recipeRepository,times(1)).findById(anyLong());
    }



    private Optional<Recipe> getValidTestRecipe() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId(3L);

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);
        return Optional.of(recipe);
    }

}