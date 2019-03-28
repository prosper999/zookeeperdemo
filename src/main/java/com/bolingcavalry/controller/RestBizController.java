package com.bolingcavalry.controller;

import org.apache.zookeeper.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * rest响应的controller
 *
 * @author bolingcavalry
 * @email zq2599@gmail.com
 * @date 2017/04/09 12:43
 */
@RestController
public class RestBizController {

    static final String _SERVER = "192.168.21.129:2181";

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String zkget(String node) {
        Watcher watcher = new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("get method.receive event：" + event);
            }
        };

        String value = null;
        try {
            final ZooKeeper zookeeper = new ZooKeeper(_SERVER, 999999, watcher);
            final byte[] data = zookeeper.getData(node, watcher, null);
            value = new String(data);
            zookeeper.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return "get value from zookeeper [" + value + "]";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String zkcreate(String node, String value) {
        Watcher watcher = new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("create method. receive event：" + event);
            }
        };

        try {
            final ZooKeeper zookeeper = new ZooKeeper(_SERVER, 999999, watcher);
            String path1 = zookeeper.create(node, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("Success create znode: " + path1);
            String path2 = zookeeper.create(node, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("Success create znode: " + path2);
            Thread.sleep(Integer.MAX_VALUE);
            zookeeper.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return "failed";
        }

        return "success";
    }


}