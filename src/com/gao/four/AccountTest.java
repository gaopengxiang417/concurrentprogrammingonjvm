package com.gao.four;

import java.util.concurrent.locks.ReentrantLock;

/**
 * User: wangchen.gpx
 * Date: 13-8-22
 * Time: 下午8:48
 */
public class AccountTest implements Comparable<AccountTest> {
    private Integer balance;
    public ReentrantLock lock = new ReentrantLock();

    public AccountTest(Integer balance) {
        this.balance = balance;
    }

    @Override
    public int compareTo(AccountTest o) {
        return balance.compareTo(o.balance);
    }

    /**
     * 存款的操作
     * @param amount
     */
    public void disposit(Integer amount) {
        lock.lock();
        try {
            if(amount > 0)
                balance += amount;
        }finally {
            lock.unlock();
        }
    }

    public boolean withdraw(Integer amount){
        lock.lock();
        try{
            if ( amount > 0 && balance >= amount){
                balance -= amount;
                return true;
            }
            return false;
        }finally {
            lock.unlock();
        }
    }
}
