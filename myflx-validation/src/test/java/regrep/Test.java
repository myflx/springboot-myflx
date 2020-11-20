package regrep;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Test {
    public boolean isMatch(String s, String p) {
        return false;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final Future<?> submit = Executors.newWorkStealingPool(2).submit(() -> System.out.println("666"));
        System.out.println(submit.get());
    }
}
