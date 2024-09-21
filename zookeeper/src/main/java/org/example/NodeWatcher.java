package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class NodeWatcher implements Watcher {
    private static final String CONNECTION_ADDRESS = "localhost:2181";
    private static final int SESSION_TIMEOUT = 5000;
    private static final Logger logger = LoggerFactory.getLogger(NodeWatcher.class);
    private final String znode;
    private final String[] exec;
    private ZooKeeper zooKeeper;
    private Process process;

    public NodeWatcher(String znode, String[] exec) {
        this.exec = exec;
        this.znode = znode;
        try {
            zooKeeper = new ZooKeeper(CONNECTION_ADDRESS, SESSION_TIMEOUT, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        switch(event.getType()){
            case NodeCreated:
                try {
                    process = Runtime.getRuntime().exec(exec);
                    logger.info("Program opened");
                } catch (IOException e) {
                    logger.info("Opening the program failed");
                }
                break;
            case NodeDeleted:
                if (process == null || !process.isAlive()) {
                    logger.info("Process is not running");
                } else {
                    process.destroy();
                    logger.info("Process destroyed");
                }
                break;
            case NodeChildrenChanged:
                try {
                    if(zooKeeper.exists(znode, this) != null){
                        int noChilderen = zooKeeper.getChildren(znode, this).size();
                        logger.info("There is " + noChilderen + " children");
                    }
                } catch (KeeperException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;
        }

        watchNode();
    }

    public void watchNode() {
        try {
            Stat nodeStat = zooKeeper.exists(znode, this);
            if (nodeStat != null) {
                logger.info("Node exists");
                zooKeeper.getChildren(znode, this);
            } else {
                logger.info("Node doesn't exists");
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startCommandLine() throws IOException, InterruptedException, KeeperException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String command = br.readLine();
            if (command.equals("tree")) {
                Stat nodeStat = zooKeeper.exists(znode, this);
                if (nodeStat != null) {
                    printNodeTree();
                } else {
                    logger.info("Node /a doesn't exists");
                }
            }
        }
    }

    public void printNodeTree(){
        List<String> nodeTree = createNodeTree();
        nodeTree.sort(null);

        for(String nodePath: nodeTree){
            int noTabs = nodePath.split("/").length - 1;
            System.out.println("\t".repeat(noTabs - 1) + "|__ " + nodePath);
        }
    }

    private List<String> createNodeTree() {
        Queue<String> queue = new LinkedList<>();
        List<String> nodeTree = new ArrayList<>();
        queue.add(znode);

        while(!queue.isEmpty()){
            String currentNode = queue.poll();
            List<String> children = null;
            try {
                children = zooKeeper.getChildren(currentNode, false);
                children.forEach(child -> queue.add(currentNode + "/" + child));
                nodeTree.add(currentNode);
            } catch (KeeperException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return nodeTree;
    }
}
