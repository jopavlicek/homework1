package cz.mendelu.pef.petstore.communication.user

import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.communication.pets.IPetsRemoteRepository
import cz.mendelu.pef.petstore.communication.pets.PetsAPI
import cz.mendelu.pef.petstore.model.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRemoteRepositoryImpl
@Inject constructor(private val usersAPI: UsersAPI) : IUsersRemoteRepository {

    override suspend fun loginUser(
        username: String,
        password: String,
    ): CommunicationResult<ApiResponse> {
        return processResponse(
            withContext(Dispatchers.IO) {
                usersAPI.loginUser(username, password)
            }
        )
    }

}