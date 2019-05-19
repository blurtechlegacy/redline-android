package tech.blur.redline.features.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import tech.blur.redline.App
import tech.blur.redline.R
import tech.blur.redline.core.PreferencesApi
import tech.blur.redline.core.model.User
import tech.blur.redline.features.BaseFragment
import tech.blur.redline.features.MainFlowFragment
import javax.inject.Inject

class ProfileFragment : BaseFragment() {

    @Inject
    lateinit var prefs: SharedPreferences

    lateinit var user: User
    lateinit var prefsGroup: ChipGroup
    lateinit var nameTextView: TextView
    lateinit var nicknameView: TextView
    lateinit var nameEditText: EditText
    lateinit var loginEditText: EditText
    lateinit var logoutButton: Button

    init {
        App.INSTANCE.getAppComponent().inject(this)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutID(), container, false)

        nameTextView = view.name_text_view
        nicknameView = view.nickname_text_view
        nameEditText = view.edit_profile_name
        loginEditText = view.edit_profile_login
        prefsGroup = view.prefs_group
        logoutButton = view.button_logout

        logoutButton.setOnClickListener {
            run {
                PreferencesApi.delData(prefs)
                activity!!.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, MainFlowFragment.newInstance(), "MAIN_FLOW_FRAGMENT")
                    .commit()
            }
        }

        val bundle = this.arguments
        if (bundle != null) {
            val jsonString = bundle.getString("User")
            user = Gson().fromJson(jsonString, User::class.java)
            showUser()
        }


        return view
    }

    private fun showUser() {
        nameTextView.text = user.name
        nicknameView.text = user.login

        nameEditText.setText(user.name)
        loginEditText.setText(user.login)

        user.preferences.forEach {
            val chip = Chip(prefsGroup.context)
            chip.text = it
            //chip.isClickable = true
            //chip.isCheckable = true
            //TODO Color state list for the chip
            prefsGroup.addView(chip)
        }

    }

    override fun getLayoutID(): Int = R.layout.fragment_profile

    companion object {
        fun newInstance(user: User): ProfileFragment {
            val profileFragment = ProfileFragment()
            val bundle = Bundle()
            bundle.putString("User", Gson().toJson(user))
            profileFragment.arguments = bundle
            return profileFragment
        }
    }
}