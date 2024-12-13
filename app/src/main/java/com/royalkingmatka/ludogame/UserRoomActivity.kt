package com.royalkingmatka.ludogame

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.Secure.ANDROID_ID
import android.util.Log
import android.view.LayoutInflater
import com.android.winmo.util.hide
import com.android.winmo.util.toast
import com.google.firebase.FirebaseApp

import com.royalkingmatka.ludogame.base.BaseActivity

import com.google.firebase.database.*
import com.royalkingmatka.ludogame.LudoMultiPlayerActivity
import com.royalkingmatka.ludogame.constant.LudoConstants.DEVICE_ID
import com.royalkingmatka.ludogame.constant.LudoConstants.FB_LUDO
import com.royalkingmatka.ludogame.constant.LudoConstants.ROOM_ID
import com.royalkingmatka.ludogame.constant.LudoConstants.ROOM_OWNER
import com.royalkingmatka.ludogame.constant.LudoConstants.SELECTED_PLAYER
import com.royalkingmatka.ludogame.databinding.ActivityUserRoomBinding
import com.royalkingmatka.model.Ludo
import java.util.*
import kotlin.properties.Delegates

class UserRoomActivity : BaseActivity<ActivityUserRoomBinding>() {

    private lateinit var roomKey: String
    private var isRoomOwner by Delegates.notNull<Boolean>()
    private val TAG = "UserRoomActivity"
    private lateinit var deviceId: String
    private lateinit var dbReference: DatabaseReference
    private var playerNo = -1
    private var ludo = Ludo()
    private var gStart = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //FirebaseApp.initializeApp(this)
        init()
        binding.btnStartGame.setOnClickListener { startGameBtn() }
        binding.player1.setOnClickListener { selectPlayer(1) }
        binding.player2.setOnClickListener { selectPlayer(2) }
        binding.player3.setOnClickListener { selectPlayer(3) }
        binding.player4.setOnClickListener { selectPlayer(0) }

        if (isRoomOwner) initFireBase()
        fbListener()
        updateUi()
    }

    private fun updateUi() {
        if (ludo.player0 == 1 && ludo.player1 == 1 && ludo.player2 == 1 && ludo.player3 == 1) {
            toast("Room already full")
            finish()
        }
        val sum=ludo.player0+ludo.player1+ludo.player2+ludo.player3
        if(sum==4) startGameBtn()
        if(!isRoomOwner) binding.btnStartGame.hide()
        if(ludo.gameStart == 1 && !isRoomOwner && !gStart) {
            startGameBtn()
        }
        if (ludo.player0 == 1) binding.player4.hide()
        if (ludo.player1 == 1) binding.player1.hide()
        if (ludo.player2 == 1) binding.player2.hide()
        if (ludo.player3 == 1) binding.player3.hide()
    }

    private fun fbListener() {
        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value == null){
                    toast("Room does not exist....")
                    this@UserRoomActivity.finish()
                    return
                }
                ludo = snapshot.getValue(Ludo::class.java)!!
                updateUi()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun selectPlayer(player: Int) {
        if (playerNo != -1) return
        playerNo = player

        val map = HashMap<String, Any>()
        map["player$player"] = 1
        dbReference.updateChildren(map)
    }

    private fun initFireBase() {
        val ludo = Ludo(
            0, 0, deviceId, "4", "0", " 0", "0", "0", " 0", "0", "0", " 0", "0", " 0",
            "0", "0", " 0", "0", "0", " 0", "0", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0
        )
        dbReference.setValue(ludo)
            .addOnFailureListener { e -> Log.d(TAG, "initFireBase: $e") }
            .addOnSuccessListener { Log.d(TAG, "onSuccess: ") }
    }

    private fun startGameBtn() {
        if(ludo.gameStart==0) {
            gStart = true

            val map = HashMap<String, Any>()


            map["gameStart"] = 1
            dbReference.updateChildren(map)

            val intent = Intent(this, LudoMultiPlayerActivity::class.java)
            intent.putExtra(ROOM_ID, roomKey)
            intent.putExtra(DEVICE_ID, deviceId)
            intent.putExtra(SELECTED_PLAYER, playerNo)
            startActivity(intent)
            finish()
        }
    }

    private fun init() {
        //FirebaseApp.initializeApp(this)
        deviceId = Settings.Secure.getString(this.contentResolver, ANDROID_ID)
        intent.extras!!.apply {
            roomKey = getString(ROOM_ID)!!
            isRoomOwner = getBoolean(ROOM_OWNER)
        }
        binding.tvRoomKey.text = roomKey
         dbReference = FirebaseDatabase.getInstance().reference.child(FB_LUDO).child(roomKey)

    }

    override fun activityBinding(layoutInflater: LayoutInflater) =
        ActivityUserRoomBinding.inflate(layoutInflater)

}