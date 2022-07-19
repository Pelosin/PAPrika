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
import com.example.mynaoseioqueapp.databinding.FragmentCartBinding
import com.example.mynaoseioqueapp.domain.mapper.LineOrderMapper
import com.example.mynaoseioqueapp.domain.model.LineOrder
import com.example.mynaoseioqueapp.domain.model.Order
import com.example.mynaoseioqueapp.presentation.adapters.CartRecyclerAdapter
import com.example.mynaoseioqueapp.presentation.check_table.CheckTableViewModel
import com.example.mynaoseioqueapp.util.LineOrderSetter
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal

@AndroidEntryPoint
class CartFragmentSub : Fragment(), CartRecyclerAdapter.CartViewHolder.OnClickCartEvent {

    private var _binding: FragmentCartBinding? = null

    private val binding get() = _binding!!

    private val cartViewModel: CartFragmentViewModel by viewModels()

    private val tableViewModel: CheckTableViewModel by viewModels()

    private var lineOrderList: List<LineOrder> = emptyList()

    private var totalPrice: BigDecimal = BigDecimal(0)

    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)

        sharedPref = requireActivity().getSharedPreferences(
            Constant.APP_PREF, Context.MODE_PRIVATE
        )

        val authToken = readDataFromSharedPref(Constant.ACCESS_TOKEN, sharedPref)

        val tableId = readDataFromSharedPref(Constant.TABLE_ID, sharedPref)

        if (tableId.equals("Token Not Found")) {
            binding.hamburger.visibility = View.GONE
            Toast.makeText(requireContext(),
                "Scan a table to start ordering",
                Toast.LENGTH_SHORT).show()
        }


        binding.cartRecyclerView.setHasFixedSize(false)
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(context)

        cartViewModel.setLineOrderListWithPrices()

        binding.orderConstLayout.setOnClickListener {
            if (readDataFromSharedPref(Constant.TABLE_ID, sharedPref).equals("Token Not Found")) {
                Toast.makeText(
                    requireContext(),
                    "You need to scan a table to order food",
                    Toast.LENGTH_SHORT
                ).show()
//                return@setOnClickListener
            } else {
                try {
                    val token = readDataFromSharedPref(Constant.ACCESS_TOKEN, sharedPref)!!.substring(7)
                    val lineOrderRequestList =
                        LineOrderMapper.mapLineOrderListToLineOrderRequestList(lineOrderList)
                    val order: Order = Order(
                        lineOrderRequestList, totalPrice,
                        readDataFromSharedPref(Constant.TABLE_ID, sharedPref)!!.toLong(),
                        token
                    )
                    cartViewModel.saveOrderOnApi(authToken!!, order)
                    Toast.makeText(
                        requireContext(),
                        "Your order was placed successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Log.d("MyOrderrrrrrrErrorrrrrrrrrr", e.localizedMessage)
                }
            }
        }

        binding.hamburger.setOnClickListener {
            if (readDataFromSharedPref(Constant.TABLE_ID, sharedPref).equals("Token Not Found")) {
                Toast.makeText(
                    requireContext(),
                    "You need to scan a table first",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                tableViewModel.leaveTableApi(
                    readDataFromSharedPref(Constant.ACCESS_TOKEN, sharedPref)!!,
                    readDataFromSharedPref(Constant.TABLE_ID, sharedPref)!!,
                    sharedPref
                )
            }
        }

        lifecycleScope.launchWhenCreated {
            cartViewModel.lineOrderRequest.collect { event ->
                when (event) {
                    is CartFragmentViewModel.LineOrderListEvent.Success -> {
                        binding.cartRecyclerView.adapter =
                            CartRecyclerAdapter(
                                requireContext(),
                                event.lineOrderList,
                                this@CartFragmentSub,
                                binding.priceTextView
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
                        cleanWholeCart()
                    }
                    is CartFragmentViewModel.OrderEvent.Loading -> {

                    }
                    is CartFragmentViewModel.OrderEvent.Error -> {
                        Log.d("MyOrderrrrrrrrrrrLogggggggggg", event.errorText)
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            tableViewModel.leaveTableRequest.collect { event ->
                when (event) {
                    is CheckTableViewModel.TableEvent.Success -> {
                        Log.d("PoPo", readDataFromSharedPref(Constant.TABLE_ID, sharedPref)!!)
                        binding.hamburger.visibility = View.GONE
                    }
                    is CheckTableViewModel.TableEvent.Failure -> {}
                }
            }
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if (readDataFromSharedPref(Constant.TABLE_ID, sharedPref).equals("Token Not Found")) {
            binding.hamburger.visibility = View.GONE
        } else {
            binding.hamburger.visibility = View.VISIBLE
        }

    }

    private fun checkTableIsOccupied(tableId: String) {
        if (tableId.equals("Token Not Found")) {
            binding.leaveButton.visibility = View.GONE
        }
    }

    private fun readDataFromSharedPref(key: String, sharedPref: SharedPreferences): String? {
        return sharedPref.getString(key, "Token Not Found")
    }

    override fun onClickSubEvent(lineOrder: LineOrder, position: Int) {
//        binding.priceTextView.text = LineOrderSetter.getTotalPrice().toString()

//        if(lineOrder.quantity.equals(1)){
//            LineOrderSetter.removeFromCart(lineOrder)
//            binding.cartRecyclerView.adapter!!.notifyItemRemoved(position)
//            binding.priceTextView.text = LineOrderSetter.getTotalPrice().toString()
//        } else {
//            LineOrderSetter.subQuantity(lineOrder)
//            binding.priceTextView.text = LineOrderSetter.getTotalPrice().toString()
//        }
//        LineOrderSetter.removeFromCart(lineOrder)
//        binding.cartRecyclerView.adapter!!.notifyItemRemoved(position)
    }

    override fun onClickAddEvent(lineOrder: LineOrder, position: Int) {
//        LineOrderSetter.addQuantity(lineOrder)
//        binding.priceTextView.text = LineOrderSetter.getTotalPrice().toString()
    }

    override fun cleanWholeCart() {
        LineOrderSetter.cleanCart()
        binding.cartRecyclerView.adapter!!.notifyDataSetChanged()
        binding.priceTextView.text = LineOrderSetter.getTotalPrice().toString()
    }


}