package hr.hofman.customgooglesearch.data.network

import hr.hofman.customgooglesearch.data.network.models.ApiSearchResult
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleCustomSearchService {

    @GET("customsearch/v1")
    suspend fun getSearchResult(
        @Query("q") query: String,
        @Query("cx") customSearchId: String = "004785902684984064423:npuxlr36ea0",
        @Query("key") key: String = "AIzaSyBCvwdTitSfeF53hy-uvM_RR-klHk_OB2k"
    ): Response<ApiSearchResult>

    companion object {
        const val BASE_URL = "https://www.googleapis.com/"

        private val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        fun instance(): GoogleCustomSearchService =
            retrofit.create(GoogleCustomSearchService::class.java)
    }
}
