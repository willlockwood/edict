package com.willlockwood.edict.activity

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.willlockwood.edict.R
import com.willlockwood.edict.receiver.AlarmReceiver
import com.willlockwood.edict.receiver.AlarmScheduler
import com.willlockwood.edict.viewmodel.EdictVM
import com.willlockwood.edict.viewmodel.NewEdictVM
import com.willlockwood.edict.viewmodel.ToolbarVM
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var edictVM: EdictVM
    private lateinit var newEdictVM: NewEdictVM
    private lateinit var toolbarVM: ToolbarVM

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = (nav_host_fragment as NavHostFragment).navController
        val intent = intent
        when (intent.getStringExtra("destination")) {
            "review" -> navController.navigate(R.id.action_homeFragment_to_reviewPagerFragment)
            "check_in" -> navController.navigate(R.id.action_homeFragment_to_checkInPagerFragment)
        }

        setUpViewModels()
        setUpToolbar()

        observeEdictsForSessionRefresh()
    }

    private fun setUpViewModels() {
        edictVM = ViewModelProviders.of(this).get(EdictVM::class.java)
        toolbarVM = ViewModelProviders.of(this).get(ToolbarVM::class.java)
    }

    private fun observeEdictsForSessionRefresh() {
        edictVM.getAllEdicts().observe(this, Observer {
            if (it.isNotEmpty()) {
                setEdictSessionRefresh()
            }
        })
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar as Toolbar)
        toolbarVM.getToolbarTitle().observe(this, Observer {
            // TODO: figure out why there is no space between title and logo
            supportActionBar?.title = "  $it"
        })
        toolbarVM.getToolbarVisible().observe(this, Observer {
            when (it) {
                true -> supportActionBar?.show()
                false -> supportActionBar?.hide()
            }
        })
        supportActionBar?.elevation = 0f
        supportActionBar?.setLogo(R.drawable.ic_launcher_foreground)
    }

    private fun setEdictSessionRefresh() {
        val sharedPref = getSharedPreferences("com.willlockwood.edict_preferences", Context.MODE_PRIVATE)
        val minutes = sharedPref.getInt("review_time", 0)
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

}
