package cz.mendelu.pef.petstore.communication.user

import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.architecture.IBaseRemoteRepository
import cz.mendelu.pef.petstore.model.ApiResponse

interface IUsersRemoteRepository: IBaseRemoteRepository {
    suspend fun loginUser(username: String, password: String): CommunicationResult<ApiResponse>
}