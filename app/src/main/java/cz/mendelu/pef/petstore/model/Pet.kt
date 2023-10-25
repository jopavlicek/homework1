package cz.mendelu.pef.petstore.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Pet(
    var id: Long?,
    @Json(name = "category")
    var category: Category?,
    var name: String?,
    var photoUrls: List<String>?,
    var tags: List<Tag>?,
    var status: String?
) : Serializable