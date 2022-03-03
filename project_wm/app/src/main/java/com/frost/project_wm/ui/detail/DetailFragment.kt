package com.frost.project_wm.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.frost.project_wm.databinding.FragmentDetailBinding
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.frost.project_wm.Base64Helper
import com.frost.project_wm.R
import com.frost.project_wm.UserPrefs
import com.frost.project_wm.model.Product
import com.frost.project_wm.model.User

class DetailFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this)[DetailViewModel::class.java] }
    private var _binding: FragmentDetailBinding? = null
    private lateinit var helper: Base64Helper
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
        context?.let { helper = Base64Helper(it) }
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
        binding.userLayout.visibility = View.GONE

        val role = UserPrefs(requireContext()).getString(getString(R.string.shared_pref_role))
        if ( role == "admin") binding.textLabel.text = "ID: ${product.id} ${product.title}"
        else binding.textLabel.text = product.title

        binding.textDescription.text = product.description
        binding.textAvailable.text = "Stock: ${product.stock}"
        binding.textCost.text = "$ ${product.cost}"
        binding.ivImage.setImageBitmap(helper.decode(product.image))
    }

    private fun subscribeToLiveData() {
        viewModel.userLiveData.observe(viewLifecycleOwner, Observer { handleUserLiveData(it) })
    }

    private fun handleUserLiveData(user: User?) {
        user?.let { showUserData(it) }
            ?:run { Toast.makeText(context, R.string.error_modify, Toast.LENGTH_SHORT).show() }
    }

    private fun showUserData(user: User) {
        binding.productLayout.visibility = View.GONE
        binding.textName.text = user.nombre
        binding.textEmail.text = user.email
        binding.textCompany.text = user.empresa
        binding.textRole.text = user.rol
        binding.btn.setOnClickListener { validateAndChangeUser() }
    }

    private fun validateAndChangeUser() {
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