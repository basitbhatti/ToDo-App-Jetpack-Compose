package com.shoppingapp.todoapp_project.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionUtil(val activity: Activity, val requestCode : Int) {

    val permissions = listOf(
        Manifest.permission.SCHEDULE_EXACT_ALARM,
        Manifest.permission.USE_EXACT_ALARM,
        Manifest.permission.POST_NOTIFICATIONS
    )

    fun checkAndRequestPermissions() {
        var list = ArrayList<String>()

        permissions.forEach {
            if(ContextCompat.checkSelfPermission(activity, it) != PERMISSION_GRANTED){
                list.add(it)
                Log.d("TAGPERMISSION", "adding: $it")
            } else {
                Log.d("TAGPERMISSION", "NOTT Adding: $it")
            }
        }

        list.forEach{
            ActivityCompat.requestPermissions(activity, list.toTypedArray(), requestCode)
            Log.d("TAGPERMISSION", "requesting: $it")
        }

    }


}