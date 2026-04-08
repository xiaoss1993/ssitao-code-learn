package com.ssitao.code.jdk.phase03.reflection;

import java.lang.annotation.*;
import java.lang.reflect.*;

/**
 * 反射与注解示例
 * 演示注解的定义和使用
 */
public class AnnotationDemo {

    public static void main(String[][] args) throws Exception {
        System.out.println("=== Annotation Demo ===\n");

        // 1. 获取类注解
        System.out.println("--- 类注解 ---");
        Class<?> clazz = User.class;
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        if (tableAnnotation != null) {
            System.out.println("Table name: " + tableAnnotation.name());
        }

        // 2. 获取字段注解
        System.out.println("\n--- 字段注解 ---");
        for (Field field : clazz.getDeclaredFields()) {
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                System.out.println("Field: " + field.getName() +
                                 " -> Column: " + columnAnnotation.name() +
                                 " (type=" + columnAnnotation.type() + ")");
            }
        }

        // 3. 获取方法注解
        System.out.println("\n--- 方法注解 ---");
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Deprecated.class)) {
                Deprecated deprecated = method.getAnnotation(Deprecated.class);
                System.out.println("Deprecated method: " + method.getName());
            }
        }

        // 4. 使用注解进行验证
        System.out.println("\n--- 使用注解验证 ---");
        User user = new User();
        user.setId(1L);
        user.setUserName("alice");
        user.setEmail("alice@example.com");
        user.setAge(25);

        validate(user);

        // 5. 处理带参数注解
        System.out.println("\n--- 处理带参数注解 ---");
        for (Method method : clazz.getDeclaredMethods()) {
            MethodInfo methodInfo = method.getAnnotation(MethodInfo.class);
            if (methodInfo != null) {
                System.out.println("Method: " + method.getName());
                System.out.println("  Author: " + methodInfo.author());
                System.out.println("  Version: " + methodInfo.version());
                System.out.println("  Description: " + methodInfo.description());
            }
        }
    }

    // 验证对象（使用注解信息）
    public static void validate(Object obj) throws Exception {
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            // 检查@NotNull注解
            if (field.isAnnotationPresent(NotNull.class)) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value == null) {
                    System.out.println("Validation failed: " + field.getName() + " is null");
                }
            }

            // 检查@Min注解
            if (field.isAnnotationPresent(Min.class)) {
                field.setAccessible(true);
                Min min = field.getAnnotation(Min.class);
                int value = (int) field.get(obj);
                if (value < min.value()) {
                    System.out.println("Validation failed: " + field.getName() +
                                     " is " + value + " (min=" + min.value() + ")");
                }
            }
        }
    }

    // ===== 注解定义 =====

    @Retention(RetentionPolicy.RUNTIME)
    @interface Table {
        String name();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Column {
        String name();
        String type() default "VARCHAR";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface NotNull { }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Min {
        int value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface MethodInfo {
        String author() default "unknown";
        int version() default 1;
        String description() default "";
    }

    // ===== 测试类 =====

    @Table(name = "users")
    static class User {
        private Long id;

        @Column(name = "user_name", type = "VARCHAR(50)")
        @NotNull
        private String userName;

        @Column(name = "email", type = "VARCHAR(100)")
        private String email;

        @Column(name = "age")
        @Min(0)
        private int age;

        @Deprecated
        public void oldMethod() { }

        @MethodInfo(author = "John", version = 2, description = "Get user info")
        public String getInfo() {
            return "User: " + userName + ", " + email;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }
}
