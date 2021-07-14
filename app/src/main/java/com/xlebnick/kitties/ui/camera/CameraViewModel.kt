package com.xlebnick.kitties.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xlebnick.kitties.data.model.RequestStatus
import com.xlebnick.kitties.data.remote.Api
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject


class CameraViewModel @Inject constructor(private val api: Api) : ViewModel() {
    private val _uploadStatus = MutableLiveData<RequestStatus<Unit, UploadError>>()
    val uploadStatus: LiveData<RequestStatus<Unit, UploadError>> = _uploadStatus

    fun upload(requestFile: MultipartBody.Part) {
        viewModelScope.launch {
            _uploadStatus.value = RequestStatus.Loading
            try {
                api.upload(requestFile)
                _uploadStatus.value = RequestStatus.Success(Unit)
            } catch (e: Exception) {
                _uploadStatus.value = RequestStatus.Error(UploadError.GeneralError)
            }
            _uploadStatus.value = RequestStatus.Idle
        }
    }
}

// TODO: populate with real errors
sealed class UploadError {
    object GeneralError : UploadError()
}
