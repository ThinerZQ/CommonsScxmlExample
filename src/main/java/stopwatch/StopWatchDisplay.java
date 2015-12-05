
package stopwatch;

import org.apache.commons.scxml2.model.ModelException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;



public class StopWatchDisplay extends JFrame
        implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JLabel displayLabel;
    private JButton startButton;
    private JButton stopButton;
    private JButton resetButton;


    private StopWatchStateMachine stopWatchStateMachine;

    public static void main(String[] args) throws Exception {
        new StopWatchDisplay();
    }

    public StopWatchDisplay() throws ModelException {
        super("SCXML StopWatch StateMachine");
        stopWatchStateMachine = new StopWatchStateMachine();
       initUI();
    }

    /**
     * 监听器需要执行的方法，自动调用
     * @param event 事件源
     */
    public void actionPerformed(ActionEvent event) {
        //得到绑定在每个按钮上的命令
        String command = event.getActionCommand();
        //对各个命令进行判断，在执行相应的内容

            if ("START".equals(command)) {
                //生成watch.start事件，将转到running状态
                stopWatchStateMachine.fireEvent(StopWatchStateMachine.EVENT_START);
                //设置一些列按钮的可见性
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                resetButton.setEnabled(false);

            } else if ("STOP".equals(command)) {
                //生成watch.stop事件，将转到stoped状态
                stopWatchStateMachine.fireEvent(StopWatchStateMachine.EVENT_STOP);

                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                resetButton.setEnabled(true);

            } else if ("RESET".equals(command)) {
                //生成watch.reset事件，将转到reset状态
                stopWatchStateMachine.fireEvent(StopWatchStateMachine.EVENT_RESET);

                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                resetButton.setEnabled(false);

            }

    }

    /**
     * 初始化界面
     */
    private void initUI() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout());
        displayLabel = new JLabel("0:00:00,000");
        displayLabel.setFont(new Font(Font.DIALOG, 100, 50));
        contentPanel.add(displayLabel, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        startButton = createButton("START", "Start");
        buttonPanel.add(startButton);

        stopButton = createButton("STOP", "Stop");
        stopButton.setEnabled(false);
        buttonPanel.add(stopButton);

        resetButton = createButton("RESET", "Reset");
        resetButton.setEnabled(false);
        buttonPanel.add(resetButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


        setLocation(250, 300);
        setSize(400,200);

        setResizable(true);
        setVisible(true);


        Timer displayTimer = new Timer();

        displayTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                displayLabel.setText(stopWatchStateMachine.getDisplay());
            }
        }, 100, 100);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
    /**
     * 创建一个按钮
     * @param command 按钮的命令
     * @param text 按钮上的文本
     * @return 返回一个新建的按钮
     */
    private JButton createButton(final String command, final String text) {
        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.addActionListener(this);
        return button;
    }

}

