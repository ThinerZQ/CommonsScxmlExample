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

      //����������صĶ�д���ݿ������
        System.out.println("����ˣ�"+name);
        System.out.println("���ԭ��"+reason);
        System.out.println("��ʼʱ�䣺"+from);
        System.out.println("����ʱ�䣺" + to);
    }

    private void departmentApprove(){
        //�������ݿ�
        System.out.println("���ž���ͬ�����");
    }


    private void personnelApprove(){
        //�������ݿ�
        System.out.println("���¾���ͬ�����");
    }

    private void rejectd(){
        sendEmail(false);
    }
    private void approve(){
        sendEmail(true);
    }

    private void sendEmail(boolean b){

        //��ȡ���ݿ⣬˭ͬ���ˣ����������ͨ�������������ݽ����ݣ�
        //����Ϊ�˷��������ѡ���״̬�����洫����

        if (true){
            System.out.println(":��ã��������Ѿ�ͨ����");
        }else{
            System.out.println(":��ã�������û��ͨ������������д");
        }
    }
    private void end(){
        System.out.println("��ʼ�鵵");
    }




}
