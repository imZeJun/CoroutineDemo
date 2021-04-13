package com.demo.lib_generic;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public abstract class Job {

    private final CountDownLatch dependentCount;

    public Job() {
        this.dependentCount = new CountDownLatch(getDependentJobs() != null ? getDependentJobs().size() : 0);
    }

    abstract List<String> getDependentJobs();

    public void waitParent() {
        try {
            dependentCount.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onParentFinished() {
        dependentCount.countDown();
    }

    void work() {
        if (runOnUi()) {
            waitParent();
            doWork();
            notifyFinished();
        } else {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    waitParent();
                    doWork();
                    notifyFinished();
                }
            }.start();
        }
    }

    void notifyFinished() {
        JobFactory.getInstance().notifyFinished(this);
    }

    abstract void doWork();

    abstract boolean runOnUi();
}
