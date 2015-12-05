import leave.LeaveEntity;
import org.apache.commons.scxml2.Context;
import org.apache.commons.scxml2.Evaluator;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.TriggerEvent;
import org.apache.commons.scxml2.env.SimpleErrorReporter;
import org.apache.commons.scxml2.env.jexl.JexlEvaluator;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.SCXML;
import stopwatch.StopWatchEntity;


import java.net.URL;

/**
 * Created by zhengshouzi on 2015/11/28.
 */
public class TransitionTest {
    public static void main(String[] args) {
        //�õ���Դ�ļ�·��
        final URL leaveApprovel = TransitionTest.class.getResource("test.xml");
        //ʵ��������ģ�ͽ�����
        Evaluator evaluator = new JexlEvaluator();

        //ʵ��������
        SCXMLExecutor executor = new SCXMLExecutor(evaluator, null, new SimpleErrorReporter());
        try {
            //������Դ�ļ�,ʵ������һ��SCXML��������֮��һһ��Ӧ
            SCXML scxml = SCXMLReader.read(leaveApprovel);

            //��������һ��SCXMLʵ������Ϊ״̬�����󣬴��뵽�������档
            executor.setStateMachine(scxml);

            //��������ִ�еĸ�������
            Context rootContext = evaluator.newContext(null);
            StopWatchEntity leaveEntity = new StopWatchEntity();
            rootContext.set("stopWatch", leaveEntity);
            executor.setRootContext(rootContext);

            //��ʼ��������
            executor.go();

            executor.triggerEvent(new TriggerEvent("watch.start", TriggerEvent.SIGNAL_EVENT, null));

            Thread.sleep(3000);

            executor.triggerEvent(new TriggerEvent("watch.stop",TriggerEvent.SIGNAL_EVENT,null));



        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(executor.getGlobalContext().getSystemContext().get("_sessionid"));
    }
}
