package com.gao.four;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * User: wangchen.gpx
 * Date: 13-8-22
 * Time: 下午8:58
 */
public class AccountService {
    public boolean transfer(AccountTest from , AccountTest to , Integer amount) throws InterruptedException {
        //首先进行配需
        AccountTest[] accountTests = new AccountTest[]{from, to};
        Arrays.sort(accountTests);

        try {
            if (accountTests[0].lock.tryLock(1, TimeUnit.SECONDS)) {
                try{
                    if (accountTests[1].lock.tryLock(1 , TimeUnit.SECONDS)){
                        if(from.withdraw(amount)){
                            to.disposit(amount);
                            return true;
                        }
                        return false;
                    }
                }finally {
                    accountTests[1].lock.unlock();
                }
            }
        } finally {
            accountTests[0].lock.unlock();
        }
        throw new IllegalStateException("异常");
    }
}
