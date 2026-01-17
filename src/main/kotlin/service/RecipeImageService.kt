package service

import api.service.ByteArraySourceFile
import models.dto.RecipeImageEntry
import net.schmizz.sshj.SSHClient
import org.jetbrains.exposed.sql.transactions.transaction
import repository.RecipeImageRepository

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
        val ssh = SSHClient().apply {
            connect("ssh.cyz59nsua.service.one")              // SFTP host = your domain
            authPassword("cyz59nsua_ssh", "Codecore123")
        }

        val source = ByteArraySourceFile(fileName, bytes)

        val sftp = ssh.newSFTPClient()
        try {
            val remotePath = "/webroots/18c388e7/mix-and-meal/$fileName"
            sftp.put(source, remotePath)
        } finally {
            sftp.close()
        }
        ssh.disconnect()
        val publicUrl = "https://yourdomain.com/uploads/$fileName"

        return addImage(recipeId , publicUrl)
    }

    suspend fun deleteAllRecipeImages(recipeId: Int) : Boolean {
        recipeImageRepository.deleteImagesByRecipeId(recipeId)
        val exists = recipeImageRepository.getImagesForRecipe(recipeId)
        return exists.isEmpty()
    }
}