package kr.ifttutilities.uber.settings


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.content.pm.ShortcutInfoCompat
import android.support.v4.content.pm.ShortcutManagerCompat
import android.support.v4.graphics.drawable.IconCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import homeAddress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_uber_setting.*
import kr.ifttutilities.AppPreferenceManager
import kr.ifttutilities.R
import kr.ifttutilities.data.remote.Api
import kr.ifttutilities.uber.UberShortcutActivity
import kr.ifttutilities.uber.UberUtils
import kr.ifttutilities.uber.model.ProductWrapper
import kr.ifttutilities.uber.setUberPoolProductId


class UberSettingFragment : Fragment() {

    private var disposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_uber_setting, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            pinnedShortcutButtonFromHomeToWork.visibility = View.GONE
            pinnedShortcutButtonFromWorkToHome.visibility = View.GONE
        }
        buttonGetProducts.setOnClickListener {
            disposable = Api.getAppApi(activity)
                    .getUberProducts(UberUtils.accessToken, homeAddress.lat.toString(), homeAddress.lng.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : DisposableSingleObserver<ProductWrapper>() {
                        override fun onError(e: Throwable) {
                            Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
                        }

                        override fun onSuccess(productWrapper: ProductWrapper) {
                            val product = productWrapper.products?.find { it.shortDescription == "POOL" }
                            product?.run {
                                AppPreferenceManager.getInstance(activity).setUberPoolProductId(productId)
                            }
                        }

                    })
        }

        pinnedShortcutButtonFromHomeToWork.setOnClickListener {
            createPinShortcutFromHomeToWork()
        }
        pinnedShortcutButtonFromWorkToHome.setOnClickListener {
            createPinShortcutFromWorkToHome()
        }
        pinnedShortcutButtonToHome.setOnClickListener {
            createPinShortcutToHome()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    inner class ShortcutCreatedReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            createPinShortcutFromWorkToHome()
        }

    }

    private fun createPinShortcutToHome() {
        val workToHomeShortcut = ShortcutInfoCompat.Builder(activity, "ToHome")
                .setShortLabel("ToHome")
                .setLongLabel("Book uber to home")
                .setIcon(IconCompat.createWithResource(activity, R.drawable.ic_location_searching))
                .setIntent(UberShortcutActivity.createIntent(activity, UberShortcutActivity.ACTION_TO_HOME).also {
                    it.action = "kr.e.shortcutaction"
                })
                .build()
        ShortcutManagerCompat.requestPinShortcut(activity, workToHomeShortcut, null)
    }


    private fun createPinShortcutFromWorkToHome() {
        val workToHomeShortcut = ShortcutInfoCompat.Builder(activity, "workToHome")
                .setShortLabel("workToHome")
                .setLongLabel("Book uber from work to home")
                .setIcon(IconCompat.createWithResource(activity, R.drawable.ic_home_black))
                .setIntent(UberShortcutActivity.createIntent(activity, UberShortcutActivity.ACTION_WORK_TO_HOME).also {
                    it.action = "kr.e.shortcutaction"
                })
                .build()
        ShortcutManagerCompat.requestPinShortcut(activity, workToHomeShortcut, null)
    }

    private fun createPinShortcutFromHomeToWork() {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(activity)) {
            val homeToWorkShortcut = ShortcutInfoCompat.Builder(activity, "homeToWork")
                    .setShortLabel("homeToWork")
                    .setLongLabel("Book uber from home to work")
                    .setIcon(IconCompat.createWithResource(activity, R.drawable.ic_work_black))
                    .setIntent(UberShortcutActivity.createIntent(activity, UberShortcutActivity.ACTION_HOME_TO_WORK).also {
                        it.action = "kr.e.shortcutaction"
                    })
                    .build()
            ShortcutManagerCompat.requestPinShortcut(activity, homeToWorkShortcut, null)
        } else
            Toast.makeText(activity, "Pinned shortcuts are not supported!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable?.dispose()
    }


    companion object {
        fun newInstance(): UberSettingFragment {
            val fragment = UberSettingFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}
