import com.sun.source.tree.Tree;

import java.util.Arrays;

/**
 * ScapeGoat Tree class
 *
 * This class contains some of the basic code for implementing a ScapeGoat tree.
 * This version does not include any of the functionality for choosing which node
 * to scapegoat.  It includes only code for inserting a node, and the code for rebuilding
 * a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     *
     * This class holds the data for a node in a binary tree.
     *
     * Note: we have made things public here to facilitate problem set grading/testing.
     * In general, making everything public like this is a bad idea!
     *
     */
    public static class TreeNode {
        int key;
        public TreeNode left = null;
        public TreeNode right = null;

        TreeNode(int k) {
            key = k;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the specified subtree
     *
     * @param node  the parent node, not to be counted
     * @param child the specified subtree
     * @return number of nodes
     */
    public int countNodes(TreeNode node, Child child) {
        if (child == Child.RIGHT) {
            if (node.right == null) {
                return 0;
            } else {
                return 1 + countNodes(node.right, Child.RIGHT) + countNodes(node.right, Child.LEFT);
            }
        } else {
            if (node.left == null) {
                return 0;
            } else {
                return 1 + countNodes(node.left, Child.RIGHT) + countNodes(node.left, Child.LEFT);
            }
        }
    }

    public TreeNode[] helper(TreeNode node, TreeNode[] arr, int index) {
        if (node.left == null && node.right == null) {
            arr[index] = node;
            return arr;
        }
        if (node.left != null) {
            arr = helper(node.left, arr, index);
        }

        int num_elems = countNodes(node, Child.LEFT);
        index += num_elems;
        arr[index] = node;
        index++;

        if (node.right != null) {
            arr = helper(node.right, arr, index);
        }
        return arr;
    }

    /**
     * Builds an array of nodes in the specified subtree
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    public TreeNode[] enumerateNodes(TreeNode node, Child child) {
        int size = countNodes(node, child);
        TreeNode[] arr = new TreeNode[size];

        if (size == 0) {
            TreeNode[] empty = {};
            return empty;
        }

        if (child == Child.LEFT) {
            if (node.left != null) {
                return helper(node.left, arr, 0);
            } else {
                return null;
            }
        } else {
            if (node.right != null) {
                return helper(node.right, arr, 0);
            } else {
                return null;
            }
        }
    }

    public TreeNode builder(TreeNode[] arr, int lower, int higher) {
        if (lower > higher) {
            return null;
        }
        int mid = (lower + higher) / 2;

        int midNum = arr[mid].key;
        TreeNode root = new TreeNode(midNum);
        TreeNode left = builder(arr, lower, mid - 1);
        TreeNode right = builder(arr, mid + 1, higher);

        root.left = left;
        root.right = right;

        return root;
    }

    /**
     * Builds a tree from the list of nodes
     * Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    public TreeNode buildTree(TreeNode[] nodeList) {
        return builder(nodeList, 0, nodeList.length - 1);
    }

    /**
    * Rebuilds the specified subtree of a node
    * 
    * @param node the part of the subtree to rebuild
    * @param child specifies which child is the root of the subtree to rebuild
    */
    public void rebuild(TreeNode node, Child child) {
        // Error checking: cannot rebuild null tree
        if (node == null) return;
        // First, retrieve a list of all the nodes of the subtree rooted at child
        TreeNode[] nodeList = enumerateNodes(node, child);
        // Then, build a new subtree from that list
        TreeNode newChild = buildTree(nodeList);
        // Finally, replace the specified child with the new subtree
        if (child == Child.LEFT) {
            node.left = newChild;
        } else if (child == Child.RIGHT) {
            node.right = newChild;
        }
    }

    /**
    * Inserts a key into the tree
    *
    * @param key the key to insert
    */
    public void insert(int key) {
        if (root == null) {
            root = new TreeNode(key);
            return;
        }

        TreeNode node = root;

        while (true) {
            if (key <= node.key) {
                if (node.left == null) break;
                node = node.left;
            } else {
                if (node.right == null) break;
                node = node.right;
            }
        }

        if (key <= node.key) {
            node.left = new TreeNode(key);
        } else {
            node.right = new TreeNode(key);
        }
    }

    // Simple main function for debugging purposes
    public static void main(String[] args) {
//        SGTree tree = new SGTree();
//        TreeNode left_tree = new TreeNode(2);
//        TreeNode left1 = new TreeNode(1);
//        TreeNode right1 = new TreeNode(3);
//
//        left_tree.left = left1;
//        left_tree.right = right1;
//
//        TreeNode right_tree = new TreeNode(6);
//        TreeNode left2 = new TreeNode(5);
//        TreeNode right2 = new TreeNode(7);
//
//        right_tree.left = left2;
//        right_tree.right = right2;
//
//        TreeNode mid = new TreeNode(4);
//
//        mid.left = left_tree;
//        mid.right = right_tree;
//
//        TreeNode big = new TreeNode(8);
//        big.left = mid;
//
//        tree.root = big;
//
//
//        System.out.println(Arrays.toString(enumerateNodes1(tree.root, Child.LEFT)));
//        System.out.println(countNodes(tree, Child.LEFT));
    }
}
