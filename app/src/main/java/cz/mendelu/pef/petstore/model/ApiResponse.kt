package cz.mendelu.pef.petstore.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse(
    var code: Int?,
    var type: String?,
    var message: String?,
)