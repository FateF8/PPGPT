import java.util.Iterator;
import java.util.NoSuchElementException;

@Verantwortlich(person = "Julian Gao")
public class List implements Iterable {
    private Node head, tail;
    private int size = 0;

    static class Node {
        private Object data;
        private Node next;

        Node(Object data, Node next) {
            this.data = data;
            this.next = next;
        }

        public Object getData() {
            return this.data;
        }

        public Node getNext() {
            return this.next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

/*    public void addFirst(Object data) {
        head = new Node(data, head);
        if (tail == null) {
            tail = head;
        }
        size++;
    }
*/

    public void addLast(Object data) {
        Node newNode = new Node(data, null);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
    }

    public boolean remove(Object data) {
        Node current = head;
        Node prev = null;

        while (current != null) {
            if (current.getData().equals(data)) {
                if (prev == null) { // Element ist head
                    head = current.getNext();
                } else {
                    prev.setNext(current.getNext());
                }
                if (current == tail) { // Element ist tail
                    tail = prev;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.getNext();
        }
        return false;
    }

    public Iterator iterator() {
        return new Iterator() {
            private Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Object next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Object data = current.getData();
                current = current.getNext();
                return data;
            }
        };
    }

    public int getSize() {
        return size;
    }


}