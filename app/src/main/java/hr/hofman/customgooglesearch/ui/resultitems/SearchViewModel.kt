package hr.hofman.customgooglesearch.ui.resultitems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import hr.hofman.customgooglesearch.data.network.GoogleCustomSearchService
import hr.hofman.customgooglesearch.data.network.NetworkDataSource
import hr.hofman.customgooglesearch.data.network.models.ApiSearchResult.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch


class SearchViewModel(
    private val networkDataSource: NetworkDataSource = NetworkDataSource(GoogleCustomSearchService.instance())
) : ViewModel() {

    private val searchTerm = MutableStateFlow("")
    private val searchResultItems: MutableStateFlow<List<Item>> = MutableStateFlow(listOf())
    private val isLoading = MutableStateFlow(false)

    fun observeSearchTermResults() = searchResultItems.asLiveData()
    fun observeIsLoading() = isLoading.asLiveData()

    init {
        viewModelScope.launch {
            searchTerm.debounce(1000).collectLatest { term ->
                if (term.isNotEmpty()) {
                    val job = launch {
                        isLoading.value = true

                        val result = networkDataSource.getSearchResult(term)

                        if (result.isSuccessful) {
                            searchResultItems.value = result.body()?.items ?: emptyList()
                        } else {
                            // TODO() -> inform user of failure and
                            //           gracefully degrade experience
                        }
                    }
                    job.invokeOnCompletion { isLoading.value = false }
                    job.join()
                }
            }
        }
    }

    fun updateSearchTerm(term: String) {
        searchTerm.value = term
    }
}
