package control.user.com.usercontrol.database;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;

public class Encode {
    static final boolean readFromFile = false;
    static final boolean newTextBasedOnOldOne = false;

    static PriorityQueue<Node> nodes = new PriorityQueue<>((o1, o2) -> (o1.value < o2.value) ? -1 : 1);
    static TreeMap<Character, String> codes = new TreeMap<>();
    static String text = ""; //textviewden gelen değer
    static String encoded = "";

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

    public static String encodeText(String txt) {
        text=txt;  //text global değişken, yukarıda textview den gelecek
        encoded = "";
        ASCII = new int[128];
        nodes.clear();
        codes.clear();
        encoded = "";

        System.out.println("Text: " + text);
        calculateCharIntervals(nodes, true);
        buildTree(nodes);
        generateCodes(nodes.peek(), "");
        for (int i = 0; i < text.length(); i++)
            encoded += codes.get(text.charAt(i));
        return encoded;
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
