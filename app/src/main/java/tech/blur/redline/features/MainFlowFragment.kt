package tech.blur.redline.features

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_mainflow.view.*
import tech.blur.redline.App
import tech.blur.redline.R
import tech.blur.redline.core.PreferencesApi
import tech.blur.redline.core.model.Route
import tech.blur.redline.features.map.MapFragment
import tech.blur.redline.features.profile.ProfileFragment
import tech.blur.redline.features.signin.SignInFragment
import javax.inject.Inject

class MainFlowFragment : BaseFragment() {

    @Inject
    lateinit var prefs: SharedPreferences

    var route: Route? = null


    init {
        App.INSTANCE.getAppComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutID(), container, false)
        setHasOptionsMenu(true)

        view.fab_run.setOnClickListener {
            if (PreferencesApi.getUser(prefs) != null)
                (childFragmentManager.findFragmentById(R.id.flow_container) as MapFragment).buildCustomRoute()
            else
                Toast.makeText(context, "Вы должны войти", Toast.LENGTH_SHORT).show()
        }

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
                createBottomFragment()
            }
            android.R.id.home -> {
                createBottomFragment()
            }
        }
        return true
    }

    private fun createBottomFragment() {
        val bottomNavDrawerFragment = BottomNavigationDrawerFragment.newInstance(route)
        bottomNavDrawerFragment.show(childFragmentManager, bottomNavDrawerFragment.tag)
    }

    override fun getLayoutID(): Int = R.layout.fragment_mainflow

    private fun getContainerID(): Int = R.id.flow_container

    companion object {
        fun newInstance() = MainFlowFragment()
    }
}