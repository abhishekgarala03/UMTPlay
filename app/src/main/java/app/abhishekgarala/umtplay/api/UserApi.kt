package app.abhishekgarala.umtplay.api

import app.abhishekgarala.umtplay.data.CreateUserRequest
import app.abhishekgarala.umtplay.data.CreateUserResponse
import app.abhishekgarala.umtplay.data.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @GET("users")
    suspend fun getUsers(
        @Header("x-api-key") apiKey: String = "reqres-free-v1",
        @Query("page") page: Int
    ): Response<UserResponse>

    @POST("users")
    suspend fun createUser(
        @Header("x-api-key") apiKey: String = "reqres-free-v1",
        @Body request: CreateUserRequest
    ): Response<CreateUserResponse>
}