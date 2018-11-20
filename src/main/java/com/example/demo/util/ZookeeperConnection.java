package com.example.demo.util;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2018/11/20.
 */
public class ZookeeperConnection {

    private static ZooKeeper zoo;

    static CountDownLatch countDownLatch=new CountDownLatch(1);

    public static ZooKeeper connect(String host) throws IOException, InterruptedException {
        if (zoo != null) {
            return zoo;
        }
        zoo=new ZooKeeper(host, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState()== Event.KeeperState.SyncConnected){
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
        return zoo;
    }

    public static void  close() throws InterruptedException {
        zoo.close();
    }
}
