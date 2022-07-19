package com.example.mynaoseioqueapp.presentation.main.bottom_nav.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynaoseioqueapp.common.Constant
import com.example.mynaoseioqueapp.data.remote.dto.OrderHistoryResponse
import com.example.mynaoseioqueapp.databinding.FragmentSettingsBinding
import com.example.mynaoseioqueapp.presentation.adapters.OrderHistoryAdapter
import com.example.mynaoseioqueapp.presentation.dialog.OrderHistoryDialog
import com.example.mynaoseioqueapp.presentation.login.LoginActivity
import com.example.mynaoseioqueapp.presentation.main.bottom_nav.BottomNavActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SettingsFragment : Fragment(), OrderHistoryAdapter.OnClickOrderEvent {

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    private val settingsFragmentViewModel: SettingsFragmentViewModel by viewModels()

    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.historyRecyclerView.setHasFixedSize(true)
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        sharedPref = requireActivity().getSharedPreferences(
            Constant.APP_PREF, Context.MODE_PRIVATE
        )

        if(readDataFromSharedPref(Constant.ACCESS_TOKEN, sharedPref) == "Token Not Found"){
            Log.d("MyLoggggggErrorrrrrrr", "Token not found")
        }else{
            settingsFragmentViewModel.getOrderHistory(readDataFromSharedPref(Constant.ACCESS_TOKEN, sharedPref)!!)
            settingsFragmentViewModel.getUserByToken(readDataFromSharedPref(Constant.ACCESS_TOKEN, sharedPref)!!)
        }

        binding.logOutButton.setOnClickListener {
            sharedPref.edit().remove(Constant.ACCESS_TOKEN)
            startActivity(
                Intent(
                    requireContext(), LoginActivity::class.java
                )
            )
            activity!!.finish()
        }

        lifecycleScope.launchWhenCreated {
            settingsFragmentViewModel.orderHistoryRequest.collect { event ->
                when(event) {
                    is SettingsFragmentViewModel.OrderHistoryEvent.Success -> {
                        binding.historyRecyclerView.adapter = OrderHistoryAdapter(requireContext(), this@SettingsFragment,event.orderHistoryResponse.reversed())
                    }
                    SettingsFragmentViewModel.OrderHistoryEvent.Loading -> {

                    }
                    is SettingsFragmentViewModel.OrderHistoryEvent.Error -> {
                        Toast.makeText(requireContext(),
                            "Could not load history" + event.error,
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            settingsFragmentViewModel.getUserRequest.collect { event ->
                when(event) {
                    is SettingsFragmentViewModel.GetUserEvent.Success -> {
                        binding.nameTextView.text = event.userResponse.name
                        binding.usernameTextView.text = event.userResponse.username
                    }
                    SettingsFragmentViewModel.GetUserEvent.Loading -> {

                    }
                    is SettingsFragmentViewModel.GetUserEvent.Error -> {
                        Toast.makeText(requireContext(),
                            "Could not load user" + event.error,
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun readDataFromSharedPref(key: String, sharedPref: SharedPreferences): String?{
        return sharedPref.getString(key, "Token Not Found")
    }

    override fun onClickOrderEvent(order: OrderHistoryResponse) {
        val dialog = OrderHistoryDialog.orderDialogBuilder(order, readDataFromSharedPref(Constant.ACCESS_TOKEN, sharedPref)!!).show(getActivity()!!.supportFragmentManager, "OrderDialog")
    }
}