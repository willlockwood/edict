package com.willlockwood.edict

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.willlockwood.edict.data.model.EdictSession
import org.threeten.bp.format.TextStyle
import java.util.*

class EdictFragmentEdictSessionVH (
    var binding: ViewDataBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bindView(edictSession: EdictSession){
        binding.setVariable(BR.edictSession, edictSession)
        val date = edictSession.created!!
        val month = date.month.getDisplayName(TextStyle.SHORT, Locale.US)
        val day = date.dayOfMonth
        val dateString = "$month-$day"
        binding.setVariable(BR.date, dateString)
    }
}