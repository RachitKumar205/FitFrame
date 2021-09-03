package com.example.fitframe.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitframe.R
import com.example.fitframe.utils.Extensions.toast
import com.example.fitframe.utils.FirebaseUtils.firebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var poseRecyclerView : RecyclerView
    private lateinit var poseArrayList : ArrayList<Pose>

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        supportActionBar?.hide(); //hide the title bar
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        poseRecyclerView = findViewById(R.id.poseList)
        poseRecyclerView.layoutManager = LinearLayoutManager(this)
        poseRecyclerView.setHasFixedSize(true)

        poseArrayList = arrayListOf<Pose>()
        getPoseData()

        btnSignOut.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, CreateAccountActivity::class.java))
            toast("signed out")
            finish()
        }
    }

    private fun getPoseData() {

        dbref = FirebaseDatabase.getInstance("https://fitframe-6da33-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Poses")
        dbref.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    for (poseSnapshot in snapshot.children){

                        val pose = poseSnapshot.getValue(Pose::class.java)
                        poseArrayList.add(pose!!)

                    }

                    poseRecyclerView.adapter = MyAdapter(poseArrayList)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}
