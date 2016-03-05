package feuyeux.datastructure.tree;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class SimpleTreeNode<T> {

    T data;
    SimpleTreeNode<T> parent;
    List<SimpleTreeNode<T>> children;

    public SimpleTreeNode(final T data) {
        this.data = data;
        this.children = new LinkedList<SimpleTreeNode<T>>();
    }

    public SimpleTreeNode<T> addChild(final T child) {
        final SimpleTreeNode<T> childNode = new SimpleTreeNode<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    /*
     * http://en.wikipedia.org/wiki/File:Sorted_binary_tree_preorder.svg
     * Pre-order: F, B, A, D, C, E, G, I, H
     */
    public java.util.Queue<T> preOrder() {
        final Queue<T> queue = new LinkedBlockingQueue<>();
        queue.add(data);
        preOrder0(queue, children);
        return queue;
    }

    private void preOrder0(final Queue<T> queue, final List<SimpleTreeNode<T>> children) {
        if (children == null || children.isEmpty()) {
            return;
        }
        for (final SimpleTreeNode<T> node : children) {
            queue.add(node.data);
            preOrder0(queue, node.children);
        }
    }

    /*
     * http://en.wikipedia.org/wiki/File:Sorted_binary_tree_inorder.svg
     * In-order: A, B, C, D, E, F, G, H, I
     */
    public java.util.Queue<T> inOrder4BinaryTree() {
        final Queue<T> queue = new LinkedBlockingQueue<>();
        return queue;
    }

    /*
     * http://en.wikipedia.org/wiki/File:Sorted_binary_tree_postorder.svg
     * Post-order: A, C, E, D, B, H, I, G, F
     */
    public java.util.Queue<T> postOrder4BinaryTree() {
        final Queue<T> queue = new LinkedBlockingQueue<>();
        return queue;
    }

    /*
      F 
    B  G
    A D  I
    C E H
     */
    @SuppressWarnings("unused")
    public static void main(final String[] args) {
        final SimpleTreeNode<String> root = new SimpleTreeNode<String>("F");
        {
            final SimpleTreeNode<String> node0 = root.addChild("B");
            final SimpleTreeNode<String> node1 = root.addChild("G");
            {
                final SimpleTreeNode<String> node01 = node0.addChild("A");
                final SimpleTreeNode<String> node02 = node0.addChild("D");
                final SimpleTreeNode<String> node11 = node1.addChild("I");
                {
                    final SimpleTreeNode<String> node021 = node02.addChild("C");
                    final SimpleTreeNode<String> node022 = node02.addChild("E");
                    final SimpleTreeNode<String> node111 = node11.addChild("H");
                }
            }
        }
        final Queue<String> preOrderQueue = root.preOrder();
        for (final String node : preOrderQueue) {
            System.out.println(node);
        }
    }
}
