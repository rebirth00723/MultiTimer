package com.example.multitimer

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import kotlinx.android.synthetic.main.timer_btn.view.*

class TimerAdapter(val timerCtrler: TimerController): BaseAdapter() {

    private lateinit var timerList: Map<String, Long>
    private val noticeTimeA = timerCtrler.noticeTimeA
    private val noticeTimeB = timerCtrler.noticeTimeB
    private val cntDwnTime = timerCtrler.cntDwnTime

    override fun getItem(position: Int): Timer  {
        val pair = timerList.toList()[position]
        return Timer(pair.first, pair.second)
    }

    override fun getItemId(position: Int): Long = 0L

    override fun getCount(): Int                {
        timerList = timerCtrler.getTimerList()
        return timerList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: View.inflate(parent?.context, R.layout.timer_btn, null)
        val btn: Button = view.button
        val timer = getItem(position)
        var elapsedTime: Long

        /*suspend version
        if(timerCtrler.getTimerSuspendState(timer.tag)){//暫停中
            elapsedTime = timerCtrler.getSuspendTimer(timer.tag).timeStamp / 1000
        }else {
            elapsedTime = (System.currentTimeMillis() - timer.timeStamp) / 1000
        }*/
        elapsedTime = (System.currentTimeMillis() - timer.timeStamp) / 1000

        val remainingTime = cntDwnTime - elapsedTime

        when(elapsedTime){//set frame color
            in noticeTimeA until noticeTimeB -> btn.setBackgroundResource(R.drawable.timer_y)
            in noticeTimeB..cntDwnTime  -> btn.setBackgroundResource(R.drawable.timer_r)
            else -> btn.setBackgroundResource(R.drawable.timer_o)
        }

        btn.text = String.format(view.resources.getString(R.string.TimerText), timer.tag, "${remainingTime/60}:${remainingTime%60}")


        btn.setOnClickListener {
            timerCtrler.rmvTimer(timer.tag)
        }
        /*btn.setOnLongClickListener {
            println("LongCLick:${timer.tag}")
            if(timerCtrler.getTimerSuspendState(timer.tag)){
                timerCtrler.resumeTimer(timer.tag)
            }else{
                timerCtrler.suspendTimer(timer.tag)
            }
            true
        }*/
        return view
    }


}