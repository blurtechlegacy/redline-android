package tech.blur.redline.features

import android.os.Bundle
import tech.blur.redline.R
import tech.blur.redline.features.map.MapFragment
import tech.blur.redline.features.signin.SignInFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        supportFragmentManager.beginTransaction()
            .add(R.id.main_container, SignInFragment.newInstance(), "MAP")
            .commit()

    }


    override fun getLayoutId() = R.layout.activity_main

    override fun getContainerId() = R.id.main_container
}

