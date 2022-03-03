package com.example.mynaoseioqueapp

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    private lateinit var binding: ActivityMainBinding
//
//    private val viewModel: DisplayInformationViewModel by viewModels()
//
//    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "appPreferences")
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
////        binding.tv.setOnClickListener {
////            val intent = Intent(this, DisplayInformationActivity::class.java)
////            startActivity(intent)
////        }
//
//        viewModel.requestFoodFromApi()
//
//        lifecycleScope.launchWhenStarted {
//
//            viewModel.request.collect { event ->
//                when(event) {
//                    is DisplayInformationViewModel.FoodEvent.Failure -> {
//                        Toast.makeText(applicationContext, event.errorText, Toast.LENGTH_SHORT)
//                        Log.d("MyLogggggg", event.errorText)
//                        println(event.errorText)
//                        binding.tv.text = event.errorText
//                    }
//                    is DisplayInformationViewModel.FoodEvent.Loading -> {
//                        binding.tv.text = "Loading"
//                    }
//                    is DisplayInformationViewModel.FoodEvent.Success -> {
//                        Toast.makeText(applicationContext, event.foodList.get(0).name, Toast.LENGTH_SHORT)
//                        event.foodList.forEach {
//                            println(it.name)
//                        }
//                        Log.d("MyLogggggg", event.foodList.toString())
//                        binding.tv.text = event.foodList.get(0).name
//                    }
//
//                    else -> Unit
//                }
//            }
//        }
//
//
//    }
}