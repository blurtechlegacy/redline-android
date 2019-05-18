package tech.blur.redline.features.signin

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

@InjectViewState
class SignInPresenter: MvpPresenter<SignInView>(){
    var login = ""
    var password = ""
}