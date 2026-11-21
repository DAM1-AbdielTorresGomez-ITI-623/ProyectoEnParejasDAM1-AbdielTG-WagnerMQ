package cr.ac.utn.rota.util

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import cr.ac.utn.rota.R
import java.lang.ref.WeakReference

object NoticeUtil {
    enum class Type { SUCCESS, ERROR, INFO }
    private const val MESSAGE_DURATION = 3500L
    private const val UNDO_DURATION = 5000L
    private var currentPopup: PopupWindow? = null
    private var currentActivity: WeakReference<Activity>? = null
    private var currentMessage: Int? = null
    private var currentType: Type? = null

    fun show(activity: Activity, message: Int, type: Type = Type.INFO,
             actionText: Int? = null, onAction: (() -> Unit)? = null,
             hiddenView: View? = null, onDismiss: (() -> Unit)? = null,
             keepHiddenOnDismiss: Boolean = false,
             durationMillis: Long? = null) {
        if (actionText == null && currentPopup?.isShowing == true &&
            currentActivity?.get() === activity && currentMessage == message &&
            currentType == type) return

        currentPopup?.dismiss()
        val view = activity.layoutInflater.inflate(R.layout.view_notice, null)
        view.findViewById<TextView>(R.id.noticeText).apply {
            setText(message)
            setTextColor(ContextCompat.getColor(activity, when (type) {
                Type.SUCCESS -> R.color.rota_dark_green
                Type.ERROR -> R.color.rota_red
                Type.INFO -> R.color.rota_navy
            }))
        }
        val popup = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, false)
        val action = view.findViewById<TextView>(R.id.noticeAction)
        val divider = view.findViewById<View>(R.id.noticeDivider)
        val hasAction = actionText != null && onAction != null
        if (hasAction) {
            action.setText(actionText!!)
            action.visibility = View.VISIBLE
            divider.visibility = View.VISIBLE
            action.setOnClickListener {
                popup.dismiss()
                onAction?.invoke()
            }
        }
        popup.elevation = activity.resources.displayMetrics.density * 5
        popup.isTouchable = hasAction
        popup.setOnDismissListener {
            if (currentPopup === popup) {
                currentPopup = null
                currentActivity = null
                currentMessage = null
                currentType = null
            }
            if (!keepHiddenOnDismiss) hiddenView?.visibility = View.VISIBLE
            onDismiss?.invoke()
        }
        hiddenView?.visibility = View.INVISIBLE
        currentPopup = popup
        currentActivity = WeakReference(activity)
        currentMessage = message
        currentType = type
        popup.showAtLocation(activity.window.decorView, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
            0, (80 * activity.resources.displayMetrics.density).toInt())
        view.postDelayed({ if (popup.isShowing) popup.dismiss() },
            durationMillis ?: if (hasAction) UNDO_DURATION else MESSAGE_DURATION)
    }
}
