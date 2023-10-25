package cz.mendelu.pef.petstore.model

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Category(
    var id: Long?,
    var name: String?
) : Serializable