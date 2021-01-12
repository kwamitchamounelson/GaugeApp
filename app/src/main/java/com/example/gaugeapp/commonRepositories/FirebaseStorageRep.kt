package com.example.gaugeapp.commonRepositories

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.example.gaugeapp.commonRepositories.StorageCollRef.STORAGE_USER_PROFILS
import com.example.gaugeapp.utils.compressImageFIle

object FirebaseStorageRep {
    const val IMAGE_USER_STORAGE_REF = "photos/profils_users/"
    val storageRef = FirebaseStorage.getInstance().reference

    val TAG = "StorageUtil"

    /**Permet d'uploader les images dans une reference de firestore**/
    fun uploadUserProfilFromLocalFile(
        filePath: Uri,
        onSuccess: (String) -> Unit,
        onError: () -> Unit,
        context: Context
    ) {

        compressImageFIle(filePath, context, onFinish = { compressedUri ->
            val file = compressedUri
            val riversRef = storageRef.child(
                STORAGE_USER_PROFILS+"/"+ FireStoreAuthUtil.getUserUID()
            )

            val uploadTask = file.let { riversRef.putFile(it) }

            uploadTask.addOnFailureListener {

            }.addOnSuccessListener {
                val urlTask =
                    uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        return@Continuation riversRef.downloadUrl
                    }).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            onSuccess(downloadUri.toString())
                        } else {
                            // Handle failures
                            onError()

                        }
                    }
            }
        })
    }

    /**
     * Cette fonction a pour but de prendre d'uploader un
     * fichier dans firebaseFirestoe après l'avoir compresser
     * @author Tsafix
     * @param fileUri: l'URI du fichier à uploader
     * @param subFolder: "optionel", il s'agit du nom du sous dossier qui va prendre l'image
     * @param fileName: le nom du fichier en question
     * @see par défaut le nom du fichier est concaténé à l'heure GMT fin d'éviter les noms identiques
     * @return l'urL du fichier en cas de succes
     */
    fun communUploadFromLocalFile(
        fileUri: Uri,
        subFolder: String = "",
        fileName: String,
        onSuccess: (String, String) -> Unit,
        onError: () -> Unit,
        context: Context,
        isImageFile: Boolean
    ) {
        if (isImageFile) {
            //If it is an image, we compress and upload it
            compressImageFIle(fileUri, context, onFinish = { compressedUri ->
                finallyUpload(subFolder, fileName, compressedUri, onError, onSuccess)
            })
        } else {
            //if it is not image file, we simply upload it
            finallyUpload(subFolder, fileName, fileUri, onError, onSuccess)
        }
    }

    /**
     * This function simply permit to
     * upload file
     * */
    private fun finallyUpload(
        sousDossier: String,
        fileName: String,
        compressedUri: Uri,
        onError: () -> Unit,
        onSuccess: (String, String) -> Unit
    ) {
        val fileLocation = if (sousDossier.isEmpty())
            fileName
        else
            "$sousDossier/$fileName"

        val riversRef = storageRef.child(fileLocation)
        val uploadTask = compressedUri.let { riversRef.putFile(it) }

        uploadTask.addOnFailureListener {
            Log.d(TAG, "Erreur d'upload ${it.message}")
            onError()
        }.addOnSuccessListener {
            val urlTask =
                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation riversRef.downloadUrl
                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        onSuccess(downloadUri.toString(), fileLocation)
                    } else {
                        Log.d(TAG, "Erreur d'upload")
                        onError()
                    }
                }
        }
    }


}