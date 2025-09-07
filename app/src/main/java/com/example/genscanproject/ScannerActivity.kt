package com.example.genscanproject

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
// Removed @Composable from class declaration as it's an AppCompatActivity using CameraX traditionally
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScannerActivity : AppCompatActivity() {

    private lateinit var barcodeScanner: BarcodeScanner
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("ScannerActivity", "Camera permission granted")
                bindCameraUseCases()
            } else {
                Log.e("ScannerActivity", "Camera permission denied")
                Toast.makeText(this, "Camera permission is required to scan QR codes.", Toast.LENGTH_LONG).show()
                // Optionally, close the activity or guide the user to settings
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Note: No setContentView as we are primarily doing image analysis.
        // If a PreviewView is needed, it should be added to a layout and set here.

        val options = BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).build()
        barcodeScanner = BarcodeScanning.getClient(options)

        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        startCamera()
    }

    private fun startCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            bindCameraUseCases()
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun bindCameraUseCases() {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, MyImageAnalyzer())
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll() // Unbind use cases before rebinding
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, imageAnalysis
                )
                Log.d("ScannerActivity", "Camera use cases bound")
            } catch (exc: Exception) {
                Log.e("ScannerActivity", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private inner class MyImageAnalyzer : ImageAnalysis.Analyzer {
        @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                barcodeScanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        for (barcode in barcodes) {
                            Log.d("ScannerActivity", "Barcode value: ${barcode.rawValue}")
                            // TODO: Handle the scanned QR code value (e.g., display it, send it to ViewModel)

                            // For now, let's make a Toast to show it's working
                            // Ensure this runs on the main thread if updating UI directly from here
                            // imageProxy.close() might be called too early if we want to show a toast
                            // with the value *before* closing the proxy.
                            // Consider passing the result to the main thread to handle UI.
                            runOnUiThread {
                                Toast.makeText(this@ScannerActivity, "Scanned: ${barcode.rawValue}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("ScannerActivity", "Error processing image", e)
                    }
                    .addOnCompleteListener {
                        imageProxy.close() // Close the image proxy after processing
                    }
            } else {
                imageProxy.close() // Ensure proxy is closed if mediaImage is null
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
