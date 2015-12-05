

/**
 * Created by zhengshouzi on 2015/11/24.
 */

package leave;

import org.apache.commons.scxml2.model.ModelException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;


public class LeaveFrame1 extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

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

    private LeaveEntity1 leaveEntity = null;

    public LeaveFrame1() {
        super("SCXML Leave");
        initUI();
    }
    public static void main(String[] args) {
        new LeaveFrame1();
    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if ("submit".equals(command)) {

            setEnabledAndDisabled(new JComponent[]{reject, departmentApprove}, new JComponent[]{submit});

            //添加请假的内容进去
            Map<String, String> payloadData = new HashMap<String, String>();
            payloadData.put("name", nameTest.getText());
            payloadData.put("reason", reasonTest.getText());
            payloadData.put("from", fromTest.getText());
            payloadData.put("to", toTest.getText());


            leaveEntity.fireEvent("filled");


        } else if ("departmentApprove".equals(command)) {


            setEnabledAndDisabled(new JComponent[]{personeelApprove}, new JComponent[]{departmentApprove});

            leaveEntity.fireEvent("approve");

        } else if ("personeelApprove".equals(command)) {

            setEnabledAndDisabled(new JComponent[]{}, new JComponent[]{personeelApprove, reject, departmentApprove});
            leaveEntity.fireEvent("approve");


        } else if ("reject".equals(command)) {

            setEnabledAndDisabled(new JComponent[]{continueFill, archive}, new JComponent[]{submit, personeelApprove, reject, departmentApprove});
            leaveEntity.fireEvent("reject");

        } else if ("continueFill".equals(command)) {

            setEnabledAndDisabled(new JComponent[]{submit}, new JComponent[]{departmentApprove, personeelApprove, reject, continueFill, archive});

            leaveEntity.fireEvent("goFilling");

        } else if ("archive".equals(command)) {


            setEnabledAndDisabled(new JComponent[]{}, new JComponent[]{departmentApprove, personeelApprove, reject, continueFill, archive, submit});
            leaveEntity.fireEvent("goEnd");

        } else if ("start".equals(command)) {

            setEnabledAndDisabled(new JComponent[]{submit}, new JComponent[]{departmentApprove, personeelApprove, reject, continueFill, archive, start});
            setEnabledAndDisabled(new JComponent[]{nameTest, reasonTest, fromTest, toTest}, new JComponent[]{});

            initWorkflow();

        }
    }

    private void initUI() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();

        contentPanel.setLayout(new GridLayout(8, 2));

        applicant = new JLabel("申请人：");
        nameTest = new JTextField(10);

        reason = new JLabel("原因：");
        reasonTest = new JTextField(50);


        from = new JLabel("开始时间：");
        fromTest = new JTextField(10);


        to = new JLabel("结束时间：");
        toTest = new JTextField(10);

        start = createButton("start", "请假");
        submit = createButton("submit", "Submit");
        departmentApprove = createButton("departmentApprove", "部门同意");
        personeelApprove = createButton("personeelApprove", "人事同意");
        reject = createButton("reject", "拒绝");
        continueFill = createButton("continueFill", "继续填写");
        archive = createButton("archive", "结束");


        setEnabledAndDisabled(new JComponent[]{start}, new JComponent[]{departmentApprove, personeelApprove, reject, continueFill, archive, submit});
        setEnabledAndDisabled(new JComponent[]{}, new JComponent[]{nameTest, reasonTest, fromTest, toTest});


        gridLayoutAdd(contentPanel, new JComponent[]{applicant, nameTest, reason, reasonTest, from, fromTest, to, toTest, start, submit, departmentApprove, personeelApprove, reject, continueFill, archive});

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        setLocation(200, 200);
        setSize(400, 400);

        setResizable(true);
        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);


    }


    private void initWorkflow() {

        try {
            leaveEntity = new LeaveEntity1();
        } catch (ModelException e) {
            e.printStackTrace();
        }

        System.out.println(leaveEntity.getEngine());
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
