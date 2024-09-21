package org.example;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        if (args.length < 2) {
            System.out.println("Wrong number of arguments");
            System.exit(1);
        }
        String znode = args[0];
        String[] exec = new String[args.length - 1];
        System.arraycopy(args, 1, exec, 0, exec.length);

        NodeWatcher nodeWatcher = new NodeWatcher(znode, exec);
        nodeWatcher.watchNode();
        nodeWatcher.startCommandLine();

    }
}