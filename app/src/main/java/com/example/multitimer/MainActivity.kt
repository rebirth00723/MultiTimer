package com.example.multitimer

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.timer_btn.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.*
class MainActivity : Activity() {

    val TM = TimerManager()
    val countDownTime = 30
    val noticeTimerA = 10
    val noticeTimerB = 20
    var timerTotalCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val TimerGrids = listOf(TimerGrid1, TimerGrid2, TimerGrid3)
        val addBtns = listOf(addBtn1, addBtn2, addBtn3)

        repeat(3){
            val controller = TM.addController(TimerController(countDownTime, noticeTimerA,noticeTimerB))
            TimerGrids[it].adapter = TimerAdapter(controller)
            addBtns[it].setOnClickListener(OnClick(TM, controller))

        }

        Thread{
            while(true){
                Thread.sleep(200L)
                runOnUiThread {
                    TimerGrids.forEach {
                        (it.adapter as BaseAdapter).notifyDataSetChanged()
                    }
                    val pair = TM.getTimerState()
                    timerYellowTxt.text = pair.first.toString()
                    timerRedTxt.text = pair.second.toString()
                }
            }
        }.start()

        Thread{//test
            Thread.sleep(3000)
            repeat(3){val i = it
                val controller = TM.getController(it)
                repeat(10000){val j = it
                    if (controller != null) {

                        controller.addTimer("${'A'+i}$j")
                        Thread.sleep(10L)
                    }
                }
            }//.start()
        }
    }
}

class OnClick(val TM: TimerManager, val timerCtrler: TimerController) : View.OnClickListener{

    override fun onClick(v: View?) {
        var tag = ""

        if(TM.timerCount > 25)//make tag
            tag+='A'+TM.timerCount/26 - 1
        tag+='A'+TM.timerCount%26

        TM.timerCount++

        //println("add:$tag")
        timerCtrler.addTimer(tag)

    }
}

class OnLongClick(): View.OnLongClickListener{

    override fun onLongClick(v: View?): Boolean {
        return true
    }

}


