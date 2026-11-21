package cr.ac.utn.rota

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.rota.domain.model.Item

class MyItemAdapter(private var items: List<Item>,
                    private val onEdit: (String) -> Unit,
                    private val onDelete: (String) -> Unit) :
    RecyclerView.Adapter<MyItemViewHolder>() {

    private var searching = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_my_item, parent, false)
        return MyItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyItemViewHolder, position: Int) {
        holder.bind(items[position], searching, onEdit, onDelete)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(newItems: List<Item>, isSearching: Boolean) {
        items = newItems
        searching = isSearching
        notifyDataSetChanged()
    }
}

class MyItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title = view.findViewById<TextView>(R.id.myItemTitle)
    private val description = view.findViewById<TextView>(R.id.myItemDescription)
    private val status = view.findViewById<TextView>(R.id.myItemStatus)
    private val location = view.findViewById<TextView>(R.id.myItemLocation)
    private val image = view.findViewById<ImageView>(R.id.myItemImage)
    private val edit = view.findViewById<TextView>(R.id.myItemEdit)
    private val delete = view.findViewById<TextView>(R.id.myItemDelete)

    fun bind(item: Item, searching: Boolean, onEdit: (String) -> Unit,
             onDelete: (String) -> Unit) {
        title.text = item.title
        description.text = item.description
        ItemUi.bindStatus(status, item.status)
        ItemUi.bindLocation(location, item.generalLocation)
        ItemUi.bindPhoto(image)

        itemView.setOnClickListener(if (searching) View.OnClickListener { onEdit(item.id) } else null)
        edit.setOnClickListener { onEdit(item.id) }
        (edit.parent as View).setOnClickListener { onEdit(item.id) }
        delete.setOnClickListener { onDelete(item.id) }
        (delete.parent as View).setOnClickListener { onDelete(item.id) }
    }
}
