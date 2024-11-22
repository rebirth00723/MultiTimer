Project說明： 製作多個欄位來存放計時器，可以當作有很多的紅綠燈倒數計時器，按＋之後可以新增一個計時器，時間沒有特別設計可以進行彈性調整

每一個計時器會倒數30秒，倒數至20秒時會變成黃匡，倒數至10秒時會變成紅匡，倒數至0秒之後計時器會消失 
右上角顯示計時器數量，分別為進入黃匡的數量以及紅匡的數量 

我使用@Synchronized Method 來防止競爭資源

![image](https://github.com/rebirth00723/MultiTimer/blob/master/example.png)


Project中有幾個檔案我說明一下 
TimerController - 用於操作Timer , 此專案建立三個TimerController物件 
TimerManager - 用於管理TimerController，操作TimerController Housekeeping（清除逾時Timer/統計警示時間{紅黃}）
