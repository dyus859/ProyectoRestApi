package www.iesmurgi.proyectorestapi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url
import www.iesmurgi.proyectorestapi.models.Article

interface APIService {
    @GET
    suspend fun getArticles(@Url url: String): Response<List<Article>>
}