package com.example.gaugeapp.repositories.common

import android.net.Uri
import com.example.gaugeapp.dataSource.storage.StorageDataSourceImpl
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class StorageRepository @ExperimentalCoroutinesApi
@Inject constructor(private val commonUploadFileDataSource: StorageDataSourceImpl) {


    /**
     * Upload file of any type to the serve
     * Note: this function not compress the file befor upload
     * @param sousDossierdd
     * @param fileName
     * @param fileUri
     */
    fun uploadAnyFile(
        sousDossier: String = "",
        fileName: String,
        fileUri: Uri
    ): Flow<DataState<MutableMap<String, Any>>> = flow {

        emit(DataState.Loading(null))
        //and then, we upload it to the server
        emitAll(commonUploadFileDataSource.uploadAnyFile(sousDossier, fileName, fileUri))
    }


    /**
     * Upload user image:
     * the goal of this function consist to take image Uri, compress it,
     * upload it to the server and return the imageUrl
     * @param imageUri
     * @return imageUrl:String
     */
    fun uploadUserImage(imageUri: Uri): Flow<DataState<String>> = flow {

        emit(DataState.Loading(null))
        //and then, we upload it to the server
        emitAll(commonUploadFileDataSource.uploadUserImage(imageUri))

    }
}