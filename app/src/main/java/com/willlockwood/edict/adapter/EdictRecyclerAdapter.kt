package com.willlockwood.edict.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.willlockwood.edict.BR
import com.willlockwood.edict.R
import com.willlockwood.edict.adapter.diffutil.EdictDiffCallback
import com.willlockwood.edict.data.model.Edict

//
class EdictRecyclerAdapter internal constructor(
    private val context: Context
) : RecyclerView.Adapter<EdictRecyclerAdapter.EdictViewHolder>() {
//
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var edicts = emptyList<Edict>()

    private lateinit var binding: ViewDataBinding

    inner class EdictViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(edict: Edict) {
            binding.setVariable(BR.edict, edict)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EdictViewHolder {
        binding = DataBindingUtil.inflate(inflater, R.layout.item_edict, parent, false)
        return EdictViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EdictViewHolder, position: Int) {
        val edict = edicts[position]
        holder.bindView(edict)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    internal fun setEdicts(edicts: List<Edict>) { updateEdicts(edicts) }

    private fun updateEdicts(e: List<Edict>) {
        val diffCallback = EdictDiffCallback(this.edicts, e)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        this.edicts = e
    }

    override fun getItemCount() = edicts.size
}