package com.demo.lib_generic;

import java.util.List;

public class JobA extends Job {


    @Override
    List<String> getDependentJobs() {
        return null;
    }

    @Override
    void doWork() {
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("JobA");
    }

    @Override
    boolean runOnUi() {
        return false;
    }
}
