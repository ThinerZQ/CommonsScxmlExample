
package stopwatch;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class StopWatchEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    //ʱ����
    private int hr;
    private int min;
    private int sec;
    //100����
    private int fract;

    private transient Timer timer;

    /**
     * ���õ�ǰ״̬��
     */
    public synchronized void reset() {
        hr = min = sec = fract = 0;
    }

    /**
     * �������
     */
    public synchronized void run() {
        if (timer == null) {
            timer = new Timer(true);
            //ʹ��timer����ʱִ�У���������ÿ100���룬ִ��һ��increment����
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    increment();
                }
            }, 100, 100);
        }
    }

    /**
     * ֹͣ���
     */
    public synchronized void stop() {
        timer.cancel();
        timer = null;
    }

    /**
     * �õ���ǰ����ʱ��
     * @return
     */
    public synchronized String getDisplay() {
        return String.format("%d:%02d:%02d,%d", hr, min, sec, fract);
    }

    /**
     * ��������
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