package cz.mendelu.pef.petstore.communication.pets

import cz.mendelu.pef.petstore.model.Pet
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PetsAPI {

    @GET("pet/findByStatus")
    suspend fun findByStatus(@Query("status") status: String): Response<List<Pet>>

}