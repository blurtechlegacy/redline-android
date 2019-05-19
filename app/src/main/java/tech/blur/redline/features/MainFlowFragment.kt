package tech.blur.redline.features

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import tech.blur.redline.App
import tech.blur.redline.R
import tech.blur.redline.core.PreferencesApi
import tech.blur.redline.features.map.MapFragment
import tech.blur.redline.features.profile.ProfileFragment
import tech.blur.redline.features.signin.SignInFragment
import javax.inject.Inject

class MainFlowFragment : BaseFragment() {

    @Inject
    lateinit var prefs: SharedPreferences

    init {
        App.INSTANCE.getAppComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutID(), container, false)
        setHasOptionsMenu(true)

        (activity as MainActivity).setSupportActionBar(view.bottom_app_bar)

        childFragmentManager.beginTransaction()
            .add(getContainerID(), MapFragment.newInstance(), "MAP")
            .commit()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_bottom, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_profile -> {
                val user = PreferencesApi.getUser(prefs)
                if (user == null) {
                    activity!!.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_container, SignInFragment.newInstance(), "SIGNIN")
                        .addToBackStack(null)
                        .commit()
                } else {
                    activity!!.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_container, ProfileFragment.newInstance(user), "PROFILE")
                        .addToBackStack(null)
                        .commit()
                }
            }
            R.id.app_bar_search -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show(childFragmentManager, bottomNavDrawerFragment.tag)
            }
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show(childFragmentManager, bottomNavDrawerFragment.tag)
            }
        }
        return true
    }

    override fun getLayoutID(): Int = R.layout.fragment_main

    private fun getContainerID(): Int = R.id.flow_container

    companion object {
        fun newInstance() = MainFlowFragment()
    }
}