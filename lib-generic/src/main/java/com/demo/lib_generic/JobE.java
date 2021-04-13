package com.demo.lib_generic;

import java.util.List;

public class JobE extends Job {


    @Override
    List<String> getDependentJobs() {
        return null;
    }

    @Override
    void doWork() {
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("JobE");
    }

    @Override
    boolean runOnUi() {
        return false;
    }
}
