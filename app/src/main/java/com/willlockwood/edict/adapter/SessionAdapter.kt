package com.willlockwood.edict.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.willlockwood.edict.R
import com.willlockwood.edict.adapter.diffutil.SessionDiffCallback
import com.willlockwood.edict.data.model.NewEdictSession
import com.willlockwood.edict.viewholder.SessionVH
import com.willlockwood.edict.viewmodel.SessionsVM

class SessionAdapter internal constructor(
    context: Context,
    private val sessionsVM: SessionsVM
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var sessions = emptyList<NewEdictSession>()

    private lateinit var binding: ViewDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = DataBindingUtil.inflate(inflater, R.layout.item_session, parent, false)
        return SessionVH(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as SessionVH
        vh.bindView(sessions[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    internal fun setEdicts(sessions: List<NewEdictSession>) { updateEdicts(sessions) }

    private fun updateEdicts(s: List<NewEdictSession>) {
        val diffCallback = SessionDiffCallback(this.sessions, s)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        this.sessions = s
    }

    override fun getItemCount() = sessions.size
}