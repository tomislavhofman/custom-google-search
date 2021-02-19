package hr.hofman.customgooglesearch.data.network

import hr.hofman.customgooglesearch.data.network.models.ApiSearchResult
import retrofit2.Response

class NetworkDataSource(private val service: GoogleCustomSearchService) {

    suspend fun getSearchResult(query: String): Response<ApiSearchResult> {
        return service.getSearchResult(query)
    }
}
