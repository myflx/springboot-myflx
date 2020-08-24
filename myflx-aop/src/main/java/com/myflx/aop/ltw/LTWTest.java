package com.myflx.aop.ltw;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @see <a href='https://www.cnblogs.com/takumicx/p/10150344.html'/>
 * -javaagent:C:\Users\10394\.m2\repository\org\springframework\spring-agent\2.5.6\spring-agent-2.5.6.jar
 * -javaagent:C:\Users\10394\.m2\repository\org\springframework\spring-instrument\5.0.6.RELEASE\spring-instrument-5.0.6.RELEASE.jar
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CustomLtwConfig.class})
public class LTWTest {
    @Autowired
    private LtwBean ltwBean;

    @Test
    public void testLTW() {

        ltwBean.test();

    }
}
