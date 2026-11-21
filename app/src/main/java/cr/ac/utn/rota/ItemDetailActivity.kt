package cr.ac.utn.rota

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.rota.domain.model.Item
import cr.ac.utn.rota.util.NoticeUtil
import cr.ac.utn.rota.viewmodel.ItemViewModel

class ItemDetailActivity : AppCompatActivity() {

    private val viewModel = ItemViewModel(RotaData.repository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        findViewById<View>(R.id.actionBack).setOnClickListener { finish() }
        findViewById<View>(R.id.actionRequestItem).setOnClickListener {
            NoticeUtil.show(this, R.string.TextNotAvailable)
        }
    }

    override fun onResume() {
        super.onResume()
        val id = intent.getStringExtra(PublishItemActivity.EXTRA_ITEM_ID)
        val item = id?.let { viewModel.getItemById(it) }
        if (item == null) {
            NoticeUtil.show(this, R.string.rota_item_not_found, NoticeUtil.Type.ERROR)
            findViewById<View>(R.id.actionBack).postDelayed({ finish() }, 1400)
        } else {
            showItem(item)
        }
    }

    private fun showItem(item: Item) {
        findViewById<TextView>(R.id.detailTitle).text = item.title
        findViewById<TextView>(R.id.detailStatus).text = ItemUi.statusLabel(this, item.status)
        findViewById<TextView>(R.id.detailDescription).text = item.description
        findViewById<TextView>(R.id.detailCategory).text = ItemUi.categoryLabel(this, item.category)
        findViewById<TextView>(R.id.detailCondition).text = ItemUi.conditionLabel(this, item.condition)
        findViewById<View>(R.id.detailLocation).visibility = View.GONE
        ItemUi.bindPhoto(findViewById<ImageView>(R.id.detailImage))
    }
}
