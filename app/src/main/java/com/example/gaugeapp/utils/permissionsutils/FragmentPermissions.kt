package com.example.gaugeapp.utils.permissionsutils


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.utils.EntitiesUtils.PermissionResult
import com.example.gaugeapp.utils.completeDescription
import com.example.gaugeapp.utils.simpleAlert
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.permission_ask_seperatly_layout.view.*

open class FragmentPermissions() : Fragment() {


    private val ALL_PERMISSION_REQUEST = 1111
    var permissionResults = MutableLiveData<MutableMap<String, PermissionResult>>()

    var globalState = MutableLiveData<Boolean>().apply {
        value = false
    }
    private var itemPermissions = mutableListOf<ItemPermission>()


    fun requestPermission(
        permissions: List<String>
    ) {
        Log.e("permission", "permission called")
        permissions.forEach { permissionName ->
            var permissionResult = PermissionResult(permissionName)
            if (permissionResults.value?.get(permissionName)==null){

            }
            if (checkForPermissions(permissionName)) {
                permissionResult.state = true
            }

            permissionResults.value?.set(permissionName, permissionResult)
            Log.e("permission", "permission called ${permissionResult}")
        }
        var permissionsToask = mutableListOf<String>()
        permissionResults.value?.forEach { permissionEntry ->
            if (permissionEntry.value.state == null) {
                permissionsToask.add(permissionEntry.value.permissionName)
            }
            permissionEntry.value.state?.let { permissionEntryState ->
                if (!permissionEntryState) {
                    permissionsToask.add(permissionEntry.value.permissionName)
                }
            }

        }
        if (permissionsToask.isNotEmpty()) {
            requestPermissions(permissionsToask.toTypedArray(), ALL_PERMISSION_REQUEST)
        }
        observeResult()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.e("permission", "permission result")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ALL_PERMISSION_REQUEST -> {
                //Ne ne lira les sms pour recupérer les transactions que s'il a valider la permission de lecture des SMS
                if ((grantResults.isNotEmpty())) {
                    var index = 0
                    var permissionResultTemp = mutableMapOf<String, PermissionResult>()
                    permissionResults.value?.let {
                        permissionResultTemp = it
                    }
                    while (index < grantResults.size && index < permissions.size) {
                        var permissionTemp = permissionResultTemp.get(permissions[index])
                        permissionTemp?.shouldRational =
                            shouldShowRequestPermissionRationale(permissions[index])
                        permissionTemp?.state =
                            grantResults[index] == PermissionChecker.PERMISSION_GRANTED
                        permissionResultTemp.set(permissionTemp!!.permissionName, permissionTemp)
                        index++
                    }
                    permissionResults.value = permissionResultTemp
                    Log.e("permission", "permissionResultTemp ${permissionResultTemp}"+"${permissionResultTemp.size}")
                }
            }
        }
    }

    fun observeResult() {
        try {
            permissionResults?.let {
                Log.e("permission", "permissionResults in the observer ${it.value}"+"${it.value?.size}")
                it.observe(viewLifecycleOwner) { mapOfPermissions ->
                    var state: Boolean? = true
                    var permissionsRefused = mutableListOf<PermissionResult>()
                    mapOfPermissions.forEach { permissionEntry ->
                        Log.e("permission", "permissionResults permissionEntry ${permissionEntry.value}")
                       permissionEntry.value.state?.let {
                           if (!it) {
                               permissionsRefused.add(permissionEntry.value)
                           }
                       }
                        Log.e("permission", "permissionResults permissionEntry permissionsRefused ${permissionsRefused}"+"${permissionsRefused.size}")
                    }
                    mapOfPermissions.forEach { permissionEntry ->
                        Log.e("permission", "permissionResults permissionEntry permissionsRefused ${permissionEntry}")
                        permissionEntry.value.state?.let {
                            if (!it) {
                                state = false
                                Log.e("permission", "permissionResults state false ${permissionEntry}")
                                return@forEach
                            }

                        }

                    }
                    state?.let { state ->
                        Log.e("permission", "permissionResults permissionResults state  ${state}")
                        if (state) {
                            globalState.value = true

                        } else {
                            openView(permissions = permissionsRefused)
                        }
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun openView(
        permissions: List<PermissionResult>
    ) {
        Log.e("permission", "permissionResults permissionResults state  ${permissions}")
        var view = layoutInflater.inflate(R.layout.permission_ask_seperatly_layout, null)
        initRecyclerView(view, permissions)
        val alert = simpleAlert(view)

        view.apply {
            var shouldRational=true
            permissions.forEach { permissionresult ->
                permissionresult.shouldRational?.let {
                  if (!it) {
                   this.permission_btn_reset.visibility=View.GONE
                      this.permission_btn_go_to_settings.visibility=View.VISIBLE
                      this.permission_settings_advice.visibility = View.VISIBLE
                      return@forEach
                  }
               }
            }
            this.permission_btn_go_to_settings.setOnClickListener {
                alert.dismiss()
                openSettings()

            }

            this.permission_btn_continue.setOnClickListener {
                globalState.value = true
                alert.dismiss()
            }
            this.permission_btn_reset.setOnClickListener {
                alert.dismiss()
                var permissionsToReAsk = mutableListOf<String>()
                permissions.forEach {
                    permissionsToReAsk.add(it.permissionName)
                }
                requestPermissions(permissionsToReAsk.toTypedArray(), ALL_PERMISSION_REQUEST)
            }
        }
    }

    private fun initRecyclerView(view: View, permissions: List<PermissionResult>) {
        view.permissions_list.apply {
            layoutManager = LinearLayoutManager(context)
            itemPermissions.clear()
            permissions.forEach { currentPermissionResult ->
                itemPermissions.add(
                    ItemPermission(
                        currentPermissionResult,
                        requireActivity(),
                        viewLifecycleOwner
                    )
                )
            }
            itemPermissions.forEach {
                it.permissionResult.completeDescription(requireContext())
            }
            val finalList=itemPermissions.distinctBy {
                it.permissionResult.name
            }
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(finalList))
            }
        }

    }
    fun openSettings(){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", "com.kola.kolaapplication", null)
        intent.data = uri
        ContextCompat.startActivity(requireContext(), intent, null)
    }

    open fun checkForPermissions(permission: String): Boolean {
        return (checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED)

    }

    open fun checkFOrPermissionss(permissions: List<String>):Boolean{
        var result=true
        permissions.forEach {
            if (!checkForPermissions(it)){
                result=false
                return@forEach
            }
        }
        return result
    }
}