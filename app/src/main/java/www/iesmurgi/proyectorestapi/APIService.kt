package www.iesmurgi.proyectorestapi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url
import www.iesmurgi.proyectorestapi.models.Emoji

interface APIService {
    @GET
    suspend fun getEmojis(@Url url: String): Response<Emoji>
}