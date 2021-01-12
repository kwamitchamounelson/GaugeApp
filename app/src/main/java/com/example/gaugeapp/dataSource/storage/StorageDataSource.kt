package com.example.gaugeapp.dataSource.storage

import android.net.Uri
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.flow.Flow

interface StorageDataSource {
    /**
     * Upload file of any type to the serve
     * Note: this function not compress the file befor upload
     * @param sousDossier
     * @param fileName
     * @param fileUri
     */
    fun uploadAnyFile(
        sousDossier: String = "",
        fileName: String,
        fileUri: Uri
    ): Flow<DataState<MutableMap<String, Any>>>

    /**
     * Upload user image
     * premit too upload image profil of the user
     * @param imageUri
     */
    fun uploadUserImage(imageUri: Uri): Flow<DataState<String>>
}