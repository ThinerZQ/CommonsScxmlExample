/**
 * Created by zhengshouzi on 2015/11/20.
 */
package stopwatch;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

import org.apache.commons.scxml2.Context;
import org.apache.commons.scxml2.Evaluator;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.env.SimpleErrorReporter;
import org.apache.commons.scxml2.env.jexl.JexlEvaluator;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.commons.scxml2.model.SCXML;

public class StopWatchFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JLabel displayLabel;

    private JButton startButton;
    private JButton stopButton;
    private JButton resetButton;

    private SCXMLExecutor executor;
    private StopWatchEntity stopWatchEntity;

    public static void main(String[] args) {

        new StopWatchFrame();
    }

    public StopWatchFrame() {
        super("SCXML StopWatch");
        //��ʼ��״̬��
        initStopWatch();
        //��ʼ������
        initUI();
    }



    /**
     * ��������Ҫִ�еķ������Զ�����
     * @param event �¼�Դ
     */
    public void actionPerformed(ActionEvent event) {
        //�õ�����ÿ����ť�ϵ�����
        String command = event.getActionCommand();
        //�Ը�����������жϣ���ִ����Ӧ������
        try {
            if ("START".equals(command)) {
                //����watch.start�¼�����ת��running״̬
                executor.triggerEvent(new TriggerEvent("watch.start", TriggerEvent.SIGNAL_EVENT));
                //����һЩ�а�ť�Ŀɼ���
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                resetButton.setEnabled(false);

            } else if ("STOP".equals(command)) {
                //����watch.stop�¼�����ת��stoped״̬
                executor.triggerEvent(new TriggerEvent("watch.stop", TriggerEvent.SIGNAL_EVENT));

                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                resetButton.setEnabled(true);

            } else if ("RESET".equals(command)) {
                //����watch.reset�¼�����ת��reset״̬
                executor.triggerEvent(new TriggerEvent("watch.reset", TriggerEvent.SIGNAL_EVENT));

                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                resetButton.setEnabled(false);

            }
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }

    /**
     * ��ʼ�����
     */
    private void initStopWatch() {
        //�õ���Դ�ļ�·��
        final  URL STOPWATCH = this.getClass().getResource("stopwatch1.xml");


        //ʵ��������ģ�ͽ�����
        Evaluator evaluator = new JexlEvaluator();

        //ʵ��������
        executor = new SCXMLExecutor(evaluator, null, new SimpleErrorReporter());


        try {
            //������Դ�ļ�,ʵ������һ��SCXML��������֮��һһ��Ӧ
            SCXML scxml = SCXMLReader.read(STOPWATCH);

            //��������һ��SCXMLʵ������Ϊ״̬�����󣬴��뵽�������档
            executor.setStateMachine(scxml);

            //��������ִ�еĸ�������
            Context rootContext = evaluator.newContext(null);
            final StopWatchEntity stopWatchEntity = new StopWatchEntity();
            rootContext.set("stopWatchEntity", stopWatchEntity);
            executor.setRootContext(rootContext);

            //���õ�ǰ����
            this.stopWatchEntity = stopWatchEntity;

            //��ʼ��������
            executor.go();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * ��ʼ������
     */
    private void initUI() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout());
        displayLabel = new JLabel("0:00:00,000");
        displayLabel.setFont(new Font(Font.DIALOG,100,50));
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


        setLocation(250,300);
        setSize(400,200);

        setResizable(true);
        setVisible(true);


        Timer displayTimer = new Timer();

        displayTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                displayLabel.setText(stopWatchEntity.getDisplay());
            }
        }, 100, 100);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    /**
     * ����һ����ť
     * @param command ��ť������
     * @param text ��ť�ϵ��ı�
     * @return ����һ���½��İ�ť
     */
    private JButton createButton(final String command, final String text) {
        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.addActionListener(this);
        return button;
    }

}
