package com.example.mynaoseioqueapp.presentation.qr_scanner

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Size
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mynaoseioqueapp.databinding.ActivityObjectDetectionBinding
import com.example.mynaoseioqueapp.presentation.check_table.CheckTableActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ObjectDetectionActivity : AppCompatActivity(), ImageAnalysis.Analyzer {

    private val context = this

    private lateinit var binding: ActivityObjectDetectionBinding

    private lateinit var cameraExecutor: ExecutorService

    private val REQUEST_CODE_PERMISSIONS = 10

    private lateinit var analyser: ImageAnalysis

    private lateinit var constraintLayout: ConstraintLayout

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

//    private var hasHideBottomDialog = true
//    private var hasShowBottomDialog = false

    override fun onDestroy() {
        super.onDestroy()

        cameraExecutor.shutdown()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        constraintLayout = findViewById(com.example.mynaoseioqueapp.R.id.bottom_dialog)
        bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        this.window.setFlags(1024, 1024)

        cameraExecutor = Executors.newSingleThreadExecutor()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(
            {
                try {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        != (PackageManager.PERMISSION_GRANTED)
                    ) {
                        ActivityCompat.requestPermissions(
                            context,
                            arrayOf(Manifest.permission.CAMERA),
                            REQUEST_CODE_PERMISSIONS
                        )
                    } else {
                        val processCameraProvider: ProcessCameraProvider =
                            cameraProviderFuture.get()
                        bindpreview(processCameraProvider)
                    }
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(this)
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        if (requestCode == REQUEST_CODE_PERMISSIONS && grantResults.isNotEmpty()) {
            val processCameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            bindpreview(processCameraProvider)
        } else {
            Toast.makeText(
                this,
                "Permissions not granted by user.",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun bindpreview(processCameraProvider: ProcessCameraProvider) {
        val preview: Preview = Preview.Builder().build()
        val cameraSelector: CameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        preview.setSurfaceProvider(binding.previewView.surfaceProvider)
        val imageCapture: ImageCapture = ImageCapture.Builder().build()
        val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        imageAnalysis.setAnalyzer(cameraExecutor, this)
        processCameraProvider.unbindAll()
        processCameraProvider.bindToLifecycle(
            this,
            cameraSelector,
            preview,
            imageCapture,
            imageAnalysis
        )
    }

    override fun analyze(image: ImageProxy) {
        scanBarcode(image)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun scanBarcode(image: ImageProxy) {
        val mediaImage = image.image
        assert(mediaImage != null)
        val inputImage = InputImage.fromMediaImage(mediaImage!!, image.imageInfo.rotationDegrees)
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC)
            .build()
        val scanner = BarcodeScanning.getClient(options)
        scanner
            .process(inputImage)
            .addOnSuccessListener { barcodes ->
                if(barcodes.filter {
                        if (it.valueType == Barcode.TYPE_URL && it.boundingBox != null){
                            return@filter true
                        }
                        return@filter false
                    }.isEmpty()) {

//                    hideBottomDialogView()
                    return@addOnSuccessListener
                }
                val uri = Uri.parse(barcodes.first().url!!.url!!)
//                val tableId = uri.getQueryParameter("id")
//                if(tableId.isNullOrEmpty()){
//                    return@addOnSuccessListener
//                }

                onQrCodeScanned(uri.toString())
//                showBottomDialog()
//                readBarcodeData(barcodes)
            }
            .addOnFailureListener {
                // Task failed with an exception
                // ...
            }
            .addOnCompleteListener {
                image.close()
            }
    }

    private fun onQrCodeScanned(urlForRequest: String) {
        val intent = Intent(this@ObjectDetectionActivity, CheckTableActivity::class.java)
        intent.putExtra("selectedTable", urlForRequest)
        startActivity(intent)
        cameraExecutor.shutdown()
        finish()
    }

//    private fun showBottomDialog() {
//        if(!hasShowBottomDialog){
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//            bottomSheetBehavior.setHideableInternal(false)
//            hasHideBottomDialog = false
//            hasShowBottomDialog = true
//        }
//    }
//
//    private fun hideBottomDialogView() {
//        if(!hasHideBottomDialog){
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//            hasHideBottomDialog = true
//            hasShowBottomDialog = false
//        }
//    }

//    private fun readBarcodeData(barcodes: List<Barcode>) {
//        for (barcode in barcodes) {
//            val bounds = barcode.boundingBox
//            val corners = barcode.cornerPoints
//
//            val rawValue = barcode.rawValue
//
//            val valueType = barcode.valueType
//            // See API reference for complete list of supported types
//            when (valueType) {
//                Barcode.TYPE_WIFI -> {
//                    val ssid = barcode.wifi!!.ssid
//                    val password = barcode.wifi!!.password
//                    val type = barcode.wifi!!.encryptionType
//                }
//                Barcode.TYPE_URL -> {
//                    val title = barcode.url!!.title
//                    val url = barcode.url!!.url
//
//                }
//            }
//        }
//    }
}
