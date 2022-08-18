package com.robosoft.playverse.feature.presentation.view.profile

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.playverse.data.models.AvatarResponse
import com.robosoft.playverse.R
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.databinding.FragmentSetProfilePicBinding
import com.robosoft.playverse.feature.presentation.viewModel.LoginViewModel
import com.robosoft.playverse.utilities.TemporaryStorage
import com.robosoft.playverse.utilities.debugLog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


@AndroidEntryPoint
class SetProfilePicFragment : Fragment() {
    private lateinit var binding: FragmentSetProfilePicBinding

    private val args: SetProfilePicFragmentArgs by navArgs()

    @Inject
    lateinit var appStorage: AppStorage


    private val viewModel: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetProfilePicBinding.inflate(inflater, container, false)
        subscribeUI()
        bindUI()

        return binding.root
    }

    private fun bindUI() {

        binding.btnCancel.setOnClickListener {
            val action = SetProfilePicFragmentDirections.actionSetProfilePicFragmentToProfileOptionsFragment(args.fromProfileCreate)
            findNavController().navigate(action)
        }

        binding.btnDone.setOnClickListener {
            binding.progressBar.isVisible = true
            val result = binding.ivCroppedNew.croppedImage
            val file= bitmapToFile(result,"temp_fle")

                val body = file?.asRequestBody()?.let { it1 ->
                    MultipartBody.Part.createFormData(
                        "image",
                        "f_" + System.currentTimeMillis() + ".jpeg",
                        it1
                    )
                }
                TemporaryStorage.image = body
                val data: LiveData<AvatarResponse.MainResponse> =
                    viewModel.updateProfilePic(
                        body!!, appStorage.userId.toInt()
                    )

                data.observe(viewLifecycleOwner) {
                    if (it.success) {
                        binding.progressBar.isGone = true
                        if(args.fromProfileCreate) findNavController().navigate(R.id.profileCreateFragment)
                        else findNavController().navigate(R.id.profileEditFragment)
                    }
                }
            }
        }


    private fun subscribeUI() {
        viewModel.updateProfilePic(args.url.toUri())
        viewModel.profilePicUri.observe(viewLifecycleOwner) {
            binding.ivCroppedNew.setImageUriAsync(it.toUri())
            /*Glide.with(this)
                .load(it)
                .centerCrop()
                .into(binding.ivCroppedNew)*/
        }
    }

    fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
        //create a file to write bitmap data
        var file: File? = null
        return try {
            file = File(Environment.getExternalStorageDirectory().toString() + File.separator + fileNameToSave)
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            file

        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }


}