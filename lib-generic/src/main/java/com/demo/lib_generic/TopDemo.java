package com.demo.lib_generic;

public class TopDemo {

    public static void main(String[] args) {
        JobFactory.getInstance()
                .add(new JobA())
                .add(new JobB())
                .add(new JobC())
                .add(new JobD())
                .add(new JobE())
                .add(new JobF())
                .add(new JobG()).run();
    }
}
