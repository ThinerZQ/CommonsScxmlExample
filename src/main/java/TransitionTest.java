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
        //得到资源文件路径
        final URL leaveApprovel = TransitionTest.class.getResource("test.xml");
        //实例化数据模型解析器
        Evaluator evaluator = new JexlEvaluator();

        //实例化引擎
        SCXMLExecutor executor = new SCXMLExecutor(evaluator, null, new SimpleErrorReporter());
        try {
            //加载资源文件,实例化到一个SCXML对象，两者之间一一对应
            SCXML scxml = SCXMLReader.read(leaveApprovel);

            //将这样的一个SCXML实例，作为状态机对象，传入到引擎里面。
            executor.setStateMachine(scxml);

            //设置引擎执行的根上下文
            Context rootContext = evaluator.newContext(null);
            StopWatchEntity leaveEntity = new StopWatchEntity();
            rootContext.set("stopWatch", leaveEntity);
            executor.setRootContext(rootContext);

            //开始启动流程
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
