package leave;

import org.apache.commons.scxml2.env.AbstractStateMachine;
import org.apache.commons.scxml2.model.ModelException;


/**
 * Created by zhengshouzi on 2015/11/24.
 */
public class LeaveEntity1 extends AbstractStateMachine {

    public LeaveEntity1() throws ModelException {

        super(LeaveEntity1.class.getResource("leaveApprove2.xml"));
    }


    public void filling(){

    }
    public void approving(){

    }

    public void departmentApproving(){

    }
    public void personeelApproving(){

    }

    private void filled(String name,String reason,String from,String to){

      //在这里做相关的读写数据库操作。
        System.out.println("请假人："+name);
        System.out.println("请假原因："+reason);
        System.out.println("开始时间："+from);
        System.out.println("结束时间：" + to);
    }

    private void departmentApprove(){
        //更新数据库
        System.out.println("部门经理同意请假");
    }


    private void personnelApprove(){
        //跟新数据库
        System.out.println("人事经理同意请假");
    }

    private void rejectd(){
        sendEmail(false);
    }
    private void approve(){
        sendEmail(true);
    }

    private void sendEmail(boolean b){

        //读取数据库，谁同意了，输出，或者通过方法参数传递进数据，
        //这里为了方便起见，选择从状态机里面传出来

        if (true){
            System.out.println(":你好，你的请假已经通过了");
        }else{
            System.out.println(":你好，你的请假没有通过，请重新填写");
        }
    }
    private void end(){
        System.out.println("开始归档");
    }




}
