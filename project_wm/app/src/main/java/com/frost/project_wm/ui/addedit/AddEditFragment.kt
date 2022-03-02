package com.frost.project_wm.ui.addedit

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
                    it.getString(getString(R.string.title_add)) == getString(R.string.title_add) -> { viewModel.getList() }
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
        setButtons()
        subscribeToLiveData()
    }

    private fun setButtons() {
        binding.btnPic.setOnClickListener { setCaptureButton() }
        binding.btnGallery.setOnClickListener { setGalleryButton() }
        binding.btnAdd.setOnClickListener { validateAndSave() }
    }

    private fun subscribeToLiveData() {
        viewModel.productLiveData.observe(viewLifecycleOwner, { handleProduct(it) })
        viewModel.newProductLiveData.observe(viewLifecycleOwner, { handleNewProduct(it) })
    }

    private fun handleNewProduct(newProduct: Product?) {
        loadingDialog.dismiss()
        newProduct
            ?.let { findNavController().popBackStack() }
            ?:run { Toast.makeText(context, getString(R.string.error_save_product), Toast.LENGTH_SHORT).show()}
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
        val uri: Uri = Uri.parse(product.image)
        binding.ivAddimage.setImageURI(uri)
    }

    private fun setGalleryButton() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 120)
    }

    private fun setCaptureButton() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 100)
    }

    private fun validateAndSave() {
        viewModel.productLiveData.value
            ?.let { checkEditProduct() }
            ?:run { checkNewProduct() }
    }

    private fun checkEditProduct() {
        Toast.makeText(context, "Aun no implementado" , Toast.LENGTH_SHORT).show()
    }

    private fun checkNewProduct() {
        when {
            binding.editTextLabel.text.isNullOrBlank() -> binding.editTextLabel.hint = "Por favor ingrese texto"
            binding.editTextDescription.text.isNullOrBlank() -> binding.editTextDescription.hint = "Por favor ingrese texto"
            binding.editTextAvailable.text.isNullOrBlank() -> binding.editTextAvailable.hint = "Por favor ingrese texto"
            binding.editTextCost.text.isNullOrBlank() -> binding.editTextCost.hint = "Por favor ingrese texto"
            viewModel.imageString == null -> Toast.makeText(context, "Por favor seleccione una imagen" , Toast.LENGTH_SHORT).show()
            else -> createAndSave()
        }
    }

    private fun createAndSave() {
        val newProduct = Product(
            title = binding.editTextLabel.text.toString(),
            description = binding.editTextDescription.text.toString(),
            stock = binding.editTextAvailable.text.toString().toInt(),
            cost = binding.editTextCost.text.toString().toDouble(),
            company = getString(R.string.company_name),
            id = viewModel.productList!!.size+1,
            type = "Indefinido",
            image = viewModel.imageString?:""
        )
        viewModel.saveProduct(newProduct)
        loadingDialog.show(parentFragmentManager)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            100 -> {
                viewModel.imageString = data?.extras?.get("data") as String
                binding.ivAddimage.setImageBitmap(data.extras?.get("data") as Bitmap) }
            120 -> {
                viewModel.imageString = data?.data.toString()
                binding.ivAddimage.setImageURI(data?.data) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}