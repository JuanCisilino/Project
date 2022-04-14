package com.frost.project_wm.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.frost.project_wm.R
import com.frost.project_wm.databinding.FragmentProfileBinding
import com.frost.project_wm.logOut
import com.frost.project_wm.model.User
import com.frost.project_wm.ui.adapters.UsersAdapter
import com.frost.project_wm.ui.login.LoginActivity

class ProfileFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this)[ProfileViewModel::class.java] }
    private var _binding: FragmentProfileBinding? = null
    private lateinit var adapter : UsersAdapter

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
        setupRecycler()
        subscribeToLiveData()
    }

    private fun setupRecycler() {
        binding.recycler.layoutManager = GridLayoutManager(context, 3)
        binding.recycler.adapter = adapter
    }

    private fun subscribeToLiveData() {
        viewModel.userList.observe(viewLifecycleOwner, { handleLiveData(it) })
    }

    private fun handleLiveData(list: List<User>?) {
        list?.let { adapter.setList(it) }
            ?:run { Toast.makeText(context, getString(R.string.error_list), Toast.LENGTH_LONG).show()}
    }

    private fun initMembers() {
        setComponents()
        adapter = UsersAdapter()
        adapter.onUserClickCallback = { navigateToDetail(it) }
        binding.logOutBtn.setOnClickListener { logOut() }
    }

    private fun navigateToDetail(user: User) {
        val bundle = Bundle()
        bundle.putParcelable(getString(R.string.detail_user), user)
        findNavController().navigate(R.id.navigation_detail, bundle)
    }

    private fun setComponents() {
        when (viewModel.getData(getString(R.string.shared_pref_role))){
            getString(R.string.detail_admin) -> showAdminLayout()
            getString(R.string.detail_g0d) -> showGodLayout()
            else -> showUserLayout()
        }
    }

    private fun showUserLayout() {
        binding.textCompany.visibility = View.GONE
        setData()
    }

    @SuppressLint("HardwareIds")
    private fun setData() {
        binding.godLayout.visibility = View.GONE
        binding.textName.text = viewModel.getData(getString(R.string.shared_pref_name))
        binding.textEmail.text = viewModel.getData(getString(R.string.shared_pref_email))
        binding.textVersion.text = Settings.Secure.getString(activity?.contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun showGodLayout() {
        binding.userLayout.visibility = View.GONE
        binding.ivImage.setImageResource(R.drawable.horus)
        viewModel.getList()
        binding.ivImage.setOnClickListener { logOut() }
    }

    private fun showAdminLayout() {
        binding.textCompany.text = viewModel.getData(getString(R.string.shared_pref_company))
        setData()
    }

    private fun logOut() {
        clearData()
        requireActivity().logOut()
        LoginActivity.start(requireActivity())
    }

    private fun clearData(){
        viewModel.userPrefs.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}