package com.example.themoviedbandfirebase.ui.images.addImages

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.themoviedbandfirebase.R
import com.example.themoviedbandfirebase.databinding.ActivityAddImagesBinding
import com.example.themoviedbandfirebase.models.Images
import com.example.themoviedbandfirebase.util.Dialog
import com.example.themoviedbandfirebase.util.Features
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class AddImagesActivity : AppCompatActivity(), View.OnClickListener, TextWatcher {

    private lateinit var binding: ActivityAddImagesBinding

    //Features
    private val features by lazy { Features() }

    //Dialog
    private val dialog by lazy { Dialog(this@AddImagesActivity) }

    //ImagesUri
    private var selectedImageUri: Uri? = null

    //Firebase Firestore
    val db = Firebase.firestore
    private lateinit var storageReference: StorageReference

    //ViewModel
    @Inject
    lateinit var addImagesViewModel: AddImagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.add_images)
        }

        binding.buttonSelectImage.setOnClickListener(this@AddImagesActivity)
        binding.buttonUploadImages.setOnClickListener(this@AddImagesActivity)

        binding.editTextDescribe.addTextChangedListener(this)
        binding.editTextDescribe.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.editTextDescribe.setRawInputType(InputType.TYPE_CLASS_TEXT)

    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.buttonSelectImage -> {

                val i = Intent()
                i.type = "image/*"
                i.action = Intent.ACTION_GET_CONTENT

                launchSomeActivity.launch(i)

            }

            R.id.buttonUploadImages -> {

                if(features.isConnected(this@AddImagesActivity)){

                    if(binding.editTextDescribe.text.isNullOrEmpty()){
                        Toast.makeText(this@AddImagesActivity, "Descripcion es necesaria", Toast.LENGTH_LONG).show()
                        binding.textInputLayoutDescribe.error = "Descripcion es necesaria"
                    }else if(selectedImageUri == null){
                        Toast.makeText(this@AddImagesActivity, "Necesitas una imagen es necesaria", Toast.LENGTH_LONG).show()
                    }
                    else{

                        dialog.showDialog()
                        saveImagesinFireStorage()

                    }

                }

            }

        }

    }

    private fun saveImagesinFireStorage() {

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
        val now = Date()
        val fileName: String = formatter.format(now)

        storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        val uploadTask = storageReference.putFile(selectedImageUri!!)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            storageReference.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                saveInfoInFireStore(downloadUri)
            }else {
                Toast.makeText(this@AddImagesActivity, "Error al Subir la Imagen", Toast.LENGTH_LONG).show()
                dialog.dismissDialog()
                onBackPressed()
            }
        }

    }

    private fun saveInfoInFireStore(downloadUri: Uri?) {

        val data = hashMapOf(
            "describe" to binding.editTextDescribe.text.toString(),
            "url" to downloadUri.toString(),
            "date" to Date()
        )

        db.collection("images")
            .add(data)
            .addOnSuccessListener { documentReference ->
                val images = Images(documentReference.id, binding.editTextDescribe.text.toString(), downloadUri.toString(), Date())
                addImagesViewModel.insertImages(images)
                dialog.dismissDialog()
                onBackPressed()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@AddImagesActivity, "Error al subir tu informacion", Toast.LENGTH_LONG).show()
                dialog.dismissDialog()
                onBackPressed()
            }

    }

    private var launchSomeActivity = registerForActivityResult<Intent, ActivityResult>(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult -> if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data != null && data.data != null) {
                selectedImageUri = data.data!!
                binding.image.setImageURI(selectedImageUri)
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(p0: Editable?) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        when (p0.hashCode()) {

            binding.editTextDescribe.text.hashCode() -> {
                if(binding.textInputLayoutDescribe.isErrorEnabled){
                    binding.textInputLayoutDescribe.isErrorEnabled = false
                    binding.textInputLayoutDescribe.error = null
                }
            }

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}