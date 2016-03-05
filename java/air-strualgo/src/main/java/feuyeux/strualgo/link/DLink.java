package feuyeux.strualgo.link;

import java.util.ArrayList;

/**
 * bidirectional chain table
 * 02/05(May)/2012
 * Thomson Reuters
 * @author Administrator
 */
public class DLink {
    private DNode first;

    public void insert(DNode e, DNode n) {
        DNode c = first;
        if (c == null) {
            first = n;
            return;
        }
        while (c != null) {
            if (c == e) {
                DNode t = c.getNext();
                c.setNext(n);
                n.setPrevious(c);
                if (t != null) {
                    n.setNext(t);
                    t.setPrevious(n);
                }
                break;
            } else
                c = c.getNext();
        }
    }

    public void remove(DNode r) {
        DNode c = first;
        if (c == null) {
            return;
        }
        while (c != null) {
            if (c == r) {
                DNode n = c.getNext();
                DNode p = c.getPrevious();
                if (p != null) {
                    p.setNext(n);
                }
                if (n != null) {
                    n.setPrevious(p);
                }
                break;
            } else
                c = c.getNext();
        }
    }

    public void print() {
        System.out.println("DLink print:");
        DNode c = first;
        if (c == null) {
            return;
        }
        while (c != null) {
            System.out.println(c.getData());
            c = c.getNext();
        }
    }

    public static void main(String[] args) {
        DLink bioLinkTest = new DLink();

        ArrayList<DNode> temp = new ArrayList<DNode>();
        for (int i = 0; i < 10; i++) {
            temp.add(new DNode(i));
        }
        bioLinkTest.insert(null, temp.get(0));
        for (int i = 1; i < temp.size(); i++) {
            DNode e = temp.get(i - 1);
            DNode n = temp.get(i);
            bioLinkTest.insert(e, n);
        }
        bioLinkTest.print();
        bioLinkTest.remove(temp.get(3));
        bioLinkTest.remove(temp.get(6));
        bioLinkTest.print();
    }
}
