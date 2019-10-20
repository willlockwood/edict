package com.willlockwood.edict.viewmodel.binding

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.willlockwood.edict.data.model.NewEdict

class NewEdictNotificationsBVM(
    private var newEdict: NewEdict
) : BaseObservable() {

    @Bindable
    fun getHasEdictStartNotification(): Boolean { return newEdict.hasNotification(NewEdict.NotificationType.START) }
    fun setHasEdictStartNotification(value: Boolean) {
        when (value) {
            true -> newEdict.addToNotifications(NewEdict.NotificationType.START, null)
            false -> newEdict.removeNotification(NewEdict.NotificationType.START)
        }
        notifyPropertyChanged(BR.hasEdictStartNotification)
    }

    @Bindable
    fun getHasEdictEndNotification(): Boolean { return newEdict.hasNotification(NewEdict.NotificationType.END) }
    fun setHasEdictEndNotification(value: Boolean) {
        when (value) {
            true -> newEdict.addToNotifications(NewEdict.NotificationType.END, null)
            false -> newEdict.removeNotification(NewEdict.NotificationType.END)
        }
        notifyPropertyChanged(BR.hasEdictEndNotification)
    }

    @Bindable
    fun getHasCheckInStartNotification(): Boolean { return newEdict.hasNotification(NewEdict.NotificationType.CHECK_IN_START) }
    fun setHasCheckInStartNotification(value: Boolean) {
        when (value) {
            true -> newEdict.addToNotifications(NewEdict.NotificationType.CHECK_IN_START, null)
            false -> newEdict.removeNotification(NewEdict.NotificationType.CHECK_IN_START)
        }
        notifyPropertyChanged(BR.hasCheckInStartNotification)
    }

    @Bindable
    fun getHasCheckInEndNotification(): Boolean { return newEdict.hasNotification(NewEdict.NotificationType.CHECK_IN_BEFORE_END) }
    fun setHasCheckInEndNotification(value: Boolean) {
        when (value) {
            true -> newEdict.addToNotifications(NewEdict.NotificationType.CHECK_IN_BEFORE_END, checkInEndVar)
            false -> newEdict.removeNotification(NewEdict.NotificationType.CHECK_IN_BEFORE_END)
        }
        notifyPropertyChanged(BR.hasCheckInEndNotification)
    }

    var checkInEndVar = when (newEdict.hasNotification(NewEdict.NotificationType.CHECK_IN_BEFORE_END)) {
        true -> newEdict.getNotification(NewEdict.NotificationType.CHECK_IN_BEFORE_END)!!.second
        false -> 15
    }
    @Bindable
    fun getCheckInEndNotificationVar(): String { return "$checkInEndVar min" }
    fun setCheckInEndNotificationVar(value: Int) {
        if (value != checkInEndVar) {
            checkInEndVar = value
            if (newEdict.hasNotification(NewEdict.NotificationType.CHECK_IN_BEFORE_END)) {
                setHasCheckInEndNotification(true)
            }
            notifyPropertyChanged(BR.checkInEndNotificationVar)
        }
    }

    fun getNewEdict(): NewEdict { return this.newEdict }
}