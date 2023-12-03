import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<T> {
    private Node<T> head, tail;
    private int size = 0;

    public MyLinkedList() {
        // Konstruktor bleibt leer
    }

    public void addFirst(T data) {
        head = new Node<>(data, head);
        if (tail == null) {
            tail = head;
        }
        size++;
    }

    public void addLast(T data) {
        Node<T> newNode = new Node<>(data, null);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
    }

    public T getFirst() {
        return size == 0 ? null : head.getData();
    }

    public T getLast() {
        return size == 0 ? null : tail.getData();
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> n = head;

            @Override
            public boolean hasNext() {
                return n != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = n.getData();
                n = n.getNext();
                return data;
            }
        };
    }

}

class Node<T> {
    private final T data;
    private Node<T> next;

    Node(T data, Node<T> next) {
        this.data = data;
        this.next = next;
    }

    public T getData() {
        return this.data;
    }

    public Node<T> getNext() {
        return this.next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }
}
