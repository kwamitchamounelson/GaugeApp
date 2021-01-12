package com.example.gaugeapp.dataSource.storage


import android.net.Uri
import com.example.gaugeapp.dataSource.common.BaseRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi

import javax.inject.Inject

private const val TAG = "CommonUploadFileDataSou"

@ExperimentalCoroutinesApi
class StorageDataSourceImpl @Inject constructor(
    private val storageService: StorageService
) : StorageDataSource, BaseRemoteDataSource() {

    /**
     * Upload file of any type to the serve
     * Note: this function not compress the file befor upload
     * @param sousDossierdd
     * @param fileName
     * @param fileUri
     */
    override fun uploadAnyFile(
        sousDossier: String,
        fileName: String,
        fileUri: Uri
    ) = getResult { storageService.uploadAnyFile(sousDossier, fileName, fileUri) }


    /**
     * Upload user image
     * premit too upload image profil of the user
     * @param imageUri
     */
    override fun uploadUserImage(imageUri: Uri) =
        getResult { storageService.uploadUserImage(imageUri) }

}