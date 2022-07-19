package com.example.mynaoseioqueapp.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynaoseioqueapp.R
import com.example.mynaoseioqueapp.data.remote.dto.LineOrderRequest
import com.example.mynaoseioqueapp.data.remote.dto.LineOrderResponse
import com.example.mynaoseioqueapp.data.remote.dto.OrderHistoryResponse
import com.example.mynaoseioqueapp.databinding.OrderHistoryDialogBinding
import com.example.mynaoseioqueapp.databinding.RowOrderByIdBinding
import com.example.mynaoseioqueapp.domain.model.Order
import com.example.mynaoseioqueapp.presentation.adapters.OrderByIdAdapter
import com.example.mynaoseioqueapp.presentation.main.bottom_nav.settings.SettingsFragmentViewModel
import com.example.mynaoseioqueapp.util.LineOrderSetter
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal

@AndroidEntryPoint
class OrderHistoryDialog() : DialogFragment() {

    private var binding: OrderHistoryDialogBinding? = null

    private var orderIdText = ""
    private var orderDateText = ""
    private var orderCreateDate = ""
    private var orderPrice = ""
    private var lineOrderList :List<LineOrderResponse> = ArrayList()
    private var totalPrice = BigDecimal(0)
    private var id : Long = 0
    private var authToken: String = ""

    private val settingsFragmentViewModel: SettingsFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE);
        }

        binding = OrderHistoryDialogBinding.inflate(inflater)

        binding?.apply {
            orderHistoryRecycler.layoutManager = LinearLayoutManager(context)
            orderHistoryRecycler.setHasFixedSize(true)
            orderIdTextView.text = orderIdText
            orderDateTextView.text = orderCreateDate
            priceTextView.text = orderPrice

            orderAgainButton.setOnClickListener {
                LineOrderSetter.orderAgain(lineOrderList)
                Toast.makeText(
                    context,
                    "Order added to cart.",
                    Toast.LENGTH_SHORT
                ).show()
                dialog!!.dismiss()
            }
        }


        settingsFragmentViewModel.getOrderById(authToken, id)

        lifecycleScope.launchWhenCreated {
            settingsFragmentViewModel.getOrderByIdRequest.collect { event ->
                when(event) {
                    is SettingsFragmentViewModel.GetOrderByIdEvent.Success -> {
                        lineOrderList = event.orderByIdResponse.lineOrderList
                        binding!!.orderHistoryRecycler.adapter = OrderByIdAdapter(requireContext(), event.orderByIdResponse.lineOrderList)
                        Log.d("FMSAOIFMOIGNAIOSNIFDSFIOA", event.orderByIdResponse.toString())
                    }
                    SettingsFragmentViewModel.GetOrderByIdEvent.Loading -> {

                    }
                    is SettingsFragmentViewModel.GetOrderByIdEvent.Error -> {
                        Log.d("FMSAOIFMOIGNAIOSNIFDSFIOA", event.error)
                    }
                }
            }
        }

//        binding.apply {
////            orderIdTextView.text
//        }
        return binding!!.root
    }

    companion object {
        fun orderDialogBuilder( order: OrderHistoryResponse, authToken: String ) : OrderHistoryDialog{
            val fragment = OrderHistoryDialog()
//            fragment.orderIdText = order.id.toString()
            fragment.id = order.id
            fragment.orderIdText= "Order Nº ${order.id.toString()}"
            fragment.orderCreateDate = order.createdDate.split(" ")[0]
            fragment.orderPrice= "Total price${order.price.toString()}"
            fragment.authToken = authToken
            return fragment
        }
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return activity?.let {
//            val builder = AlertDialog.Builder(it)
//            val inflater = requireActivity().layoutInflater
//
//            builder.setView(inflater.inflate(R.layout.order_history_dialog, null))
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//    }
}