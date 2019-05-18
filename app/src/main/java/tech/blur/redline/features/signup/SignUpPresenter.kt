package tech.blur.redline.features.signup

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import tech.blur.redline.App
import tech.blur.redline.core.model.Wrapper
import tech.blur.redline.core.model.rUser
import tech.blur.redline.features.signup.api.SignUpApi
import javax.inject.Inject

@InjectViewState
class SignUpPresenter : MvpPresenter<SingUpView>() {


    @Inject
    lateinit var retrofit: Retrofit

    private val signUpApi: SignUpApi

    init {
        App.INSTANCE.getAppComponent().inject(this)
        signUpApi = retrofit.create(SignUpApi::class.java)
    }

    var name = ""
    var password = ""
    var login = ""
    var prefs: ArrayList<String> = ArrayList()

    fun regUser(){
        if (name.isNotBlank() && password.isNotBlank() && login.isNotBlank())
            signUpApi.regUser(rUser(login, name, password, prefs)).enqueue(object : Callback<Void>{
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
    }

    fun getPrefs() {
        signUpApi.getPrefs().enqueue(object : Callback<Wrapper<ArrayList<String>>> {
            override fun onFailure(call: Call<Wrapper<ArrayList<String>>>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(
                call: Call<Wrapper<ArrayList<String>>>,
                response: Response<Wrapper<ArrayList<String>>>
            ) {
                if (response.body() != null)
                    viewState.showPrefsList(response.body()!!.data)
            }

        })
    }

}