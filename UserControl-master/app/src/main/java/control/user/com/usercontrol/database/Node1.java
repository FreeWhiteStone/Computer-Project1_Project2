package control.user.com.usercontrol.database;

/**
 * Created by AzizBaybur on 13.11.2017.
 */
class Node1 {
    public char data;
    Node left, right;
    double value;
    String character;


    public Node1(double value, String character) {
        this.value = value;
        this.character = character;
        left = null;
        right = null;
    }

    public Node1(Node left, Node right) {
        this.value = left.value + right.value;
        character = left.character + right.character;
        if (left.value < right.value) {
            this.right = right;
            this.left = left;
        } else {
            this.right = left;
            this.left = right;
        }
    }
}
