package com.csei.crane.data.analysis;

/**
 * 时域分析相关算法
 *
 * @author 李伯华
 *
 */
public class ShiYu {

    private ShiYu() {
    }

    // 获取最大值
    public static int getMax(int[] arr) {
        int max = 0;
        for (int x = 1; x < arr.length; x++) {
            if (arr[x] > arr[max]) {
                max = x;
            }
        }
        return arr[max];
    }

    // 获取最小值
    public static int getMin(int[] arr) {
        int min = 0;
        for (int x = 1; x < arr.length; x++) {
            if (arr[x] < arr[min])
                min = x;
        }
        return arr[min];
    }

    // 获取均值
    public static double getAverage(int[] arr) {
        double sum = 0;
        double average = 0;
        for (int j = 0; j < arr.length; j++) {
            sum = sum + arr[j];
        }
        average = sum / arr.length;
        return average;
    }

    // 获取均方差
    public static double getAqe(int[] arr) {
        double aqe = 0;
        double a = 0;
        double b = 0;
        for (int j = 0; j < arr.length; j++) {
            a = arr[j] - getAverage(arr);
            b = b + Math.pow(a, 2);
        }
        b = b / arr.length;
        aqe = Math.sqrt(b);// 开平方
        return aqe;
    }

    // 获取方差
    public static double getVar(int[] arr) {
        double var = 0;
        double a = 0;
        double b = 0;
        int size = 0;
        for (int j = 0; j < arr.length; j++) {
            a = arr[j] - getAverage(arr);
            b = b + Math.pow(a, 2);
        }
        var = Math.sqrt(b);
        size = arr.length - 1;
        var = var / size;
        return var;
    }

    // 自相关内部循环调用
    private static float AutoCorrelation(float[] data, int N, int m) {
        float r = 0.0f;
        for (int i = 0; i < N; i++)

        {
            r += data[i] * data[i + m];
        }
        return r;
    }

    // 自相关
    public static void countAutoCorrelation(float[] data) {
        float[] f = new float[data.length];
        for (int i = 0; i < data.length; i++) {
            f[i] = AutoCorrelation(data, data.length, i);
            System.out.println(f[i]);
        }
    }

    // 数据测试
    public static void main(String[] args) {
        ShiYu d = new ShiYu();
        int a[] = { 0, 1, 2, 3, 4 };
        System.out.println(d.getMax(a));

        float[] data = new float[1200];

        for (int i = 0; i < 1200;) {
            data[i] = 2;
            data[i + 1] = 2;
            data[i + 2] = 2;
            data[i + 3] = 0;
            data[i + 4] = 0;
            data[i + 5] = 0;
            i = i + 6;
        }
        countAutoCorrelation(data);
    }

}
