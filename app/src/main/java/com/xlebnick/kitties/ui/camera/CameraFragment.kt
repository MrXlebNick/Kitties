package com.xlebnick.kitties.ui.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.xlebnick.kitties.data.model.RequestStatus
import com.xlebnick.kitties.databinding.FragmentCameraBinding
import com.xlebnick.kitties.ui.base.BaseFragment
import com.xlebnick.kitties.utils.CameraHelper
import com.xlebnick.kitties.utils.FileUtils
import com.xlebnick.kitties.utils.PermissionHelper
import javax.inject.Inject

class CameraFragment : BaseFragment<FragmentCameraBinding>() {

    private val viewModel by diActivityViewModels<CameraViewModel>()

    @Inject
    lateinit var cameraHelper: CameraHelper

    @Inject
    lateinit var fileUtils: FileUtils

    @Inject
    lateinit var permissionHelper: PermissionHelper

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FragmentCameraBinding {
        return FragmentCameraBinding.inflate(inflater, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        cameraHelper.bindToLifecycleOwner(viewLifecycleOwner)

        // Request camera permissions
        if (permissionHelper.allPermissionsGranted()) {
            cameraHelper.startCamera(binding?.viewFinder?.surfaceProvider)
        } else {
            requestPermissions(
                PermissionHelper.REQUIRED_PERMISSIONS,
                PermissionHelper.REQUEST_CODE_PERMISSIONS
            )
        }

        // Set up the listener for take photo button
        binding?.cameraCaptureButton?.setOnClickListener {
            // take photo with provided onSuccess callback
            cameraHelper.takePhoto({ uri ->
                viewModel.upload(fileUtils.convertUriToMultipart(uri))
            },
                {
                    Toast.makeText(requireContext(), "Failed to take a photo", Toast.LENGTH_LONG)
                        .show()
                })
        }

        viewModel.uploadStatus.observe(viewLifecycleOwner) { status ->
            val context = context ?: return@observe
            when (status) {
                is RequestStatus.Error -> Toast.makeText(
                    context,
                    "Photo failed to upload",
                    Toast.LENGTH_LONG
                ).show()
                is RequestStatus.Success -> Toast.makeText(
                    context,
                    "Photo uploaded!!",
                    Toast.LENGTH_LONG
                ).show()
                else -> Unit
            }
        }
    }
}