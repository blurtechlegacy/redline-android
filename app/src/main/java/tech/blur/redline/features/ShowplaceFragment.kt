package tech.blur.redline.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_showplace.view.*
import tech.blur.redline.R
import tech.blur.redline.core.model.Showplace

class ShowplaceFragment : BaseFragment() {

    private lateinit var showplace: Showplace
    lateinit var nameTextView: TextView
    lateinit var descriptionView: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutID(), container, false)

        nameTextView = view.place_name_text
        descriptionView = view.place_descr_text

        val bundle = this.arguments
        if (bundle != null) {
            val jsonString = bundle.getString("Showplace")
            showplace = Gson().fromJson(jsonString, Showplace::class.java)
            showPlace()
        }


        return view
    }

    private fun showPlace() {
        nameTextView.text = showplace.name
        descriptionView.text = showplace.description
    }

    override fun getLayoutID(): Int = R.layout.fragment_showplace

    companion object {
        fun newInstance(showplace: Showplace): ShowplaceFragment {
            val showplaceFragment = ShowplaceFragment()
            val bundle = Bundle()
            bundle.putString("Showplace", Gson().toJson(showplace))
            showplaceFragment.arguments = bundle
            return showplaceFragment
        }
    }
}