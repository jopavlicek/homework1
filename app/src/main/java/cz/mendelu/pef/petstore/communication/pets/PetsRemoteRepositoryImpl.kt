package cz.mendelu.pef.petstore.communication.pets

import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.model.ApiResponse
import cz.mendelu.pef.petstore.model.Pet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PetsRemoteRepositoryImpl
@Inject constructor(private val petsAPI: PetsAPI) : IPetsRemoteRepository {

    override suspend fun findByStatus(status: String): CommunicationResult<List<Pet>> {
        return processResponse(
            withContext(Dispatchers.IO) {
                petsAPI.findByStatus(status)
            }
        )
    }

    override suspend fun findById(id: Long): CommunicationResult<Pet> {
        return processResponse(
            withContext(Dispatchers.IO) {
                petsAPI.findById(id)
            }
        )
    }

    override suspend fun deletePet(id: Long): CommunicationResult<ApiResponse> {
        return processResponse(
            withContext(Dispatchers.IO) {
                petsAPI.deletePet(id)
            }
        )
    }

    override suspend fun createPet(pet: Pet): CommunicationResult<Pet> {
        return processResponse(
            withContext(Dispatchers.IO) {
                petsAPI.createPet(pet)
            }
        )
    }

}