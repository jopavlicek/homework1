package cz.mendelu.pef.petstore.architecture

import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface IBaseRemoteRepository {

    fun <T: Any> processResponse(response: Response<T>): CommunicationResult<T> {
        try {
            if (response.isSuccessful) {
                if (response.body() != null){
                    // vse je ok
                    return CommunicationResult.Success(response.body()!!)
                } else {
                    return CommunicationResult.Error(
                        cz.mendelu.pef.petstore.architecture.Error(response.code(),
                            response.errorBody().toString())
                    )
                }
            } else {
                return CommunicationResult.Error(
                    cz.mendelu.pef.petstore.architecture.Error(response.code(),
                        response.errorBody().toString())
                )
            }
        } catch (ex: UnknownHostException){
            return CommunicationResult.CommunicationError()
        } catch (ex: JsonDataException){
            return CommunicationResult.Exception(ex)
        } catch (ex: SocketTimeoutException) {
            return CommunicationResult.Exception(ex)
        } catch (ex: Exception) {
            return CommunicationResult.Exception(ex)
        }
    }

}