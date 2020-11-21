package stack;

import java.util.ArrayList;
import java.util.List;

/**
 * 不使用额外空间(实际使用相同列表也是占用了额外空间)
 */
public class MinStack3 {
    int min;
    List<Integer> list = new ArrayList<>();

    public MinStack3() {

    }

    public void push(int x) {
        if (list.size() == 0) {
            min = x;
        } else if (x <= min) {
            list.add(min);
            min = x;
        }
        list.add(x);

    }

    public void pop() {
        if (list.size() == 0) {
            return;
        }
        Integer remove = list.remove(list.size() - 1);
        if (remove == min && list.size() > 0) {
            min = list.remove(list.size() - 1);
        }
    }

    public Integer top() {
        return list.size() == 0 ? null : list.get(list.size() - 1);
    }

    public int getMin() {
        return min;
    }

    public static void main(String[] args) {
        MinStack3 minStack = new MinStack3();
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
