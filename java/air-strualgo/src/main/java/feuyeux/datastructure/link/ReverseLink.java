package feuyeux.datastructure.link;

/**
 * Created by erichan on 12/12/13.
 */
public class ReverseLink {
    public static void main(final String[] args) {
        final LinkNode a = new LinkNode("A");
        final LinkNode b = new LinkNode("B");
        final LinkNode c = new LinkNode("C");
        final LinkNode d = new LinkNode("D");
        a.next = b;
        b.next = c;
        c.next = d;

        iter(a);
        reverse(a);
        iter(d);
        reverseMe(d);
        iter(a);
    }

    private static void iter(LinkNode current) {
        while (current != null) {
            System.out.println(current);
            current = current.next;
        }
        System.out.println("----");
    }

    public static LinkNode reverse(LinkNode current) {
        //initialization
        LinkNode previousNode = null;
        LinkNode nextNode = null;

        while (current != null) {
            //save the next node
            nextNode = current.next;
            //update the value of "next"
            current.next = previousNode;
            //shift the pointers
            previousNode = current;
            current = nextNode;
        }
        return previousNode;
    }

    public static LinkNode reverseMe(final LinkNode current) {
        if (current == null || current.next == null) {
            return current;
        }
        final LinkNode nextNode = current.next;
        current.next = null;
        final LinkNode reverseNode = reverseMe(nextNode);
        nextNode.next = current;
        return reverseNode;
    }
}

class LinkNode {
    public String data;
    public LinkNode next;

    public LinkNode(final String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data;
    }
}
