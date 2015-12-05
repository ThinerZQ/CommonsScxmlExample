package helloworld;

/**
 * Created by zhengshouzi on 2015/11/19.
 */

import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.SCXML;

import java.net.URL;

public class HelloWorld {

    //ͨ������HelloWorld��������������helloworld.xml��Դ�ļ����õ�URL
    private static final URL HELLOWORLD = HelloWorld.class.getResource("helloworld.xml");

    public static void main(String[] args) throws Exception {

        //�õ�xml�ļ�����Ӧ�� SCXML����
        SCXML scxml = SCXMLReader.read(HELLOWORLD);

        //ʵ����״̬�����棬
        SCXMLExecutor executor = new SCXMLExecutor();

        //���õ���SCXML���󣬽���״̬���������
        executor.setStateMachine(scxml);

        //Ȼ���������.go()��������״̬����
        executor.go();

    }
}