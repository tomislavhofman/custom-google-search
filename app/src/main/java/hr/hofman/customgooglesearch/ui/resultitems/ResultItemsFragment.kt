package hr.hofman.customgooglesearch.ui.resultitems

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import hr.hofman.customgooglesearch.data.network.models.ApiSearchResult.Item
import hr.hofman.customgooglesearch.databinding.FragmentResultItemsBinding
import hr.hofman.customgooglesearch.ui.resultitems.ResultItemsAdapter.Callbacks

class ResultItemsFragment : Fragment() {

    private var binding: FragmentResultItemsBinding? = null
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: ResultItemsAdapter
    private lateinit var progressTimeLatch: ProgressTimeLatch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ResultItemsAdapter()

        adapter.callbacks = object : Callbacks {
            override fun onUrlClicked(url: String) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
        }

        viewModel.observeSearchTermResults().observe(this, ::renderList)
        viewModel.observeIsLoading().observe(this, ::renderLoading)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultItemsBinding.inflate(inflater, container, false).apply {
            resultItemsRecyclerView.adapter = adapter

            searchTerm.addTextChangedListener {
                viewModel.updateSearchTerm(it.toString().trim())
            }

            progressTimeLatch = ProgressTimeLatch { refreshing ->
                if (refreshing) {
                    progress.visibility = View.VISIBLE
                } else {
                    progress.visibility = View.GONE
                }
            }
        }
        return binding!!.root
    }

    private fun renderList(list: List<Item>) {
        if (list.isNotEmpty()) {
            (binding!!.searchTerm.layoutParams as ConstraintLayout.LayoutParams).run {
                bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            }
            hideKeyboardAndRemoveSearchFocus()
        }
        adapter.submitList(list)
        adapter.notifyDataSetChanged()
    }

    private fun renderLoading(isLoading: Boolean) {
        if (isLoading) binding!!.progress.visibility = View.VISIBLE
        else binding!!.progress.visibility = View.GONE
    }

    private fun hideKeyboardAndRemoveSearchFocus() {
        val imm: InputMethodManager? = requireActivity().getSystemService()
        val currentFocus = requireActivity().currentFocus
        if (currentFocus != null && imm != null) {
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }

        binding!!.searchTerm.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
