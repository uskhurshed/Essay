package com.easyapps.zkplayer.utils


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

object OtherFunction{
    fun share(context: Context,appName:String,shareMessage: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,appName)
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        context.startActivity(Intent.createChooser(shareIntent, "Выбор:"))
    }
    fun copyClipboard(context: Context,text: String){
        val clipboard = context.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Status", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Скопировано!", Toast.LENGTH_LONG).show()
    }
    fun sendEmail(context: Context) {
        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf("underamour188@gmail.com"))
        email.putExtra(Intent.EXTRA_SUBJECT, "Написать письмо")
        email.type = "message/rfc822"
        context.startActivity(Intent.createChooser(email, "Выбор:"))
    }

    fun goWithUrl(context: Context,url:String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }


}
