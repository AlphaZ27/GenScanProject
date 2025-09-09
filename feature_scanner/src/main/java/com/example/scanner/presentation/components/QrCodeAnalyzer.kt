package com.example.scanner.presentation.components

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
@Composable
fun QrCodeAnalyzer(
    modifier: Modifier = Modifier,
    onDetected: (String) -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Remember a camera provider instance
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    // Remember a single-threaded executor for image analysis
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    // Set up ML Kit Barcode Scanner
    val barcodeScanner = remember {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()
        BarcodeScanning.getClient(options)
    }

    // Use DisposableEffect to manage the camera lifecycle
    DisposableEffect(lifecycleOwner) {
        onDispose {
            // Shut down the executor when the composable is disposed
            cameraExecutor.shutdown()
        }
    }
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProvider = cameraProviderFuture.get()

            // --- 1. SET UP PREVIEW USE CASE ---
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            // --- 2. SET UP IMAGE ANALYSIS USE CASE ---
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        val image = imageProxy.image
                        if (image != null) {
                            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
                            barcodeScanner.process(inputImage)
                                .addOnSuccessListener { barcodes ->
                                    for (barcode in barcodes) {
                                        barcode.rawValue?.let { scannedValue ->
                                            // When a value is found, call the onDetect callback
                                            onDetect(scannedValue)
                                        }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Log.e("QrCodeAnalyzer", "Barcode scanning failed", e)
                                }
                                .addOnCompleteListener {
                                    imageProxy.close() // Always close the ImageProxy
                                }
                        } else {
                            imageProxy.close()
                        }
                    }
                }

            // --- 3. BIND TO LIFECYCLE ---
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll() // Unbind previous use cases
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageAnalysis
                )
            } catch (exc: Exception) {
                Log.e("QrCodeAnalyzer", "Use case binding failed", exc)
            }
            previewView
        }
    )
}