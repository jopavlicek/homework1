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
        try {
            val response = withContext(Dispatchers.IO) {
                petsAPI.findByStatus(status)
            }

            if (response.isSuccessful) {
                if (response.body() != null){
                    // success, send response data
                    return CommunicationResult.Success(response.body()!!)
                } else {
                    // error, send error object with code and description
                    return CommunicationResult.Error(
                        cz.mendelu.pef.petstore.architecture.Error(response.code(),
                            response.errorBody().toString())
                    )
                }
            } else {
                // communication error, send error object with code and description
                return CommunicationResult.Error(
                    cz.mendelu.pef.petstore.architecture.Error(response.code(),
                        response.errorBody().toString())
                )
            }
        } catch (ex: Exception) {
            return CommunicationResult.Exception(ex)
        }
    }

}