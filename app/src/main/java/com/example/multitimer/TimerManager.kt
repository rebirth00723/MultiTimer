package com.example.multitimer

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import kotlinx.android.synthetic.main.timer_btn.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlin.system.measureTimeMillis

class TimerManager {

    var timerCount = 0
    private var timeATotalCount = 0
    private var timeBTotalCount = 0
    private var refreshPeriod = 200L
    private var running = true

    private val timerCtrlerList = mutableListOf<TimerController>()

    init {
        HouseKeeper()
    }

    public fun getController(index: Int): TimerController?{
        return timerCtrlerList[index]
    }
    public fun addController(controller: TimerController): TimerController {
        timerCtrlerList.add(controller)
        return controller
    }

    @Synchronized
    public fun getTimerState(): Pair<Int,Int>{
        return Pair(timeATotalCount, timeBTotalCount)
    }

    fun HouseKeeper (): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            var time = 0L
            while(running) {
                delay(refreshPeriod - time)
                time = measureTimeMillis {
                    HouseKeeping()
                }
            }
        }
    }

    @Synchronized
    private fun HouseKeeping(){
        timeATotalCount = 0
        timeBTotalCount = 0

        timerCtrlerList.forEach{
            it.HouseKeeping()
            timeATotalCount += it.timeACount
            timeBTotalCount += it.timeBCount
        }
    }

}