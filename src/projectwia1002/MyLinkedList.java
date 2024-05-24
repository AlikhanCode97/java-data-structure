package projectwia1002;

import java.io.Serializable;

public class MyLinkedList<E> implements Serializable {
    private Node<E> head, tail;
    private int size;


    public MyLinkedList() {
    }

    public int getSize() {
        return size;
    }

    public void addFirst(E e) {
        Node<E> newNode = new Node<E>(e);
        newNode.next = head;
        head = newNode;
        size++;

        if (tail == null)
            tail = head;
    }

    public void addLast(E e) {
        Node<E> newNode = new Node<E>(e);

        if (tail == null) {
            head = tail = newNode;
        }
        else {
            tail.next = newNode;
            tail = tail.next;
        }
        size++;
    }


    public void add(int index, E e) {
        if (index == 0) {
            addFirst(e);
        }
        else if (index >= size) {
            addLast(e);
        }
        else {
            Node<E> current = head;
            for (int i = 1; i < index; i++) {
                current = current.next;
            }
            Node<E> temp = current.next;
            current.next = new Node<E>(e);
            (current.next).next = temp;
            size++;
        }
    }


    public E removeFirst() {
        if (size == 0) {
            return null;
        }
        else {
            Node<E> temp = head;
            head = head.next;
            size--;
            if (head == null) {
                tail = null;
            }
            return temp.element;
        }
    }
    public void clear() {
        size = 0;
        head = tail = null;
    }
    public boolean contains(E e) {
        Node<E> current = head;
        for(int i = 0; i <  size ; i++){
            if(current.element.equals(e) ){
                return true;

            }
            current = current.next;


        }
        return false;
    }

    public E get(int index) {
        Node<E> current  = head;
        for(int i = 0 ; i < size ; i++){

            if( i == index){
                return current.element;
            }
            current = current.next;
        }
        return null;
    }
    public int indexOf(E e) {
        Node<E> current = head;
        for(int i = 0 ; i < size ; i++){

            if(current.element == e){
                return i;

            }
            current = current.next;

        }
        return 0;
    }
    public E removeLast() {
        if (size == 0) {
            return null;
        }
        else if (size == 1) {
            Node<E> temp = head;
            head = tail = null;
            size = 0;
            return temp.element;
        }
        else {
            Node<E> current = head;

            for (int i = 0; i < size - 2; i++) {
                current = current.next;
            }

            Node<E> temp = tail;
            tail = current;
            tail.next = null;
            size--;
            return temp.element;
        }
    }
    public E remove(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        else if (index == 0) {
            return removeFirst();
        }
        else if (index == size - 1) {
            return removeLast();
        }
        else {
            Node<E> previous = head;

            for (int i = 1; i < index; i++) {
                previous = previous.next;
            }

            Node<E> current = previous.next;
            previous.next = current.next;
            size--;
            return current.element;
        }
    }

    public E getLast() {
        if (size == 0) {
            return null;
        }
        else {
            return tail.element;
        }
    }
    @Override
    public String toString() {
        Node<E> current = head;
        String result = "[size=" + getSize() + "]";
        for (int i = 0; i < size; i++) {
            result += " >> " + current.element ;
            current = current.next;
        }

        return result;
    }
}


class Node<E> implements Serializable{

    E element;
    Node<E> next;

    public Node(E element) {
        this.element = element;
    }
}



