package com.bibliotecaibi.ui.books

import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@Composable
fun IsbnScanner(onIsbnDetected: (String) -> Unit) {

    val context = LocalContext.current

    AndroidView(factory = { ctx ->
        val previewView = PreviewView(ctx)

        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

            val selector = CameraSelector.DEFAULT_BACK_CAMERA

            val analysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            val scanner = BarcodeScanning.getClient()

            analysis.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imgProxy ->
                val media = imgProxy.image
                if (media != null) {
                    val image = InputImage.fromMediaImage(
                        media,
                        imgProxy.imageInfo.rotationDegrees
                    )
                    scanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            for (barcode in barcodes) {
                                if (barcode.format == Barcode.FORMAT_EAN_13 ||
                                    barcode.format == Barcode.FORMAT_EAN_8) {

                                    barcode.rawValue?.let { isbn ->
                                        onIsbnDetected(isbn)
                                    }
                                }
                            }
                        }
                        .addOnCompleteListener {
                            imgProxy.close()
                        }
                } else {
                    imgProxy.close()
                }
            }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                ctx as androidx.activity.ComponentActivity,
                selector,
                preview,
                analysis
            )

        }, ContextCompat.getMainExecutor(ctx))

        previewView
    })
}
