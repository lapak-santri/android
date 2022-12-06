package com.example.lapaksantri.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.example.lapaksantri.R
import com.example.lapaksantri.databinding.LoadingDialogBinding
import com.google.android.material.snackbar.Snackbar

fun showErrorSnackbar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(ContextCompat.getColor(view.context, R.color.red))
        .setTextColor(ContextCompat.getColor(view.context, R.color.white))
        .show()
}

fun showSuccessSnackbar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(ContextCompat.getColor(view.context, R.color.primary_green))
        .setTextColor(ContextCompat.getColor(view.context, R.color.white))
        .show()
}

fun createLoadingDialog(context: Context, layoutInflater: LayoutInflater): Dialog {
    val dialogBinding = LoadingDialogBinding.inflate(layoutInflater)
    val loadingDialog = Dialog(context)

    loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    loadingDialog.setContentView(dialogBinding.root)
    loadingDialog.setCancelable(false)

    return loadingDialog
}

fun View.visible(){
    visibility= View.VISIBLE
}

fun View.gone(){
    visibility= View.GONE
}