package com.example.greenmessenger

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.greenmessenger.adapter.UserAdapter
import com.example.greenmessenger.databinding.FragmentMessageBinding
import com.example.greenmessenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage


class MessageFragment : Fragment() {
    private lateinit var binding: FragmentMessageBinding
    var database: FirebaseDatabase? = null
    var users: ArrayList<User>? = null
    var storage: FirebaseStorage? = null
    var userAdapter: UserAdapter? = null
    var dialog: ProgressDialog? = null
    var user:User? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMessageBinding.inflate(layoutInflater)
        dialog = ProgressDialog(context)
        dialog?.setMessage("Uploading image..")
        dialog?.setCancelable(false)
        database = FirebaseDatabase.getInstance()
        users = ArrayList<User>()
        userAdapter = context?.let { UserAdapter(it, users!!) }
        val layoutManager = GridLayoutManager(requireContext(),2)
        binding.mRec.layoutManager = layoutManager
        binding.mRec.adapter = userAdapter
        database!!.reference.child("users")
            .child(FirebaseAuth.getInstance().uid!!)
        database!!.getReference("upperReference").child("users")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    users!!.clear()
                    for (snapshot1 in snapshot.children){
                        val user:User? = snapshot1.getValue(User::class.java)
                        if (!user!!.equals(FirebaseAuth.getInstance().uid))users!!.add(user)

                        userAdapter?.notifyDataSetChanged()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        if (currentId != null) {
            database!!.reference.child("Presence")
                .child(currentId).setValue("online")
        }

    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        if (currentId != null) {
            database!!.reference.child("Presence")
                .child(currentId).setValue("off")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}




