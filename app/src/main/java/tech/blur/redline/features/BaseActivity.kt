package tech.blur.redline.features

import android.os.Bundle
import tech.blur.redline.moxy.MvpAndroidxActivity
import tech.blur.redline.moxy.MvpAndroidxFragment

abstract class BaseActivity : MvpAndroidxActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
//        injectDependencies()
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
    }

//    protected abstract fun injectDependencies()

    protected abstract fun getLayoutId(): Int

    protected abstract fun getContainerId(): Int
}