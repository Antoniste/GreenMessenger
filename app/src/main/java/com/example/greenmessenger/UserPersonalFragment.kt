package com.example.greenmessenger

import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.greenmessenger.databinding.FragmentUserPersonalFregmentBinding
import com.example.greenmessenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.Date


class UserPersonalFragment : Fragment() {
    private lateinit var binding: FragmentUserPersonalFregmentBinding
    private var auth: FirebaseAuth? = null
    private lateinit var database: DatabaseReference
    var storage: FirebaseStorage? = null
    var selectedImage: Uri? = null
    lateinit var selectedDefaultImage: Uri
    private var dialog: ProgressDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentUserPersonalFregmentBinding.inflate(layoutInflater)
        dialog = ProgressDialog(context)
        dialog!!.setMessage("updating profile")
        dialog!!.setCancelable(false)
        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (data.data != null) {
                val uri = data.data
                val storage = FirebaseStorage.getInstance()
                val time = Date().time
                val reference = storage.reference
                    .child("Profile")
                    .child((time.toString() + ""))
                reference.putFile(uri!!).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        reference.downloadUrl.addOnCompleteListener { uri ->
                            val filePath = uri.toString()
                            val obj = HashMap<String, Any>()
                            obj["image"] = filePath
                            database?.child("user")
                                ?.child(FirebaseAuth.getInstance().uid!!)
                                ?.updateChildren(obj)?.addOnSuccessListener {

                                }
                        }
                    }
                }
                binding.imageView.setImageURI(data.data)
                selectedImage = data.data
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = Firebase.database.reference
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.imageView.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/+"
            startActivityForResult(intent, 45)
        }

        binding.setupBtn.setOnClickListener {
            val name: String = binding.nameBox.text.toString()
            val image: String = binding.imageView.toString()
            if (name.isEmpty()) {
                binding.nameBox.error = "Please type name "
            }
            dialog?.show()
            val reference = auth?.uid?.let { it1 ->
                storage?.reference?.child("Profile")
                    ?.child(it1)
            }
            selectedDefaultImage =
                Uri.parse(
                    ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(
                        R.drawable.avatar
                    ) + '/' + resources.getResourceTypeName(R.drawable.avatar) + '/' + resources.getResourceEntryName(
                        R.drawable.avatar
                    )
                )
            selectedImage?.let { it1 ->
                reference?.putFile(it1)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            reference.downloadUrl.addOnCompleteListener { uri ->
                                val imageUrl = uri.toString()
                                val uid = auth?.uid
                                val user = User(name, imageUrl)
                                if (uid != null) {
                                    database?.child("users")
                                        ?.child(uid)
                                        ?.setValue(user)
                                        ?.addOnCompleteListener {
                                            dialog?.dismiss()
                                            findNavController().navigate(R.id.action_userPersonalFregment_to_messageFragment)
                                        }
                                        ?.addOnCanceledListener {
                                            dialog?.dismiss()
                                        }
                                }
                            }
                        }
                    }
            }
        }
    }
    fun newUser(name: String, profileImage: String) {
        val user = User(name,profileImage)
        database.child("users").setValue(user)
    }
}




