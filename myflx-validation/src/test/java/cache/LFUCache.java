package cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LFUCache {

    private int capacity;
    private int minFreq;
    private Map<Integer, Node> cacheTable = new HashMap<>();
    private Map<Integer, LinkedList<Node>> freTable = new HashMap<>();

    public static class Node {
        private int key;
        private int value;
        private int fre;

        public Node(int key, int value, int fre) {
            this.key = key;
            this.value = value;
            this.fre = fre;
        }
    }

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
    }

    public void put(int key, int value) {
        if (capacity <= 0) {
            return;
        }
        if (!cacheTable.containsKey(key)) {
            if (cacheTable.size() == capacity) {
                final Node last = freTable.get(minFreq).peekLast();
                cacheTable.remove(last.key);
                freTable.get(minFreq).pollLast();
                if (freTable.get(minFreq).size() == 0) {
                    freTable.remove(minFreq);
                }
            }
            LinkedList<Node> list = freTable.getOrDefault(1, new LinkedList<>());
            final Node node = new Node(key, value, 1);
            list.offerFirst(node);
            freTable.put(1, list);
            cacheTable.put(key, node);
            minFreq = 1;
        } else {
            final Node node = cacheTable.get(key);
            int fre = node.fre;
            freTable.get(fre).remove(node);
            if (freTable.get(fre).size() == 0) {
                freTable.remove(fre);
                if (minFreq == fre) {
                    minFreq += 1;
                }
            }
            final LinkedList<Node> orDefault = freTable.getOrDefault(fre + 1, new LinkedList<>());
            node.fre = fre + 1;
            node.value = value;
            orDefault.offerFirst(node);
            freTable.put(fre + 1, orDefault);
        }
    }

    public Integer get(int key) {
        //
        if (capacity <= 0) {
            return -1;
        }
        // 如果哈希表中没有键 key，返回 -1
        if (!cacheTable.containsKey(key)) {
            return -1;
        }
        final Node node = cacheTable.get(key);
        int fre = node.fre;
        final LinkedList<Node> nodes = freTable.getOrDefault(fre, new LinkedList<>());
        nodes.remove(node);
        if (nodes.size() == 0) {
            freTable.remove(fre);
            if (minFreq == fre) {
                minFreq += 1;
            }
        }
        node.fre = fre + 1;
        final LinkedList<Node> orDefault = freTable.getOrDefault(fre + 1, new LinkedList<>());
        orDefault.addFirst(node);
        freTable.put(fre + 1, orDefault);
        return node.value;
    }

    public static void main(String[] args) {
        LFUCache cache = new LFUCache(2 /* capacity (缓存容量) */);

        cache.put(1, 1);
        cache.put(2, 2);
        cache.get(1);       // 返回 1
        cache.put(3, 3);    // 去除 key 2
        cache.get(2);       // 返回 -1 (未找到key 2)
        cache.get(3);       // 返回 3
        cache.put(4, 4);    // 去除 key 1
        cache.get(1);       // 返回 -1 (未找到 key 1)
        cache.get(3);       // 返回 3
        cache.get(4);       // 返回 4
    }
}
