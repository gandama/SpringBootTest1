package com.example.demo.util;

import org.I0Itec.zkclient.ZkClient;

/**
 * Created by Administrator on 2018/11/22.
 */
public class ZkcilentUtil {
    private static ZkClient zkClient;

    public static ZkClient getZkClient(String host){
        if (zkClient != null) {
            return zkClient;
        }
        zkClient=new ZkClient(host,10000);
        return zkClient;
    }
}
