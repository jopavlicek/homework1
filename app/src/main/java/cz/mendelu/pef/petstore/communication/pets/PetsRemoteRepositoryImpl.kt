package cz.mendelu.pef.petstore.communication.pets

import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.architecture.Error
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

}