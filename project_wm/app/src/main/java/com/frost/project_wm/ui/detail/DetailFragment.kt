package com.frost.project_wm.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.frost.project_wm.databinding.FragmentDetailBinding
import com.frost.project_wm.ui.adapters.ProductsAdapter
import android.view.MenuItem
import android.widget.Toast
import com.frost.project_wm.R
import com.frost.project_wm.model.Product
import com.frost.project_wm.model.User

class DetailFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this)[DetailViewModel::class.java] }
    private var _binding: FragmentDetailBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let {
            viewModel.user = it.getParcelable(getString(R.string.detail_user))
            viewModel.product = it.getParcelable(getString(R.string.detail_product))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
        validate()
        subscribeToLiveData()
    }

    private fun validate() {
        viewModel.user?.let { showUserData(it) }
        viewModel.product?.let { showProductData(it) }
    }

    private fun showProductData(product: Product) {
        binding.textDetail.text = product.title
    }

    private fun subscribeToLiveData() {
        viewModel.userLiveData.observe(viewLifecycleOwner, Observer { handleUserLiveData(it) })
    }

    private fun handleUserLiveData(user: User?) {
        user?.let { showUserData(it) }
            ?:run { Toast.makeText(context, R.string.error_modify, Toast.LENGTH_SHORT).show() }
    }

    private fun showUserData(user: User) {
        binding.textDetail.text = user.rol
        binding.btn.setOnClickListener { validateAndChange() }
    }

    private fun validateAndChange() {
        val newRole = binding.editText.text?.trim()?:""
        if (newRole.isNotBlank()) viewModel.changeRole(newRole.toString())
        else Toast.makeText(context, R.string.empty_space, Toast.LENGTH_SHORT).show()
    }

    private fun initMembers() { }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}