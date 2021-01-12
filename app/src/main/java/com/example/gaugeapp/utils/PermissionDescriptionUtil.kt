package com.example.gaugeapp.utils

import android.content.Context
import android.util.Log
import com.example.gaugeapp.R
import com.example.gaugeapp.utils.EntitiesUtils.PermissionResult

fun PermissionResult.completeDescription(context: Context){
    Log.e("permissions","completion: ${this.permissionName}")
    when(this.permissionName){

        android.Manifest.permission.READ_SMS->{
            this.name = context.getString(R.string.read_sms)
            this.image= R.drawable.blue_message_icon_foreground
            this.limitations= context.resources.getString(R.string.read_sms_limitation)
            this.descrition= context.resources.getString(R.string.read_sms_description)
        }

        android.Manifest.permission.RECEIVE_SMS->{
            this.name = context.getString(R.string.read_sms)
            this.image= R.drawable.blue_message_icon_foreground
            this.limitations= context.resources.getString(R.string.read_sms_limitation)
            this.descrition= context.resources.getString(R.string.read_sms_description)
        }

        android.Manifest.permission.READ_CONTACTS->{
            this.name = context.getString(R.string.read_contact)
            this.image= R.drawable.icon_material_contacts_foreground
            this.limitations= context.resources.getString(R.string.read_contact_limitation)
            this.descrition= context.resources.getString(R.string.read_contact_description)
        }

        android.Manifest.permission.CALL_PHONE->{
            this.name = context.getString(R.string.phone_call)
            this.image= R.drawable.icon_material_local_phone_foreground
            this.limitations= context.resources.getString(R.string.phone_call_limitation)
            this.descrition= context.resources.getString(R.string.phone_call_description)
        }
        android.Manifest.permission.READ_PHONE_STATE->{
            this.name = context.getString(R.string.phone_call)
            this.image= R.drawable.icon_material_local_phone_foreground
            this.limitations= context.resources.getString(R.string.phone_call_limitation)
            this.descrition= context.resources.getString(R.string.phone_call_description)
        }

        "OVERLAY"->{
            this.name = context.getString(R.string.overlay)
            this.image= R.drawable.overlay_icon_foreground
            this.limitations= context.resources.getString(R.string.phone_call_limitation)
            this.descrition= context.resources.getString(R.string.phone_call_description)
        }
        android.Manifest.permission.CAMERA->{
            this.name = context.getString(R.string.camera)
            this.image= R.drawable.ic_photo_camera_black_24dp
            this.limitations= context.resources.getString(R.string.camera_limitation)
            this.descrition= context.resources.getString(R.string.camera_description)
        }
        android.Manifest.permission.READ_EXTERNAL_STORAGE->{
            this.name = context.getString(R.string.storage)
            this.image= R.drawable.ic_baseline_storage_24
            this.limitations= context.resources.getString(R.string.read_external_limitation)
            this.descrition= context.resources.getString(R.string.read_external_description)
        }

        android.Manifest.permission.WRITE_EXTERNAL_STORAGE->{
            this.name = context.getString(R.string.storage)
            this.image= R.drawable.ic_baseline_storage_24
            this.limitations= context.resources.getString(R.string.read_external_limitation)
            this.descrition= context.resources.getString(R.string.read_external_description)
        }
        android.Manifest.permission.RECORD_AUDIO ->{
            this.name = context.getString(R.string.microphone)
            this.image = R.drawable.ic_baseline_mic_24
            this.limitations = context.getString(R.string.microphone_limitation)
            this.descrition = context.getString(R.string.microphone_description)
        }
    }
}
