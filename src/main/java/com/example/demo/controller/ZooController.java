package com.example.demo.controller;

import com.example.demo.util.ZkcilentUtil;
import com.example.demo.util.ZookeeperConnection;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2018/11/20.
 */
@Controller
@RequestMapping("/zoo")
public class ZooController {

    ZooKeeper zooKeeper;
    ZkClient zkClient;
    String path = "/test1";
    String path2 = "/test2222";

    /**
     * 创建节点
     *
     * create(String path, byte[] data, List<ACL> acl, CreateMode createMode)
     */
    @RequestMapping("createZoo")
    public String createZoo() throws IOException, InterruptedException, KeeperException {
        zooKeeper = ZookeeperConnection.connect("127.0.0.1");
        zooKeeper.create(path, "666".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zooKeeper.close();
        return "/index";
    }

    /**
     * 判断节点是否存在
     *
     * exists(String path, boolean watcher)
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
     *
     * getData(String path, boolean watch, Stat stat)
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
     *
     * getData(String path, Watcher watcher, Stat stat)
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

    /**
     * 修改数据
     *
     * setData(String path, byte[] data, int version)
     */
    @RequestMapping("setDataZoo")
    public String setDataZoo() throws IOException, InterruptedException, KeeperException {
        zooKeeper = ZookeeperConnection.connect("127.0.0.1");
        zooKeeper.setData(path, "999".getBytes(), zooKeeper.exists(path, true).getVersion());
        return "/index";
    }

    /**
     * 获取子节点
     */
    @RequestMapping("getChrildrenZoo")
    public String getChrildrenZoo() throws IOException, InterruptedException, KeeperException {
        zooKeeper = ZookeeperConnection.connect("127.0.0.1");
        List<String> children = zooKeeper.getChildren(path, false);
        System.out.println("children = " + children);
        return "/index";
    }

    /** 创建节点*/
    @RequestMapping("createZnode")
    public String createZnodeZk() {
        zkClient = ZkcilentUtil.getZkClient("127.0.0.1");
//        zkClient.createEphemeral(path2,"111");//创建临时节点
        zkClient.createPersistent(path2,"111");//创建永久节点
        /** 设置子节点监听*/
        zkClient.subscribeChildChanges(path2, new IZkChildListener() {
            @Override
            public void handleChildChange(String path, List<String> currentChrildren) throws Exception {
                System.out.println("path = " + path);
                System.out.println("currentChrildren = " + currentChrildren);
            }
        });
        /** 设置数据监听*/
        zkClient.subscribeDataChanges(path2, new IZkDataListener() {
            @Override
            public void handleDataChange(String path, Object data) throws Exception {
                System.out.println("path = " + path+"改变了数据");
                System.out.println("data = " + data);
            }

            @Override
            public void handleDataDeleted(String path) throws Exception {
                System.out.println("path = " + path+"删除了数据");
            }
        });
        return "/index";
    }

    /** 获得子节点*/
    @RequestMapping("getChrildrenZk")
    public String getChrildrenZk(){
        zkClient=ZkcilentUtil.getZkClient("127.0.0.1");
        List<String> children = zkClient.getChildren(path);
        System.out.println("children = " + children);
        return "/index";
    }

    /** 获取节点数据*/
    @RequestMapping("getDataZk")
    public String getDataZk(){
        zkClient = ZkcilentUtil.getZkClient("127.0.0.1");
        String data = zkClient.readData(path2);
        System.out.println("data = " + data);
        return "/index";
    }

    /** 设置节点数据*/
    @RequestMapping("setDataZk")
    public String setDataZk(){
        zkClient=ZkcilentUtil.getZkClient("127.0.0.1");
        zkClient.writeData(path2,"222");
        return "/index";
    }
}
