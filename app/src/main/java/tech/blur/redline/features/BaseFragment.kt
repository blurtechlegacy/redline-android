package tech.blur.redline.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tech.blur.redline.moxy.MvpAndroidxFragment

abstract class BaseFragment : MvpAndroidxFragment(){

//    override fun onCreate(savedInstanceState: Bundle?) {
//        injectDependencies()
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(getLayoutID(), container, false)
    }

   //protected abstract fun injectDependencies()

    protected abstract fun getLayoutID(): Int
}
