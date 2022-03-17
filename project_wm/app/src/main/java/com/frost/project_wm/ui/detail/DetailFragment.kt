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
        validateAndShowLayout()
        subscribeToLiveData()
    }

    private fun validateAndShowLayout() {
        viewModel.user?.let { showUserLayout(it) }
        viewModel.product?.let { showProductLayout(it) }
    }

    private fun showProductLayout(product: Product) {
        binding.userLayout.visibility = View.GONE

        val role = UserPrefs(requireContext()).getString(getString(R.string.shared_pref_role))
        if ( role == getString(R.string.detail_admin)) setIdAndCheckBox(product)
        else hideIdAndCheckBox()

        binding.textLabel.text = product.title
        binding.textDescription.text = product.description
        binding.textAvailable.text = "Stock: ${product.stock}"
        binding.textCost.text = "$ ${product.cost}"
        product.image?.let { if (it != "") binding.ivImage.setImageBitmap(helper.decode(it)) }
    }

    private fun hideIdAndCheckBox(){
        binding.textId.visibility = View.GONE
        binding.checkBox.visibility = View.GONE
    }

    private fun setIdAndCheckBox(product: Product){
        binding.textId.text = "ID: ${product.id}"
        binding.checkBox.isChecked = product.isActive
    }

    private fun subscribeToLiveData() {
        viewModel.userLiveData.observe(viewLifecycleOwner, { handleUserLiveData(it) })
        viewModel.userDeletedLiveData.observe(viewLifecycleOwner, { handleUserDeletedLiveData(it) })
    }

    private fun handleUserDeletedLiveData(user: User?) {
        user?.let { findNavController().popBackStack() }
            ?:run { Toast.makeText(context, R.string.error_delete, Toast.LENGTH_SHORT).show() }
    }

    private fun handleUserLiveData(user: User?) {
        user?.let { showUserLayout(it) }
            ?:run { Toast.makeText(context, R.string.error_modify, Toast.LENGTH_SHORT).show() }
    }

    private fun showUserLayout(user: User) {
        binding.productLayout.visibility = View.GONE
        binding.textName.text = user.nombre
        binding.textEmail.text = user.email
        binding.textCompany.text = user.empresa
        binding.textRole.text = user.rol
        binding.btnRole.setOnClickListener { validateAndChangeUser() }
        binding.btnDelete.setOnClickListener { removeUser() }
    }

    private fun removeUser() {
        val email = binding.textEmail.text.toString()
        viewModel.removeUser(email)
    }

    private fun validateAndChangeUser() {
        val newRole = binding.editText.text?.trim()?:""
        when {
            newRole == getString(R.string.detail_user) -> viewModel.changeRole(newRole.toString())
            newRole == getString(R.string.detail_g0d) -> viewModel.changeRole(newRole.toString())
            newRole == getString(R.string.detail_admin) -> viewModel.changeRole(newRole.toString())
            newRole.isBlank() -> Toast.makeText(context, R.string.empty_space, Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(context, R.string.valid_role, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}