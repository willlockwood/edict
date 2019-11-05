package com.willlockwood.edict.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.willlockwood.edict.R
import com.willlockwood.edict.adapter.EdictAdapter
import com.willlockwood.edict.viewmodel.NewEdictVM
import kotlinx.android.synthetic.main.fragment_edicts_page.*

class EdictsPage : Fragment() {

    private val edictVM: NewEdictVM by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var edictAdapter: EdictAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edicts_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setUpViewModels()

        setUpRecyclerView()

        observeEdictsForRecycler()

        setUpToolbar()

        setUpButtons()
    }

//    private fun setUpViewModels() {
//        edictVM = ViewModelProviders.of(this).get(EdictVM::class.java)
//    }

    private fun setUpRecyclerView() {
        recyclerView = edict_recycler
        edictAdapter = EdictAdapter(this.context!!, edictVM)
        recyclerView.adapter = edictAdapter
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
    }

    private fun observeEdictsForRecycler() {
        edictVM.getAllEdicts().observe(viewLifecycleOwner, Observer {
            edictAdapter.setEdicts(it)
        })
    }

    private fun setUpToolbar() {
//        (requireActivity() as AppCompatActivity).supportActionBar!!.show()
    }

    private fun setUpButtons() {}
}