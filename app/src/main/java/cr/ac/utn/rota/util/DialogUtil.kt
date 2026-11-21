package cr.ac.utn.rota.util

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import cr.ac.utn.rota.R

object DialogUtil {
    fun confirm(activity: Activity, title: Int, message: Int, action: Int,
                destructive: Boolean = false, onConfirm: () -> Unit) {
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.dialog_confirmation)
        dialog.findViewById<TextView>(R.id.dialogTitle).setText(title)
        dialog.findViewById<TextView>(R.id.dialogMessage).setText(message)
        dialog.findViewById<TextView>(R.id.dialogConfirm).apply {
            setText(action)
            setTextColor(ContextCompat.getColor(activity,
                if (destructive) R.color.rota_red else R.color.rota_green))
            setOnClickListener {
                dialog.dismiss()
                onConfirm()
            }
        }
        dialog.findViewById<android.view.View>(R.id.dialogClose).setOnClickListener { dialog.dismiss() }
        dialog.show()
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setGravity(Gravity.BOTTOM)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setDimAmount(0.42f)
        }
    }
}
