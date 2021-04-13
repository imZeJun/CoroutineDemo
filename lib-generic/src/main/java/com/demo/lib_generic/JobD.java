package com.demo.lib_generic;

import java.util.Arrays;
import java.util.List;

public class JobD extends Job {


    @Override
    List<String> getDependentJobs() {
        String[] a = new String[]{JobB.class.getCanonicalName(),
                JobC.class.getCanonicalName(), JobG.class.getCanonicalName()};
        return Arrays.asList(a);
    }

    @Override
    void doWork() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("JobD");
    }

    @Override
    boolean runOnUi() {
        return false;
    }
}
