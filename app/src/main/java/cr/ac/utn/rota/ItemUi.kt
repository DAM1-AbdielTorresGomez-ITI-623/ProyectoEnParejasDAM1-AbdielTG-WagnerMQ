package cr.ac.utn.rota

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import cr.ac.utn.rota.domain.model.ItemCategory
import cr.ac.utn.rota.domain.model.ItemStatus
import cr.ac.utn.rota.viewmodel.ItemError

object ItemUi {
    fun categoryLabel(context: Context, category: ItemCategory): String {
        val textId = when (category) {
            ItemCategory.CLOTHING -> R.string.TextClothing
            ItemCategory.ELECTRONICS -> R.string.TextElectronics
            ItemCategory.FURNITURE -> R.string.TextFurniture
            ItemCategory.HOME -> R.string.TextHome
            ItemCategory.SPORTS -> R.string.TextSports
            ItemCategory.BABY -> R.string.TextBaby
            ItemCategory.SPARE_PARTS -> R.string.TextSpareParts
            ItemCategory.OTHER -> R.string.TextOther
        }
        return context.getString(textId)
    }

    fun statusLabel(context: Context, status: ItemStatus): String {
        val textId = when (status) {
            ItemStatus.AVAILABLE -> R.string.TextAvailable
            ItemStatus.RESERVED -> R.string.TextReserved
            ItemStatus.DONATED -> R.string.TextDonated
        }
        return context.getString(textId)
    }

    fun conditionLabel(context: Context, condition: String): String {
        val textId = when (condition) {
            "NEW" -> R.string.TextNewCondition
            "USED" -> R.string.TextUsedCondition
            "GOOD" -> R.string.TextGoodCondition
            else -> return condition
        }
        return context.getString(textId)
    }

    fun errorLabel(error: ItemError): Int {
        return when (error) {
            ItemError.DUPLICATE -> R.string.TextDuplicateData
            ItemError.NOT_FOUND -> R.string.rota_item_not_found
            ItemError.NOT_OWNER -> R.string.rota_not_owner
            ItemError.INVALID_DATA -> R.string.TextCompleteRequiredFields
            ItemError.INVALID_STATUS -> R.string.rota_invalid_item_status
        }
    }

    fun bindStatus(textView: TextView, status: ItemStatus) {
        textView.text = statusLabel(textView.context, status)
        textView.setTextColor(colorStatus(textView.context, status))
        textView.setBackgroundResource(if (status == ItemStatus.AVAILABLE)
            R.drawable.bg_pale_green else R.drawable.bg_pale_yellow)
    }

    fun bindLocation(textView: TextView, location: String) {
        textView.text = location
        (textView.parent as View).visibility = if (location.isBlank()) View.GONE else View.VISIBLE
    }

    fun bindPhoto(image: ImageView) {
        image.setImageResource(R.drawable.ic_rota_box)
        image.scaleType = ImageView.ScaleType.CENTER_INSIDE
    }

    fun colorStatus(context: Context, status: ItemStatus): Int {
        val colorId = if (status == ItemStatus.AVAILABLE) R.color.rota_dark_green else R.color.rota_gray
        return ContextCompat.getColor(context, colorId)
    }
}
