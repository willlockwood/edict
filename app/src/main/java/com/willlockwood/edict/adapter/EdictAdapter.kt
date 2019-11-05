package com.willlockwood.edict.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.willlockwood.edict.R
import com.willlockwood.edict.adapter.diffutil.NewEdictDiffCallback
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.viewholder.NewEdictVH
import com.willlockwood.edict.viewmodel.NewEdictVM

class EdictAdapter internal constructor(
    context: Context,
    private val edictVM: NewEdictVM
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var edicts = emptyList<NewEdict>()

    private lateinit var binding: ViewDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = DataBindingUtil.inflate(inflater, R.layout.item_newedict, parent, false)
        return NewEdictVH(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as NewEdictVH
        vh.bindView(edicts[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    internal fun setEdicts(edicts: List<NewEdict>) { updateEdicts(edicts) }

    private fun updateEdicts(e: List<NewEdict>) {
        val diffCallback = NewEdictDiffCallback(this.edicts, e)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        this.edicts = e
    }

    override fun getItemCount() = edicts.size
}