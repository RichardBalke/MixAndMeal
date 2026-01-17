package api

import api.repository.IngredientUnitRepository
import api.repository.IngredientUnitRepositoryImpl
import api.repository.IngredientsRepository
import api.repository.IngredientsRepositoryImpl
import api.repository.RecipesRepository
import api.repository.RecipesRepositoryImpl
import api.repository.UserRepository
import api.repository.UserRepositoryImpl
import api.service.IngredientService
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import repository.AllergensRepository
import repository.AllergensRepositoryImpl
import repository.DietsRepository
import repository.DietsRepositoryImpl
import repository.RecipeAllergensRepository
import repository.RecipeAllergensRepositoryImpl
import repository.RecipeDietsRepository
import repository.RecipeDietsRepositoryImpl
import repository.RecipeImageRepository
import repository.RecipeImagesRepositoryImpl
import repository.UserAllergensRepository
import repository.UserAllergensRepositoryImpl
import repository.UserDietsRepository
import repository.UserDietsRepositoryImpl
import repository.UserFavouritesRepository
import repository.UserFavouritesRepositoryImpl
import repository.UserFridgeRepository
import repository.UserFridgeRepositoryImpl
import service.AllergenService
import service.DietsService
import service.IngredientUnitService
import service.JwtService
import service.RecipeAllergenService
import service.RecipeDietsService
import service.RecipeImagesService
import service.RecipeService
import service.UserAllergensService
import service.UserDietsService
import service.UserFavouritesService
import service.UserFridgeService
import service.UserService

fun Application.configureKoin(){
    // create all dependencies that can be injected.
    // You can use 'single' for singletons and 'factory' for multiple instances
    val appModule = module {
        singleOf(::UserRepositoryImpl) bind UserRepository::class
        factory { UserService(get()) }
        singleOf(::AllergensRepositoryImpl) bind AllergensRepository::class
        factory { AllergenService(get()) }
        singleOf(::DietsRepositoryImpl) bind DietsRepository::class
        factory { DietsService(get()) }
        singleOf(::IngredientUnitRepositoryImpl) bind IngredientUnitRepository::class
        factory { IngredientUnitService(get()) }
        singleOf(::RecipeAllergensRepositoryImpl) bind RecipeAllergensRepository::class
        factory { RecipeAllergenService(get()) }
        singleOf(::RecipeDietsRepositoryImpl) bind RecipeDietsRepository::class
        factory { RecipeDietsService(get()) }
        singleOf(::RecipesRepositoryImpl) bind RecipesRepository::class
        factory {
            RecipeService(
                recipeRepository = get(),
                dietsRepository = get(),
                allergensRepository = get()
            )
        }
        singleOf(::UserAllergensRepositoryImpl) bind UserAllergensRepository::class
        factory { UserAllergensService(get()) }
        singleOf(::UserDietsRepositoryImpl) bind UserDietsRepository::class
        factory { UserDietsService(get()) }
        singleOf(::UserFavouritesRepositoryImpl) bind UserFavouritesRepository::class
        factory { UserFavouritesService(get()) }
        singleOf(::UserFridgeRepositoryImpl) bind UserFridgeRepository::class
        factory { UserFridgeService(get()) }
        singleOf(::RecipeImagesRepositoryImpl) bind RecipeImageRepository::class
        factory { RecipeImagesService(get()) }
        singleOf(::IngredientsRepositoryImpl) bind IngredientsRepository::class
        factory { IngredientService(get()) }
    }

    install(Koin){
        slf4jLogger()
        modules(appModule)
    }
}