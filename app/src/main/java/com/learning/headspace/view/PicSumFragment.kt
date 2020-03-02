package com.learning.headspace.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import com.learning.headspace.R
import com.learning.headspace.view.adapter.PicSumAdapter
import com.learning.headspace.viewmodel.PicSumViewModel
import kotlinx.android.synthetic.main.pic_sum_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class PicSumFragment : Fragment() {

    private lateinit var picSumViewModel: PicSumViewModel
    private var picSumAdapter: PicSumAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pic_sum_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureUI()
        picSumViewModel.fetchPicSumData(requireContext())
        observeData()
    }

    private fun configureUI() {
        progressBar.visibility  = View.VISIBLE
        picSumAdapter = PicSumAdapter(requireContext())
        picSumViewModel = ViewModelProvider(this).get(PicSumViewModel::class.java)
        picSumRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = picSumAdapter
        }
    }
    private fun observeData() {
        picSumViewModel.getPicSumList().observe(viewLifecycleOwner, Observer { picSumList ->
            picSumAdapter?.setData(picSumList)
            showStatusMessage(false)

        })
        picSumViewModel.getStatusMessage().observe(viewLifecycleOwner, Observer { message ->
            statusMessage.text = message
            showStatusMessage(true)
        })
    }
    private fun showStatusMessage(value:Boolean) {
        if(value) {
            progressBar.visibility  = View.GONE
            picSumRecyclerView.visibility = View.GONE
            statusMessage.visibility = View.VISIBLE
        }else {
            progressBar.visibility  = View.GONE
            statusMessage.visibility = View.GONE
            picSumRecyclerView.visibility = View.VISIBLE
        }
    }

}
