package com.demo.lib_generic;

import java.util.Arrays;
import java.util.List;

public class JobC extends Job {


    @Override
    List<String> getDependentJobs() {
        String[] a = new String[]{ JobA.class.getCanonicalName()};
        return Arrays.asList(a);
    }

    @Override
    void doWork() {
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("JobC");
    }

    @Override
    boolean runOnUi() {
        return false;
    }
}
