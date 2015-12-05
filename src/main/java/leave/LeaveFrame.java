

/**
 * Created by zhengshouzi on 2015/11/24.
 */

package leave;

import org.apache.commons.scxml2.Context;
import org.apache.commons.scxml2.Evaluator;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.env.SimpleErrorReporter;
import org.apache.commons.scxml2.env.jexl.JexlEvaluator;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.commons.scxml2.model.SCXML;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class LeaveFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    //一系列的按钮，标签
    private JLabel applicant;
    private JLabel reason;
    private JLabel from;
    private JLabel to;

    private JTextField nameTest;
    private JTextField reasonTest;
    private JTextField fromTest;
    private JTextField toTest;

    private JButton submit;
    private JButton departmentApprove;
    private JButton personeelApprove;
    private JButton reject;
    private JButton continueFill;
    private JButton archive;
    private JButton start;

    private SCXMLExecutor executor=null;

    public LeaveFrame() {
        super("SCXML Leave");
        initUI();
    }

    public static void main(String[] args) {
        new LeaveFrame();
    }

    /**
     * 初始化请假流程
     */
    private void initWorkflow()  {
        //得到资源文件路径
        final URL leaveApprovel = this.getClass().getResource("leaveApprove1.xml");
        //实例化数据模型解析器
        Evaluator evaluator = new JexlEvaluator();

        //实例化引擎
        executor = new SCXMLExecutor(evaluator, null, new SimpleErrorReporter());

        try {
            //加载资源文件,实例化到一个SCXML对象，两者之间一一对应
            SCXML scxml = SCXMLReader.read(leaveApprovel);

            //将这样的一个SCXML实例，作为状态机对象，传入到引擎里面。
            executor.setStateMachine(scxml);

            //设置引擎执行的根上下文
            Context rootContext = evaluator.newContext(null);
            LeaveEntity leaveEntity = new LeaveEntity();
            rootContext.set("leaveEntity", leaveEntity);
            executor.setRootContext(rootContext);

            //开始启动流程
            executor.go();

        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(executor.getGlobalContext().getSystemContext().get("_sessionid"));
    }

    /**
     * 监听器里面的方法，根据界面上的事件来触发对应的转移
     * @param event
     */
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        try {
            if ("submit".equals(command)) {

                setEnabledAndDisabled(new JComponent[]{reject,departmentApprove}, new JComponent[]{submit});

                //添加请假的内容进去
                Map<String, String> payloadData = new HashMap<String, String>();
                payloadData.put("name",nameTest.getText());
                payloadData.put("reason",reasonTest.getText());
                payloadData.put("from", fromTest.getText());
                payloadData.put("to", toTest.getText());
                //生成表单填完的事件，携带外部数据进去，然后触发
                executor.triggerEvent(new TriggerEvent("fill.end", TriggerEvent.SIGNAL_EVENT,payloadData));

            }else if ("departmentApprove".equals(command)){


                setEnabledAndDisabled(new JComponent[]{personeelApprove}, new JComponent[]{departmentApprove});

                executor.triggerEvent(new TriggerEvent("approve", TriggerEvent.SIGNAL_EVENT));

            }else if ("personeelApprove".equals(command)){

                setEnabledAndDisabled(new JComponent[]{}, new JComponent[]{personeelApprove,reject,departmentApprove});
                executor.triggerEvent(new TriggerEvent("approve", TriggerEvent.SIGNAL_EVENT));


            }else if ("reject".equals(command)){

                setEnabledAndDisabled(new JComponent[]{continueFill,archive}, new JComponent[]{submit,personeelApprove,reject,departmentApprove});

                executor.triggerEvent(new TriggerEvent("reject", TriggerEvent.SIGNAL_EVENT));

            }else if ("continueFill".equals(command)){

                setEnabledAndDisabled(new JComponent[]{submit}, new JComponent[]{departmentApprove, personeelApprove, reject, continueFill, archive});

                executor.triggerEvent(new TriggerEvent("goFilling", TriggerEvent.SIGNAL_EVENT));

            }else if ("archive".equals(command)){


                setEnabledAndDisabled(new JComponent[]{}, new JComponent[]{departmentApprove, personeelApprove, reject, continueFill, archive, submit});
                executor.triggerEvent(new TriggerEvent("goEnd", TriggerEvent.SIGNAL_EVENT));
            }else if ("start".equals(command)){

                setEnabledAndDisabled(new JComponent[]{submit}, new JComponent[]{departmentApprove, personeelApprove, reject, continueFill, archive, start});
                setEnabledAndDisabled(new JComponent[]{nameTest,reasonTest,fromTest,toTest}, new JComponent[]{});

                initWorkflow();

            }
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化界面
     */
    private void initUI() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();

        contentPanel.setLayout(new GridLayout(8,2));

        applicant = new JLabel("申请人：");
        nameTest = new JTextField(10);

        reason = new JLabel("原因：");
        reasonTest = new JTextField(50);


        from = new JLabel("开始时间：");
        fromTest = new JTextField(10);


        to = new JLabel("结束时间：");
        toTest = new JTextField(10);

        start = createButton("start","请假");
        submit = createButton("submit","Submit");
        departmentApprove= createButton("departmentApprove","部门同意");
        personeelApprove= createButton("personeelApprove","人事同意");
        reject = createButton("reject","拒绝");
        continueFill = createButton("continueFill","继续填写");
        archive= createButton("archive","结束");



        setEnabledAndDisabled(new JComponent[]{start}, new JComponent[]{departmentApprove, personeelApprove, reject, continueFill, archive, submit});
        setEnabledAndDisabled(new JComponent[]{}, new JComponent[]{nameTest, reasonTest, fromTest, toTest});


        gridLayoutAdd(contentPanel,new JComponent[]{applicant,nameTest,reason,reasonTest,from,fromTest,to,toTest,start,submit,departmentApprove,personeelApprove,reject,continueFill,archive});

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        setLocation(200, 200);
        setSize(400, 400);

        setResizable(true);
        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);


    }

    private void gridLayoutAdd(JPanel content, JComponent[] components){

        for (int i = 0; i < components.length; i++) {
            content.add(components[i]);
        }

    }


    private JButton createButton(final String command, final String text) {
        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.addActionListener(this);
        return button;
    }
    private void setEnabledAndDisabled(JComponent[] enabled,JComponent[] disabled){

        for (int i=0;i<enabled.length;i++){
            enabled[i].setEnabled(true);
        }
        for (int i=0;i<disabled.length;i++){
            disabled[i].setEnabled(false);
        }
    }




}
