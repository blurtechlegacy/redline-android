package tech.blur.redline.features.signin.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import tech.blur.redline.core.model.User
import tech.blur.redline.core.model.UserLoginPass
import tech.blur.redline.core.model.Wrapper

interface SignInApi {

    @POST("users/auth/")
    fun authUser(@Body userLoginPass: UserLoginPass): Call<Wrapper<User>>

}