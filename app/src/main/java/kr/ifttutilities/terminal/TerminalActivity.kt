package kr.ifttutilities.terminal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_terminal.*
import kr.ifttutilities.R
import kr.ifttutilities.data.remote.Api

class TerminalActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terminal)
        buttonSend.setOnClickListener {
            if (editText.text.isNotEmpty()) {
                disposable = Api.getAppApi(this).sendTerminalMessage(editText.text.toString())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(object : DisposableSingleObserver<String>() {
                            override fun onError(e: Throwable) {
                                //todo display error
                            }

                            override fun onSuccess(t: String) {
                                editText.setText("")
                            }

                        })
            }
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, TerminalActivity::class.java)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
