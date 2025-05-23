package mx.edu.unpa.calificaciones

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import mx.edu.unpa.calificaciones.*

object FooterHelper {
    fun setupBottomNavigation(activity: Activity, bottomNav: BottomNavigationView) {
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_notifies -> {
                    Toast.makeText(activity, "Notificaciones", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_casa -> {
                    if (activity !is HomeActivity) {
                        activity.startActivity(Intent(activity, HomeActivity::class.java))
                        activity.finish()
                    }
                    true
                }
                R.id.action_perf -> {
                    if (activity !is ProfileActivity) {
                        activity.startActivity(Intent(activity, ProfileActivity::class.java))
                        activity.finish()
                    }
                    true
                }
                else -> false
            }
        }
    }
}
