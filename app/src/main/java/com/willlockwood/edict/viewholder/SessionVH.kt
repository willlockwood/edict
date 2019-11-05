package com.willlockwood.edict.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.willlockwood.edict.BR
import com.willlockwood.edict.data.model.Edict
import com.willlockwood.edict.data.model.NewEdictSession

class SessionVH (
    var binding: ViewDataBinding
): RecyclerView.ViewHolder(binding.root) {

    private lateinit var edict: Edict
    private lateinit var sessionVH: SessionVH

//    fun bindView(edictSession: EdictSession, sessionVM: EdictVM){
    fun bindView(session: NewEdictSession){
        binding.setVariable(BR.session, session)
//        suspend fun getEdict() {
//            edict = edictVM.getEdictById(edictSession.edict)
//
//            withContext(Dispatchers.Main) {
//                edictSessionVM = EdictSessionVM(edict)
//                binding.setVariable(BR.vm, edictSessionVM)
//                binding.setVariable(BR.edict, edict)
//                binding.setVariable(BR.edictSession, edictSession)
//            }
//        }
//
//        GlobalScope.launch(Dispatchers.Main) {
//            getEdict()
//        }
//
//        val endMinutes = edictSession.endMinutes - edictSession.startMinutes
//        val startMinutes = edictSession.startMinutes - edictSession.startMinutes
//        var rightNow = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60 + Calendar.getInstance().get(
//            Calendar.MINUTE) - edictSession.startMinutes
//
//        binding.setVariable(BR.timeMin, startMinutes)
//        binding.setVariable(BR.timeMax, endMinutes)
//        binding.setVariable(BR.rightNow, rightNow)
    }
}