package com.robosoft.playverse.core.util


import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.playverse.data.util.BlurBuilder
import com.playverse.data.util.BlurEngine
import com.robosoft.playverse.R
import com.robosoft.playverse.feature.presentation.view.gamedetails.GameClickListener
import jp.wasabeef.blurry.Blurry

class AlertDialogUtil {

    fun showCustomAlert(
        context: Context,
        listener: GameClickListener,
        url: String,
        entryFee: Int,
        gameId: Int,
        tournamentId: Int,
    ) {
        val factory = LayoutInflater.from(context)
        val blurEngine: BlurEngine = BlurBuilder()
        val dialogView = factory.inflate(R.layout.custom_game_popup, null)
        val btDismiss = dialogView.findViewById<ImageView>(R.id.btDismissCustomDialog)
//        val blurImg = dialogView.findViewById<ImageView>(R.id.blurImg)
        val popSubmit = dialogView.findViewById<Button>(R.id.pop_submit)
        val amount = dialogView.findViewById<TextView>(R.id.amount)
        val balance = dialogView.findViewById<TextView>(R.id.tvbalanceprice)
        /*Blurry.with(context)
            .radius(10)
            .sampling(8)
            .async()
            .capture(dialogView.findViewById<ImageView>(R.id.blurImg))
            .into(dialogView.findViewById(R.id.blurImg))*/
        /*blurImg.setImageBitmap(
            blurEngine.blur((blurImg.drawable as BitmapDrawable).bitmap, 25)
        )*/

        amount.text = entryFee.toString()
        val customDialog =AlertDialog.Builder(context,R.style.CustomAlertDialog)
            .setView(dialogView)
            .show()


        btDismiss.setOnClickListener {
            try {
            customDialog.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        popSubmit.setOnClickListener {
            try {
                customDialog.dismiss()
                listener.gameLoadfromPopup(url,entryFee,gameId,tournamentId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        customDialog.setCanceledOnTouchOutside(false)

    }
}