package tech.blur.redline.features.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import tech.blur.redline.R
import tech.blur.redline.features.BaseFragment

class SignInFragment: BaseFragment(), SignInView{

    @InjectPresenter
    lateinit var presenter: SignInPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutID() = R.layout.fragment_signin

    companion object {
        fun newInstance() = SignInFragment()
    }

}