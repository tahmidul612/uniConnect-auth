package com.lakeheadu.uniconnect_auth.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lakeheadu.uniconnect_auth.R
import com.lakeheadu.uniconnect_auth.utils.FirebaseUtils
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        save_button.setOnClickListener {
            updateUserProfileDetails()
        }
    }


    /**
     * Function to update user profile details to the firestore.
     */
    private fun updateUserProfileDetails() {


        FirebaseUtils.user?.let {

            // Get the FirstName from editText and trim the space
            val firstName = et_first_name.text.toString().trim { it <= ' ' }
            val lastName = et_last_name.text.toString().trim { it <= ' ' }
            val displayName = et_displayname.text.toString()
            val department = et_department.text.toString()
            val email = et_email.text.toString()

            // do this instead of calling update_ methods so we don't make multiple requests
            it.firstName = firstName
            it.lastName = lastName
            it.displayName = displayName
            it.department = department
            it.email = email
            it.update()
        }
    }
}