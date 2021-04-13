package com.demo.lib_generic;

import java.util.List;

public class JobF extends Job {


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
        System.out.println("JobF");
    }

    @Override
    boolean runOnUi() {
        return false;
    }
}
