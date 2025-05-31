package com.wj.bookstore.common.utils;

import com.wj.bookstore.common.enums.StatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-24-19:52
 **/
@Slf4j
public class ObjectUtil {
    public static void copyNonNullProperties(Object source, Object target){
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        // 获取所有源对象字段
        Field[] sourceFields = sourceClass.getDeclaredFields();
        for (Field sourceField : sourceFields) {
            sourceField.setAccessible(true); // 允许访问私有字段

            try {
                // 检查源字段值是否为null
                Object sourceValue = sourceField.get(source);
                if (sourceValue == null) {
                    continue; // 跳过null值
                }

                // 获取目标对象对应字段
                Field targetField = targetClass.getDeclaredField(sourceField.getName());
                targetField.setAccessible(true);

                // 类型兼容性检查
                if (!isAssignable(targetField.getType(), sourceValue.getClass())) {
                    throw new IllegalArgumentException(
                            String.format("字段类型不匹配: %s -> %s",
                                    sourceField.getName(), targetField.getType().getSimpleName())
                    );
                }

                // 处理基本类型和包装类
                if (sourceValue instanceof Date && targetField.getType().equals(Date.class)) {
                    targetField.set(target, new Date(((Date) sourceValue).getTime()));
                } else {
                    targetField.set(target, sourceValue);
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                // 记录日志并跳过无法处理的字段
                log.error("属性复制失败: {}", sourceField.getName(), e);
                throw ExceptionUtil.of(StatusEnum.UNEXPECT_ERROR,"属性复制失败");
            }
        }
    }

    private static boolean isAssignable(Class<?> targetType, Class<?> valueType) {
        return targetType.isAssignableFrom(valueType) ||
                (targetType.isPrimitive() &&
                        valueType.equals(Boolean.class) && targetType == boolean.class) ||
                (targetType.isPrimitive() &&
                        valueType.equals(Character.class) && targetType == char.class) ||
                (targetType.isPrimitive() &&
                        valueType.equals(Byte.class) && targetType == byte.class) ||
                (targetType.isPrimitive() &&
                        valueType.equals(Short.class) && targetType == short.class) ||
                (targetType.isPrimitive() &&
                        valueType.equals(Integer.class) && targetType == int.class) ||
                (targetType.isPrimitive() &&
                        valueType.equals(Long.class) && targetType == long.class) ||
                (targetType.isPrimitive() &&
                        valueType.equals(Float.class) && targetType == float.class) ||
                (targetType.isPrimitive() &&
                        valueType.equals(Double.class) && targetType == double.class);
    }

}
