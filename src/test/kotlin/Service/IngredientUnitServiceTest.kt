//package service.Service
//
//import api.repository.IngredientUnitRepositoryImpl
//import io.mockk.*
//import kotlinx.coroutines.runBlocking
//import models.dto.IngredientUnitEntry
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.Assertions.*
//import service.IngredientUnitService
//
//class IngredientUnitServiceTest {
//
//    private lateinit var ingredientRepo: IngredientUnitRepositoryImpl
//    private lateinit var ingredientService: IngredientUnitService
//
//    @BeforeEach
//    fun setup() {
//        ingredientRepo = mockk()
//        ingredientService = IngredientUnitService(ingredientRepo)
//    }

//    @Test
//    fun `getIngredientsByRecipeId returns list`() = runBlocking {
//        val ingredients = listOf(
//            IngredientUnitEntry(1,
//                "Tomato",
//                100.0,
//                "gr"),
//            IngredientUnitEntry(1,
//                "Potato",
//                100.0,
//                "gr"
//        )
//        coEvery { ingredientRepo.findAllByRecipeId(1) } returns ingredients
//
//        val result = ingredientService.getIngredientsByRecipeId(1)
//
//        assertEquals(2, result.size)
//        coVerify { ingredientRepo.findAllByRecipeId(1) }
//    }

//    @Test
//    fun `addIngredientUnit calls repository`() = runBlocking {
//        val ingredient = IngredientUnitEntry(1,
//            "Potato",
//            100.0,
//            "gr")
//
//        ingredientRepo = mockkClass(IngredientUnitRepositoryImpl::class, relaxed = true)
//        coEvery { ingredientRepo.create(ingredient) } just Runs
//
//        ingredientService.addIngredientUnit(ingredient)
//
//        coVerify { ingredientRepo.create(ingredient) }
//    }
//}