package tech.blur.redline.features.signin

import android.content.SharedPreferences
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import tech.blur.redline.App
import tech.blur.redline.core.PreferencesApi
import tech.blur.redline.core.model.User
import tech.blur.redline.core.model.UserLoginPass
import tech.blur.redline.core.model.Wrapper
import tech.blur.redline.features.signin.api.SignInApi
import javax.inject.Inject

@InjectViewState
class SignInPresenter: MvpPresenter<SignInView>(){

    var login = ""
    var password = ""

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var prefs:SharedPreferences

    val api:SignInApi

    init {
        App.INSTANCE.getAppComponent().inject(this)
        api = retrofit.create(SignInApi::class.java)
    }

    fun signInUser() {
        if (login.isNotBlank() && password.isNotBlank())
            api.authUser(UserLoginPass(login, password)).enqueue(object : Callback<Wrapper<User>>{
                override fun onFailure(call: Call<Wrapper<User>>, t: Throwable) {
                    viewState.showMessage("Неверные данные")
                }

                override fun onResponse(call: Call<Wrapper<User>>, response: Response<Wrapper<User>>) {
                    PreferencesApi.setUser(prefs, response.body()!!.data)
                    viewState.onUserAuthDone()
                }

            })

    }
}