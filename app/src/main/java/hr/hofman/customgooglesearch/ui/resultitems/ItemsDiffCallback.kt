package hr.hofman.customgooglesearch.ui.resultitems

import androidx.recyclerview.widget.DiffUtil
import hr.hofman.customgooglesearch.data.network.models.ApiSearchResult.Item

object ItemsDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.snippet == newItem.snippet
    }
}
