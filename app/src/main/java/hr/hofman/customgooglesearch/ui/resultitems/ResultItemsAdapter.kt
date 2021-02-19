package hr.hofman.customgooglesearch.ui.resultitems

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hr.hofman.customgooglesearch.data.network.models.ApiSearchResult.Item
import hr.hofman.customgooglesearch.databinding.ResultItemViewBinding
import hr.hofman.customgooglesearch.ui.resultitems.ResultItemsAdapter.ViewHolder

class ResultItemsAdapter : ListAdapter<Item, ViewHolder>(ItemsDiffCallback) {

    var callbacks: Callbacks? = null

    interface Callbacks {
        fun onUrlClicked(url: String)
    }

    class ViewHolder(val binding: ResultItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ResultItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            titleText.text = Html.fromHtml(item.htmlTitle)

            if (item.pagemap.cseThumbnail != null && item.pagemap.cseThumbnail.isNotEmpty()) {
                thumbnailImage.visibility = View.VISIBLE

                Glide.with(holder.itemView)
                    .load(item.pagemap.cseThumbnail[0].src)
                    .into(thumbnailImage)
            } else {
                thumbnailImage.visibility = View.GONE
            }

            snippetText.text = Html.fromHtml(item.htmlSnippet)

            visitLinkText.apply {
                text = Html.fromHtml(item.htmlFormattedUrl)
                setOnClickListener {
                    callbacks?.onUrlClicked(item.formattedUrl)
                }
            }
        }
    }
}
