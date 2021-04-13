package com.demo.lib_generic;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * 如何通过 getTypeParameters 获取类上的泛型参数列表，列表的元素类型为 TypeVariable。
 */
public class TypeVariableClass {

    private class Demo1<T1, T2 extends Integer> {

        public void method(T1 T) {

        }
    }

    public static void main(String[] args) {
        //使用 Class 类的方法返回泛型参数列表。
        TypeVariable<Class<Demo1>>[] typeParameters = Demo1.class.getTypeParameters();
        for (TypeVariable<Class<Demo1>> var : typeParameters) {
            //1.在源码中定义的名字：T1,T2
            logUtil("getName=" + var.getName());
            //2.声明该泛型变量的原始类型：class com.demo.lib_generic.TypeVariableDemo$Demo1
            logUtil("getGenericDeclaration=" + var.getGenericDeclaration());
            //3.上边界：Object,Integer
            Type[] bounds = var.getBounds();
            for (Type bound : bounds) {
                System.out.println("bound=" + bound.getTypeName());
            }
        }
        Method[] methods = Demo1.class.getDeclaredMethods();
        Type[] params = methods[0].getGenericParameterTypes();
        if (params[0] instanceof TypeVariable) {
            logUtil("param[0] is variable=" + params[0]);
        }
    }

    private static void logUtil(String str) {
        System.out.println(str);
    }


}