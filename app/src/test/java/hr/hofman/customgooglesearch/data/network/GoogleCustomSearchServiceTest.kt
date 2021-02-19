package hr.hofman.customgooglesearch.data.network

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GoogleCustomSearchServiceTest {

    private val SEARCH_QUERY = "DOGS"
    private val INVALID_CX = "INVALID:CX"
    private val INVALID_API_KEY = "INVALID:API_KEY"

    private lateinit var googleCustomSearchService: GoogleCustomSearchService
    private lateinit var mockWebServer: MockWebServer
    private lateinit var retrofit: Retrofit

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        googleCustomSearchService = retrofit.create(GoogleCustomSearchService::class.java)
    }

    // https://github.com/Kotlin/kotlinx.coroutines/issues/1204
    @Test
    fun `should return succesful response on valid params`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(TestData.successResponseJson))

        val response = googleCustomSearchService.getSearchResult(SEARCH_QUERY)

        assert(response.isSuccessful)
    }

    // https://github.com/Kotlin/kotlinx.coroutines/issues/1204
    @Test
    fun `should return 400 response on invalid cx`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(400).setBody(TestData.invalidCxResponseJson)
        )

        val response = googleCustomSearchService.getSearchResult(
            query = SEARCH_QUERY,
            customSearchId = INVALID_CX
        )

        assert(!response.isSuccessful)
        assertThat(
            response.errorBody()!!.string(),
            `is`(TestData.invalidCxResponse!!.errorBody()!!.string())
        )
    }

    // https://github.com/Kotlin/kotlinx.coroutines/issues/1204
    @Test
    fun `should return 400 response on invalid api key`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(400).setBody(TestData.invalidKeyResponseJson)
        )

        val response =
            googleCustomSearchService.getSearchResult(query = SEARCH_QUERY, key = INVALID_API_KEY)

        assert(!response.isSuccessful)
        assertThat(
            response.errorBody()!!.string(),
            `is`(TestData.invalidKeyResponse!!.errorBody()!!.string())
        )
    }

    // https://github.com/Kotlin/kotlinx.coroutines/issues/1204
    @Test
    fun `should return 403 response on missing keys`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(403).setBody(TestData.missingKeyResponseJson)
        )

        val response = googleCustomSearchService.getSearchResult(
            query = SEARCH_QUERY,
            customSearchId = "",
            key = ""
        )

        assert(!response.isSuccessful)
        assertThat(
            response.errorBody()!!.string(),
            `is`(TestData.missingKeyResponse!!.errorBody()!!.string())
        )
    }


    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
