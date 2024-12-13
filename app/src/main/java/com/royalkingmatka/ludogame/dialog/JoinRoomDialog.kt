package com.android.winmo.ui.game.ludo.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment


import com.android.winmo.util.toast
import com.royalkingmatka.ludogame.R
import com.royalkingmatka.ludogame.UserRoomActivity
import com.royalkingmatka.ludogame.constant.LudoConstants.ROOM_ID
import com.royalkingmatka.ludogame.constant.LudoConstants.ROOM_OWNER

class JoinRoomDialog() : DialogFragment() {

    private lateinit var v: View
    private lateinit var roomKey: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        v = requireActivity().layoutInflater.inflate(R.layout.dialog_join_room, null)
        val builder = AlertDialog.Builder(activity)
        builder.setView(v)

        builder.setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog!!.dismiss() }
        builder.setPositiveButton("Join") { _, _ -> joinRoomBtn() }
        return builder.create()
    }

    private fun joinRoomBtn() {
        if (isValid()) {
            try{
                val intent = Intent(activity, UserRoomActivity::class.java)
                intent.putExtra(ROOM_ID, roomKey)
                intent.putExtra(ROOM_OWNER,false)
                startActivity(intent)
            } catch (e : Exception){
                requireActivity().toast("Join again......")
            }
        }
    }

    private fun isValid(): Boolean {
        val etKey = v.findViewById<EditText>(R.id.etRoomKey)
        roomKey = etKey.text.toString()
        etKey.apply {
            if (roomKey.isEmpty()) {
                requireActivity().toast("Room id can't be empty")
                requestFocus()
                return false
            } else if (roomKey.length != 16) {
                requireActivity().toast("Invalid Room Id")
                requestFocus()
                return false
            }
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}