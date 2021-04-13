package com.demo.lib_generic;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

public class TypeVariableMethod {

    private class Demo2 {
        public <T1, T2> List<T1> method(List<T1> t1, T2 t2, Integer t3) {
            return t1;
        }
    }

    public static void main(String[] args) {
        Method[] methods = Demo2.class.getDeclaredMethods();
        Method method = methods[0];
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        for (Type type : genericParameterTypes) {
            if (type instanceof ParameterizedType) {
                logUtil(type.getTypeName() + " is ParameterizedType");
                Type[] actual = ((ParameterizedType) type).getActualTypeArguments();
                if (actual[0] instanceof TypeVariable) {
                    logUtil("actual[0]=" + actual[0].getTypeName());
                }
                logUtil("rawType is " + ((ParameterizedType) type).getRawType());
            } else if (type instanceof TypeVariable) {
                logUtil(type.getTypeName() + " is TypeVariable");
            } else if (type instanceof Class) {
                logUtil(type.getTypeName() + " is Class");
            }
        }
        Type returnType = method.getGenericReturnType();
        if (returnType instanceof ParameterizedType) {
            logUtil("returnType " + returnType.getTypeName() + " is ParameterizedType");
        }
        TypeVariable<Method>[] typeParameters = method.getTypeParameters();
        for (TypeVariable<Method> typeVariable : typeParameters) {
            logUtil("typeName=" + typeVariable.getTypeName());
        }
    }

    private static void logUtil(String str) {
        System.out.println(str);
    }


}
