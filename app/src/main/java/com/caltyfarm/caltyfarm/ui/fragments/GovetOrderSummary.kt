package com.caltyfarm.caltyfarm.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.Vet
import com.caltyfarm.caltyfarm.ui.GoVetActivity
import com.caltyfarm.caltyfarm.utils.BASE_PRICE_GOVET
import com.caltyfarm.caltyfarm.utils.LoggingListener
import com.caltyfarm.caltyfarm.viewmodel.GoVetViewModel
import kotlinx.android.synthetic.main.fragment_govet_order_summary.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.text.NumberFormat
import java.util.*

class GovetOrderSummary : Fragment() {
    companion object {

        private lateinit var vetData: Vet
        private lateinit var polyDistance: String
        private lateinit var polyDuration: String

        fun newInstance(vetData: Vet, polyDistance: String, polyDuration: String): GovetOrderSummary {
            this.vetData = vetData
            this.polyDistance = polyDistance
            this.polyDuration = polyDuration

            return GovetOrderSummary()
        }
    }

    lateinit var viewModel: GoVetViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_govet_order_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (context as GoVetActivity).viewModel

        view.tv_location_from.text = viewModel.getVetAddress()
        view.tv_location_to.text = viewModel.getUserAddress()
        view.tv_total_item_price.text = NumberFormat
            .getCurrencyInstance(Locale("in", "ID"))
            .format(BASE_PRICE_GOVET)
        view.tv_deliv_fee.text = NumberFormat
            .getCurrencyInstance(Locale("in", "ID"))
            .format(viewModel.calculateDelivCost())
        view.tv_total_price.text = NumberFormat
            .getCurrencyInstance(Locale("in", "ID"))
            .format(viewModel.calculateDelivCost() + BASE_PRICE_GOVET)
        view.tv_vet_name.text = vetData.name
        view.tv_vet_distance.text = if(polyDistance.toLong() > 1000) "${polyDistance.toInt()/1000} Km dari Anda" else "${polyDistance.toInt()} m dari Anda"
        if (vetData.profileUrl != "") Glide.with(context!!).load(vetData.profileUrl).listener(LoggingListener<Drawable>()).into(
            view.iv_vet_profile
        )
        view.bt_order_now.onClick {
            viewModel.buildOrder()
        }
    }
}