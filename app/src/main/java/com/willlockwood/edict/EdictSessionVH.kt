package com.willlockwood.edict

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.willlockwood.edict.data.model.Edict
import com.willlockwood.edict.data.model.EdictSession
import com.willlockwood.edict.viewmodel.EdictVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    }
}