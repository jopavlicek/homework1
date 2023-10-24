package cz.mendelu.pef.petstore.communication.pets

import cz.mendelu.pef.petstore.model.ApiResponse
import cz.mendelu.pef.petstore.model.Pet
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PetsAPI {

    @GET("pet/findByStatus")
    suspend fun findByStatus(@Query("status") status: String): Response<List<Pet>>

    @GET("pet/{id}")
    suspend fun findById(@Path("id") id: Long): Response<Pet>

    @DELETE("pet/{id}")
    suspend fun deletePet(@Path("id") id: Long): Response<ApiResponse>

    @POST("pet")
    suspend fun createPet(@Body pet: Pet): Response<Pet>

}