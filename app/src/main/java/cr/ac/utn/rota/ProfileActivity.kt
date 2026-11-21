package cr.ac.utn.rota

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.rota.util.NoticeUtil

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        findViewById<View>(R.id.actionMyItems).setOnClickListener {
            startActivity(Intent(this, MyItemsActivity::class.java))
        }
        findViewById<View>(R.id.actionChangePhoto).setOnClickListener { notAvailable() }
        findViewById<View>(R.id.actionLogout).setOnClickListener { notAvailable() }
        findViewById<View>(R.id.navExplore).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
        findViewById<View>(R.id.navPublish).setOnClickListener {
            startActivity(Intent(this, PublishItemActivity::class.java))
        }
        findViewById<View>(R.id.navMessages).setOnClickListener { notAvailable() }
        findViewById<View>(R.id.navRequests).setOnClickListener { notAvailable() }
    }

    private fun notAvailable() {
        NoticeUtil.show(this, R.string.TextNotAvailable)
    }
}
