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
import kr.ifttutilities.uber.UberShortcutActivity
import kr.ifttutilities.uber.getUberPoolProductId
import officeAddress

/**
 * Created by krishan on 10/03/18.
 */
class UberMainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View = inflater.inflate(R.layout.fragment_uber_main, container, false)

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

        val rideParamsToHome = RideParameters.Builder()
                .setProductId(poolProductId)
                .setPickupToMyLocation()
                .setDropoffLocation(homeAddress.lat, homeAddress.lng, "Home", "SerenityLayout, Sarjapur fire station, Bangalore")
                .build()

        //todo instead of three button just use one
        requestButtonHomeToWork.setRideParameters(rideParamsHomeToWork)
        requestButtonWorkToHome.setRideParameters(rideParamsWorkToHome)
        requestButtonToHome.setRideParameters(rideParamsToHome)

        arguments?.getString(EXTRAS_ACTION_KEY)?.let {
            if (it == UberShortcutActivity.ACTION_HOME_TO_WORK) {
                requestButtonHomeToWork.performClick()
                activity.finish()
            } else if (it == UberShortcutActivity.ACTION_WORK_TO_HOME) {
                requestButtonWorkToHome.performClick()
                activity.finish()
            } else {
                requestButtonToHome.performClick()
                activity.finish()
            }
        }

        workToHomebutton.setOnClickListener {
            requestButtonWorkToHome.performClick()
        }

        homeToWorkButton.setOnClickListener {
            requestButtonHomeToWork.performClick()
        }

        toHomebutton.setOnClickListener {
            requestButtonToHome.performClick()
        }

    }


    companion object {
        private const val EXTRAS_ACTION_KEY = "action"
        fun newInstance(action: String?): UberMainFragment {
            val fragment = UberMainFragment()
            action?.let {
                fragment.arguments = Bundle().also { it.putString(EXTRAS_ACTION_KEY, action) }
            }
            return fragment
        }
    }
}
