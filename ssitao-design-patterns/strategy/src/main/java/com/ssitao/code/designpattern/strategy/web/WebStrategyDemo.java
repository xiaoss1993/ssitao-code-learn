package com.ssitao.code.designpattern.strategy.web;

import java.util.HashMap;
import java.util.Map;

/**
 * Web应用中的策略模式示例
 *
 * Web应用场景：
 * 1. 多种登录方式 - 账号密码/微信/QQ/手机号
 * 2. 多种缓存策略 - LRU/FIFO/LFU
 * 3. 多种加密算法 - AES/DES/RSA
 * 4. 多种压缩算法 - GZIP/ZIP/7z
 * 5. 多种排序算法 - 前端排序/后端排序
 * 6. 多种鉴权策略 - JWT/Session/Cookie
 */
public class WebStrategyDemo {

    public static void main(String[] args) {
        System.out.println("=== Web策略模式示例 ===\n");

        // 1. 多种登录方式
        System.out.println("1. 多种登录方式");
        loginDemo();

        // 2. 多种加密策略
        System.out.println("\n2. 多种加密策略");
        encryptionDemo();

        // 3. 多种缓存策略
        System.out.println("\n3. 多种缓存策略");
        cacheDemo();
    }

    /**
     * 多种登录方式示例
     */
    private static void loginDemo() {
        // 创建登录上下文
        LoginContext context = new LoginContext();

        // 用户名密码登录
        System.out.println("--- 用户名密码登录 ---");
        context.setStrategy(new UsernamePasswordStrategy());
        context.login("zhangsan", "123456");

        // 微信登录
        System.out.println("\n--- 微信登录 ---");
        context.setStrategy(new WechatLoginStrategy());
        context.login("wx_code_123", null);

        // 手机验证码登录
        System.out.println("\n--- 手机验证码登录 ---");
        context.setStrategy(new PhoneVerifyStrategy());
        context.login("13800138000", "123456");

        // GitHub登录
        System.out.println("\n--- GitHub登录 ---");
        context.setStrategy(new GithubLoginStrategy());
        context.login("github_token_xxx", null);
    }

    /**
     * 多种加密策略示例
     */
    private static void encryptionDemo() {
        String data = "Hello World";

        // AES加密
        System.out.println("--- AES加密 ---");
        EncryptionContext context = new EncryptionContext(new AesEncryptionStrategy());
        String encrypted = context.encrypt(data);
        System.out.println("加密后: " + encrypted);
        System.out.println("解密后: " + context.decrypt(encrypted));

        // DES加密
        System.out.println("\n--- DES加密 ---");
        context.setStrategy(new DesEncryptionStrategy());
        encrypted = context.encrypt(data);
        System.out.println("加密后: " + encrypted);
        System.out.println("解密后: " + context.decrypt(encrypted));

        // RSA加密
        System.out.println("\n--- RSA加密 ---");
        context.setStrategy(new RsaEncryptionStrategy());
        encrypted = context.encrypt(data);
        System.out.println("加密后: " + encrypted);
        System.out.println("解密后: " + context.decrypt(encrypted));
    }

    /**
     * 多种缓存策略示例
     */
    private static void cacheDemo() {
        // LRU缓存
        System.out.println("--- LRU缓存 ---");
        CacheContext lruContext = new CacheContext(new LruCacheStrategy(3));
        lruContext.put("a", "1");
        lruContext.put("b", "2");
        lruContext.put("c", "3");
        System.out.println("缓存内容: " + lruContext.get("a") + ", " + lruContext.get("b") + ", " + lruContext.get("c"));
        lruContext.put("d", "4"); // 淘汰最老的
        System.out.println("添加d后, a是否还存在: " + lruContext.get("a"));

        // LFU缓存
        System.out.println("\n--- LFU缓存 ---");
        CacheContext lfuContext = new CacheContext(new LfuCacheStrategy(3));
        lfuContext.put("a", "1");
        lfuContext.put("b", "2");
        lfuContext.put("c", "3");
        lfuContext.get("a"); // 访问a
        lfuContext.get("a"); // 访问a
        lfuContext.put("d", "4"); // 淘汰使用最少的
        System.out.println("添加d后, b是否还存在: " + lfuContext.get("b"));
    }
}

// ============================================
// 1. 登录相关类
// ============================================

/**
 * 登录策略接口
 */
interface LoginStrategy {
    void login(String... params);
    String getLoginType();
}

/**
 * 用户名密码登录
 */
class UsernamePasswordStrategy implements LoginStrategy {

    @Override
    public void login(String... params) {
        String username = params[0];
        String password = params[1];
        System.out.println("执行用户名密码登录: " + username);
        System.out.println("验证密码...");
    }

    @Override
    public String getLoginType() {
        return "用户名密码";
    }
}

/**
 * 微信登录
 */
class WechatLoginStrategy implements LoginStrategy {

    @Override
    public void login(String... params) {
        String wxCode = params[0];
        System.out.println("执行微信登录: code=" + wxCode);
        System.out.println("获取微信用户信息...");
    }

    @Override
    public String getLoginType() {
        return "微信";
    }
}

/**
 * 手机验证码登录
 */
class PhoneVerifyStrategy implements LoginStrategy {

    @Override
    public void login(String... params) {
        String phone = params[0];
        String verifyCode = params[1];
        System.out.println("执行手机验证码登录: " + phone);
        System.out.println("验证验证码: " + verifyCode);
    }

    @Override
    public String getLoginType() {
        return "手机验证码";
    }
}

/**
 * GitHub登录
 */
class GithubLoginStrategy implements LoginStrategy {

    @Override
    public void login(String... params) {
        String token = params[0];
        System.out.println("执行GitHub登录: token=" + token.substring(0, 10) + "...");
        System.out.println("通过GitHub API获取用户信息...");
    }

    @Override
    public String getLoginType() {
        return "GitHub";
    }
}

/**
 * 登录上下文
 */
class LoginContext {
    private LoginStrategy strategy;

    public void setStrategy(LoginStrategy strategy) {
        this.strategy = strategy;
    }

    public void login(String... params) {
        System.out.println("当前登录方式: " + strategy.getLoginType());
        strategy.login(params);
    }
}

// ============================================
// 2. 加密相关类
// ============================================

/**
 * 加密策略接口
 */
interface EncryptionStrategy {
    String encrypt(String data);
    String decrypt(String data);
    String getAlgorithmName();
}

/**
 * AES加密
 */
class AesEncryptionStrategy implements EncryptionStrategy {

    @Override
    public String encrypt(String data) {
        // 模拟AES加密
        return "AES[" + new StringBuilder(data).reverse().toString() + "]";
    }

    @Override
    public String decrypt(String data) {
        // 模拟AES解密
        String decrypted = data.substring(4, data.length() - 1);
        return new StringBuilder(decrypted).reverse().toString();
    }

    @Override
    public String getAlgorithmName() {
        return "AES";
    }
}

/**
 * DES加密
 */
class DesEncryptionStrategy implements EncryptionStrategy {

    @Override
    public String encrypt(String data) {
        return "DES[" + data.replace("o", "0").replace("l", "1") + "]";
    }

    @Override
    public String decrypt(String data) {
        return data.substring(4, data.length() - 1).replace("0", "o").replace("1", "l");
    }

    @Override
    public String getAlgorithmName() {
        return "DES";
    }
}

/**
 * RSA加密
 */
class RsaEncryptionStrategy implements EncryptionStrategy {

    @Override
    public String encrypt(String data) {
        return "RSA[" + data.toCharArray().length + " chars encrypted]";
    }

    @Override
    public String decrypt(String data) {
        return "RSA decrypted data";
    }

    @Override
    public String getAlgorithmName() {
        return "RSA";
    }
}

/**
 * 加密上下文
 */
class EncryptionContext {
    private EncryptionStrategy strategy;

    public EncryptionContext(EncryptionStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(EncryptionStrategy strategy) {
        this.strategy = strategy;
    }

    public String encrypt(String data) {
        System.out.println("使用" + strategy.getAlgorithmName() + "算法加密");
        return strategy.encrypt(data);
    }

    public String decrypt(String data) {
        return strategy.decrypt(data);
    }
}

// ============================================
// 3. 缓存相关类
// ============================================

/**
 * 缓存策略接口
 */
interface CacheStrategy<K, V> {
    void put(K key, V value);
    V get(K key);
    void remove(K key);
}

/**
 * LRU缓存策略（最近最少使用）
 */
class LruCacheStrategy<K, V> implements CacheStrategy<K, V> {
    private int capacity;
    private Map<K, V> cache = new HashMap<>();
    private java.util.List<K> accessOrder = new java.util.ArrayList<>();

    public LruCacheStrategy(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void put(K key, V value) {
        if (cache.size() >= capacity && !cache.containsKey(key)) {
            // 淘汰最老的
            K oldest = accessOrder.remove(0);
            cache.remove(oldest);
        }
        cache.put(key, value);
        accessOrder.remove(key);
        accessOrder.add(key);
    }

    @Override
    public V get(K key) {
        if (cache.containsKey(key)) {
            accessOrder.remove(key);
            accessOrder.add(key);
            return cache.get(key);
        }
        return null;
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
        accessOrder.remove(key);
    }
}

/**
 * LFU缓存策略（最少使用）
 */
class LfuCacheStrategy<K, V> implements CacheStrategy<K, V> {
    private int capacity;
    private Map<K, V> cache = new HashMap<>();
    private Map<K, Integer> frequency = new HashMap<>();

    public LfuCacheStrategy(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void put(K key, V value) {
        if (cache.size() >= capacity && !cache.containsKey(key)) {
            // 找出使用最少的
            K minFreqKey = null;
            int minFreq = Integer.MAX_VALUE;
            for (Map.Entry<K, Integer> entry : frequency.entrySet()) {
                if (entry.getValue() < minFreq) {
                    minFreq = entry.getValue();
                    minFreqKey = entry.getKey();
                }
            }
            if (minFreqKey != null) {
                cache.remove(minFreqKey);
                frequency.remove(minFreqKey);
            }
        }
        cache.put(key, value);
        frequency.put(key, frequency.getOrDefault(key, 0) + 1);
    }

    @Override
    public V get(K key) {
        if (cache.containsKey(key)) {
            frequency.put(key, frequency.get(key) + 1);
            return cache.get(key);
        }
        return null;
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
        frequency.remove(key);
    }
}

/**
 * 缓存上下文
 */
class CacheContext {
    private CacheStrategy<String, String> strategy;

    public CacheContext(CacheStrategy<String, String> strategy) {
        this.strategy = strategy;
    }

    public void put(String key, String value) {
        strategy.put(key, value);
    }

    public String get(String key) {
        return strategy.get(key);
    }

    public void remove(String key) {
        strategy.remove(key);
    }
}
