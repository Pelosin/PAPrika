package com.example.mynaoseioqueapp.presentation.qr_scanner

import android.app.Application
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.IllegalStateException
import java.util.concurrent.ExecutionException
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application){

    private val _getProcessCameraProvider by lazy {
        MutableLiveData<ProcessCameraProvider>().apply {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(getApplication())
            cameraProviderFuture.addListener(
                {
                    try {
                        value = cameraProviderFuture.get()
                    } catch (exception: ExecutionException){
                        throw IllegalStateException(
                            "Failed to retrive a ProcessCameraProvider instance", exception
                        )
                    } catch (exception: InterruptedException){
                        throw IllegalStateException(
                            "Failed to retrive a ProcessCameraProvider instance", exception
                        )
                    }
                },
                ContextCompat.getMainExecutor(getApplication())
            )
        }
    }

    val processCameraProvider: LiveData<ProcessCameraProvider>
        get() = _getProcessCameraProvider
}