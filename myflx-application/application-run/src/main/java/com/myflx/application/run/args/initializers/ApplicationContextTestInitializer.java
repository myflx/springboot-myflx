package com.myflx.application.run.args.initializers;


import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

/**
 * @author LuoShangLin
 */
public class ApplicationContextTestInitializer implements
        ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {
    /**
     * Initialize the given application context.
     *
     * @param applicationContext the application to configure
     */
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.out.println("" +
                "                            _ooOoo_\n" +
                "                           o8888888o\n" +
                "                           88\" . \"88\n" +
                "                           (| -_- |)\n" +
                "                            O\\ = /O\n" +
                "                        ____/`---'\\____\n" +
                "                      .   ' \\\\| |// `.\n" +
                "                       / \\\\||| : |||// \\\n" +
                "                     / _||||| -:- |||||- \\\n" +
                "                       | | \\\\\\ - /// | |\n" +
                "                     | \\_| ''\\---/'' | |\n" +
                "                      \\ .-\\__ `-` ___/-. /\n" +
                "                   ___`. .' /--.--\\ `. . __\n" +
                "                .\"\" '< `.___\\_<|>_/___.' >'\"\".\n" +
                "               | | : `- \\`.;`\\ _ /`;.`/ - ` : | |\n" +
                "                 \\ \\ `-. \\_ __\\ /__ _/ .-` / /\n" +
                "         ======`-.____`-.___\\_____/___.-`____.-'======\n" +
                "                            `=---='\n" +
                "\n" +
                "         .............................................\n" +
                "                  佛祖镇楼                  BUG辟易\n" +
                "          佛曰:\n" +
                "                  写字楼里写字间，写字间里程序员；\n" +
                "                  程序人员写程序，又拿程序换酒钱。\n" +
                "                  酒醒只在网上坐，酒醉还来网下眠；\n" +
                "                  酒醉酒醒日复日，网上网下年复年。\n" +
                "                  但愿老死电脑间，不愿鞠躬老板前；\n" +
                "                  奔驰宝马贵者趣，公交自行程序员。\n" +
                "                  别人笑我忒疯癫，我笑自己命太贱；\n" +
                "                  不见满街漂亮妹，哪个归得程序员？\n" +
                "————————————————\n");
        System.out.println("ApplicationContextTestInitializer is running........");
    }

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
