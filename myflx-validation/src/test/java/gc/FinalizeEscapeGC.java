package gc;

import java.util.concurrent.TimeUnit;

public class FinalizeEscapeGC {
    private static FinalizeEscapeGC SAVE_HOOK = null;


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method executed");
        SAVE_HOOK = this;
    }

    public static void main(String[] args) throws InterruptedException {
        SAVE_HOOK = new FinalizeEscapeGC();
        SAVE_HOOK = null;
        System.gc();
        TimeUnit.SECONDS.sleep(2);
        if (SAVE_HOOK != null) {
            System.out.println("yes i am alive!");
        } else {
            System.out.println("i am died");
        }

        SAVE_HOOK = null;
        System.gc();
        TimeUnit.SECONDS.sleep(2);
        if (SAVE_HOOK != null) {
            System.out.println("yes i am alive!");
        } else {
            System.out.println("i am died");
        }

    }
}
