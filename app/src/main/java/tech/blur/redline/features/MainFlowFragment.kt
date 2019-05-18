package tech.blur.redline.features

import android.os.Bundle
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_main.view.*
import tech.blur.redline.R
import tech.blur.redline.features.map.MapFragment

class MainFlowFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutID(), container, false)
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
            R.id.app_bar_fav -> Toast.makeText(context,"Fav menu item is clicked!", Toast.LENGTH_SHORT)
            R.id.app_bar_search -> Toast.makeText(context,"Search menu item is clicked!", Toast.LENGTH_SHORT)
            R.id.app_bar_settings -> Toast.makeText(context,"Settings menu item is clicked!", Toast.LENGTH_SHORT)
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show(childFragmentManager, bottomNavDrawerFragment.tag)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getLayoutID(): Int = R.layout.fragment_main

    private fun getContainerID(): Int = R.id.flow_container

    companion object {
        fun newInstance() = MainFlowFragment()
    }
}