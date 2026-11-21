package cr.ac.utn.rota

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.rota.domain.model.Item

class ExploreItemAdapter(private var items: List<Item>,
                         private val onItemClick: (Item) -> Unit) :
    RecyclerView.Adapter<ExploreItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_explore_item, parent, false)
        return ExploreItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExploreItemViewHolder, position: Int) {
        holder.bind(items[position], onItemClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }
}

class ExploreItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title = view.findViewById<TextView>(R.id.exploreItemTitle)
    private val description = view.findViewById<TextView>(R.id.exploreItemDescription)
    private val status = view.findViewById<TextView>(R.id.exploreItemStatus)
    private val location = view.findViewById<TextView>(R.id.exploreItemLocation)
    private val image = view.findViewById<ImageView>(R.id.exploreItemImage)

    fun bind(item: Item, onItemClick: (Item) -> Unit) {
        title.text = item.title
        description.text = item.description
        ItemUi.bindStatus(status, item.status)
        ItemUi.bindLocation(location, item.generalLocation)
        ItemUi.bindPhoto(image)
        itemView.setOnClickListener { onItemClick(item) }
    }
}
