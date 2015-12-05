
package stopwatch;

import org.apache.commons.scxml2.env.AbstractStateMachine;
import org.apache.commons.scxml2.model.ModelException;

import java.util.Timer;
import java.util.TimerTask;


public class StopWatchStateMachine extends AbstractStateMachine {


    public static final String EVENT_START = "watch.start";
    public static final String EVENT_STOP = "watch.stop";
    public static final String EVENT_RESET = "watch.reset";


    private int hr, min, sec, fract;

    private Timer timer;

    public StopWatchStateMachine() throws ModelException {
        super(StopWatchStateMachine.class.
            getResource("stopwatch3.xml"));
    }

    /**
     * 重置当前状态机，方法名和所在的状态名相同，又框架自己调用
     */
    public void reset() {
        hr = min = sec = fract=0;
        timer=null;
    }
    /**
     * 运行秒表，方法名和所在的状态名相同，又框架自己调用
     */
    public void running() {
        if (timer == null) {
            timer = new Timer(true);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    increment();
                }
            }, 100, 100);
        }
    }
    /**
     * 停止秒表，方法名和所在的状态名相同，又框架自己调用
     */
    public void stopped() {
        timer.cancel();
        timer = null;
    }

    /**
     * 得到当前秒表的时间
     * @return
     */
    public synchronized String getDisplay() {
        return String.format("%d:%02d:%02d,%d", hr, min, sec, fract);
    }


    public String getCurrentState() {
        return getEngine().getStatus().getStates().iterator().next().getId();
    }

    /**
     * 自增方法
     */
    private synchronized void increment() {
        if (fract < 9) {
            fract++;
        } else {
            fract = 0;

            if (sec < 59) {
                sec++;
            } else {
                sec = 0;
                if (min < 59) {
                    min++;
                } else {
                    min = 0;
                    hr++;
                }
            }
        }
    }



}

