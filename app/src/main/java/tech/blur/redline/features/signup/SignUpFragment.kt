package tech.blur.redline.features.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_signin.view.*
import kotlinx.android.synthetic.main.fragment_signup.view.*
import tech.blur.redline.R
import tech.blur.redline.core.DefaultTextWatcher
import tech.blur.redline.features.BaseFragment
import tech.blur.redline.features.MainFlowFragment

class SignUpFragment : BaseFragment(), SingUpView {

    var prefsList = ArrayList<String>()

    @InjectPresenter
    lateinit var presenter: SignUpPresenter

    lateinit var prefsGroup: ChipGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(getLayoutID(), container, false)

        presenter.getPrefs()


        prefsGroup = v.prefs_group

        v.edit_signup_name.addTextChangedListener(object : DefaultTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.name = s!!.toString()
            }
        })

        v.edit_signup_password.addTextChangedListener(object : DefaultTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.password = s!!.toString()
            }
        })

        v.edit_signup_login.addTextChangedListener(object : DefaultTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.login = s!!.toString()
            }
        })

        v.reguser_button.setOnClickListener {
            run {
                val prefList: ArrayList<String> = ArrayList()
                for (i: Int in 0 until prefsGroup.childCount)
                    if ((prefsGroup.getChildAt(i) as Chip).isChecked)
                        prefList.add(((prefsGroup.getChildAt(i) as Chip).text.toString()))
                presenter.regUser(prefList)
            }
        }

        return v
    }

    override fun showPrefsList(list: ArrayList<String>) {
        prefsList.addAll(list)
        list.forEach {
            val chip = Chip(prefsGroup.context)
            chip.text = it
            chip.isClickable = true
            chip.isCheckable = true
            //TODO Color state list for the chip
            prefsGroup.addView(chip)
        }
//        prefsGroup.setOnCheckedChangeListener { group, checkedId ->
//            if (!presenter.prefsTags.contains((group.getChildAt(checkedId) as Chip).text.toString())) {
//                presenter.prefsTags.add((group.getChildAt(checkedId) as Chip).text.toString())
//            } else {
//                presenter.prefsTags.remove((group.getChildAt(checkedId) as Chip).text.toString())
//            }
//        }

    }

    override fun getLayoutID() = R.layout.fragment_signup

    override fun onSignUpComplete() {
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(main_container.id, MainFlowFragment.newInstance(), "MAIN_FLOW_FRAGMENT")
            .commit()
    }
    companion object {
        fun newInstance() = SignUpFragment()

    }

}
