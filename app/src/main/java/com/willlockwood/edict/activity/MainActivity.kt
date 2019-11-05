package com.willlockwood.edict.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.willlockwood.edict.R
import com.willlockwood.edict.data.model.EdictSession
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.receiver.AlarmReceiver
import com.willlockwood.edict.receiver.AlarmScheduler
import com.willlockwood.edict.viewmodel.EdictVM
import com.willlockwood.edict.viewmodel.NewEdictNewVM
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(),
        FloatingActionsMenu.OnFloatingActionsMenuUpdateListener
{
    // ViewModels
    private val edictVM: EdictVM by viewModels()
    private val viewmodel: NewEdictNewVM by viewModels()

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = (nav_host_fragment as NavHostFragment).navController
        val intent = intent
        when (intent.getStringExtra("destination")) {
            "review" -> navController.navigate(R.id.action_homeFragment_to_reviewPagerFragment)
            "check_in" -> navController.navigate(R.id.action_homeFragment_to_checkInPagerFragment)
        }

        setUpFABMenu()

        setUpSharedPreferences()

        setUpToolbar()

        observeEdictsAndSessions()

        hideKeyboard(toolbar as View)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun observeEdictsAndSessions() {
        includeForTesting()
        edictVM.getAllEdicts().observe(this, Observer {
            if (it.isNotEmpty()) {
                setEdictSessionRefresh() // when there's a change in edicts, make sure that the edict sessions are refreshed
            }
        })
        edictVM.getActiveEdictSessions().observe(this, Observer {
            if (it.isNotEmpty()) {
                rescheduleAllNotificationsFromEdictSessions(it)
            }
        })
//        viewmodel.getAllEdicts().observe(this, Observer {
//            if (it.isNotEmpty()) {
//                val blah = it
//                Log.i("MainActivity", blah.toString())
//            }
//        })
//        viewmodel.getAllSessions().observe(this, Observer {
//            val blah = it
//            val blah2 = blah
//        })
//        viewmodel.getUncheckedSessions().observe(this, Observer {
//            val uncheckedSessions = it
//            val blah = uncheckedSessions
//        })
//        viewmodel.getUnresolvedSessions().observe(this, Observer {
//            if (it != null) {
//                val blah = it
//            }
//            val blah = it
////            Log.i("mainActivity", it.toString())
//        })
    }

    private fun setUpSharedPreferences() {
        sharedPreferences = getSharedPreferences("com.willlockwood.edict_preferences", Context.MODE_PRIVATE)
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferencesChangeListener)
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar!!.setShowHideAnimationEnabled(true)
        supportActionBar!!.show()
    }

    override fun onStart() {
        super.onStart()
        supportActionBar!!.show()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun rescheduleAllNotificationsFromEdictSessions(edictSessions: List<EdictSession>) {
        var allExtraNotificationsToday = emptyArray<Triple<EdictSession, Int, String>>()
        for (es in edictSessions) {
            val esNotifMap = es.notificationMinutes
            val mapEntries = esNotifMap.entries
            if (mapEntries.isNotEmpty()) {
                for (e in mapEntries) {
                    allExtraNotificationsToday = allExtraNotificationsToday.plus(Triple(es, e.value, e.key))
                }
            }
        }
        allExtraNotificationsToday.sortBy { it.second }
        val rightNow = Calendar.getInstance()
        val minutes = rightNow.get(Calendar.HOUR_OF_DAY) * 60 + rightNow.get(Calendar.MINUTE)
        allExtraNotificationsToday = allExtraNotificationsToday.filter { it.second > minutes }.toTypedArray()
        val groupedExtraNotifications = allExtraNotificationsToday.groupBy { it.second }
        AlarmScheduler.scheduleAllExtraAlarms(this, AlarmReceiver::class.java, groupedExtraNotifications)
    }

    private fun setEdictSessionRefresh() {
        val minutes = sharedPreferences.getInt("review_time", -1)
        AlarmScheduler.scheduleAlarm(this, AlarmReceiver::class.java, minutes, AlarmScheduler.AlarmType.REFRESH_SESSIONS)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> navController.navigate(R.id.open_settings_fragment)
        }
        return true
    }

    private val onSharedPreferencesChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPrefs, key ->
        when {
            listOf("morning_deadline", "midday_deadline", "evening_deadline").contains(key) -> {
                val minutes = sharedPrefs.getInt(key, -1)
                if (minutes >= 0) {
                    when (key) {
                        "morning_deadline" -> edictVM.updateDeadlineForEdicts("morning", minutes)
                        "midday_deadline" -> edictVM.updateDeadlineForEdicts("mid-day", minutes)
                        "evening_deadline" -> edictVM.updateDeadlineForEdicts("evening", minutes)
                    }
                }
            }
        }
    }

    private fun includeForTesting() {}

    // SET UP FAB MENU AND FAB ACTIONS

    enum class FabAction { CLOSE, OPEN, UNHIDE, CLOSE_HIDE, CREATE_ROUTINE, CREATE_RESTRICTION }

    private fun setUpFABMenu() {
        scrim.setBackgroundColor(resources.getColor(R.color.fab_menu_scrim))

        multiple_actions.setOnFloatingActionsMenuUpdateListener(this as FloatingActionsMenu.OnFloatingActionsMenuUpdateListener)

        doFabAction(FabAction.UNHIDE)

        routine_fab.size = FloatingActionButton.SIZE_MINI
        restriction_fab.size = FloatingActionButton.SIZE_MINI

        routine_fab.setOnClickListener {        doFabAction(FabAction.CREATE_ROUTINE) }
        restriction_fab.setOnClickListener {    doFabAction(FabAction.CREATE_RESTRICTION) }
        scrim.setOnClickListener {              doFabAction(FabAction.CLOSE) }
    }

    fun doFabAction(action: FabAction) {
        val menu = multiple_actions as FloatingActionsMenu
        when (action) {
            FabAction.CLOSE ->  menu.collapse()
            FabAction.OPEN ->   menu.expand()
            FabAction.UNHIDE -> menu.visibility = View.VISIBLE
            FabAction.CLOSE_HIDE -> {
                menu.collapse()
                menu.visibility = View.GONE
            }
            FabAction.CREATE_RESTRICTION -> {
                val extras = bundleOf("type" to NewEdict.Type.RESTRICTION)
                supportActionBar!!.setShowHideAnimationEnabled(true)
                supportActionBar!!.hide()
                navController.navigate(R.id.action_homeFragment_to_createEdict, extras)

            }
            FabAction.CREATE_ROUTINE -> {
                val extras = bundleOf("type" to NewEdict.Type.ROUTINE)
                supportActionBar!!.setShowHideAnimationEnabled(true)
                supportActionBar!!.hide()
                navController.navigate(R.id.action_homeFragment_to_createEdict, extras)
            }
        }
    }

    override fun onMenuCollapsed() {    scrim.visibility = View.GONE }
    override fun onMenuExpanded() {     scrim.visibility = View.VISIBLE }

    fun setStatusBarColor(colorId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(colorId)
        }
    }

    fun toggleKeyboard(view: View) {
        view.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
}


