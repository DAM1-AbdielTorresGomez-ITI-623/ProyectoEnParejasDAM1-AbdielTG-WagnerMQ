package cr.ac.utn.rota

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.rota.domain.model.ItemCategory
import cr.ac.utn.rota.util.NoticeUtil
import cr.ac.utn.rota.viewmodel.ItemViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel = ItemViewModel(RotaData.repository)
    private var selectedCategory: ItemCategory? = null

    private lateinit var searchInput: EditText
    private lateinit var itemsAdapter: ExploreItemAdapter
    private lateinit var emptyMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        searchInput = findViewById(R.id.inputSearchItems)
        val itemsList = findViewById<RecyclerView>(R.id.exploreItemsList)
        itemsAdapter = ExploreItemAdapter(emptyList()) { item ->
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra(PublishItemActivity.EXTRA_ITEM_ID, item.id)
            startActivity(intent)
        }
        itemsList.layoutManager = LinearLayoutManager(this)
        itemsList.adapter = itemsAdapter
        emptyMessage = findViewById(R.id.exploreEmptyMessage)

        setSearchListener()
        setCategoryFilters()
        setNavigation()
    }

    override fun onResume() {
        super.onResume()
        showItems()
    }

    private fun setSearchListener() {
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showItems()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setCategoryFilters() {
        val filterIds = intArrayOf(R.id.categoryChip0, R.id.categoryChip1,
            R.id.categoryChip2, R.id.categoryChip3, R.id.categoryChip4)
        val categories = arrayOf<ItemCategory?>(null, ItemCategory.HOME,
            ItemCategory.BABY, ItemCategory.ELECTRONICS, ItemCategory.SPARE_PARTS)

        for (index in filterIds.indices) {
            findViewById<TextView>(filterIds[index]).setOnClickListener {
                selectedCategory = categories[index]
                updateFilters(filterIds, index)
                showItems()
            }
        }
        updateFilters(filterIds, 0)
    }

    private fun updateFilters(ids: IntArray, selected: Int) {
        for (index in ids.indices) {
            val chip = findViewById<TextView>(ids[index])
            chip.setBackgroundResource(if (index == selected) R.drawable.bg_pill_on else R.drawable.bg_pill)
            chip.setTextColor(ContextCompat.getColor(this,
                if (index == selected) R.color.rota_white else R.color.rota_gray))
        }
    }

    private fun showItems() {
        val search = searchInput.text.toString().trim()
        val items = viewModel.getItems().filter { item ->
            (selectedCategory == null || item.category == selectedCategory) &&
                (search.isEmpty() || item.title.contains(search, true) ||
                    item.description.contains(search, true))
        }

        itemsAdapter.updateItems(items)
        emptyMessage.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        if (items.isEmpty()) {
            emptyMessage.setText(if (search.isEmpty() && selectedCategory == null)
                R.string.TextNoItems else R.string.TextNoResults)
        }
    }

    private fun setNavigation() {
        findViewById<View>(R.id.navPublish).setOnClickListener {
            startActivity(Intent(this, PublishItemActivity::class.java))
        }
        findViewById<View>(R.id.navProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        findViewById<View>(R.id.navMessages).setOnClickListener { notAvailable() }
        findViewById<View>(R.id.navRequests).setOnClickListener { notAvailable() }
    }

    private fun notAvailable() {
        NoticeUtil.show(this, R.string.TextNotAvailable)
    }
}
