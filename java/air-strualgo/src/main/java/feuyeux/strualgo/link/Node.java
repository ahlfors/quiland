package feuyeux.strualgo.link;

/**
 *
 * Created by feuyeux@gmail.com
 * Date: Feb 19 2014
 * Time: 3:02 PM
 */
public class Node {
    public Object data;
    public Node next;

    public Node(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}