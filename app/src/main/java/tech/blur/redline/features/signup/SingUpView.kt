package tech.blur.redline.features.signup

import com.arellomobile.mvp.MvpView

interface SingUpView: MvpView{
    fun showPrefsList(list: ArrayList<String>)
    fun onSignUpComplete()
}
