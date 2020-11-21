package cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 寻找使用最优实现方式
 */
public class LRUCache extends LinkedHashMap<Integer, Integer> {
    private static final long serialVersionUID = 1L;
    protected int maxElements;

    public LRUCache(int maxSize) {
        super(maxSize, 0.75F, true);
        this.maxElements = maxSize;
    }

    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
        return this.size() > this.maxElements;
    }

    @Override
    public Integer get(Object key) {
        Integer integer = super.get(key);
        return Objects.isNull(integer) ? -1 : integer;
    }

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(Objects.equals(cache.get(1), 1));       // 返回  1
        cache.put(3, 3);    // 该操作会使得密钥 2 作废
        System.out.println(Objects.equals(cache.get(2), -1));
        cache.put(4, 4);    // 该操作会使得密钥 1 作废
        System.out.println(Objects.equals(cache.get(1), -1));       // 返回 -1 (未找到)
        System.out.println(Objects.equals(cache.get(3), 3));       // 返回  3
        System.out.println(Objects.equals(cache.get(4), 4));       // 返回  4
    }
}

