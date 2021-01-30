package gc;

public class GCTest {
    public Object instance = null;

    /**
     * 对应的参数列表
     * <p>
     * -XX:+PrintGC 输出GC日志
     * -XX:+PrintGCDetails 输出GC的详细日志
     * -XX:+PrintGCTimeStamps 输出GC的时间戳（以基准时间的形式）
     * -XX:+PrintGCDateStamps 输出GC的时间戳（以日期的形式，如 2013-05-04T21:53:59.234+0800）
     * -XX:+PrintHeapAtGC 在进行GC的前后打印出堆的信息
     * -Xloggc:../logs/gc.log 日志文件的输出路径
     */
    public static void main(String[] args) {
        GCTest gcTest = new GCTest();
        GCTest gcTest1 = new GCTest();
        gcTest.instance = gcTest1;
        gcTest1.instance = gcTest;
        System.gc();
    }
}
