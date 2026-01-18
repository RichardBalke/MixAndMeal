package service

import api.service.ByteArraySourceFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import models.dto.RecipeImageEntry
import repository.RecipeImageRepository
import java.io.FileOutputStream
import java.io.File

class RecipeImagesService(private val recipeImageRepository: RecipeImageRepository) {

    suspend fun getImagesForRecipe(recipeId: Int): List<RecipeImageEntry> {
        return recipeImageRepository.getImagesForRecipe(recipeId)
    }

    suspend fun addImage(recipeId: Int, imageUrl: String): RecipeImageEntry {
        return recipeImageRepository.addImage(recipeId, imageUrl)
    }

    suspend fun deleteImage(imageId: Int) {
        recipeImageRepository.deleteImage(imageId)
    }

    suspend fun uploadImage(recipeId:Int, fileName: String, bytes: ByteArray): RecipeImageEntry {

        val uploadDir = File("uploads/recipe_images/$recipeId")
        uploadDir.mkdirs()
        val imageFile = File(uploadDir, fileName)

        // Schrijf bytes naar disk (runBlocking of coroutine scope)
        runBlocking {
            withContext(Dispatchers.IO) {
                FileOutputStream(imageFile).use { fos ->
                    fos.write(bytes)
                }
            }
        }
        val publicUrl = "/images/$recipeId/$fileName"
        return addImage(recipeId,publicUrl)
    }

    suspend fun deleteAllRecipeImages(recipeId: Int) : Boolean {
        recipeImageRepository.deleteImagesByRecipeId(recipeId)
        val exists = recipeImageRepository.getImagesForRecipe(recipeId)
        return exists.isEmpty()
    }
}