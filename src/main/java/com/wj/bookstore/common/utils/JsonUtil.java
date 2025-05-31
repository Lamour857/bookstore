package com.wj.bookstore.common.utils;

import com.wj.bookstore.common.vo.ResVo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import javax.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-13:51
 **/
public class JsonUtil {
    private final static ObjectMapper jsonMapper = new ObjectMapper();

    public static JsonNode toNode(String str){
        try{
            return jsonMapper.readTree(str);
        }catch(Exception e){
            throw new UnsupportedOperationException(e);
        }
    }

    public static <T> T toObj(String str, Class<T> clz){
        try{
            return jsonMapper.readValue(str,clz);
        }catch (Exception e){
            throw new UnsupportedOperationException(e);
        }
    }

    public static <T> String toStr(T t){
        try{
            return jsonMapper.writeValueAsString(t);
        }catch (Exception e){
            throw new UnsupportedOperationException(e);
        }
    }
    public static void writeResVoToResponse(HttpServletResponse response, ResVo<?> resVo) throws IOException {
        // 设置响应内容类型为 JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // 将 ResVo 对象转换为 JSON 字符串并写入响应体
        jsonMapper.writeValue(response.getWriter(), resVo);
    }

    public static SimpleModule bigIntToStrsimpleModule(){
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, newSerializer(s->String.valueOf(s)));
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(long[].class, newSerializer((Function<Long, String>) String::valueOf));
        simpleModule.addSerializer(Long[].class, newSerializer((Function<Long, String>) String::valueOf));
        simpleModule.addSerializer(BigDecimal.class, newSerializer(BigDecimal::toString));
        simpleModule.addSerializer(BigDecimal[].class, newSerializer(BigDecimal::toString));
        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        simpleModule.addSerializer(BigInteger[].class, newSerializer((Function<BigInteger, String>) BigInteger::toString));
        return simpleModule;
    }

    public static <T,K> JsonSerializer<T> newSerializer(Function<K,String> func){
        return new JsonSerializer<T>(){
            @Override
            public void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                if( t == null ) {
                    jsonGenerator.writeNull();
                    return;
                }
                if(t.getClass().isArray()){
                    jsonGenerator.writeStartArray();
                    Stream.of(t).forEach(s->{
                        try{
                            jsonGenerator.writeString(func.apply((K) s));
                        }catch (IOException e){
                            throw new RuntimeException(e);
                        }
                    });
                    jsonGenerator.writeEndArray();
                }else{
                    jsonGenerator.writeString(func.apply((K) t));
                }
            }
        };
    }
}
