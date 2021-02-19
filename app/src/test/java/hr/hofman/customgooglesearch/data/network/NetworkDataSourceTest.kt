package hr.hofman.customgooglesearch.data.network

import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NetworkDataSourceTest : TestCase() {

    private val SEARCH_QUERY = "DOGS"

    private lateinit var googleCustomSearchService: GoogleCustomSearchService
    private lateinit var networkDataSource: NetworkDataSource

    @Before
    fun setup() {
        googleCustomSearchService = Mockito.mock(GoogleCustomSearchService::class.java)
        networkDataSource = NetworkDataSource(googleCustomSearchService)
    }

    @Test
    fun `should return success response when search is made`() = runBlockingTest {
        Mockito.`when`(googleCustomSearchService.getSearchResult(SEARCH_QUERY))
            .thenReturn(TestData.successResponse)

        val networkResponse = networkDataSource.getSearchResult(SEARCH_QUERY)

        assert(networkResponse.isSuccessful)
        assertThat(networkResponse, `is`(TestData.successResponse))
    }

    @Test
    fun `should return error body when missing key`() = runBlockingTest {
        Mockito.`when`(googleCustomSearchService.getSearchResult(SEARCH_QUERY))
            .thenReturn(TestData.missingKeyResponse)

        val networkResponse = networkDataSource.getSearchResult(SEARCH_QUERY)

        assert(!networkResponse.isSuccessful)
        assertThat(networkResponse, `is`(TestData.missingKeyResponse))
    }

    @Test
    fun `should return error body when invalid cx`() = runBlockingTest {
        Mockito.`when`(googleCustomSearchService.getSearchResult(SEARCH_QUERY))
            .thenReturn(TestData.invalidCxResponse)

        val networkResponse = networkDataSource.getSearchResult(SEARCH_QUERY)

        assert(!networkResponse.isSuccessful)
        assertThat(networkResponse, `is`(TestData.invalidCxResponse))
    }


    @Test
    fun `should return error body when invalid key`() = runBlockingTest {
        Mockito.`when`(googleCustomSearchService.getSearchResult(SEARCH_QUERY))
            .thenReturn(TestData.invalidKeyResponse)

        val networkResponse = networkDataSource.getSearchResult(SEARCH_QUERY)

        assert(!networkResponse.isSuccessful)
        assertThat(networkResponse, `is`(TestData.invalidKeyResponse))
    }
}
