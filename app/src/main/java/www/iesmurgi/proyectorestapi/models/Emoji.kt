package www.iesmurgi.proyectorestapi.models

import com.google.gson.annotations.SerializedName

data class Emoji(
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String,
    @SerializedName("group") val group: String,
    @SerializedName("htmlCode") val htmlCode: ArrayList<String>,
    @SerializedName("unicode") val unicode: ArrayList<String>
)
