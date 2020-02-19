package com.example.multitimer

import java.sql.Timestamp
import java.util.*
import kotlin.concurrent.timer

class TimerController(val cntDwnTime: Int,val noticeTimeA: Int,val noticeTimeB: Int){

    private val timerList = mutableMapOf<String, Long>()
    private val suspendTimer: MutableMap<String, Long> = mutableMapOf()

    private var timerTotalCount = 0
        @Synchronized
        get
    var timeACount = 0
        @Synchronized
        get
    var timeBCount = 0
        @Synchronized
        get

    /* unused
    @Synchronized
    public fun getTimerSuspendState(tag: String): Boolean{
        return tag in suspendTimer
    }

    @Synchronized
    public fun suspendTimer(tag: String){
        val timer = getTimer(tag)
        if(!timer.tag.equals(""))
            suspendTimer[tag] = System.currentTimeMillis() - timer.timeStamp//elapsedTimestamp
    }

    @Synchronized
    public fun resumeTimer(tag:String){
        val timer = getSuspendTimer(tag)
        if(timer.tag != ""){
            if(timerList.contains(tag)){
                timerList[tag] = System.currentTimeMillis() - timer.timeStamp
            }else{
                suspendTimer.remove(tag)
            }
        }
    }
    */
    @Synchronized
    public fun getSuspendTimer(tag: String): Timer{
        val value = suspendTimer[tag]
        return if(value == null) Timer("",0) else Timer(tag, value)
    }

    @Synchronized
    public fun addTimer(tag: String){
        timerList[tag] = System.currentTimeMillis()
    }

    @Synchronized
    public fun getTimer(tag: String): Timer{
        val value = timerList[tag]
        return if(value == null) Timer("",0) else Timer(tag, value)
    }

    @Synchronized
    public fun getTimerList(): Map<String, Long> {

        return timerList.toMap()
    }
    @Synchronized
    public fun rmvTimer(tag: String){
        //if(getTimerAmount() > index)//此判斷避免List無資料但Listen queue仍有在列隊的ClickEvent
        timerList.remove(tag)
    }

    @Synchronized
    public fun getTimerAmount(): Int{
        return timerList.size
    }

    @Synchronized
    public fun HouseKeeping(){
        val it = timerList.iterator()

        timeACount = 0
        timeBCount = 0
        timerTotalCount = 0

        while(it.hasNext()){
            val item = it.next()
            val elapsedTime =  (System.currentTimeMillis() - item.value) / 1000

            if(elapsedTime >= cntDwnTime || elapsedTime < 0 ){//Time out or Valid
                if(! suspendTimer.contains(item.key))
                    it.remove()
            }else{//Count
                timerTotalCount++
                when(elapsedTime){
                    in noticeTimeA until noticeTimeB -> timeACount++
                    in noticeTimeB..cntDwnTime  -> timeBCount++
                }
            }
        }
    }
}