package com.demo.lib_generic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JobFactory {

    private final HashMap<Integer, Job> mJobs = new HashMap<>();
    private final HashMap<String, Integer> mJobNames = new HashMap<>();
    private int[][] matrix;
    private int[] vertex;

    public JobFactory() {}

    public JobFactory add(Job job) {
        int index = mJobs.size();
        mJobs.put(index, job);
        mJobNames.put(job.getClass().getCanonicalName(), index);
        return this;
    }

    private List<Integer> topSort() {
        List<Integer> topRes = new ArrayList<>();
        int[] inDegree = new int[vertex.length];
        for (int[] parent : matrix) {
            for (int child = 0; child < parent.length; child++) {
                if (parent[child] == 1) {
                    inDegree[child]++;
                }
            }
        }
        Deque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < vertex.length; i++) {
            if (inDegree[i] == 0) {
                deque.offer(i);
                inDegree[i]--;
            }
        }
        int[][] tmp = new int[vertex.length][vertex.length];
        for(int i = 0; i < matrix.length; i++){
            tmp[i] = matrix[i].clone();
        }
        while (!deque.isEmpty()) {
            int top = deque.poll();
            topRes.add(top);
            for (int i = 0; i < vertex.length; i++) {
                if (tmp[top][i] == 1) {
                    tmp[top][i] = 0;
                    inDegree[i]--;
                    if (inDegree[i] == 0) {
                        deque.offer(i);
                        inDegree[i]--;
                    }
                }
            }
        }
        System.out.println(vertex.length);
        return topRes.size() == vertex.length ? topRes : new ArrayList<Integer>();
    }

    public void run() {
        int size = mJobs.size();
        matrix = new int[size][size];
        vertex = new int[size];
        for (Map.Entry<Integer, Job> entry : mJobs.entrySet()) {
            Integer index = entry.getKey();
            Job vertex = entry.getValue();
            List<String> dependents = vertex.getDependentJobs();
            if (dependents != null) {
                for (String dependent : dependents) {
                    int parent = mJobNames.get(dependent);
                    matrix[parent][index] = 1;
                }
            }
        }
        List<Integer> list = topSort();
        System.out.println("-- 拓扑排序后任务-- " + list.size());
        for (Integer index: list) {
            System.out.print(mJobs.get(index).getClass().getSimpleName() + ",");
        }
        System.out.println("-- 拓扑排序后任务-- " + list.size());
        if (list.size() != vertex.length) {
            System.out.println("存在环");
            return;
        }
        List<Integer> syncJobs = new ArrayList<>();
        List<Integer> asyncJobs = new ArrayList<>();
        for (Integer index: list) {
            Job job = mJobs.get(index);
            if (job.runOnUi()) {
                syncJobs.add(index);
            } else {
                asyncJobs.add(index);
            }
        }
        runJobs(asyncJobs);
        runJobs(syncJobs);
    }

    private void runJobs(List<Integer> jobs) {
        for (Integer index: jobs) {
            Job job = mJobs.get(index);
            job.work();
        }
    }

    private Set<Integer> getNeighbors(int index) {
        Set<Integer> hashSet = new HashSet<>();
        for (int i = 0; i < vertex.length; i++) {
            if (matrix[index][i] == 1)
                hashSet.add(i);
        }
        return hashSet;
    }

    public void notifyFinished(Job job) {
        Set<Integer> neighbors = getNeighbors(mJobNames.get(job.getClass().getCanonicalName()));
        for (Integer index: neighbors) {
            mJobs.get(index).onParentFinished();
        }
    }

    private static class Holder {
        private static final JobFactory INSTANCE = new JobFactory();
    }

    public static JobFactory getInstance() {
        return Holder.INSTANCE;
    }

}
