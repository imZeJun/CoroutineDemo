package com.demo.lib_generic;

import java.util.Arrays;
import java.util.List;

public class JobG extends Job {


    @Override
    List<String> getDependentJobs() {
        String[] a = new String[]{ JobE.class.getCanonicalName()};
        return Arrays.asList(a);
    }

    @Override
    void doWork() {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("JobG");
    }

    @Override
    boolean runOnUi() {
        return false;
    }
}
