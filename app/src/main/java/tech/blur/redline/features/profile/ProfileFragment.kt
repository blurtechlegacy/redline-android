package tech.blur.redline.features.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import tech.blur.redline.R
import tech.blur.redline.core.model.User
import tech.blur.redline.features.BaseFragment

class ProfileFragment : BaseFragment() {

    lateinit var user: User
    lateinit var prefsGroup: ChipGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutID(), container, false)
        val bundle = this.arguments
        if (bundle != null) {
            val jsonString = bundle.getString("User")
            user = Gson().fromJson(jsonString, User::class.java)
            showUser()
        }
        prefsGroup = view.prefs_group

        return view
    }

    private fun showUser() {
        name_text_view.text = user.name
        nickname_text_view.text = user.login

        edit_profile_name.setText(user.name)
        edit_profile_login.setText(user.login)

        user.preferences.forEach {
            val chip = Chip(prefsGroup.context)
            chip.text = it
            chip.isClickable = true
            chip.isCheckable = true
            //TODO Color state list for the chip
            prefsGroup.addView(chip)
        }

    }

    override fun getLayoutID(): Int = R.layout.fragment_profile

    companion object {
        fun newInstance(user: User) : ProfileFragment {
            val profileFragment = ProfileFragment()
            val bundle = Bundle()
            bundle.putString("User", Gson().toJson(user))
            profileFragment.arguments = bundle
            return profileFragment
        }
    }
}