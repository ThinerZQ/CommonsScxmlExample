package helloworld;

/**
 * Created by zhengshouzi on 2015/11/19.
 */

import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.SCXML;

import java.net.URL;

public class HelloWorld {

    //通过加载HelloWorld类的类加载器加载helloworld.xml资源文件，得到URL
    private static final URL HELLOWORLD = HelloWorld.class.getResource("helloworld.xml");

    public static void main(String[] args) throws Exception {

        //得到xml文件所对应的 SCXML对象
        SCXML scxml = SCXMLReader.read(HELLOWORLD);

        //实例化状态机引擎，
        SCXMLExecutor executor = new SCXMLExecutor();

        //将得到的SCXML对象，交给状态机引擎管理
        executor.setStateMachine(scxml);

        //然后引擎调用.go()方法启动状态机。
        executor.go();

    }
}