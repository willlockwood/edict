package com.willlockwood.edict

import android.os.CountDownTimer
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.willlockwood.edict.data.model.Edict
import com.willlockwood.edict.data.model.EdictSession
import com.willlockwood.edict.viewmodel.EdictVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class EdictSessionVH (
    var binding: ViewDataBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bindView(edictSession: EdictSession, edictVM: EdictVM){

        var edict: Edict?

        suspend fun getEdict() {
            edict = edictVM.getEdictById(edictSession.edict)

            withContext(Dispatchers.Main) {
                binding.setVariable(BR.edict, edict)
                binding.setVariable(BR.edictSession, edictSession)
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            getEdict()
        }

        val deadlineMinutes = edictSession.deadlineMinutes - edictSession.startMinutes
        val endMinutes = edictSession.endMinutes - edictSession.startMinutes
        val startMinutes = edictSession.startMinutes - edictSession.startMinutes
        var rightNow = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60 + Calendar.getInstance().get(Calendar.MINUTE) - edictSession.startMinutes
        binding.setVariable(BR.timeMin, startMinutes)
        binding.setVariable(BR.timeMax, endMinutes)
        binding.setVariable(BR.rightNow, rightNow)
        val countDownTimer = object : CountDownTimer(60000 * (deadlineMinutes - startMinutes).toLong(), 1000.toLong()) {
            override fun onFinish() {}
            override fun onTick(p0: Long) {
                rightNow = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60 + Calendar.getInstance().get(Calendar.MINUTE) - edictSession.startMinutes
                binding.setVariable(BR.rightNow, rightNow)
            }
        }
        countDownTimer.start()
    }
}