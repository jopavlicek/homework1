package cz.mendelu.pef.petstore.communication.user

import cz.mendelu.pef.petstore.model.ApiResponse
import cz.mendelu.pef.petstore.model.Pet
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersAPI {

    @GET("user/login")
    suspend fun loginUser(
        @Query("username") username: String,
        @Query("password") password: String,
    ): Response<ApiResponse>

}