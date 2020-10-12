package stack;

import java.util.ArrayList;
import java.util.List;

/**
 * 辅助栈结构
 */
public class MinStack2 {
    int min;
    List<Integer> minList = new ArrayList<>();
    List<Integer> list = new ArrayList<>();

    public MinStack2() {

    }

    public void push(int x) {
        if (list.size() == 0) {
            min = x;
        } else if (x < min) {
            min = x;
        }
        list.add(x);
        minList.add(min);

    }

    public void pop() {
        if (minList.size() == 0) {
            return;
        }
        minList.remove(list.size() - 1);
        list.remove(list.size() - 1);
        if (minList.size() != 0) {
            min = minList.get(minList.size() - 1);
        }
    }

    public Integer top() {
        return list.size() == 0 ? null : list.get(list.size() - 1);
    }

    public int getMin() {
        return min;
    }

    public static void main(String[] args) {
        MinStack2 minStack = new MinStack2();
        minStack.push(395);
        minStack.push(276);
        minStack.push(29);
        minStack.push(-482);
        minStack.pop();

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
