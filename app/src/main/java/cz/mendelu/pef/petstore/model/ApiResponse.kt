package cz.mendelu.pef.petstore.model

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class ApiResponse(
    var code: Int?,
    var type: String?,
    var message: String?,
) : Serializable