package com.loremipsum.eyespy.client

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionLabel
import java.io.ByteArrayOutputStream

class ImageDetectionClient {

    fun compressBitmap(bitmap: Bitmap): Bitmap {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val bitmapData = byteArrayOutputStream.toByteArray()
        val compressedImage = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.size)
        return compressedImage
    }

    fun runDeviceImageLabeling(bitmap: Bitmap, onSuccessCallback: (List<FirebaseVisionLabel>) -> Unit,
                               onErrorCallback: (Exception) -> Unit) {
        val detector = FirebaseVision.getInstance().visionLabelDetector
        val firebaseImage = FirebaseVisionImage.fromBitmap(bitmap)
        detector.detectInImage(firebaseImage).addOnSuccessListener(onSuccessCallback)
                .addOnFailureListener(onErrorCallback)
    }

    fun runCloudImageLabeling(bitmap: Bitmap, onSuccessCallback: (List<FirebaseVisionCloudLabel>) -> Unit,
                              onErrorCallback: (Exception) -> Unit) {
        val detector = FirebaseVision.getInstance().visionCloudLabelDetector
        val firebaseImage = FirebaseVisionImage.fromBitmap(bitmap)
        detector.detectInImage(firebaseImage).addOnSuccessListener(onSuccessCallback)
                .addOnFailureListener(onErrorCallback)
    }
}