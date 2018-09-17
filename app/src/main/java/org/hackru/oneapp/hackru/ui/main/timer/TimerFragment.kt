package org.hackru.oneapp.hackru.ui.main.timer

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_timer.*
import org.hackru.oneapp.hackru.R
import org.hackru.oneapp.hackru.api.Resource
import org.hackru.oneapp.hackru.ui.main.MainActivity
import java.util.*
import kotlin.concurrent.timer

class TimerFragment : android.support.v4.app.Fragment() {
    // TAG is used with Android's Log class to organize debugging logs
    val TAG = "TimerFragment"
    private lateinit var viewModel: TimerViewModel
    private lateinit var mainActivity: MainActivity
    var countDownTimer: CountDownTimer? = null

    companion object {
        // Wondering why we use newInstance() instead of a constructor? Read this: https://stackoverflow.com/a/30867846/9968228
        fun newInstance() = TimerFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_timer, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(mainActivity).get(TimerViewModel::class.java)
        viewModel.deadline.observe(this, Observer {
            it?.let {
                when(it.state) {
                    Resource.LOADING -> {
                        // The resource is loading
                    }
                    Resource.SUCCESS -> {
                        // The resource has successfully been fetched
                        val deadline = it.data
                        val millisecondsLeft = deadline * 1000 - GregorianCalendar().timeInMillis

                        if(millisecondsLeft > 0) {
                            timer_progress.max = 86400 // 86400 seconds is 24 hours
                            countDownTimer = object : CountDownTimer(millisecondsLeft, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                    val secondsUntilFinished = millisUntilFinished / 1000
                                    if(secondsUntilFinished < timer_progress.max) {
                                        timer_progress.progress = secondsUntilFinished.toInt()
                                        val secondsDisplay = String.format("%02d", secondsUntilFinished % 60)
                                        val minutesDisplay = String.format("%02d", (secondsUntilFinished / 60) % 60)
                                        val hoursDisplay = String.format("%02d", secondsUntilFinished / 3600)
                                        val timerCount = "$hoursDisplay:$minutesDisplay:$secondsDisplay"
                                        // We do this because it's bad practice to concatenate
                                        // strings inside TextView.setText(). If you didn't know,
                                        // timer_count.text is Kotlin shorthand for timer_count.setText()
                                        timer_count.text = timerCount
                                    } else {
                                        timer_progress.progress = timer_progress.max
                                        timer_count.text = "24:00:00"
                                    }
                                }

                                override fun onFinish() {
                                    timer_count.text = getString(R.string.timer_default_count)
                                }
                            }
                            countDownTimer?.start()
                        }
                    }
                    Resource.FAILURE -> {
                        // There was an error fetching the resource
                        Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

}