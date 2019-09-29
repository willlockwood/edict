package com.willlockwood.edict.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ToolbarVM(application: Application) : AndroidViewModel(application) {

    enum class AppLocation {
        HOME_FRAGMENT, REVIEW_FRAGMENT, CHECK_IN_FRAGMENT, PREFERENCES
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
                setToolbarTitle("home")
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
        }

        currentLocation.value = appLocation
    }
}