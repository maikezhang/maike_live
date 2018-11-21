package cn.idongjia.live.restructure;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/10/13
 * Time: 下午12:47
 */
public class Task extends TimerTask {

    private Timer timer;

    private int number=0;

    public Task(Timer timer,int number) {
        this.timer = timer;
        this.number=number;
    }
    final int[] i = {0};
    @Override
    public void run() {
        // task to run goes here
        System.out.println("Hello !!!");
//        i[0]++;
//        if(i[0] ==5) {
//            this.cancel();
//            this.timer.cancel();
//        }
        i[0]++;
        if(i[0]==number){
            this.cancel();
            this.timer.cancel();
        }
    }
}
