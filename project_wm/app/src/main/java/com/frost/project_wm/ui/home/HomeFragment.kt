package com.frost.project_wm.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.frost.project_wm.R
import com.frost.project_wm.databinding.FragmentHomeBinding
import com.frost.project_wm.model.Product
import com.frost.project_wm.ui.adapters.ProductsAdapter

class HomeFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }
    private var _binding: FragmentHomeBinding? = null
    private lateinit var adapter : ProductsAdapter

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel.getList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
        setupRecycler()
        subscribeToLiveData()
    }

    private fun setupRecycler() {
        binding.recycler.layoutManager = GridLayoutManager(context, 3)
        binding.recycler.adapter = adapter
    }

    private fun initMembers() {
        adapter = ProductsAdapter()
        adapter.onProductClickCallback = { navigateToDetail(it) }
    }

    private fun navigateToDetail(product: Product) {
        val bundle = Bundle()
        bundle.putParcelable(getString(R.string.detail_product), product)
        findNavController().navigate(R.id.navigation_detail, bundle)
    }

    private fun subscribeToLiveData() {
        viewModel.productList.observe(viewLifecycleOwner, { handleLiveData(it) })
    }

    private fun handleLiveData(list: List<Product>?) {
        list?.let { adapter.setList(it) }
            ?:run { Toast.makeText(context, getString(R.string.error_list), Toast.LENGTH_LONG).show()}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}