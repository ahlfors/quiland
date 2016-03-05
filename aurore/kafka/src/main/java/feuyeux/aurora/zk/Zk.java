package feuyeux.aurora.zk;

import feuyeux.aurora.kafka.Conf;
import org.apache.zookeeper.*;

/**
 * Created by erichan
 * on 16/2/22.
 */
public class Zk {
    public static void main(String[] args) throws Exception {
        ZooKeeper zk = new ZooKeeper(Conf.zkConnect, Conf.zkTimeout, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("Event:" + event.getType());
            }
        });

        zk.create("/my_path", "root_data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.create("/my_path/my_branch", "branch_data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        System.out.println("my_path data:" + new String(zk.getData("/my_path", false, null)));
        System.out.println("my_branch data:" + new String(zk.getData("/my_path/my_branch", false, null)));
        System.out.println("my_path path:" + zk.getChildren("/my_path", true));

        zk.setData("/my_path/my_branch", "branch_new_data".getBytes(), -1);
        System.out.println("my_branch data:" + new String(zk.getData("/my_path/my_branch", false, null)));

        zk.delete("/my_path/my_branch", -1);
        System.out.println("my_path path:" + zk.getChildren("/my_path", true));
        zk.delete("/my_path", -1);
    }
}
