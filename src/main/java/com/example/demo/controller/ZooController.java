package com.example.demo.controller;

import com.example.demo.util.ZookeeperConnection;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2018/11/20.
 */
@Controller
@RequestMapping("/zoo")
public class ZooController {

    /**
     * 创建节点
     */
    ZooKeeper zooKeeper;
    String path = "/test1";

    @RequestMapping("createZoo")
    public String createZoo() throws IOException, InterruptedException, KeeperException {
        zooKeeper = ZookeeperConnection.connect("127.0.0.1");
        zooKeeper.create(path, "666".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zooKeeper.close();
        return "/index";
    }

    /**
     * 判断节点是否存在
     */
    @RequestMapping("existZoo")
    public String existZoo() throws IOException, InterruptedException, KeeperException {
        zooKeeper = ZookeeperConnection.connect("127.0.0.1");
        Stat stat = zooKeeper.exists(path, true);
        if (stat != null) {
            System.out.println("stat = " + stat);
            System.out.println("stat.getVersion() = " + stat.getVersion());
        }
        zooKeeper.close();
        return "/index";
    }

    /**
     * 获取节点数据
     */
    @RequestMapping("getDataZoo")
    public String getDataZoo() throws IOException, InterruptedException, KeeperException {
        zooKeeper = ZookeeperConnection.connect("127.0.0.1");
        byte[] data = zooKeeper.getData(path, false, null);
        System.out.println(new String(data));
        zooKeeper.close();
        return "/index";
    }

    /**
     * 数据变更自动获取
     */
    @RequestMapping("getDataZoo2")
    public String getDataZoo2() throws IOException, InterruptedException, KeeperException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        zooKeeper = ZookeeperConnection.connect("127.0.0.1");
        byte[] data = zooKeeper.getData(path, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (Event.EventType.None == watchedEvent.getType()) {
                    switch (watchedEvent.getState()) {
                        case Expired:
                            countDownLatch.countDown();
                            break;
                    }
                } else {
                    try {
                        byte[] data1 = zooKeeper.getData(path, false, null);
                        System.out.println("data1 = " + new String(data1));
                        countDownLatch.countDown();
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, null);
        return "/index";
    }

    @RequestMapping("setDataZoo")
    public String setDataZoo() throws IOException, InterruptedException, KeeperException {
        zooKeeper = ZookeeperConnection.connect("127.0.0.1");
        zooKeeper.setData(path, "999".getBytes(), zooKeeper.exists(path, true).getVersion());
        return "/index";
    }
}
