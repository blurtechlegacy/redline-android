package tech.blur.redline.features
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_bottom_nav_drawer.*
import kotlinx.android.synthetic.main.fragment_bottom_nav_drawer.view.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import tech.blur.redline.core.MvpBottomSheetDialogFragment
import tech.blur.redline.R

class BottomNavigationDrawerFragment: MvpBottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutID(), container, false)
        view.search_button.setOnClickListener { Toast.makeText(context, view.search_edit_text.text, Toast.LENGTH_LONG).show() }
        return view
    }

    private fun getLayoutID(): Int = R.layout.fragment_bottom_nav_drawer

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        close_imageview.setOnClickListener {
            this.dismiss()
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog

            val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
            bottomSheetBehavior.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    if (slideOffset > 0.1) {
                        close_imageview.visibility = View.VISIBLE
                    } else {
                        close_imageview.visibility = View.GONE
                    }
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN-> dismiss()
                        else -> close_imageview.visibility = View.GONE
                    }
                }
            })
        }

        return dialog
    }

    private fun disableNavigationViewScrollbars(navigationView: NavigationView?) {
        val navigationMenuView = navigationView?.getChildAt(0) as NavigationMenuView
        navigationMenuView.isVerticalScrollBarEnabled = false
    }


}