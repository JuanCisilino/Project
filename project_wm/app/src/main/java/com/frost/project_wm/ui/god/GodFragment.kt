package com.frost.project_wm.ui.god

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.frost.project_wm.R
import com.frost.project_wm.databinding.FragmentGodBinding
import com.frost.project_wm.databinding.FragmentHomeBinding
import com.frost.project_wm.model.Product
import com.frost.project_wm.model.User
import com.frost.project_wm.ui.adapters.ProductsAdapter
import com.frost.project_wm.ui.adapters.UsersAdapter
import com.frost.project_wm.ui.home.HomeViewModel

class GodFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this)[GodViewModel::class.java] }
    private var _binding: FragmentGodBinding? = null
    private lateinit var adapter : UsersAdapter

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentGodBinding.inflate(inflater, container, false)
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
        adapter = UsersAdapter()
        adapter.onUserClickCallback = { navigateToDetail(it) }
    }

    private fun navigateToDetail(user: User) {
        val bundle = Bundle()
        bundle.putParcelable(getString(R.string.detail_user), user)
        findNavController().navigate(R.id.navigation_detail, bundle)
    }

    private fun subscribeToLiveData() {
        viewModel.userList.observe(viewLifecycleOwner, { handleLiveData(it) })
    }

    private fun handleLiveData(list: List<User>?) {
        list?.let { adapter.setList(it) }
            ?:run { Toast.makeText(context, getString(R.string.error_list), Toast.LENGTH_LONG).show()}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}