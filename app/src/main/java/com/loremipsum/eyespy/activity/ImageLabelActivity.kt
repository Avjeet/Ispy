package com.loremipsum.eyespy.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel
import com.google.firebase.ml.vision.label.FirebaseVisionLabel
import com.google.firebase.storage.FirebaseStorage
import com.loremipsum.eyespy.adapter.ImageLabelAdapter
import com.loremipsum.eyespy.client.ImageDetectionClient
import com.loremipsum.eyespy.client.RestApiClient
import com.loremipsum.eyespy.model.Dataset
import com.loremipsum.eyespy.model.Phrases
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.util.*


class ImageLabelActivity : BaseCameraActivity() {

    private lateinit var itemAdapter: ImageLabelAdapter

    private val imageDetectionClient = ImageDetectionClient()
    private lateinit var phrase: Phrases
    private val cloudStorageClient by lazy {
        FirebaseStorage.getInstance("gs://loremipsum-fb0a5.appspot.com")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phrase = getPhrase()
        clueText.text = "Clue: ${phrase.displayText}"
        itemAdapter = ImageLabelAdapter(listOf())
        rvLabel.layoutManager = LinearLayoutManager(this)
        rvLabel.adapter = itemAdapter
    }

    private fun initSnackbar(text: String) {
        val snackbar = Snackbar.make(container_layout, text, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("Dismiss") {
            snackbar.dismiss()
        }
    }

    private fun getPhrase(): Phrases {
        val extras = intent.extras
        val phrase = extras?.getParcelable(PHRASE_EXTRA)
                ?: Phrases("Bottle", arrayListOf("water", "water bottle", "bottle", "aqua", "drinkware"))
        return phrase
    }

    override fun onClick(v: View?) {
        progressBar.visibility = View.VISIBLE
        cameraView.captureImage { cameraKitImage ->
            // Get the Bitmap from the captured shot
            val bitmapImage = cameraKitImage.bitmap
            val compressedImage = imageDetectionClient.compressBitmap(bitmapImage)
            runOnUiThread {
                showPreview()
                imagePreview.setImageBitmap(compressedImage)
            }
            try {
                runCloudImageDetect(compressedImage)
            } catch (socketError: Exception) {
                runDeviceImageDetect(compressedImage)
            }
        }
    }

    fun runDeviceImageDetect(compressedImage: Bitmap) {
        initSnackbar("Using on-device detection, may be inaccurate")
        imageDetectionClient.runDeviceImageLabeling(compressedImage, {
            progressBar.visibility = View.GONE
            val cleanedData = cleanLabelData(it)
            itemAdapter.setList(cleanedData)
            if (cleanedData.isNotEmpty()) {
                uploadToCloudStore(compressedImage, cleanedData)
            }
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }, {
            Toast.makeText(baseContext, "Sorry Something went wrong!", Toast.LENGTH_LONG).show()
        }
        )
    }

    fun runCloudImageDetect(compressedImage: Bitmap) {
        imageDetectionClient.runCloudImageLabeling(compressedImage,
                { it ->
                    progressBar.visibility = View.GONE
                    val cleanedData = cleanLabelData(it)
                    itemAdapter.setList(cleanedData)
                    if (cleanedData.isNotEmpty()) {
                        uploadToCloudStore(compressedImage, cleanedData)
                    }
                    sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                },
                { it ->
                    progressBar.visibility = View.GONE
                    Toast.makeText(baseContext, "Sorry, something went wrong!", Toast.LENGTH_SHORT).show()
                    Log.e("ImageLabelActivity", "Error aa gya socket wala", it)
                    throw java.lang.Exception()
                })
    }

    fun uploadToCloudStore(compressedImage: Bitmap, cleanedData: List<HashMap<String, String>>) {
        val uploaderThread = Runnable {
            val cloudStorageReference = cloudStorageClient.getReference("/loremipsum/dataset")
            val storageFileReference = cloudStorageReference.child("${Date().time}-${phrase.displayText}.png")
            val stream = ByteArrayOutputStream()
            compressedImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            storageFileReference.putBytes(byteArray)
                    .addOnCompleteListener(this) { it ->
                        if (it.isSuccessful) {
                            val uri = it.result?.metadata?.path!!
                            Log.d("IMAGE_LABEL_ACTIVITY", uri)
                            RestApiClient.restClientService.putDataSetItem(Dataset(path = uri, labels = phrase.labels))
                        }
                    }
        }
        uploaderThread.run()
    }

    fun matches_phrase(dataLabel: String): Boolean {
        for (label in phrase.labels) {
            if (dataLabel.equals(label, true)) {
                return true
            }
        }
        return false
    }

    fun cleanLabelData(list: List<Any>): List<HashMap<String, String>> {
        val cleanedDataList = ArrayList<HashMap<String, String>>()
        for (data in list) {
            if (data is FirebaseVisionLabel && data.confidence >= 0.70 && matches_phrase(data.label)) {
                val hashMap = HashMap<String, String>()
                hashMap[ImageLabelAdapter.LABEL] = data.label
                hashMap[ImageLabelAdapter.CONFIDENCE] = "${data.confidence * 100}"
                cleanedDataList.add(hashMap)
            } else if (data is FirebaseVisionCloudLabel && data.confidence >= 0.70 && matches_phrase(data.label)) {
                val hashMap = HashMap<String, String>()
                hashMap[ImageLabelAdapter.LABEL] = data.label
                hashMap[ImageLabelAdapter.CONFIDENCE] = "${data.confidence * 100}"
                cleanedDataList.add(hashMap)
            }
        }
        return cleanedDataList
    }

    companion object {
        const val PHRASE_EXTRA = "phrase_extra"
    }
}
