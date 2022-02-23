package com.frost.project_wm.ui.detail

import android.app.ActionBar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.frost.project_wm.R
import com.frost.project_wm.databinding.FragmentDetailBinding
import com.frost.project_wm.databinding.FragmentHomeBinding
import com.frost.project_wm.model.Product
import com.frost.project_wm.ui.adapters.ProductsAdapter
import com.frost.project_wm.ui.home.HomeViewModel

class DetailFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this)[DetailViewModel::class.java] }
    private var _binding: FragmentDetailBinding? = null
    private lateinit var adapter : ProductsAdapter

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        val textView: TextView = binding.textDetail
        viewModel.text.observe(viewLifecycleOwner, Observer { textView.text = it })
    }

    private fun initMembers() { }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}