package com.willlockwood.edict.data.model

import androidx.databinding.BaseObservable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.willlockwood.edict.data.converter.TimeConverters
import org.threeten.bp.OffsetDateTime

@Entity(
    tableName="edict_sessions",
//            indices = [androidx.room.Index(value = ["tag"], name = "tag")],
    foreignKeys = [
        ForeignKey(
            entity = Edict::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("edict"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE)
    ]
)
data class EdictSession(
    var edict: Int,
    var success: Boolean? = null,
    var checked: Boolean = false,

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

    fun resolveSession(success: Boolean?) {
        this.success = success
        checked = true
        resolved = OffsetDateTime.now()
    }

}