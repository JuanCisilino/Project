package com.frost.project_wm.ui.addedit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.frost.project_wm.R
import com.frost.project_wm.databinding.FragmentAddeditBinding
import com.frost.project_wm.model.Product
import com.frost.project_wm.ui.dialog.LoadingDialog

class AddEditFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this)[AddEditViewModel::class.java] }
    private var _binding: FragmentAddeditBinding? = null
    private val loadingDialog = LoadingDialog(R.string.loading_message)
    private val binding get() = _binding!!

    private fun getLabel(){
        requireArguments().let {
                when {
                    it.getString(getString(R.string.title_add)) == getString(R.string.title_add) -> { }
                    else -> {
                        val id = it.getString(getString(R.string.title_edit))
                        loadingDialog.show(parentFragmentManager)
                        id?.let { viewModel.findById(it.toInt()) }
                        getString(R.string.title_edit)
                    }
                }
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        getLabel()
        _binding = FragmentAddeditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.productLiveData.observe(viewLifecycleOwner, { handleProduct(it) })
    }

    private fun handleProduct(product: Product?) {
        loadingDialog.dismiss()
        product
            ?.let { setComponents(it) }
            ?:run { Toast.makeText(context, getString(R.string.error_message_product), Toast.LENGTH_SHORT).show()}
    }

    private fun setComponents(product: Product) {
        binding.editTextLabel.hint = product.title
        binding.editTextDescription.hint = product.description
        binding.editTextAvailable.hint = product.stock.toString()
        binding.editTextCost.hint = product.cost.toString()
        binding.btnPic.setOnClickListener {  }
        binding.btnAdd.setOnClickListener { validateAndSave() }
    }

    private fun validateAndSave() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}