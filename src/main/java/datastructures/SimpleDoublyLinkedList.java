package datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("unchecked")
public class SimpleDoublyLinkedList<T> {

    private  Node<T> root;
    private Node<T> last;


    public SimpleDoublyLinkedList(T data) {
        root = new Node<>(data);
        this.last = root;
    }

    public Node getFirst() {
        return root;
    }

    public Node getLast(){
        return last;
    }

    public Node insertLast(T data){
        if(data==null) throw new IllegalArgumentException("null value unexpected");
        if(last==null)return insertRoot(data);
        last.next = new Node(data);
        last = last.getNext();
        return last;
    }

    private Node insertRoot(T data){
        this.root = new Node(data);
        this.last = root;
        return root;
    }
    public Node removeLast(){
        if(last==null)return null;
        var oldLast = last;
        last = oldLast.previous;
        return oldLast;
    }

    public Node insertFirst(T data){
        if(data==null) throw new IllegalArgumentException("null value unexpected");
        if(root==null)return insertRoot(data);
        var oldRoot = root;
        root = new Node(data);
        root.next = oldRoot;
        return root;

    }

    public T getMin(Comparator<T> comparator){
        if(this.root == null) return null;
        var temp = root;
        var min  = root.data;
        while(temp!=null){
            if(comparator.compare(temp.data , min) < 0) min = temp.data;
            temp = temp.getNext();
        }
        return min;
    }

    public T getMax(Comparator<T> comparator){
        if(this.root == null) return null;
        var temp = root;
        var max = root.data;
        while(temp!=null){
            if(comparator.compare(temp.data , max) > 0) max = temp.data;
            temp = temp.getNext();
        }
        return max;
    }

    public Node removeFirst(){
        if(root==null)return null;
        var currentRoot = root;
        root = root.next;
        return currentRoot;
    }

    public T find(T key) {
        if(this.root == null) return null;
        var temp = root;
        while(temp!=null){
            if(temp.data.equals(key)) return temp.data;
            temp = temp.getNext();
        }
        return null;
    }

    public T remove(T key){
        if(this.root == null) return null;
        var temp = root;
        while(temp!=null){
            if(temp.data == key) break;
            temp = temp.getNext();
        }
        if(temp!=null){
            temp.previous.next = temp.next;
            temp.next.previous = temp.previous;
            temp.next = null;
            temp.previous = null;
            return temp.data;
        }
        return null;
    }

    public List<T> toList(){
        if(root==null)return Collections.emptyList();
        ArrayList<T> t = new ArrayList<>();
        var temp = root;
        while(temp != null){
            t.add(temp.data);
            temp = temp.next;
        }
        return t;
    }



    class Node<E>{
        private Node next;
        private Node previous;

        private E data;

        E getData() {
            return data;
        }

        Node getNext(){
            return this.next;
        }

        Node getPrevious(){
            return this.previous;
        }

        Node(E data) {
            this.data = data;
        }
    }


}
