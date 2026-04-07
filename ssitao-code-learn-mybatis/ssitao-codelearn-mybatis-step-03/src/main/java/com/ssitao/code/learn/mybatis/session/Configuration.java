package com.ssitao.code.learn.mybatis.session;

import com.ssitao.code.learn.mybatis.binding.MapperRegistry;
import com.ssitao.code.learn.mybatis.mapping.MappedStatement;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sizt
 * @description:
 * 配置项
 * @date 2026/1/18 11:58
 */
public class Configuration {
    /**
     * 映射注册机
     */
    protected MapperRegistry    mapperRegistry = new MapperRegistry(this);

    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    public void addMappers(String packageName){
        mapperRegistry.addMappers(packageName);
    }

    public <T> void addMapper(Class<T> type){
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type,SqlSession sqlSession){
        return mapperRegistry.getMapper(type,sqlSession);
    }
    public boolean hasMapper(Class<?> type){
        return mapperRegistry.hasMapper(type);
    }
    public void addMappedStatement(MappedStatement ms){
        mappedStatements.put(ms.getId(),ms);
    }
    public MappedStatement getMappedStatement(String id){
        return mappedStatements.get(id);
    }
}
