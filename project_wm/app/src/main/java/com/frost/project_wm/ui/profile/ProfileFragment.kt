package com.frost.project_wm.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.frost.project_wm.R
import com.frost.project_wm.databinding.FragmentGodBinding
import com.frost.project_wm.databinding.FragmentProfileBinding
import com.frost.project_wm.ui.god.GodViewModel
import com.frost.project_wm.ui.home.HomeViewModel

class ProfileFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this)[ProfileViewModel::class.java] }
    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { viewModel.setUserPrefs(it) }
        initMembers()
    }

    private fun initMembers() {
        val role = viewModel.getData(getString(R.string.shared_pref_role))
        binding.textName.text = viewModel.getData(getString(R.string.shared_pref_name))
        binding.textEmail.text = viewModel.getData(getString(R.string.shared_pref_email))
        if (role == "admin") binding.textCompany.text = viewModel.getData(getString(R.string.shared_pref_company))
        else binding.textCompany.visibility = View.GONE
        binding.textVersion.text = getString(R.string.version_app)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}