/**
 * ScapeGoat Tree class
 * <p>
 * This class contains some basic code for implementing a ScapeGoat tree. This version does not include any of the
 * functionality for choosing which node to scapegoat. It includes only code for inserting a node, and the code for
 * rebuilding a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     * <p>
     * This class holds the data for a node in a binary tree.
     * <p>
     * Note: we have made things public here to facilitate problem set grading/testing. In general, making everything
     * public like this is a bad idea!
     */
    public static class TreeNode {
        int key;
        public TreeNode left = null;
        public TreeNode right = null;
        public float weight;

        TreeNode(int k) {
            this.key = k;
            this.weight = 1;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the specified subtree.
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

        int numElems = countNodes(node, Child.LEFT);
        index += numElems;
        arr[index] = node;
        index++;

        if (node.right != null) {
            arr = helper(node.right, arr, index);
        }
        return arr;
    }

    /**
     * Builds an array of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    TreeNode[] enumerateNodes(TreeNode node, Child child) {
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
     * Builds a tree from the list of nodes Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    TreeNode buildTree(TreeNode[] nodeList) {
        return builder(nodeList, 0, nodeList.length - 1);
    }

    /**
     * Determines if a node is balanced. If the node is balanced, this should return true. Otherwise, it should return
     * false. A node is unbalanced if either of its children has weight greater than 2/3 of its weight.
     *
     * @param node a node to check balance on
     * @return true if the node is balanced, false otherwise
     */
    public boolean checkBalance(TreeNode node) {
        if (node == null) {
            return true;
        }

        float curr = 2 * node.weight / 3;

        if (node.left == null && node.right == null) {
            return true;
        } else if (node.left == null) {
            return node.right.weight <= curr;
        } else if (node.right == null) {
            return node.left.weight <= curr;
        } else {
            return node.left.weight <= curr && node.right.weight <= curr;
        }
    }

    void fixWeights(TreeNode node) {
        if (node == null) return;
        if (node.left == null && node.right == null) {
            node.weight = 1;
            return;
        }

        if (node.left == null) {
            fixWeights(node.right);
            node.weight = 1 + node.right.weight;
        } else if (node.right == null) {
            fixWeights(node.left);
            node.weight = 1 + node.left.weight;
        } else {
            fixWeights(node.right);
            fixWeights(node.left);
            node.weight = 1 + node.right.weight + node.left.weight;
        }
    }

    /**
     * Rebuilds the specified subtree of a node.
     *
     * @param node  the part of the subtree to rebuild
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
            // Update all nodes in the specified subtree
            fixWeights(node.left);
        } else if (child == Child.RIGHT) {
            node.right = newChild;
            fixWeights(node.right);
        }
    }

    /**
     * Inserts a key into the tree.
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
                node.weight += 1;
                if (node.left == null) break;
                node = node.left;
            } else {
                node.weight += 1;
                if (node.right == null) break;
                node = node.right;
            }
        }

        if (key <= node.key) {
            node.left = new TreeNode(key);
        } else {
            node.right = new TreeNode(key);
        }

        // check for rebuilding down the root to leaf path
        node = root;
        while (node.key != key) {
            if (key <= node.key) {
                if (!checkBalance(node.left)) {
                    rebuild(node, Child.LEFT);
                    break;
                }
                if (node.left == null) {
                    break;
                } else {
                    node = node.left;
                }
            } else {
                if (!checkBalance(node.right)) {
                    rebuild(node, Child.RIGHT);
                    break;
                }
                if (node.right == null) {
                    break;
                } else {
                    node = node.right;
                }
            }
        }
    }

    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 100; i++) {
            tree.insert(i);
        }
        tree.rebuild(tree.root, Child.RIGHT);
        System.out.println(tree.countNodes(tree.root, Child.RIGHT));
    }
}
