package feuyeux.strualgo.link;

/**
 *
 * Created by feuyeux@gmail.com
 * Date: Feb 19 2014
 * Time: 2:55 PM
 */
public class DNode {

    public DNode(Object data) {
        this.data = data;
    }

    private DNode previous;
    private DNode next;
    private Object data;

    public DNode getPrevious() {
        return previous;
    }

    public void setPrevious(DNode previous) {
        this.previous = previous;
    }

    public DNode getNext() {
        return next;
    }

    public void setNext(DNode next) {
        this.next = next;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}