package cz.mendelu.pef.petstore.communication.pets

import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.architecture.IBaseRemoteRepository
import cz.mendelu.pef.petstore.model.ApiResponse
import cz.mendelu.pef.petstore.model.Pet
import retrofit2.Response
import retrofit2.http.Query

interface IPetsRemoteRepository: IBaseRemoteRepository {
    suspend fun findByStatus(status: String): CommunicationResult<List<Pet>>
    suspend fun findById(id: Long): CommunicationResult<Pet>
    suspend fun deletePet(id: Long): CommunicationResult<ApiResponse>
    suspend fun createPet(pet: Pet): CommunicationResult<Pet>
}