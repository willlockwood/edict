package com.willlockwood.edict.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ToolbarVM(application: Application) : AndroidViewModel(application) {

    enum class AppLocation {
        HOME_FRAGMENT, REVIEW_FRAGMENT, CHECK_IN_FRAGMENT, PREFERENCES,
        NEW_EDICT_INTRO, NEW_EDICT_SCOPE, NEW_EDICT_SCALE, NEW_EDICT_DAYS, NEW_EDICT_TYPE,
        NEW_EDICT_TEXT, NEW_EDICT_TIME, NEW_EDICT_REMINDERS, NEW_EDICT_DEADLINE, NEW_EDICT_REVIEW
    }

    private var toolbarTitle = MutableLiveData<String>()
    private var toolbarVisible = MutableLiveData<Boolean>()
    private var currentLocation = MutableLiveData<AppLocation>()

    init {
        toolbarTitle.value = "Edict"
    }

    fun getToolbarTitle(): LiveData<String> = toolbarTitle
    fun setToolbarTitle(title: String?) {
        toolbarTitle.value = title
    }

    fun getToolbarVisible(): LiveData<Boolean> = toolbarVisible
    private fun setToolbarVisible(visible: Boolean) {
        toolbarVisible.value = visible
    }

    fun getCurrentLocation(): LiveData<AppLocation> = currentLocation
    fun setCurrentLocation(appLocation: AppLocation) {
        when (appLocation) {
            AppLocation.HOME_FRAGMENT -> {
                setToolbarTitle("Edict")
                setToolbarVisible(true)
            }
            AppLocation.REVIEW_FRAGMENT -> {
                setToolbarTitle(null)
                setToolbarVisible(false)
            }
            AppLocation.CHECK_IN_FRAGMENT -> {
                setToolbarTitle(null)
                setToolbarVisible(false)
            }
            AppLocation.PREFERENCES -> setToolbarTitle("Settings")
            AppLocation.NEW_EDICT_INTRO -> setToolbarTitle("New edict")
            AppLocation.NEW_EDICT_SCOPE -> setToolbarTitle("Days active")
            AppLocation.NEW_EDICT_DAYS -> setToolbarTitle("Days active")
            AppLocation.NEW_EDICT_TYPE -> setToolbarTitle("Rule")
            AppLocation.NEW_EDICT_SCALE -> setToolbarTitle("Rule")
            AppLocation.NEW_EDICT_TEXT -> setToolbarTitle("Rule")
            AppLocation.NEW_EDICT_TIME -> setToolbarTitle("Times active")
            AppLocation.NEW_EDICT_DEADLINE -> setToolbarTitle("Check-in window")
            AppLocation.NEW_EDICT_REMINDERS -> setToolbarTitle("Reminders")
            AppLocation.NEW_EDICT_REVIEW -> setToolbarTitle("Review and edit")
        }

        currentLocation.value = appLocation
    }
}