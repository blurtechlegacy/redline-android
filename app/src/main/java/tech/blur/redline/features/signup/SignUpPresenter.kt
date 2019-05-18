package tech.blur.redline.features.signup

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import tech.blur.redline.App
import tech.blur.redline.features.signup.api.SignUpApi
import javax.inject.Inject

@InjectViewState
class SignUpPresenter: MvpPresenter<SingUpView>() {


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

    public fun getPrefs(){
        signUpApi.getPrefs().enqueue(object : Callback<ArrayList<String>>{
            override fun onFailure(call: Call<ArrayList<String>>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<ArrayList<String>>, response: Response<ArrayList<String>>) {
                viewState.showPrefsList(response.body()!!)
            }

        })
    }

}