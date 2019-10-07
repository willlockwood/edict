package com.willlockwood.edict.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.willlockwood.edict.data.model.Edict

class EdictDiffCallback(private val oldList: List<Edict>, private val newList: List<Edict>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id === newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEdict = oldList[oldItemPosition]
        val newEdict = newList[newItemPosition]

        return oldList[oldItemPosition] === newList[newItemPosition]
//        return when {
//            oldEdict.id != newTag.id -> false
//            oldTag.name != newTag.name -> true
////            oldTag.position != newTag.position -> false
//            oldTag.type != newTag.type -> false
//            else -> true
//        }
    }

}