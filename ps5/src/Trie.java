import java.lang.reflect.Array;
import java.util.ArrayList;
import java.lang.String;
import java.util.Arrays;

public class Trie {

    // Wildcards
    final char WILDCARD = '.';
    public TrieNode root;

    private class TrieNode {
        public int asciiValue = 0;
        public TrieNode[] children = new TrieNode[62];
        public boolean isLeaf = false;

        public int getTrieNodeIndex(int i) {
            int j = 0;
            for (int h = 0; h < 62; h++) {
                if (this.children[h].asciiValue == i) {
                    j = h;
                    break;
                }
            }
            return j;
        }
        public boolean hasAnyChildren() {
            for (int i = 0; i < 62; i++) {
                if (this.children[i] != null) return true;
            }
            return false;
        }

        public boolean hasChild(int i) {
            for (int j = 0; j < this.children.length; j++) {
                if (this.children[j] != null) {
                    if (this.children[j].asciiValue == i) return true;
                }
            }
            return false;
        }
    }

    public Trie() {
        root = new TrieNode();
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        TrieNode currNode = root;

        for (int i = 0; i < s.length(); i++) {
            char currChar = s.charAt(i);
            int asciiInt = (int) currChar;

            int index = 0;

            if (asciiInt < 58 && asciiInt > 47) {
                index = asciiInt - 48;
            } else if (asciiInt > 64 && asciiInt < 91) {
                index = asciiInt - 55;
            } else {
                index = asciiInt - 61;
            }

            if (currNode.hasChild(asciiInt)) {
                TrieNode newNode = currNode.children[index];
                currNode = newNode;
            } else {
                TrieNode newNode = new TrieNode();
                newNode.asciiValue = asciiInt;
                currNode.children[index] = newNode;
                currNode = newNode;
            }
        }
        currNode.isLeaf = true;
    }

    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        TrieNode currNode = root;

        for (int i = 0; i < s.length(); i++) {
            char currChar = s.charAt(i);
            int asciiInt = (int) currChar;

            if (!currNode.hasChild(asciiInt)) return false;

            int nextIndex = currNode.getTrieNodeIndex(asciiInt);
            currNode = currNode.children[nextIndex];
        }
        return currNode.isLeaf == true;
    }

    // return null if char doesn't exist
    TrieNode charSearch(TrieNode node, char c) {
        // char to ascii
        int asciiInt = (int) c;
        for (int i = 0; i < 62; i++) {
            if (node.children[i].asciiValue == asciiInt) {
                return node.children[i];
            }
        }
        return null;
    }

    String printArrayList(ArrayList<String> als) {
        String s = "";
        for (int i = 0; i < als.size(); i++) {
            s += als.get(i) + " ";
        }
        return s;
    }

    // abc
    // acd
    // ae
    // ac

    // a.d - acd
    ArrayList<String> foo(String s, int i, ArrayList<String> results, TrieNode node, int limit) {
        // only check is leaf or nochildren once i >= s.length

        if (i >= s.length()) {
            // base case
            if (!node.hasAnyChildren()) {
                String leafNodeLetter = new Character((char) node.asciiValue).toString();
                ArrayList<String> a = new ArrayList<>();
                a.add(leafNodeLetter);
                return a;
            }

            String currNodeCharString = new Character((char) node.asciiValue).toString();
            ArrayList<String> temp1 = new ArrayList<>();

            if (node.isLeaf && node.hasAnyChildren()) {
                temp1.add(currNodeCharString);
            }
            for (int index = 0; index < 62; index++) {
                if (node.children[index] != null) {
                    ArrayList<String> temp = foo(s, i + 1, results, node.children[index], limit);
                    for (int j = 0; j < temp.size(); j++) {
                        temp1.add(currNodeCharString + temp.get(j));
                    }
                }
            }

            if (temp1.size() > limit) {
                ArrayList<String> temp2 = new ArrayList<>();
                for (int b = 0; b < limit; b++) {
                    temp2.add(temp1.get(b));
                }
                results = temp2;
                return results;
            }
            results = temp1;
            return results;

        } else {
            String currNodeCharString = new Character((char) node.asciiValue).toString();
            ArrayList<String> temp1 = new ArrayList<>();

            for (int index = 0; index < 62; index++) {
                char currChar = s.charAt(i);
                int currInt = (int) currChar;
                if (currChar == '.') { // take all
                    if (node.children[index] != null) {
                        ArrayList<String> temp = foo(s, i + 1, results, node.children[index], limit);
                        for (int j = 0; j < temp.size(); j++) {
                            temp1.add(currNodeCharString + temp.get(j));
                        }
                    }
                } else { // take just the currChar
                    if (node.children[index] != null) {
                        if (node.children[index].asciiValue == currInt) {
                            ArrayList<String> temp = foo(s, i + 1, results, node.children[index], limit);
                            for (int j = 0; j < temp.size(); j++) {
                                temp1.add(currNodeCharString + temp.get(j));
                            }
                        }
                    }
                }
            }

            if (temp1.size() > limit) {
                ArrayList<String> temp2 = new ArrayList<>();
                for (int b = 0; b < limit; b++) {
                    temp2.add(temp1.get(b));
                }
                results = temp2;
                return results;
            }
            results = temp1;
            return results;
        }
    }

    /**
     * Searches for strings with prefix matching the specified pattern sorted by lexicographical order. This inserts the
     * results into the specified ArrayList. Only returns at most the first limit results.
     *
     * @param s       pattern to match prefixes with
     * @param results array to add the results into
     * @param limit   max number of strings to add into results
     */
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        ArrayList<String> temp2 = new ArrayList<>();

        if (s == "") {
            for (int i = 0; i < 62; i++) {
                if (root.children[i] != null) {
                    temp2.addAll(foo("", 1, results, root.children[i], limit));
                }
            }
        } else {
            char currChar = s.charAt(0);
            int currInt = (int) currChar;

            if (currChar == '.') {
                for (int i = 0; i < 62; i++) {
                    if (root.children[i] != null) {
                        temp2.addAll(foo(s, 1, results, root.children[i], limit));
                    }
                }
            } else {
                for (int i = 0; i < 62; i++) {
                    if (root.children[i] != null) {
                        if (root.children[i].asciiValue == currInt) {
                            temp2.addAll(foo(s, 1, results, root.children[i], limit));
                        }
                    }
                }
            }
        }

        if (temp2.size() > limit) {
            ArrayList<String> temp3 = new ArrayList<>();
            for (int b = 0; b < limit; b++) {
                temp3.add(temp2.get(b));
            }
            results.addAll(temp3);
        } else {
            results.addAll(temp2);
        }

    }

    // PseudoCode
    /*
        foo(String s, int i, Arr A, TrieNode node)
            if i > s.length()
                return
            end

            currChar = s[i]

            if currChar == '.'
                for each elem in A
                    for each child in node
                        childChar = child.char
                        A.append(elem + child.char)
                        foo(String s, int i+1, Arr A, TrieNode child
                     end
                 end
            else
                for each elem in A
                    elem.concat(currChar)
                end
                childNode = node.children[of currChar]
                foo(String s, int i+1, Arr A, TrieNode childNode)
            end

     */


    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<String>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }

    public static void main(String[] args) {
        Trie t = new Trie();

        t.insert("abbde");
        t.insert("abcdef");

//        t.insert("paaa");
//        t.insert("paba");
//        t.insert("paca");
//        t.insert("pada");
//        t.insert("paea");
//        t.insert("pafa");
//        t.insert("paga");
//        t.insert("paha");

//        t.insert("aa");
//        t.insert("ab");
//        t.insert("and");
//        t.insert("abc");
//        t.insert("abci");

//
//        t.insert("peter");
//        t.insert("piper");
//        t.insert("picked");
//        t.insert("a");
//        t.insert("peck");
//        t.insert("of");
//        t.insert("pickled");
//        t.insert("peppers");
//        t.insert("pepppito");
//        t.insert("pepi");
//        t.insert("pik");
//        t.insert("aaa");

        String[] result1 = t.prefixSearch("......", 20);
//        String[] result1 = t.prefixSearch("pa.", 10);
        System.out.println(Arrays.toString(result1));
        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }
}
