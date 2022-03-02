package com.frost.project_wm.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.frost.project_wm.R
import com.frost.project_wm.databinding.FragmentAdminBinding
import com.frost.project_wm.model.Product

class AdminFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this)[AdminViewModel::class.java] }
    private var _binding: FragmentAdminBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBtns()
    }

    private fun initBtns() {
        binding.btnProduct.setOnClickListener { validateAndNavigate() }
        binding.btnNew.setOnClickListener { navigate(getString(R.string.title_add)) }
    }

    private fun navigate(destination: String) {
        val bundle = Bundle()
        when (destination) {
            getString(R.string.title_add) -> {
                bundle.putString(getString(R.string.title_add), destination)
                findNavController().navigate(R.id.navigation_add, bundle)
            }
            else -> {
                bundle.putString(getString(R.string.title_edit), destination)
                findNavController().navigate(R.id.navigation_add, bundle)
            }
        }
    }

    private fun validateAndNavigate() {
        if (!binding.editTextId.text.isNullOrBlank()){
            val id = binding.editTextId.text.toString()
            navigate(id)
        } else {
            Toast.makeText(context, "Por favor ingresa un numero valido de ID", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}