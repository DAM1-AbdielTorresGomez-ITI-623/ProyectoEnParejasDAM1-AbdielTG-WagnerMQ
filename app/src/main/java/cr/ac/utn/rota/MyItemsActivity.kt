package cr.ac.utn.rota

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.rota.domain.model.Item
import cr.ac.utn.rota.domain.model.ItemStatus
import cr.ac.utn.rota.util.DialogUtil
import cr.ac.utn.rota.util.NoticeUtil
import cr.ac.utn.rota.viewmodel.ItemValidationException
import cr.ac.utn.rota.viewmodel.ItemViewModel

class MyItemsActivity : AppCompatActivity() {

    private val viewModel = ItemViewModel(RotaData.repository)
    private var selectedStatus: ItemStatus? = null

    private lateinit var searchInput: EditText
    private lateinit var searchBar: View
    private lateinit var itemsAdapter: MyItemAdapter
    private lateinit var emptyMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_items)

        searchInput = findViewById(R.id.inputSearchMyItems)
        searchBar = findViewById(R.id.myItemsSearchBar)
        val itemsList = findViewById<RecyclerView>(R.id.myItemsList)
        itemsAdapter = MyItemAdapter(emptyList(), ::editItem, ::confirmDelete)
        itemsList.layoutManager = LinearLayoutManager(this)
        itemsList.adapter = itemsAdapter
        emptyMessage = findViewById(R.id.myItemsEmptyMessage)

        findViewById<View>(R.id.actionBack).setOnClickListener { finish() }
        findViewById<View>(R.id.actionSearchMyItems).setOnClickListener { toggleSearch(true) }
        findViewById<View>(R.id.actionCloseSearch).setOnClickListener { toggleSearch(false) }

        setSearchListener()
        setStatusFilters()
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

    private fun setStatusFilters() {
        val filterIds = intArrayOf(R.id.itemFilters0, R.id.itemFilters1, R.id.itemFilters2)
        val statuses = arrayOf<ItemStatus?>(null, ItemStatus.AVAILABLE, ItemStatus.RESERVED)

        for (index in filterIds.indices) {
            findViewById<TextView>(filterIds[index]).setOnClickListener {
                selectedStatus = statuses[index]
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
        val items = viewModel.getMyItems(RotaData.activeUserId).filter { item ->
            (selectedStatus == null || item.status == selectedStatus) &&
                (search.isEmpty() || item.title.contains(search, true))
        }

        itemsAdapter.updateItems(items, search.isNotEmpty())
        emptyMessage.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        if (items.isEmpty()) {
            emptyMessage.setText(if (search.isEmpty() && selectedStatus == null)
                R.string.TextNoItems else R.string.TextNoResults)
        }
    }

    private fun editItem(id: String) {
        val intent = Intent(this, PublishItemActivity::class.java)
        intent.putExtra(PublishItemActivity.EXTRA_ITEM_ID, id)
        startActivity(intent)
    }

    private fun toggleSearch(open: Boolean) {
        findViewById<View>(R.id.actionBack).visibility = if (open) View.GONE else View.VISIBLE
        findViewById<View>(R.id.myItemsHeading).visibility = if (open) View.GONE else View.VISIBLE
        findViewById<View>(R.id.actionSearchMyItems).visibility = if (open) View.GONE else View.VISIBLE
        searchBar.visibility = if (open) View.VISIBLE else View.GONE

        val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (open) {
            searchInput.requestFocus()
            searchInput.post { keyboard.showSoftInput(searchInput, InputMethodManager.SHOW_IMPLICIT) }
        } else {
            searchInput.setText("")
            keyboard.hideSoftInputFromWindow(searchInput.windowToken, 0)
            searchInput.clearFocus()
        }
    }

    private fun confirmDelete(id: String) {
        DialogUtil.confirm(this, R.string.TextDeletePublicationQuestion,
            R.string.rota_delete_publication_message, R.string.TextDelete, destructive = true) {
            try {
                val item = viewModel.getMyItemById(id, RotaData.activeUserId)
                viewModel.removeItem(id, RotaData.activeUserId)
                showItems()
                NoticeUtil.show(this, R.string.rota_delete_publication_notice,
                    NoticeUtil.Type.SUCCESS, R.string.rota_undo, onAction = {
                        restoreItem(item)
                    })
            } catch (error: ItemValidationException) {
                NoticeUtil.show(this, ItemUi.errorLabel(error.error), NoticeUtil.Type.ERROR)
            } catch (error: Exception) {
                NoticeUtil.show(this, R.string.TextDeleteError, NoticeUtil.Type.ERROR)
            }
        }
    }

    private fun restoreItem(item: Item) {
        try {
            viewModel.restoreItem(item, RotaData.activeUserId)
            showItems()
            NoticeUtil.show(this, R.string.rota_publication_restored, NoticeUtil.Type.SUCCESS)
        } catch (error: ItemValidationException) {
            NoticeUtil.show(this, ItemUi.errorLabel(error.error), NoticeUtil.Type.ERROR)
        }
    }
}
