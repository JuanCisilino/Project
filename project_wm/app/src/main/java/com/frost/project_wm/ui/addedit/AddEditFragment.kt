package com.frost.project_wm.ui.addedit

import android.content.ContentResolver
import android.content.Context
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
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.frost.project_wm.Base64Helper
import com.frost.project_wm.R
import com.frost.project_wm.databinding.FragmentAddeditBinding
import com.frost.project_wm.model.Product
import com.frost.project_wm.ui.dialog.LoadingDialog
import android.webkit.MimeTypeMap
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.item_product.view.*
import java.io.File
import java.net.URI


class AddEditFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this)[AddEditViewModel::class.java] }
    private var _binding: FragmentAddeditBinding? = null
    private val loadingDialog = LoadingDialog(R.string.loading_message)
    private val binding get() = _binding!!
    private lateinit var helper: Base64Helper
    private lateinit var storage: StorageReference

    private fun getLabel(){
        requireArguments().let {
                when {
                    it.getString(getString(R.string.title_add)) == getString(R.string.title_add) -> { setupAdd() }
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
        _binding = FragmentAddeditBinding.inflate(inflater, container, false)
        context?.let { helper = Base64Helper(it) }
        storage = FirebaseStorage.getInstance().getReference("uploads")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLabel()
        setButtons()
        subscribeToLiveData()
    }

    private fun setButtons() {
        binding.btnPic.setOnClickListener { setCaptureButton() }
        binding.btnGallery.setOnClickListener { setGalleryButton() }
        binding.btnAdd.setOnClickListener { validateAndSave() }
        binding.btnRemove.setOnClickListener { deleteProduct() }
    }

    private fun setupAdd(): String{
        binding.btnRemove.visibility = View.GONE
        return getString(R.string.btn_add_message)
    }

    private fun deleteProduct(){
        viewModel.productLiveData.value?.id?.let { viewModel.deleteProduct(it) }
        loadingDialog.show(parentFragmentManager)
    }

    private fun subscribeToLiveData() {
        viewModel.productLiveData.observe(viewLifecycleOwner, { handleProduct(it) })
        viewModel.newProductLiveData.observe(viewLifecycleOwner, { handleNewProduct(it) })
        viewModel.modifiedProductLiveData.observe(viewLifecycleOwner, { handleModifiedProduct(it) })
        viewModel.deletedProductLiveData.observe(viewLifecycleOwner, { handleDeletedProduct(it) })
    }

    private fun handleDeletedProduct(product: Product?) {
        loadingDialog.dismiss()
        product?.let { findNavController().popBackStack() }
            ?:run { Toast.makeText(context, getString(R.string.error_deleting_product), Toast.LENGTH_SHORT).show() }
    }

    private fun handleModifiedProduct(modifiedProduct: Product?) {
        loadingDialog.dismiss()
        modifiedProduct?.let { findNavController().popBackStack() }
            ?:run { Toast.makeText(context, getString(R.string.error_modifing_product), Toast.LENGTH_SHORT).show() }
    }

    private fun handleNewProduct(newProduct: Product?) {
        newProduct
            ?.let { createUrl(it) }
            ?:run {
                loadingDialog.dismiss()
                Toast.makeText(context, getString(R.string.error_save_product), Toast.LENGTH_SHORT).show()
            }
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
        binding.editTextType.hint = product.type.toString()
        binding.checkBox.isChecked = product.isActive
        binding.btnAdd.text = getString(R.string.btn_modify_message)
        product.image?.let {
            if (it.length < 800)
                glideIt(it)
            else
                view?.iv_image?.setImageBitmap(helper.decode(it))
        }
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
            ?.let { checkEditProduct(it) }
            ?:run { checkNewProduct() }
    }

    private fun checkEditProduct(product: Product) {
        val modifiedProduct = Product(
            id = product.id,
            title = getBinding(binding.editTextLabel),
            description = getBinding(binding.editTextDescription),
            stock = getBinding(binding.editTextAvailable).toInt(),
            type = getBinding(binding.editTextType),
            cost = getBinding(binding.editTextCost).toDouble(),
            image = product.image,
            isActive = binding.checkBox.isChecked,
            company = product.company
        )
        viewModel.updateProduct(modifiedProduct)
        loadingDialog.show(parentFragmentManager)
    }

    private fun getBinding(hint: AppCompatEditText): String{
        return if (hint.text.isNullOrBlank()) hint.hint.toString() else hint.text.toString()
    }

    private fun checkNewProduct() {
        when {
            binding.editTextLabel.text.isNullOrBlank() -> binding.editTextLabel.hint = getString(R.string.insert_text)
            binding.editTextDescription.text.isNullOrBlank() -> binding.editTextDescription.hint = getString(R.string.insert_text)
            binding.editTextAvailable.text.isNullOrBlank() -> binding.editTextAvailable.hint = getString(R.string.insert_text)
            binding.editTextType.text.isNullOrBlank() -> binding.editTextType.hint = getString(R.string.insert_text)
            binding.editTextCost.text.isNullOrBlank() -> binding.editTextCost.hint = getString(R.string.insert_text)
            viewModel.imageString == null -> Toast.makeText(context, getString(R.string.select_image) , Toast.LENGTH_SHORT).show()
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
            type = binding.editTextType.text.toString(),
            image = viewModel.imageString?:"",
            isActive = binding.checkBox.isChecked
        )
        viewModel.saveProduct(newProduct)
        loadingDialog.show(parentFragmentManager)
    }

    private fun createUrl(product: Product) {
        val bitmap = viewModel.imageString?.let { helper.decode(it) }
        val uri = bitmap?.let { helper.bitmapToUri(it,product.id.toString()) }
        uri?.let { uri ->
            val reference = storage.child("${product.id}")
            val uploadTask = reference.putFile(uri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) task.exception?.let { throw it }
                reference.downloadUrl
            }.addOnCompleteListener { task ->
                loadingDialog.dismiss()
                if (task.isSuccessful) {
                    product.image = task.result.toString()
                    checkEditProduct(product)
                } else {
                    Toast.makeText(context, "Ups!!\n Vuelve a intentar en un ratito.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            100 -> {
                val bitmap = data?.extras?.get("data") as Bitmap
                viewModel.imageString = helper.enconde(bitmap)
                binding.ivAddimage.setImageBitmap(bitmap) }
            120 -> {
                val bitmap = helper.uriToBitmap(data?.data!!)
                viewModel.imageString = bitmap?.let { helper.enconde(it) }
                binding.ivAddimage.setImageURI(data.data) }
        }
    }

    private fun glideIt(url: String) {
        context?.let {
            Glide.with(it)
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivAddimage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}