package kr.ifttutilities.uber.settings


import android.os.Bundle
import android.support.v4.app.Fragment
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
        buttonGetProducts.setOnClickListener {
            disposable = Api.getAppApi(activity)
                    .getUberProducts(UberUtils.accessToken, homeAddress.first.toString(), homeAddress.second.toString())
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

    }

    override fun onDestroyView() {
        super.onDestroy()
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
