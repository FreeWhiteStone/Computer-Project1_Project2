package control.user.com.usercontrol.database;

/**
 * Created by AzizBaybur on 13.11.2017.
 */

import android.os.Build;
import android.support.annotation.RequiresApi;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;

public class Decode {
    static final boolean readFromFile = false;
    static final boolean newTextBasedOnOldOne = false;

    static PriorityQueue<Node> nodes = new PriorityQueue<>((o1, o2) -> (o1.value < o2.value) ? -1 : 1);
    static TreeMap<Character, String> codes = new TreeMap<>();
    static String text = "ismet";  //veritabanından gelen değeri alacağız
    static String encoded = "100011011101"; //veritabından alacağız
    static String decoded = "";
    static int ASCII[] = new int[128];

    public static boolean IsSameCharacterSet() {
        boolean flag = true;
        for (int i = 0; i < text.length(); i++)
            if (ASCII[text.charAt(i)] == 0) {
                flag = false;
                break;
            }
        return flag;
    }

    public static void encodeText() {
        String input=text;  //text global değişken, yukarıda veritabanından gelecek
        encoded = "";
        ASCII = new int[128];
        nodes.clear();
        codes.clear();
        encoded = "";
        decoded = "";
        System.out.println("Text: " + text);
        calculateCharIntervals(nodes, true);
        buildTree(nodes);
        generateCodes(nodes.peek(), "");
        for (int i = 0; i < text.length(); i++)
            encoded += codes.get(text.charAt(i));
        System.out.println("Encoded Text: " + encoded);
    }
    public static String decodeText(String txt) {
        encoded = txt;
        decoded = "";
        Node node = nodes.peek();
        for (int i = 0; i < encoded.length(); ) {
            Node tmpNode = node;
            while (tmpNode.left != null && tmpNode.right != null && i < encoded.length()) {
                if (encoded.charAt(i) == '1')
                    tmpNode = tmpNode.right;
                else tmpNode = tmpNode.left;
                i++;
            }
            if (tmpNode != null)
                if (tmpNode.character.length() == 1)
                    decoded += tmpNode.character;
                else
                    System.out.println("Input not Valid");

        }
        return decoded;
    }



    public static void buildTree(PriorityQueue<Node> vector) {
        while (vector.size() > 1)
            vector.add(new Node(vector.poll(), vector.poll()));
    }

    public static void printCodes() {
        System.out.println("--- Printing Codes ---");
        codes.forEach((k, v) -> System.out.println("'" + k + "' : " + v));
    }

    public static void calculateCharIntervals(PriorityQueue<Node> vector, boolean printIntervals) {
        // if (printIntervals) System.out.println("-- intervals --");

        for (int i = 0; i < text.length(); i++)
            ASCII[text.charAt(i)]++;

        for (int i = 0; i < ASCII.length; i++)
            if (ASCII[i] > 0) {
                vector.add(new Node(ASCII[i] / (text.length() * 1.0), ((char) i) + ""));
	               /* if (printIntervals)
	                    System.out.println("'" + ((char) i) + "' : " + ASCII[i] / (text.length() * 1.0));*/
            }
    }
    public static void generateCodes(Node node, String s) {
        if (node != null) {
            if (node.right != null)
                generateCodes(node.right, s + "1");

            if (node.left != null)
                generateCodes(node.left, s + "0");

            if (node.left == null && node.right == null)
                codes.put(node.character.charAt(0), s);

        }
    }
}
