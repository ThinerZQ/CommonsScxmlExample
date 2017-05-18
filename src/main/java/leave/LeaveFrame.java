

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
import sun.swing.SwingAccessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class LeaveFrame extends JFrame implements ActionListener {

    // 得到显示器屏幕的宽高
    public static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

    private static final long serialVersionUID = 1L;
    private static Resource resource;
    //一系列的按钮，标签
    private JLabel applicant;
    private JLabel reason;
    private JLabel from;
    private JLabel to;
    private JLabel currentUserLabel;

    private JTextField nameTest;
    private JTextField reasonTest;
    private JTextField fromTest;
    private JTextField toTest;

    private JButton submit;
    private JButton continueFill;
    private JButton archive;
    private JButton start;
    private JButton logout;

    //登录
    private JButton confirm;
    private JButton cancel;

    private JTextField usernameField;
    private JTextField positionField;

    private JLabel usernameLabel;
    private JLabel positionLabel;

    private Map<String, LeaveEntity> leaveUsers = new HashMap<String, LeaveEntity>();
    private Map<String, Employe> currentUser = new HashMap<String, Employe>();

    private SCXMLExecutor executor = null;

    public LeaveFrame() {
        super("SCXML Leave");
        //initUI();
        setContentPane(initLogin());
    }

    public static void main(String[] args) {
        new LeaveFrame();
    }

    /**
     * 初始化请假流程
     */
    private void initWorkflow() {
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
            leaveUsers.put(currentUser.entrySet().iterator().next().getKey(), leaveEntity);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(executor.getGlobalContext().getSystemContext().get("_sessionid"));
    }

    /**
     * 监听器里面的方法，根据界面上的事件来触发对应的转移
     *
     * @param event
     */
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();
        try {
            if ("submit".equals(command)) {
                //记录下请假人的请假的信息
                setEnabledAndDisabled(new JComponent[]{}, new JComponent[]{submit});

                //添加请假的内容进去
                Map<String, String> payloadData = new HashMap<String, String>();
                payloadData.put("name", nameTest.getText());
                payloadData.put("reason", reasonTest.getText());
                payloadData.put("from", fromTest.getText());
                payloadData.put("to", toTest.getText());
                //生成表单填完的事件，携带外部数据进去，然后触发
                executor.triggerEvent(new TriggerEvent("fill.end", TriggerEvent.SIGNAL_EVENT, payloadData));


            } else if ("continueFill".equals(command)) {

                //setEnabledAndDisabled(new JComponent[]{submit}, new JComponent[]{ reject, continueFill, archive});
                executor.triggerEvent(new TriggerEvent("goFilling", TriggerEvent.SIGNAL_EVENT));
                setEnabledAndDisabled(new JComponent[]{submit,nameTest,reasonTest,fromTest,toTest }, new JComponent[]{continueFill,start});
            } else if ("archive".equals(command)) {
                //setEnabledAndDisabled(new JComponent[]{}, new JComponent[]{ reject, continueFill, archive, submit});
                executor.triggerEvent(new TriggerEvent("goEnd", TriggerEvent.SIGNAL_EVENT));
                setEnabledAndDisabled(new JComponent[]{start}, new JComponent[]{continueFill,archive,nameTest,reasonTest,fromTest,toTest });
            } else if ("start".equals(command)) {

                setEnabledAndDisabled(new JComponent[]{submit}, new JComponent[]{start});
                setEnabledAndDisabled(new JComponent[]{reasonTest, fromTest, toTest}, new JComponent[]{nameTest});
                nameTest.setText(currentUser.entrySet().iterator().next().getKey());
                initWorkflow();

            } else if ("confirm".equals(command)) {
                //登录
                if (positionField.getText().trim() != "" && positionField.getText() != null) {
                    //判断登录人员的类型，初始化对应的界面

                    Employe currentEmploye = Resource.getEmploye(positionField.getText().trim());
                    currentUser.clear();
                    currentUser.put(usernameField.getText().trim(), currentEmploye);
                    if (positionField.getText().equals("e")) {
                        //员工登录
                        setContentPane(initEmploye());
                        repaint();
                    } else if (positionField.getText().trim() != "" && positionField.getText().equals("d")) {
                        //部门经理登录
                        setContentPane(initDepartmentManager(usernameField.getText()));
                        repaint();

                    } else if (positionField.getText().trim() != "" && positionField.getText().equals("p")) {
                        //人事经理登录
                        setContentPane(initPersonalManager(usernameField.getText()));
                        repaint();
                    }

                }
            } else if ("logout".equals(command)) {

                setContentPane(initLogin());
                repaint();
            }
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化登录界面
     */

    public JPanel initLogin() {
        JPanel loginPanel = new JPanel();
        //创建组件
        confirm = createButton("confirm", "确认");
        cancel = createButton("cancel", "取消");

        usernameField = new JTextField(10);
        positionField = new JTextField(10);

        usernameLabel = new JLabel("用户名:");
        positionLabel = new JLabel("职  位:");

        JPanel jp1 = new JPanel();
        JPanel jp2 = new JPanel();
        JPanel jp3 = new JPanel();
        //设置布局管理器

        //添加组件
        jp1.add(usernameLabel);
        jp1.add(usernameField);

        jp2.add(positionLabel);
        jp2.add(positionField);

        jp3.add(confirm);
        jp3.add(cancel);

        loginPanel.add(jp1);
        loginPanel.add(jp2);
        loginPanel.add(jp3);

        // 定义窗体的宽高
        int windowsWidth = 280;
        int windowsHeight = 160;

        this.setTitle("登录界面");
        this.setBounds((width - windowsWidth) / 2,
                (height - windowsHeight) / 2, windowsWidth, windowsHeight);
        this.setResizable(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return loginPanel;
    }


    /**
     * 初始化员工请假界面
     */

    public JPanel initEmploye() {

        JPanel employePanel = new JPanel();

        employePanel.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();

        contentPanel.setLayout(new GridLayout(7, 2));

        String username = currentUser.entrySet().iterator().next().getKey();
        currentUserLabel = new JLabel("欢迎您:" + username);
        logout = createButton("logout", "注销登录");

        gridLayoutAdd(contentPanel, new JComponent[]{currentUserLabel, logout});
        setEnabledAndDisabled(new JComponent[]{logout}, new JComponent[]{});
        LeaveEntity leaveEntity = leaveUsers.get(currentUser.entrySet().iterator().next().getKey());

        //如果当前用户没有请过的假，就显示启动请假按钮
        start = createButton("start", "去请假");
        if(leaveEntity ==null){
            setEnabledAndDisabled(new JComponent[]{start}, new JComponent[]{});
        }else{
            setEnabledAndDisabled(new JComponent[]{}, new JComponent[]{start});
        }

        gridLayoutAdd(contentPanel, new JComponent[]{start});

        submit = createButton("submit", "提交");

        applicant = new JLabel("申请人：");
        nameTest = new JTextField(10);


        reason = new JLabel("原因：");
        reasonTest = new JTextField(50);


        from = new JLabel("开始时间：");
        fromTest = new JTextField(10);


        to = new JLabel("结束时间：");
        toTest = new JTextField(10);

        setEnabledAndDisabled(new JComponent[]{}, new JComponent[]{submit,nameTest, reasonTest, fromTest, toTest});
        gridLayoutAdd(contentPanel, new JComponent[]{submit, applicant, nameTest, reason, reasonTest, from, fromTest, to, toTest});

        if(leaveEntity!=null){
            nameTest.setText(leaveEntity.getAppliant());
            reasonTest.setText(leaveEntity.getReason());
            fromTest.setText(leaveEntity.getFrom());
            toTest.setText(leaveEntity.getTo());

            if (leaveEntity.isReject()) {
                //出现继续填写，归档
                continueFill = createButton("continueFill", "继续填写");
                archive = createButton("archive", "取消请假");
                setEnabledAndDisabled(new JComponent[]{continueFill, archive}, new JComponent[]{});
                gridLayoutAdd(contentPanel, new JComponent[]{continueFill, archive});
            }else if(leaveEntity.isDepartmentApprove() && leaveEntity.isPersonnelApprove()){
                JLabel archive = new JLabel("已归档：");
                gridLayoutAdd(contentPanel, new JComponent[]{ archive});
            }
        }
        employePanel.add(contentPanel, BorderLayout.CENTER);

        // 定义窗体的宽高
        int windowsWidth = 320;
        int windowsHeight = 360;

        this.setTitle("员工请假");
        this.setBounds((width - windowsWidth) / 2,
                (height - windowsHeight) / 2, windowsWidth, windowsHeight);
        this.setResizable(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        return employePanel;
    }

    /**
     * 初始化部门主管审批
     */

    public JPanel initDepartmentManager(String username) {
        JPanel departmentManagerPanel = new JPanel();
        departmentManagerPanel.setLayout(new BorderLayout());


        currentUserLabel = new JLabel("欢迎您:" + username);
        logout = createButton("logout", "注销登录");

        setEnabledAndDisabled(new JComponent[]{logout}, new JComponent[]{});


        int counter = 0;
        for (Map.Entry<String, LeaveEntity> entry : leaveUsers.entrySet()) {
            if (!entry.getValue().isDepartmentApprove()) {
                counter++;
            }
        }
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(5*counter+2, 2));

        contentPanel.add(currentUserLabel);
        contentPanel.add(logout);


        //判断有多少个符合条件的请假者
        for (Map.Entry<String, LeaveEntity> entry : leaveUsers.entrySet()) {
            if (!entry.getValue().isDepartmentApprove()) {
                //计算一下有多少个人，

                JLabel applicant = new JLabel("申请人：");
                JTextField nameTest = new JTextField(15);
                nameTest.setText(entry.getKey());

                JLabel reason = new JLabel("原因：");
                JTextField reasonTest = new JTextField(15);
                reasonTest.setText(entry.getValue().getReason());

                JLabel from = new JLabel("开始时间：");
                JTextField fromTest = new JTextField(15);
                fromTest.setText(entry.getValue().getFrom());

                JLabel to = new JLabel("结束时间：");
                JTextField toTest = new JTextField(15);
                toTest.setText(entry.getValue().getTo());

                JButton approve = new JButton("同意");
                JButton reject = new JButton("拒绝");

                approve.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            executor.triggerEvent(new TriggerEvent("approve", TriggerEvent.SIGNAL_EVENT));
                        } catch (ModelException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

                reject.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            executor.triggerEvent(new TriggerEvent("reject", TriggerEvent.SIGNAL_EVENT));
                        } catch (ModelException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

                setEnabledAndDisabled(new JComponent[]{approve, reject}, new JComponent[]{nameTest, reasonTest, fromTest, toTest});
                gridLayoutAdd(contentPanel, new JComponent[]{applicant, nameTest, reason, reasonTest, from, fromTest, to, toTest, approve, reject});
            }
        }
        if (counter == 0) {
            //提示没有请假的人
            JLabel notice = new JLabel("没有请假者，不需要审批");
            contentPanel.add(notice);
        }

        departmentManagerPanel.add(contentPanel, BorderLayout.CENTER);

        // 定义窗体的宽高
        int windowsWidth = 320;
        int windowsHeight = 600;

        this.setTitle("部门主管审批");
        this.setBounds((width - windowsWidth) / 2,
                (height - windowsHeight) / 2, windowsWidth, windowsHeight);
        this.setResizable(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return departmentManagerPanel;
    }

    /**
     * 初始化人事主管审批
     */

    public JPanel initPersonalManager(String username) {

        JPanel personalManagerPanel = new JPanel();
        personalManagerPanel.setLayout(new BorderLayout());

        currentUserLabel = new JLabel("欢迎您:" + username);
        logout = createButton("logout", "注销登录");

        setEnabledAndDisabled(new JComponent[]{logout}, new JComponent[]{});


        int counter = 0;
        for (Map.Entry<String, LeaveEntity> entry : leaveUsers.entrySet()) {
            if (!entry.getValue().isPersonnelApprove() && entry.getValue().isDepartmentApprove()) {
                counter++;
            }
        }
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(5*counter+2, 2));

        contentPanel.add(currentUserLabel);
        contentPanel.add(logout);


        for (Map.Entry<String, LeaveEntity> entry : leaveUsers.entrySet()) {
            if (!entry.getValue().isPersonnelApprove() && entry.getValue().isDepartmentApprove()) {
                //计算一下有多少个人，

                JLabel applicant = new JLabel("申请人：");
                JTextField nameTest = new JTextField(10);
                nameTest.setText(entry.getKey());

                JLabel reason = new JLabel("原因：");
                JTextField reasonTest = new JTextField(10);
                reasonTest.setText(entry.getValue().getReason());

                JLabel from = new JLabel("开始时间：");
                JTextField fromTest = new JTextField(10);
                fromTest.setText(entry.getValue().getFrom());

                JLabel to = new JLabel("结束时间：");
                JTextField toTest = new JTextField(10);
                toTest.setText(entry.getValue().getTo());

                JButton approve = new JButton("同意");
                JButton reject = new JButton("拒绝");

                approve.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            executor.triggerEvent(new TriggerEvent("approve", TriggerEvent.SIGNAL_EVENT));
                        } catch (ModelException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

                reject.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            executor.triggerEvent(new TriggerEvent("reject", TriggerEvent.SIGNAL_EVENT));
                        } catch (ModelException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

                setEnabledAndDisabled(new JComponent[]{approve, reject}, new JComponent[]{nameTest, reasonTest, fromTest, toTest});
                gridLayoutAdd(contentPanel, new JComponent[]{applicant, nameTest, reason, reasonTest, from, fromTest, to, toTest, approve, reject});
            }
        }
        if (counter == 0) {
            //提示没有请假的人
            JLabel notice = new JLabel("没有请假者，不需要审批");
            contentPanel.add(notice);
        }

        personalManagerPanel.add(contentPanel, BorderLayout.CENTER);

        // 定义窗体的宽高
        int windowsWidth = 320;
        int windowsHeight = 600;

        this.setTitle("人事主管审批");
        this.setBounds((width - windowsWidth) / 2,
                (height - windowsHeight) / 2, windowsWidth, windowsHeight);
        this.setResizable(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return personalManagerPanel;
    }

    private void gridLayoutAdd(JPanel content, JComponent[] components) {

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

    private void setEnabledAndDisabled(JComponent[] enabled, JComponent[] disabled) {

        for (int i = 0; i < enabled.length; i++) {
            enabled[i].setEnabled(true);
        }
        for (int i = 0; i < disabled.length; i++) {
            disabled[i].setEnabled(false);
        }
    }
}
