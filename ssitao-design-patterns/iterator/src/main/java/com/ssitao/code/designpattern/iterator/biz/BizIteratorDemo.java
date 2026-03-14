package com.ssitao.code.designpattern.iterator.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 业务场景中的迭代器模式应用
 *
 * 典型应用：
 * 1. 分页迭代器 - 大数据量分批处理
 * 2. 筛选迭代器 - 支持条件过滤
 * 3. 转换迭代器 - 数据转换
 * 4. 组合迭代器 - 多个集合联合遍历
 * 5. 深度优先/广度优先遍历
 */
public class BizIteratorDemo {

    public static void main(String[] args) {
        System.out.println("=== 业务场景迭代器示例 ===\n");

        // 1. 分页迭代器
        System.out.println("1. 分页迭代器");
        pagingIteratorDemo();

        // 2. 筛选迭代器
        System.out.println("\n2. 筛选迭代器");
        filterIteratorDemo();

        // 3. 转换迭代器
        System.out.println("\n3. 转换迭代器");
        transformIteratorDemo();

        // 4. 组合迭代器
        System.out.println("\n4. 组合迭代器");
        compositeIteratorDemo();

        // 5. 树形遍历
        System.out.println("\n5. 文件目录遍历");
        fileIteratorDemo();
    }

    /**
     * 分页迭代器 - 处理大数据量
     */
    private static void pagingIteratorDemo() {
        // 模拟100条数据
        List<String> allData = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            allData.add("数据" + i);
        }

        // 创建分页迭代器，每页10条
        PagingIterator<String> iterator = new PagingIterator<>(allData, 10);

        // 遍历所有页
        int pageNum = 1;
        while (iterator.hasNext()) {
            List<String> page = iterator.next();
            System.out.println("第" + pageNum + "页: " + page);
            pageNum++;
        }

        // 直接获取指定页
        System.out.println("\n直接获取第3页:");
        PagingIterator<String> iterator2 = new PagingIterator<>(allData, 10);
        iterator2.skipToPage(2);
        if (iterator2.hasNext()) {
            System.out.println("第3页: " + iterator2.next());
        }
    }

    /**
     * 筛选迭代器 - 支持条件过滤
     */
    private static void filterIteratorDemo() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 过滤偶数
        FilterIterator<Integer> evenIterator = new FilterIterator<>(numbers.iterator(),
            n -> n % 2 == 0);

        System.out.println("偶数:");
        while (evenIterator.hasNext()) {
            System.out.println("  " + evenIterator.next());
        }

        // 过滤大于5的数
        FilterIterator<Integer> gt5Iterator = new FilterIterator<>(numbers.iterator(),
            n -> n > 5);

        System.out.println("\n大于5的数:");
        while (gt5Iterator.hasNext()) {
            System.out.println("  " + gt5Iterator.next());
        }

        // 组合过滤：偶数且大于5
        FilterIterator<Integer> combinedIterator = new FilterIterator<>(
            new FilterIterator<>(numbers.iterator(), n -> n % 2 == 0),
            n -> n > 5
        );

        System.out.println("\n偶数且大于5:");
        while (combinedIterator.hasNext()) {
            System.out.println("  " + combinedIterator.next());
        }
    }

    /**
     * 转换迭代器 - 数据转换
     */
    private static void transformIteratorDemo() {
        List<String> names = Arrays.asList("alice", "bob", "charlie");

        // 转大写
        TransformIterator<String, String> upperIterator = new TransformIterator<>(
            names.iterator(),
            s -> s.toUpperCase()
        );

        System.out.println("转大写:");
        while (upperIterator.hasNext()) {
            System.out.println("  " + upperIterator.next());
        }

        // 转长度
        TransformIterator<String, Integer> lengthIterator = new TransformIterator<>(
            names.iterator(),
            s -> s.length()
        );

        System.out.println("\n转长度:");
        while (lengthIterator.hasNext()) {
            System.out.println("  " + lengthIterator.next());
        }

        // 链式转换
        System.out.println("\n链式转换 (转大写 -> 加前缀):");
        TransformIterator<String, String> chainIterator = new TransformIterator<>(
            upperIterator,
            s -> "[NAME]" + s
        );
        while (chainIterator.hasNext()) {
            System.out.println("  " + chainIterator.next());
        }
    }

    /**
     * 组合迭代器 - 联合遍历多个集合
     */
    private static void compositeIteratorDemo() {
        List<String> list1 = Arrays.asList("A1", "A2", "A3");
        List<String> list2 = Arrays.asList("B1", "B2");
        List<String> list3 = Arrays.asList("C1", "C2", "C3", "C4");

        // 创建组合迭代器
        List<Iterator<String>> iterators = Arrays.asList(
            list1.iterator(),
            list2.iterator(),
            list3.iterator()
        );

        CompositeIterator<String> composite = new CompositeIterator<>(iterators);

        System.out.println("依次遍历多个列表:");
        while (composite.hasNext()) {
            System.out.println("  " + composite.next());
        }

        // 轮流遍历
        System.out.println("\n轮流遍历:");
        List<String> list4 = Arrays.asList("1", "2", "3");
        List<String> list5 = Arrays.asList("一", "二", "三", "四", "五");

        RoundRobinIterator<String> roundRobin = new RoundRobinIterator<>(
            Arrays.asList(list4.iterator(), list5.iterator())
        );

        while (roundRobin.hasNext()) {
            System.out.println("  " + roundRobin.next());
        }
    }

    /**
     * 文件目录遍历 - 模拟文件树
     */
    private static void fileIteratorDemo() {
        // 模拟文件系统
        FileNode root = new FileNode("root", true);
        FileNode home = new FileNode("home", true);
        FileNode user = new FileNode("user", true);
        FileNode docs = new FileNode("docs", true);
        FileNode file1 = new FileNode("file1.txt", false);
        FileNode file2 = new FileNode("file2.txt", false);
        FileNode file3 = new FileNode("readme.txt", false);

        root.addChild(home);
        home.addChild(user);
        user.addChild(docs);
        docs.addChild(file1);
        user.addChild(file2);
        root.addChild(file3);

        // 深度优先遍历
        System.out.println("深度优先遍历:");
        FileIterator dfsIterator = new FileIterator(root, TraversalType.DEPTH_FIRST);
        while (dfsIterator.hasNext()) {
            System.out.println("  " + dfsIterator.next());
        }

        // 广度优先遍历
        System.out.println("\n广度优先遍历:");
        FileIterator bfsIterator = new FileIterator(root, TraversalType.BREADTH_FIRST);
        while (bfsIterator.hasNext()) {
            System.out.println("  " + bfsIterator.next());
        }
    }
}

// ============================================
// 1. 分页迭代器
// ============================================

class PagingIterator<T> implements Iterator<List<T>> {
    private List<T> data;
    private int pageSize;
    private int currentIndex = 0;

    public PagingIterator(List<T> data, int pageSize) {
        this.data = data;
        this.pageSize = pageSize;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < data.size();
    }

    @Override
    public List<T> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        int end = Math.min(currentIndex + pageSize, data.size());
        List<T> page = data.subList(currentIndex, end);
        currentIndex = end;
        return page;
    }

    public void skipToPage(int pageNumber) {
        currentIndex = (pageNumber - 1) * pageSize;
    }
}

// ============================================
// 2. 筛选迭代器
// ============================================

class FilterIterator<T> implements Iterator<T> {
    private Iterator<T> source;
    private Predicate<T> predicate;
    private T next;
    private boolean hasNext;

    public FilterIterator(Iterator<T> source, Predicate<T> predicate) {
        this.source = source;
        this.predicate = predicate;
        findNext();
    }

    private void findNext() {
        hasNext = false;
        while (source.hasNext()) {
            T item = source.next();
            if (predicate.test(item)) {
                next = item;
                hasNext = true;
                break;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public T next() {
        if (!hasNext) {
            throw new NoSuchElementException();
        }
        T result = next;
        findNext();
        return result;
    }
}

// ============================================
// 3. 转换迭代器
// ============================================

class TransformIterator<S, T> implements Iterator<T> {
    private Iterator<S> source;
    private Function<S, T> transformer;

    public TransformIterator(Iterator<S> source, Function<S, T> transformer) {
        this.source = source;
        this.transformer = transformer;
    }

    @Override
    public boolean hasNext() {
        return source.hasNext();
    }

    @Override
    public T next() {
        return transformer.apply(source.next());
    }
}

// ============================================
// 4. 组合迭代器
// ============================================

class CompositeIterator<T> implements Iterator<T> {
    private List<Iterator<T>> iterators;

    public CompositeIterator(List<Iterator<T>> iterators) {
        this.iterators = iterators;
    }

    @Override
    public boolean hasNext() {
        for (Iterator<T> iterator : iterators) {
            if (iterator.hasNext()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public T next() {
        for (Iterator<T> iterator : iterators) {
            if (iterator.hasNext()) {
                return iterator.next();
            }
        }
        throw new NoSuchElementException();
    }
}

/**
 * 轮流迭代器 - 多个迭代器轮询
 */
class RoundRobinIterator<T> implements Iterator<T> {
    private List<Iterator<T>> iterators;
    private int currentIndex = 0;

    public RoundRobinIterator(List<Iterator<T>> iterators) {
        this.iterators = iterators;
    }

    @Override
    public boolean hasNext() {
        // 检查是否还有任何迭代器有元素
        for (Iterator<T> iterator : iterators) {
            if (iterator.hasNext()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public T next() {
        int checked = 0;
        while (checked < iterators.size()) {
            Iterator<T> iterator = iterators.get(currentIndex);
            currentIndex = (currentIndex + 1) % iterators.size();
            checked++;

            if (iterator.hasNext()) {
                return iterator.next();
            }
        }
        throw new NoSuchElementException();
    }
}

// ============================================
// 5. 文件目录遍历
// ============================================

class FileNode {
    private String name;
    private boolean isDirectory;
    private List<FileNode> children = new ArrayList<>();

    public FileNode(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }

    public void addChild(FileNode child) {
        children.add(child);
    }

    public String getName() {
        return name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public List<FileNode> getChildren() {
        return children;
    }
}

enum TraversalType {
    DEPTH_FIRST,
    BREADTH_FIRST
}

class FileIterator implements Iterator<String>, Iterable<String> {
    private FileNode root;
    private TraversalType type;
    private List<FileNode> queue = new ArrayList<>();
    private int index = 0;

    public FileIterator(FileNode root, TraversalType type) {
        this.root = root;
        this.type = type;
        initialize();
    }

    private void initialize() {
        if (type == TraversalType.BREADTH_FIRST) {
            if (root != null) {
                queue.add(root);
            }
        } else {
            buildDepthFirstList(root);
        }
    }

    private void buildDepthFirstList(FileNode node) {
        if (node == null) return;
        queue.add(node);
        for (FileNode child : node.getChildren()) {
            buildDepthFirstList(child);
        }
    }

    @Override
    public boolean hasNext() {
        return index < queue.size();
    }

    @Override
    public String next() {
        FileNode node = queue.get(index++);
        if (type == TraversalType.BREADTH_FIRST && node.isDirectory()) {
            queue.addAll(node.getChildren());
        }
        return node.getName();
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }
}
