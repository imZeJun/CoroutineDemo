package com.demo.lib_generic;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JobB extends Job {


    @Override
    List<String> getDependentJobs() {
        String[] a = new String[]{ JobA.class.getCanonicalName()};
        return Arrays.asList(a);
    }

    @Override
    void doWork() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("JobB");
    }

    @Override
    boolean runOnUi() {
        return false;
    }
}
