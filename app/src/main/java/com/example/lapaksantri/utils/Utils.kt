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
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

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

fun formatDate(date: String): String {
    val dateTimeFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale("in", "ID"))
    val dateFormatted = dateTimeFormatter.parse(date)
    val stringDateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale("in", "ID"))
    dateFormatted?.let {
        return stringDateFormatter.format(dateFormatted)
    } ?: return ""
}

fun formatRupiah(number: Double): String{
    val localeID = Locale("in", "ID")
    val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
    return formatRupiah.format(number)
}

fun formatDatePicker(date: Long): String {
    val stringDateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale("in", "ID"))
    return stringDateFormatter.format(date)
}