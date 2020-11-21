package stack;

import java.util.ArrayList;
import java.util.List;

/**
 * 不使用额外空间
 */
public class MinStack {
    long min;
    List<Long> list = new ArrayList<>();

    public MinStack() {

    }

    public void push(int x) {
        if (list.size() == 0) {
            min = x;
        }
        list.add((long) x - min);
        if (x < min) {
            min = x;
        }
    }

    public void pop() {
        if (list.size() == 0) {
            return;
        }
        Long pop = list.remove(list.size() - 1);
        if (pop < 0) {
            min -= pop;
        }

    }

    public Integer top() {
        if (list.size() == 0) {
            return null;
        }
        Long pop = list.get(list.size() - 1);
        if (pop < 0) {
            //负值说明此位置存入的值是当前位置最后下一个负值之前的最小值
            return Math.toIntExact(min);
        }
        return Math.toIntExact(pop + min);
    }

    public int getMin() {
        return (int) min;
    }

    public static void main(String[] args) {
        MinStack minStack = new MinStack();
        minStack.push(395);
        minStack.push(276);
        minStack.top();
        minStack.push(29);
        minStack.push(-482);
        minStack.top();
        minStack.pop();
        minStack.top();

        minStack.push(-108);
        minStack.push(-251);
        minStack.push(-439);
        minStack.push(370);
        minStack.pop();
        minStack.pop();
        minStack.pop();

        minStack.getMin();   //--> 返回 -3.
    }
}
