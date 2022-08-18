package com.robosoft.playverse.feature.presentation.view.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.robosoft.playverse.BuildConfig
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.FragmentProfileOptionsBinding
import com.robosoft.playverse.utilities.debugLog
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class ProfileOptionsFragment : Fragment() {

    private lateinit var binding : FragmentProfileOptionsBinding

    // A callback for profile pic pickup
    private lateinit var imagePickCallback: ActivityResultLauncher<String>

    private val args: ProfileOptionsFragmentArgs by navArgs()

    private var waitingForCameraOpen = false

    private lateinit var cameraPermissionCallback: ActivityResultLauncher<String>
    private var cameraFileUri: Uri? = null
    private lateinit var cameraCaptureCallback: ActivityResultLauncher<Uri>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagePickCallback = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            // when user selected either grant or reject.
            // save the result to local temp storage which is live data.
            uri ?: return@registerForActivityResult
            if (!uri.toString().isNullOrEmpty()) {
                debugLog("args is ${args.fromProfileCreate}")
                val action =
                    ProfileOptionsFragmentDirections.actionProfileOptionsFragmentToSetProfilePicFragment(
                        uri.toString(), args.fromProfileCreate
                    )
                findNavController().navigate(action)
            }
        }
            cameraCaptureCallback = registerForActivityResult(ActivityResultContracts.TakePicture()) {

                val picUri = cameraFileUri
                if (it && picUri != null) {
                    val action = ProfileOptionsFragmentDirections.actionProfileOptionsFragmentToSetProfilePicFragment(picUri.toString(), args.fromProfileCreate)
                    findNavController().navigate(action)
                }
            }

            cameraPermissionCallback =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                    // When get camera permission
                    if (it && waitingForCameraOpen) {
                        launchCameraCapture()
                    }
                    waitingForCameraOpen = false
                }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileOptionsBinding.inflate(inflater,container,false)
        bindUI()
        return binding.root
    }

    private fun bindUI() {
        binding.tvChooseAvatar.setOnClickListener {
            val action = ProfileOptionsFragmentDirections.actionProfileOptionsFragmentToSelectAvatarFragment(args.fromProfileCreate)
            findNavController().navigate(action)
        }

        binding.btnCancel.setOnClickListener {
            if(args.fromProfileCreate) findNavController().navigate(R.id.profileCreateFragment)
            else findNavController().navigate(R.id.profileEditFragment)
        }

        binding.tvPhotoGallery.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 100
                )
            }else{
                imagePickCallback.launch("image/*")
            }
        }


        binding.tvTakePhoto.setOnClickListener {
            launchCameraCapture()
        }


    }

    private fun launchCameraCapture() {
        val context = context ?: return
        val activity = activity ?: return
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            waitingForCameraOpen = true
            try {
                cameraPermissionCallback.launch(Manifest.permission.CAMERA)
            } catch (e: Throwable) {

            }
            return
        }
        val file = File(context.filesDir, "pfc")
        val uri = FileProvider.getUriForFile(
            context,
            BuildConfig.APPLICATION_ID + ".provider",
            file
        )
        cameraFileUri = uri
        cameraCaptureCallback.launch(uri)
    }



    companion object{
        const val PERMISSION = 100
    }

}