package cr.ac.utn.rota

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.ContextCompat
import cr.ac.utn.rota.domain.model.Item
import cr.ac.utn.rota.domain.model.ItemCategory
import cr.ac.utn.rota.domain.model.ItemStatus
import cr.ac.utn.rota.util.DialogUtil
import cr.ac.utn.rota.util.IdUtil
import cr.ac.utn.rota.util.NoticeUtil
import cr.ac.utn.rota.viewmodel.ItemValidationException
import cr.ac.utn.rota.viewmodel.ItemViewModel

class PublishItemActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ITEM_ID = "item_id"
        private const val SUCCESS_NOTICE_DURATION = 1200L
        private const val NO_CHANGES_NOTICE_DURATION = 1800L
    }

    private val viewModel = ItemViewModel(RotaData.repository)
    private var editingItem: Item? = null
    private var selectedCategory: ItemCategory? = null
    private var selectedCondition: String? = null

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var categoryInput: EditText
    private lateinit var conditionInput: EditText
    private lateinit var agreementCheck: CheckBox
    private lateinit var publishButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_publish_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.publishRoot)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        titleInput = findViewById(R.id.inputItemTitle)
        descriptionInput = findViewById(R.id.inputItemDescription)
        categoryInput = findViewById(R.id.inputItemCategory)
        conditionInput = findViewById(R.id.inputItemCondition)
        agreementCheck = findViewById(R.id.checkAgreement)
        publishButton = findViewById(R.id.actionPublishItem)

        setListeners()
        loadEditingItem()
    }

    private fun setListeners() {
        findViewById<View>(R.id.actionBack).setOnClickListener { finish() }
        findViewById<View>(R.id.actionTakePhoto).setOnClickListener { notAvailable() }
        findViewById<View>(R.id.actionGallery).setOnClickListener { notAvailable() }
        findViewById<EditText>(R.id.inputItemArea).setOnClickListener { selectArea() }
        categoryInput.setOnClickListener { selectCategory() }
        conditionInput.setOnClickListener { selectCondition() }
        publishButton.setOnClickListener { saveItem() }

        findViewById<TextView>(R.id.photoCount).text = getString(R.string.rota_photos_counter, 0)
        findViewById<TextView>(R.id.descriptionCount).text = getString(R.string.rota_description_counter, 0)
        descriptionInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                findViewById<TextView>(R.id.descriptionCount).text =
                    getString(R.string.rota_description_counter, s?.length ?: 0)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        watchField(titleInput, R.id.errorItemTitle)
        watchField(descriptionInput, R.id.errorDescription)
        agreementCheck.setOnCheckedChangeListener { _, checked ->
            if (checked) findViewById<TextView>(R.id.errorAgreement).visibility = View.GONE
        }
    }

    private fun loadEditingItem() {
        val id = intent.getStringExtra(EXTRA_ITEM_ID) ?: return
        try {
            val item = viewModel.getMyItemById(id, RotaData.activeUserId)
            editingItem = item
            findViewById<TextView>(R.id.headerTitle).setText(R.string.TextEditPublication)
            publishButton.setText(R.string.TextSaveChanges)
            titleInput.setText(item.title)
            descriptionInput.setText(item.description)
            selectedCategory = item.category
            categoryInput.setText(ItemUi.categoryLabel(this, item.category))
            selectedCondition = item.condition
            conditionInput.setText(ItemUi.conditionLabel(this, item.condition))
            agreementCheck.visibility = View.GONE
            findViewById<TextView>(R.id.errorAgreement).visibility = View.GONE
        } catch (error: ItemValidationException) {
            showNotice(ItemUi.errorLabel(error.error), NoticeUtil.Type.ERROR, true)
        }
    }

    private fun selectCategory() {
        val categories = ItemCategory.values()
        val names = categories.map { ItemUi.categoryLabel(this, it) }.toTypedArray()
        showSelectionDialog(R.string.TextSelectCategory, names, categories.indexOf(selectedCategory)) { index ->
            selectedCategory = categories[index]
            categoryInput.setText(names[index])
            clearFieldError(categoryInput, R.id.errorCategory)
        }
    }

    private fun selectCondition() {
        val values = arrayOf("NEW", "USED", "GOOD")
        val labels = intArrayOf(R.string.TextNewCondition, R.string.TextUsedCondition, R.string.TextGoodCondition)
        val names = labels.map { getString(it) }.toTypedArray()
        showSelectionDialog(R.string.TextSelectCondition, names, values.indexOf(selectedCondition)) { index ->
            selectedCondition = values[index]
            conditionInput.setText(names[index])
            clearFieldError(conditionInput, R.id.errorCondition)
        }
    }

    private fun selectArea() {
        showSelectionDialog(R.string.TextSelectArea, emptyArray(), -1) { }
    }

    private fun showSelectionDialog(title: Int, names: Array<String>, selected: Int,
                                    onSelect: (Int) -> Unit) {
        val view = layoutInflater.inflate(R.layout.dialog_selection, null)
        view.findViewById<TextView>(R.id.selectionTitle).setText(title)
        val container = view.findViewById<LinearLayout>(R.id.selectionOptions)
        val dialog = AlertDialog.Builder(this).setView(view).create()
        val density = resources.displayMetrics.density
        if (names.isEmpty()) container.minimumHeight = (150 * density).toInt()

        for (index in names.indices) {
            val option = TextView(this)
            option.text = names[index]
            option.textSize = 14f
            option.gravity = Gravity.CENTER_VERTICAL
            option.setTextColor(ContextCompat.getColor(this,
                if (index == selected) R.color.rota_green else R.color.rota_navy))
            option.setPadding((14 * density).toInt(), 0, (14 * density).toInt(), 0)
            option.layoutParams = LinearLayout.LayoutParams(-1, (50 * density).toInt())
            if (index == selected) option.setBackgroundResource(R.drawable.bg_selection_selected)
            option.setOnClickListener {
                onSelect(index)
                dialog.dismiss()
            }
            container.addView(option)
        }
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout((resources.displayMetrics.widthPixels * 0.86).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT)
    }

    private fun saveItem() {
        if (!isValidData()) return
        val previous = editingItem
        val item = Item(
            previous?.id ?: IdUtil.generateId(),
            RotaData.activeUserId,
            titleInput.text.toString().trim(),
            descriptionInput.text.toString().trim(),
            selectedCategory!!,
            previous?.status ?: ItemStatus.AVAILABLE,
            selectedCondition!!,
            previous?.generalLocation ?: "",
            previous?.imageUris ?: emptyList()
        )
        if (previous == null) {
            persistItem(item, false)
        } else {
            if (item == previous) {
                showNotice(R.string.rota_no_changes_made,
                    durationMillis = NO_CHANGES_NOTICE_DURATION)
                return
            }
            DialogUtil.confirm(this, R.string.rota_save_changes_question,
                R.string.rota_save_changes_message, R.string.TextSaveChanges) {
                persistItem(item, true)
            }
        }
    }

    private fun isValidData(): Boolean {
        if (titleInput.text.toString().trim().isEmpty()) {
            showFieldError(titleInput, R.id.errorItemTitle)
            return false
        }
        if (selectedCategory == null) {
            showFieldError(categoryInput, R.id.errorCategory)
            return false
        }
        if (selectedCondition.isNullOrBlank()) {
            showFieldError(conditionInput, R.id.errorCondition)
            return false
        }
        if (descriptionInput.text.toString().trim().isEmpty()) {
            showFieldError(descriptionInput, R.id.errorDescription)
            return false
        }
        if (editingItem == null && !agreementCheck.isChecked) {
            findViewById<TextView>(R.id.errorAgreement).apply {
                setText(R.string.rota_invalid_agreement)
                visibility = View.VISIBLE
            }
            return false
        }
        return true
    }

    private fun persistItem(item: Item, edit: Boolean) {
        try {
            if (edit) viewModel.updateItem(item, RotaData.activeUserId)
            else viewModel.createItem(item, RotaData.activeUserId)

            publishButton.isEnabled = false
            showNotice(if (edit) R.string.TextPublicationUpdated
                else R.string.TextPublicationCreated, NoticeUtil.Type.SUCCESS, true)
        } catch (error: ItemValidationException) {
            showNotice(ItemUi.errorLabel(error.error), NoticeUtil.Type.ERROR)
        } catch (error: Exception) {
            showNotice(R.string.TextSaveError, NoticeUtil.Type.ERROR)
        }
    }

    private fun showFieldError(input: EditText, errorId: Int) {
        input.setBackgroundResource(R.drawable.bg_rota_field_error)
        findViewById<TextView>(errorId).apply {
            setText(R.string.TextRequiredField)
            visibility = View.VISIBLE
        }
    }

    private fun clearFieldError(input: EditText, errorId: Int) {
        input.setBackgroundResource(R.drawable.bg_field)
        findViewById<TextView>(errorId).visibility = View.GONE
    }

    private fun watchField(input: EditText, errorId: Int) {
        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearFieldError(input, errorId)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showNotice(message: Int, type: NoticeUtil.Type = NoticeUtil.Type.INFO,
                           closeAfter: Boolean = false, durationMillis: Long? = null) {
        NoticeUtil.show(this, message, type,
            hiddenView = publishButton,
            onDismiss = if (closeAfter) ({ finish() }) else null,
            keepHiddenOnDismiss = closeAfter,
            durationMillis = durationMillis ?: if (closeAfter) SUCCESS_NOTICE_DURATION else null)
    }

    private fun notAvailable() {
        showNotice(R.string.TextNotAvailable)
    }
}
