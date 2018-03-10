package kr.ifttutilities.uber.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uber.sdk.android.rides.RideParameters
import homeAddress
import kotlinx.android.synthetic.main.fragment_uber_main.*
import kr.ifttutilities.AppPreferenceManager
import kr.ifttutilities.R
import kr.ifttutilities.uber.getUberPoolProductId
import officeAddress

/**
 * Created by krishan on 10/03/18.
 */
class UberMainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_uber_main, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // preferred choice of uber product
        val poolProductId = AppPreferenceManager.getInstance(activity).getUberPoolProductId()

        val rideParamsHomeToWork = RideParameters.Builder()
                .setProductId(poolProductId)
                .setPickupLocation(homeAddress.lat, homeAddress.lng, "Home", "SerenityLayout, Sarjapur fire station, Bangalore")
                .setDropoffLocation(officeAddress.lat, officeAddress.lng, "Hike", "RGA techpark, Sarjapur, Bangalore")
                .build()

        val rideParamsWorkToHome = RideParameters.Builder()
                .setProductId(poolProductId)
                .setPickupLocation(officeAddress.lat, officeAddress.lng, "Hike", "RGA techpark, Sarjapur, Bangalore")
                .setDropoffLocation(homeAddress.lat, homeAddress.lng, "Home", "SerenityLayout, Sarjapur fire station, Bangalore")
                .build()


        requestButtonHomeToWork.setRideParameters(rideParamsHomeToWork)
        requestButtonWorkToHome.setRideParameters(rideParamsWorkToHome)
    }


    companion object {
        fun newInstance(): UberMainFragment {
            val fragment = UberMainFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
