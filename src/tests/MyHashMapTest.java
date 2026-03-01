package tests;

import myMap.MyHashMap;

public class MyHashMapTest {

    public static void main(String[] args) {
        run("Put & Get", MyHashMapTest::testPutAndGet);
        run("Overwrite", MyHashMapTest::testOverwrite);
        run("Remove", MyHashMapTest::testRemove);
        run("Resize", MyHashMapTest::testResize);
        run("Null key", MyHashMapTest::testNullKey);
        run("Contains", MyHashMapTest::testContains);
        run("Clear", MyHashMapTest::testClear);
        run("Collisions", MyHashMapTest::testCollisions);

        System.out.println("\nAll tests passed.");
    }

    static void run(String name, Runnable test) {
        try {
            test.run();
            System.out.println(name + " ✓");
        } catch (Throwable t) {
            System.out.println(name + " ✗");
            throw t;
        }
    }

    static void testPutAndGet() {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        map.put("a", 1);
        map.put("b", 2);

        check(map.size() == 2);
        check(map.get("a") == 1);
        check(map.get("b") == 2);
        check(map.get("c") == null);
    }

    static void testOverwrite() {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        map.put("a", 1);
        map.put("a", 5);

        check(map.size() == 1);
        check(map.get("a") == 5);
    }

    static void testRemove() {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        check(map.remove("b") == 2);
        check(map.size() == 2);
        check(map.get("b") == null);
        check(map.remove("x") == null);
    }

    static void testResize() {
        MyHashMap<Integer, Integer> map = new MyHashMap<>();

        for (int i = 0; i < 100; i++) {
            map.put(i, i * 10);
        }

        check(map.size() == 100);

        for (int i = 0; i < 100; i++) {
            check(map.get(i) == i * 10);
        }
    }

    static void testNullKey() {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        map.put(null, 42);
        check(map.get(null) == 42);

        map.remove(null);
        check(map.get(null) == null);
    }

    static void testContains() {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        map.put("x", 100);

        check(map.containsKey("x"));
        check(!map.containsKey("y"));
        check(map.containsValue(100));
        check(!map.containsValue(200));
    }

    static void testClear() {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        map.put("a", 1);
        map.put("b", 2);

        map.clear();

        check(map.size() == 0);
        check(map.isEmpty());
        check(map.get("a") == null);
    }


    static class BadKey {
        String value;

        BadKey(String value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BadKey)) return false;
            return value.equals(((BadKey) o).value);
        }
    }

    static void testCollisions() {
        MyHashMap<BadKey, Integer> map = new MyHashMap<>();

        BadKey k1 = new BadKey("one");
        BadKey k2 = new BadKey("two");
        BadKey k3 = new BadKey("three");

        map.put(k1, 1);
        map.put(k2, 2);
        map.put(k3, 3);

        check(map.size() == 3);
        check(map.get(k1) == 1);
        check(map.get(k2) == 2);
        check(map.get(k3) == 3);

        map.remove(k2);
        check(map.get(k2) == null);
        check(map.size() == 2);
    }

    static void check(boolean condition) {
        if (!condition) {
            throw new AssertionError();
        }
    }
}