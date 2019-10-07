package com.willlockwood.edict.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.willlockwood.edict.R
import com.willlockwood.edict.adapter.diffutil.EdictSessionDiffCallback
import com.willlockwood.edict.data.model.EdictSession
import com.willlockwood.edict.viewholder.EdictSessionVH
import com.willlockwood.edict.viewmodel.EdictVM

//
class EdictSessionAdapter internal constructor(
    context: Context,
    private val edictVM: EdictVM
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var sessions = emptyList<EdictSession>()

    private lateinit var binding: ViewDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = DataBindingUtil.inflate(inflater, R.layout.item_edict_session, parent, false)
        return EdictSessionVH(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as EdictSessionVH
        vh.bindView(sessions[position], edictVM)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    internal fun setEdicts(edictSessions: List<EdictSession>) { updateEdicts(edictSessions) }

    private fun updateEdicts(s: List<EdictSession>) {
        val diffCallback = EdictSessionDiffCallback(this.sessions, s)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        this.sessions = s
    }

    override fun getItemCount() = sessions.size
}