package leave;

/**
 * Created by zhengshouzi on 2015/11/24.
 *
 * 这个类是一个实体类，就想领域驱动设计里面的思想一样，每一个实体类不仅要有数据，而且要有实体类对应的方法。
 * 不知道什么是领域驱动设计的，请另行百度。
 */
public class LeaveEntity {

    //请假表单信息，这些字段都可以不需要，凡事需要使用这些字段的地方，都可以选择从状态机里面传递出来，我也刚研究，最好还是加上。
    private String appliant;
    private String reason;
    private String from ;
    private String to;
    private boolean departmentApprove;
    private boolean personnelApprove;
    private boolean reject;


    /**
     * 做表单数据保存操作
     * @param name
     * @param reason
     * @param from
     * @param to
     */
    public void fillForm(String name,String reason,String from,String to){

        this.appliant = name;
        this.reason = reason;
        this.from=from;
        this.to= to;
        System.out.println("请假人："+appliant);
        System.out.println("请假原因："+reason);
        System.out.println("开始时间："+from);
        System.out.println("结束时间："+to);
    }

    /**
     * 更新部门经理同意信息
     * @param b
     */
    public void departmentApprove(boolean b){
        this.departmentApprove = b;
        System.out.println("部门经理同意");
    }
    /**
     * 更新人事经理同意信息
     * @param b
     */
    public void personnelApprove(boolean b){
        this.personnelApprove =b;
        System.out.println("人事经理同意");
    }

    public void reject(boolean b){
        this.reject =b;
        System.out.println("请假被拒绝");
    }
    /**
     * 发送邮件信息
     */
    public void sendEmail(){
        if (departmentApprove && personnelApprove){
            System.out.println(appliant+":你好，你的请假已经通过了");
        }else{
            System.out.println(appliant+":你好，你的请假没有通过，请重新填写");
        }
    }

    /**
     * 自动执行归档功能
     */
    public void archive(){
        System.out.println("开始归档");
    }

    public String getAppliant() {
        return appliant;
    }

    public String getReason() {
        return reason;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public boolean isDepartmentApprove() {
        return departmentApprove;
    }

    public boolean isPersonnelApprove() {
        return personnelApprove;
    }

    public boolean isReject() {
        return reject;
    }
}
