package Server;

import java.net.*;
import java.io.*;
/**
 * 用户链表的结点类 
 */
public class Node {
    public String username = null;
    public Socket socket = null;
    public ObjectOutputStream output = null;
    public ObjectInputStream input = null;
    public Node next = null;
} 