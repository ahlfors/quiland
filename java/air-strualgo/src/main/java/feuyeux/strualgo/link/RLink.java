package feuyeux.strualgo.link;

/**
 * Created by erichan on 12/12/13.
 */
public class RLink {
    public static Node reverse(Node current) {
        //initialization
        Node previousNode = null;
        Node nextNode = null;

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

    public static Node reverseMe(Node current) {
        if (current == null || current.next == null) {
            return current;
        }
        Node nextNode = current.next;
        current.next = null;
        Node reverseNode = reverseMe(nextNode);
        nextNode.next = current;
        return reverseNode;
    }

    public static void main(String[] args) {
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        Node d = new Node("D");
        a.next = b;
        b.next = c;
        c.next = d;

        iter(a);
        reverse(a);
        iter(d);
        reverseMe(d);
        iter(a);
    }

    private static void iter(Node current) {
        while (current != null) {
            System.out.println(current);
            current = current.next;
        }
        System.out.println("----");
    }
}


