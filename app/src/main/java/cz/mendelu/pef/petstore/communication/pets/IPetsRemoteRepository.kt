package cz.mendelu.pef.petstore.communication.pets

import cz.mendelu.pef.petstore.architecture.CommunicationResult
import cz.mendelu.pef.petstore.model.Pet
import retrofit2.Response
import retrofit2.http.Query

interface IPetsRemoteRepository {
    suspend fun findByStatus(status: String): CommunicationResult<List<Pet>>
}