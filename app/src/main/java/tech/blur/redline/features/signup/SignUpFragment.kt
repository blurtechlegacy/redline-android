package tech.blur.redline.features.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.fragment_signup.view.*
import tech.blur.redline.R
import tech.blur.redline.core.DefaultTextWatcher
import tech.blur.redline.features.BaseFragment

class SignUpFragment: BaseFragment(), SingUpView{

    var prefsList = ArrayList<String>()

    @InjectPresenter
    lateinit var presenter: SignUpPresenter

    lateinit var prefsGroup: ChipGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(getLayoutID(), container, false)

        presenter.getPrefs()


        prefsGroup = v.prefs_group

        v.edit_signup_name.addTextChangedListener(object : DefaultTextWatcher(){
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.name = s!!.toString()
            }
        })

        v.edit_signup_password.addTextChangedListener(object : DefaultTextWatcher(){
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.password = s!!.toString()
            }
        })

        v.edit_signup_login.addTextChangedListener(object : DefaultTextWatcher(){
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.login = s!!.toString()
            }
        })



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
            prefsGroup.addView(chip) }
    }

    override fun getLayoutID() = R.layout.fragment_signup

    companion object {
        fun newInstance() = SignUpFragment()
    }

}
