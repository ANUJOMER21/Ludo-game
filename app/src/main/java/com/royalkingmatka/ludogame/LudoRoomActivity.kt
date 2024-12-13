package com.royalkingmatka.ludogame

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.android.winmo.ui.game.ludo.dialog.CreateRoomDialog
import com.android.winmo.ui.game.ludo.dialog.JoinRoomDialog
import com.royalkingmatka.ludogame.base.BaseActivity

import com.android.winmo.util.randomId
import com.google.firebase.FirebaseApp

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.royalkingmatka.ludogame.constant.LudoConstants
import com.royalkingmatka.ludogame.databinding.ActivityLudoRoomBinding
import com.royalkingmatka.model.Ludo

class LudoRoomActivity : BaseActivity<ActivityLudoRoomBinding>() {
    private lateinit var dbReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviceId = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        //FirebaseApp.initializeApp(this)
        binding.btnJoinrandomRoom.setOnClickListener{ getFirstRootIdWithZeroPlayer()}
        binding.btnCreateRoom.setOnClickListener { createRoomBtn() }
        binding.btnJoinRoom.setOnClickListener { joinRoomBtn() }
    }
    private lateinit var deviceId: String
     fun getFirstRootIdWithZeroPlayer() {
        // Get the Firebase Realtime Database reference
         val database: DatabaseReference? = if (FirebaseApp.getApps(this).isNotEmpty()) {
             FirebaseDatabase.getInstance().getReference(LudoConstants.FB_LUDO)
         } else {
             null
         }


         var rootId: String? = null
         database?.addListenerForSingleValueEvent(object :ValueEventListener{
             override fun onDataChange(snapshot: DataSnapshot) {
                 for(child in snapshot.children){
                     val player1=child.child("player0").getValue(Int::class.java)
                     val player2=child.child("player1").getValue(Int::class.java)
                     val player3=child.child("player2").getValue(Int::class.java)
                     val player4=child.child("player3").getValue(Int::class.java)
                     Log.d("PLAYER","$player1 || $player2 || $player3 || $player4")
                     if(player1==0||player2==0||player3==0||player4==0){
                         rootId=child.key
                         break;
                     }
                 }
                 if(rootId==null){
                     createrandomroom()
                 }
                 else
                 {
                     val intent = Intent(this@LudoRoomActivity, UserRoomActivity::class.java)
                     intent.putExtra(LudoConstants.ROOM_ID, rootId)
                     intent.putExtra(LudoConstants.ROOM_OWNER,false)
                     startActivity(intent)
                     Toast.makeText(this@LudoRoomActivity,rootId,Toast.LENGTH_SHORT).show()
                 }





             }

             override fun onCancelled(error: DatabaseError) {

             }

         })
        // Add a listener to retrieve data

    }
    private fun createrandomroom(){
        val roomKey = randomId()
                dbReference =
                    FirebaseDatabase.getInstance().reference.child(LudoConstants.FB_LUDO)
                        .child(roomKey)
                initFireBase()
        val intent = Intent(this@LudoRoomActivity, UserRoomActivity::class.java)
        intent.putExtra(LudoConstants.ROOM_ID, roomKey)
        intent.putExtra(LudoConstants.ROOM_OWNER,true)
        startActivity(intent)
            }





    private fun joinRoomBtn() {
        JoinRoomDialog().show(supportFragmentManager,"Join Room")
    }
    val TAG="LUDO"
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

    private fun createRoomBtn() {
        CreateRoomDialog().show(supportFragmentManager,"Create Room")
    }

    override fun activityBinding(layoutInflater: LayoutInflater) =
        ActivityLudoRoomBinding.inflate(layoutInflater)

}