package com.ssitao.code.designpattern.observer.reactor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * 响应式流风格示例（类似Reactor/Spring WebFlux）
 *
 * 响应式编程特点：
 * 1. 事件驱动
 * 2. 非阻塞
 * 3. 异步处理
 * 4. 背压支持
 *
 * 应用场景：
 * 1. 实时数据流处理
 * 2. 复杂事件处理（CEP）
 * 3. 响应式API
 * 4. 实时推送
 */
public class ReactorDemo {

    public static void main(String[] args) {
        System.out.println("=== 响应式流风格示例 ===\n");

        // 1. 基础响应式流
        System.out.println("1. 基础响应式流");
        basicReactorDemo();

        // 2. 转换操作
        System.out.println("\n2. 转换操作");
        transformDemo();

        // 3. 过滤操作
        System.out.println("\n3. 过滤操作");
        filterDemo();

        // 4. 组合操作
        System.out.println("\n4. 组合操作");
        combineDemo();
    }

    /**
     * 基础响应式流示例
     */
    private static void basicReactorDemo() {
        // 创建响应式数据源
        ReactiveSource<Integer> source = new ReactiveSource<>();

        // 订阅数据
        source.subscribe(new Subscriber<Integer>() {
            @Override
            public void onNext(Integer item) {
                System.out.println("收到数据: " + item);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("发生错误: " + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("数据流完成");
            }
        });

        // 发射数据
        System.out.println("--- 发射数据 ---");
        source.emit(1);
        source.emit(2);
        source.emit(3);
        source.complete();
    }

    /**
     * 转换操作示例
     */
    private static void transformDemo() {
        ReactiveSource<Integer> source = new ReactiveSource<>();

        // 转换: 数字 -> 字符串
        source.map(i -> "数字: " + i)
              .subscribe(new Subscriber<String>() {
                  @Override
                  public void onNext(String item) {
                      System.out.println("收到: " + item);
                  }

                  @Override
                  public void onError(Throwable t) {}

                  @Override
                  public void onComplete() {
                      System.out.println("转换流完成");
                  }
              });

        System.out.println("--- 发射数据 ---");
        source.emit(10);
        source.emit(20);
        source.complete();
    }

    /**
     * 过滤操作示例
     */
    private static void filterDemo() {
        ReactiveSource<Integer> source = new ReactiveSource<>();

        // 过滤: 只保留偶数
        source.filter(i -> i % 2 == 0)
              .subscribe(new Subscriber<Integer>() {
                  @Override
                  public void onNext(Integer item) {
                      System.out.println("收到偶数: " + item);
                  }

                  @Override
                  public void onError(Throwable t) {}

                  @Override
                  public void onComplete() {
                      System.out.println("过滤流完成");
                  }
              });

        System.out.println("--- 发射数据 ---");
        source.emit(1);
        source.emit(2);
        source.emit(3);
        source.emit(4);
        source.complete();
    }

    /**
     * 组合操作示例
     */
    private static void combineDemo() {
        ReactiveSource<Integer> source = new ReactiveSource<>();

        // 先过滤偶数，再转换
        source.filter(i -> i % 2 == 0)
              .map(i -> i * 2)
              .subscribe(new Subscriber<Integer>() {
                  @Override
                  public void onNext(Integer item) {
                      System.out.println("收到: " + item);
                  }

                  @Override
                  public void onError(Throwable t) {}

                  @Override
                  public void onComplete() {
                      System.out.println("组合流完成");
                  }
              });

        System.out.println("--- 发射数据 ---");
        source.emit(1);
        source.emit(2);
        source.emit(3);
        source.emit(4);
        source.emit(5);
        source.complete();
    }
}

// ============================================
// 响应式流核心接口
// ============================================

/**
 * 订阅者接口
 */
interface Subscriber<T> {
    void onNext(T item);
    void onError(Throwable t);
    void onComplete();
}

/**
 * 发布者接口
 */
interface Publisher<T> {
    void subscribe(Subscriber<? super T> subscriber);
}

/**
 * 响应式源
 */
class ReactiveSource<T> {
    private List<Subscriber<? super T>> subscribers = new CopyOnWriteArrayList<>();
    private boolean completed = false;

    /**
     * 订阅
     */
    public void subscribe(Subscriber<? super T> subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * 转换操作
     */
    public <R> ReactiveSource<R> map(java.util.function.Function<T, R> mapper) {
        ReactiveSource<R> result = new ReactiveSource<>();

        this.subscribe(new Subscriber<T>() {
            @Override
            public void onNext(T item) {
                R mapped = mapper.apply(item);
                result.emit(mapped);
            }

            @Override
            public void onError(Throwable t) {
                result.error(t);
            }

            @Override
            public void onComplete() {
                result.complete();
            }
        });

        return result;
    }

    /**
     * 过滤操作
     */
    public ReactiveSource<T> filter(java.util.function.Predicate<T> predicate) {
        ReactiveSource<T> result = new ReactiveSource<>();

        this.subscribe(new Subscriber<T>() {
            @Override
            public void onNext(T item) {
                if (predicate.test(item)) {
                    result.emit(item);
                }
            }

            @Override
            public void onError(Throwable t) {
                result.error(t);
            }

            @Override
            public void onComplete() {
                result.complete();
            }
        });

        return result;
    }

    /**
     * 发射数据
     */
    public void emit(T item) {
        if (completed) {
            return;
        }
        for (Subscriber<? super T> subscriber : subscribers) {
            subscriber.onNext(item);
        }
    }

    /**
     * 发射错误
     */
    public void error(Throwable t) {
        for (Subscriber<? super T> subscriber : subscribers) {
            subscriber.onError(t);
        }
    }

    /**
     * 完成
     */
    public void complete() {
        if (completed) {
            return;
        }
        completed = true;
        for (Subscriber<? super T> subscriber : subscribers) {
            subscriber.onComplete();
        }
    }
}

/**
 * Spring WebFlux 风格示例
 *
 * // 使用Spring WebFlux
 * @RestController
 * public class UserController {
 *
 *     @GetMapping("/users/{id}")
 *     public Mono<User> getUser(@PathVariable Long id) {
 *         return userService.findById(id)
 *             .map(user -> ResponseEntity.ok(user))
 *             .defaultIfEmpty(ResponseEntity.notFound().build());
 *     }
 *
 *     @GetMapping("/users")
 *     public Flux<User> getAllUsers() {
 *         return userService.findAll()
 *             .filter(user -> user.isActive())
 *             .map(this::toDto);
 *     }
 * }
 *
 * // 使用 Reactor
 * Mono<User> userMono = Mono.just(user);
 * Flux<User> userFlux = Flux.fromIterable(users);
 *
 * // 组合操作
 * Mono<User> combined = Mono.zip(userMono, orderMono, User::new);
 * Flux<String> names = userFlux
 *     .filter(u -> u.getAge() > 18)
 *     .map(User::getName)
 *     .distinct();
 */
