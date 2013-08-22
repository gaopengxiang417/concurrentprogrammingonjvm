package com.gao.five;

/**
 * User: wangchen.gpx
 * Date: 13-8-22
 * Time: 下午9:12
 */
public class EnerySource {
    private final Integer MAXLEVEL = 100;
    private Integer level;
    private boolean keeplive = true;

    public EnerySource() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                repleash();
            }
        }).start();
    }

    public boolean useEnery(int amount) {
        if (amount > 0 && amount < level) {
            level -= amount;
            return true;
        }
        return false;
    }

    public Integer getAvaiableLevel(){
        return level;
    }


    public void repleash() {
        while (keeplive) {
            if (level < MAXLEVEL)
                level ++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("exception ocurring.....");
            }
        }
    }
}
