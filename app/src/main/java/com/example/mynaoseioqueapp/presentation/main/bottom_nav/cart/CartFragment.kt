package com.example.mynaoseioqueapp.presentation.main.bottom_nav.cart

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynaoseioqueapp.common.Constant
import com.example.mynaoseioqueapp.data.remote.dto.LineOrderRequest
import com.example.mynaoseioqueapp.databinding.FragmentCartBinding
import com.example.mynaoseioqueapp.domain.mapper.LineOrderMapper
import com.example.mynaoseioqueapp.domain.model.LineOrder
import com.example.mynaoseioqueapp.domain.model.Order
import com.example.mynaoseioqueapp.presentation.adapters.CartRecyclerAdapter
import com.example.mynaoseioqueapp.util.LineOrderSetter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.math.BigDecimal

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null

    private val binding get() = _binding!!

    private val cartViewModel: CartFragmentViewModel by viewModels()

    private var lineOrderList: List<LineOrder> = emptyList()

    private var totalPrice: BigDecimal = BigDecimal(0)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)

        val sharedPref = requireActivity().getSharedPreferences(
            Constant.APP_PREF, Context.MODE_PRIVATE
        )

        val authToken = readDataFromSharedPref(Constant.ACCESS_TOKEN, sharedPref)

        val tableId = readDataFromSharedPref(Constant.TABLE_ID, sharedPref)

        Toast.makeText(requireContext(), tableId.toString(), Toast.LENGTH_SHORT).show()

        binding.cartRecyclerView.setHasFixedSize(true)
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(context)

        cartViewModel.setLineOrderListWithPrices()

//        if (LineOrderSetter.isLineOrderListEmpty()) {
//            binding.emptyCartText.visibility = View.VISIBLE
//            binding.priceTextView.visibility = View.GONE
//        } else {
//            binding.cartRecyclerView.adapter =
//                CartRecyclerAdapter(requireContext(), LineOrderSetter.getLineOrderList())
//
//            LineOrderSetter.getTotalPrice().toString().also { binding.priceTextView.text = it }
//        }

//        binding.orderConstLayout.setOnClickListener {
//            Toast.makeText(
//                    requireContext(),
//                    "You need to scan a table to order food",
//                    Toast.LENGTH_SHORT
//                ).show()
//        }

        binding.orderConstLayout.setOnClickListener {
            if (readDataFromSharedPref(Constant.TABLE_ID, sharedPref).isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "You need to scan a table to order food",
                    Toast.LENGTH_SHORT
                ).show()
//                return@setOnClickListener
            } else {
                try {
                    val lineOrderRequestList =
                        LineOrderMapper.mapLineOrderListToLineOrderRequestList(lineOrderList)
                    val order: Order= Order(lineOrderRequestList, totalPrice,
                        readDataFromSharedPref(Constant.TABLE_ID, sharedPref)!!.toLong())
                    cartViewModel.saveOrderOnApi(authToken!!, order)
                    Toast.makeText(
                        requireContext(),
                        "You food",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Log.d("MyOrderrrrrrrErrorrrrrrrrrr", e.localizedMessage)
                }
            }
        }

        lifecycleScope.launchWhenCreated {

            cartViewModel.lineOrderRequest.collect { event ->
                when (event) {
                    is CartFragmentViewModel.LineOrderListEvent.Success -> {
                        binding.cartRecyclerView.adapter =
                            CartRecyclerAdapter(
                                requireContext(),
                                event.lineOrderList
                            )

                        binding.priceTextView.text = event.totalPrice.toString()


                        lineOrderList = event.lineOrderList
                        totalPrice = event.totalPrice
                    }
                    CartFragmentViewModel.LineOrderListEvent.Loading -> {

                    }
                    is CartFragmentViewModel.LineOrderListEvent.Error -> {

                    }

                }
            }
        }

        lifecycleScope.launchWhenCreated {
            cartViewModel.orderRequest.collect { event ->
                when (event) {
                    is CartFragmentViewModel.OrderEvent.Success -> {
                        Log.d("MyOrderrrrrrrrrrrLogggggggggg", event.orderResponse.toString())
                    }
                    is CartFragmentViewModel.OrderEvent.Loading -> {

                    }
                    is CartFragmentViewModel.OrderEvent.Error -> {
                        Log.d("MyOrderrrrrrrrrrrLogggggggggg", event.errorText)
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

    private fun readDataFromSharedPref(key: String, sharedPref: SharedPreferences): String? {
        return sharedPref.getString(key, "Token Not Found")
    }
}