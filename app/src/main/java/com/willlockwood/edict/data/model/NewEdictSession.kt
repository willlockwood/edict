package com.willlockwood.edict.data.model

import androidx.databinding.BaseObservable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.willlockwood.edict.data.converter.EdictTypeConverters
import com.willlockwood.edict.data.converter.TimeConverters
import com.willlockwood.edict.utils.TimeHelper
import org.threeten.bp.OffsetDateTime

@Entity(
    tableName="new_edict_sessions",
    foreignKeys = [
        ForeignKey(
            entity = NewEdict::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("edict"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE)
    ]
)
data class NewEdictSession(
    var edict: Int,
    var edictText: String,
    var success: Boolean? = null,
    var checked: Boolean = false,

    @TypeConverters(EdictTypeConverters::class)
    var notifications: List<Pair<NewEdict.NotificationType, OffsetDateTime?>>,

//    @TypeConverters(TimeConverters::class)
//    var activeStart: OffsetDateTime? = null,
//    @TypeConverters(TimeConverters::class)
//    var activeEnd: OffsetDateTime? = null,
//    @TypeConverters(TimeConverters::class)
//    var checkInStart: OffsetDateTime? = null,
//    @TypeConverters(TimeConverters::class)
//    var checkInEnd: OffsetDateTime? = null,

    @TypeConverters(TimeConverters::class)
    var activeWindow: Pair<OffsetDateTime?, OffsetDateTime?> = Pair(null, null),
    @TypeConverters(TimeConverters::class)
    var checkInWindow: Pair<OffsetDateTime?, OffsetDateTime?> = Pair(null, null),
    @TypeConverters(TimeConverters::class)
    var created: OffsetDateTime? = null,
    @TypeConverters(TimeConverters::class)
    var resolved: OffsetDateTime? = null

) : BaseObservable() {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    init {
        created = OffsetDateTime.now()
    }

    companion object {
        fun newInstance(edict: NewEdict): NewEdictSession {
            return NewEdictSession(
                edict = edict.id,
                edictText = edict.toString(),
                notifications = edict.notifications.map { Pair(it.first, edict.getMinuteFromNotifType(it)) },
                activeWindow = Pair(TimeHelper.getTimeFromMinutes(edict.timeStart!!), TimeHelper.getTimeFromMinutes(edict.timeEnd!!)),
                checkInWindow = Pair(TimeHelper.getTimeFromMinutes(edict.checkInStart!!), TimeHelper.getTimeFromMinutes(edict.checkInEnd!!)),
                created = OffsetDateTime.now()
            )
        }
    }

    fun resolveSession(success: Boolean?) {
        this.success = success
        checked = true
        resolved = OffsetDateTime.now()
    }

}