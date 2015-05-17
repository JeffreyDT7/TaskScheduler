import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by jinyangsuo on 16/05/15.
 */
public class TaskScheduler {

    public static class Task{
        String taskName;
        int releasetime;
        int deadline;
        public Task(String s, int t1, int t2){
            taskName = s;
            releasetime = t1;
            deadline = t2;
        }

        public Task(){}

        public void setTaskName(String s){
            taskName = s;
        }
        public String getTaskName(){
            return taskName;
        }

        public void setReleasetime(int t1){
            releasetime = t1;
        }

        public int getReleasetime(){
            return releasetime;
        }

        public void setDeadline(int t2){
            deadline = t2;
        }
        public int getDeadline(){
            return deadline;
        }
    }


    static void scheduler(String file1, String file2, Integer m){
        try {
            FileInputStream inputFile = new FileInputStream(file1);
            BufferedReader readBuffer = new BufferedReader(new InputStreamReader(inputFile));
//            String s = new String();
            int value;
//            String name = "";
            String[] tiny = new String[1000];
            for(int i = 0; i < 1000; i++)
                tiny[i] = "";

            int i = 0;
            while ((value = readBuffer.read()) != -1){
                char S = (char) value;
                if (S == ' ' || S == '\n'){
                    ++i;
                }else{
                    tiny[i] += S;
                }
            }
            for (int j = 0 ; j <= i; j++){
                System.out.println(tiny[j]);
            }

            int total = (i+1)/3;
            Task[] task = new Task[total + 1];
            for(int j = 0;  j < total + 1; j++)
                task[j] = new Task();
//            task[0].taskName = "aaa";
            int  k = 0;
//            for(int j = 0; j <=2 ; j++)
//                System.out.println(tiny[j]);

            for (int j = 0; j <= i; ){
                task[k].setTaskName(tiny[j++]);
                task[k].setReleasetime(Integer.parseInt(tiny[j++]));
                task[k].setDeadline(Integer.parseInt(tiny[j++]));
                k++;
            }

            File f = new File(file2);
//            if (f.exists()){
//                System.out.println("file2 already exits");
//            }
            FileOutputStream outputFile = new FileOutputStream(f);

            String outputS = new String();
            for(int j = 0; j < total; j++)
                outputS += task[j].getTaskName();
//             s  = "Hello, man!";
            outputFile.write(outputS.getBytes());
//            System.out.println(s);
        }catch (FileNotFoundException e){
            System.out.println("file1 does not exits.");
        }catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) throws Exception{

        TaskScheduler.scheduler("samplefile1.txt", "feasibleschedule1", 4);
        /** There is a feasible schedule on 4 cores */
//        TaskScheduler.scheduler("samplefile1.txt", "feasibleschedule2", 3);
        /** There is no feasible schedule on 3 cores */
//        TaskScheduler.scheduler("samplefile2.txt", "feasibleschedule3", 5);
        /** There is a feasible scheduler on 5 cores */
//        TaskScheduler.scheduler("samplefile2.txt", "feasibleschedule4", 4);
        /** There is no feasible schedule on 4 cores */

        /** The sample task sets are sorted. You can shuffle the tasks and test your program again */
    }
}




interface Task<T, R, D>{
    public String getName();
    public int getReleasetime();
    public int getDeadline();
}


//begin#fragment HeapPriorityQueue
/**
 * Realization of a priority queue by means of a heap.  A complete
 * binary tree realized by means of an array list is used to
 * represent the heap.
 //end#fragment HeapPriorityQueue
 *
 * @author Roberto Tamassia, Michael Goodrich, Eric Zamore
//begin#fragment HeapPriorityQueue
 */
class HeapPriorityQueue<K,V> implements PriorityQueue<K,V> {
    protected CompleteBinaryTree<Entry<K,V>> heap;	// underlying heap
    protected Comparator<K> comp;	// comparator for the keys
    /** Inner class for heap entries. */
    protected static class  MyEntry<K,V> implements Entry<K,V> {
        protected K key;
        protected V value;
        public MyEntry(K k, V v) { key = k; value = v; }
        public K getKey() { return key; }
        public V getValue() { return value; }
        public String toString() { return "(" + key  + "," + value + ")"; }
    }
    /** Creates an empty heap with the default comparator */
    public HeapPriorityQueue() {
        heap = new ArrayListCompleteBinaryTree<Entry<K,V>>(); // use an array list
        comp = new DefaultComparator<K>();     // use the default comparator
    }
    /** Creates an empty heap with the given comparator */
    public HeapPriorityQueue(Comparator<K> c) {
        heap = new ArrayListCompleteBinaryTree<Entry<K,V>>();
        comp = c;
    }
//end#fragment HeapPriorityQueue
    /** Sets the comparator used for comparing items in the heap.
     * @throws IllegalStateException if priority queue is not empty */
    public void setComparator(Comparator<K> c) throws IllegalStateException {
        if(!isEmpty())  // this is only allowed if the priority queue is empty
            throw new IllegalStateException("Priority queue is not empty");
        comp = c;
    }
//begin#fragment HeapPriorityQueue
    /** Returns the size of the heap */
    public int size() { return heap.size(); }
    /** Returns whether the heap is empty */
    public boolean isEmpty() { return heap.size() == 0; }
    //end#fragment HeapPriorityQueue
    //begin#fragment mainMethods
    /** Returns but does not remove an entry with minimum key */
    public Entry<K,V> min() throws EmptyPriorityQueueException {
        if (isEmpty())
            throw new EmptyPriorityQueueException("Priority queue is empty");
        return heap.root().element();
    }
    /** Inserts a key-value pair and returns the entry created */
    public Entry<K,V> insert(K k, V x) throws InvalidKeyException {
        checkKey(k);  // may throw an InvalidKeyException
        Entry<K,V> entry = new MyEntry<K,V>(k,x);
        upHeap(heap.add(entry));
        return entry;
    }
    /** Removes and returns an entry with minimum key */
    public Entry<K,V> removeMin() throws EmptyPriorityQueueException {
        if (isEmpty())
            throw new EmptyPriorityQueueException("Priority queue is empty");
        Entry<K,V> min = heap.root().element();
        if (size() == 1)
            heap.remove();
        else {
            heap.replace(heap.root(), heap.remove());
            downHeap(heap.root());
        }
        return min;
    }
    /** Determines whether a given key is valid */
    protected void checkKey(K key) throws InvalidKeyException {
        try {
            comp.compare(key,key);
        }
        catch(Exception e) {
            throw new InvalidKeyException("Invalid key");
        }
    }
    //end#fragment mainMethods
    //begin#fragment auxiliary
    /** Performs up-heap bubbling */
    protected void upHeap(Position<Entry<K,V>> v) {
        Position<Entry<K,V>> u;
        while (!heap.isRoot(v)) {
            u = heap.parent(v);
            if (comp.compare(u.element().getKey(), v.element().getKey()) <= 0) break;
            swap(u, v);
            v = u;
        }
    }
    /** Performs down-heap bubbling */
    protected void downHeap(Position<Entry<K,V>> r) {
        while (heap.isInternal(r)) {
            Position<Entry<K,V>> s;		// the position of the smaller child
            if (!heap.hasRight(r))
                s = heap.left(r);
            else if (comp.compare(heap.left(r).element().getKey(),
                    heap.right(r).element().getKey()) <=0)
                s = heap.left(r);
            else
                s = heap.right(r);
            if (comp.compare(s.element().getKey(), r.element().getKey()) < 0) {
                swap(r, s);
                r = s;
            }
            else
                break;
        }
    }
    /** Swaps the entries of the two given positions */
    protected void swap(Position<Entry<K,V>> x, Position<Entry<K,V>> y) {
        Entry<K,V> temp = x.element();
        heap.replace(x, y.element());
        heap.replace(y, temp);
    }
    /** Text visualization for debugging purposes */
    public String toString() {
        return heap.toString();
    }
    //end#fragment auxiliary
}

//begin#fragment PriorityQueue
/** Interface for the priority queue ADT */
interface PriorityQueue<K,V> {
    /** Returns the number of items in the priority queue. */
    public int size();
    /** Returns whether the priority queue is empty. */
    public boolean isEmpty();
    /** Returns but does not remove an entry with minimum key. */
    public Entry<K,V> min() throws EmptyPriorityQueueException;
    /** Inserts a key-value pair and return the entry created. */
    public Entry<K,V> insert(K key, V value) throws InvalidKeyException;
    /** Removes and returns an entry with minimum key. */
    public Entry<K,V> removeMin() throws EmptyPriorityQueueException;
}
//end#fragment PriorityQueue


//begin#fragment Entry
/** Interface for a key-value pair entry **/
interface Entry<K,V> {
    /** Returns the key stored in this entry. */
    public K getKey();
    /** Returns the value stored in this entry. */
    public V getValue();
}
//end#fragment Entry
//begin#fragment HeapTree

interface CompleteBinaryTree<E> extends BinaryTree<E> {
//end#fragment HeapTree
    /** Adds an element to the tree just after the last node. Returns
     * the newly created position. */
//begin#fragment HeapTree
    public Position<E> add(E elem);
//end#fragment HeapTree
    /** Removes and returns the element stored in the last node of the
     * tree. */
//begin#fragment HeapTree
    public E remove();
}
//end#fragment HeapTree

interface BinaryTree<E> extends Tree<E> {
    /** Returns the left child of a node. */
    public Position<E> left(Position<E> v)
            throws InvalidPositionException, BoundaryViolationException;
    /** Returns the right child of a node. */
    public Position<E> right(Position<E> v)
            throws InvalidPositionException, BoundaryViolationException;
    /** Returns whether a node has a left child. */
    public boolean hasLeft(Position<E> v) throws InvalidPositionException;
    /** Returns whether a node has a right child. */
    public boolean hasRight(Position<E> v) throws InvalidPositionException;
}
//end#fragment Tree

interface Tree<E> {
    /** Returns the number of nodes in the tree. */
    public int size();
    /** Returns whether the tree is empty. */
    public boolean isEmpty();
    /** Returns an iterator of the elements stored in the tree. */
    public Iterator<E> iterator();
    /** Returns an iterable collection of the the nodes. */
    public Iterable<Position<E>> positions();
    /** Replaces the element stored at a given node. */
    public E replace(Position<E> v, E e)
            throws InvalidPositionException;
    /** Returns the root of the tree. */
    public Position<E> root() throws EmptyTreeException;
    /** Returns the parent of a given node. */
    public Position<E> parent(Position<E> v)
            throws InvalidPositionException, BoundaryViolationException;
    /** Returns an iterable collection of the children of a given node. */
    public Iterable<Position<E>> children(Position<E> v)
            throws InvalidPositionException;
    /** Returns whether a given node is internal. */
    public boolean isInternal(Position<E> v)
            throws InvalidPositionException;
    /** Returns whether a given node is external. */
    public boolean isExternal(Position<E> v)
            throws InvalidPositionException;
    /** Returns whether a given node is the root of the tree. */
    public boolean isRoot(Position<E> v)
            throws InvalidPositionException;
}
//end#fragment Tree

//begin#fragment All
interface Position<E> {
    /** Return the element stored at this position. */
    E element();
}
//end#fragment All


//begin#fragment VectorHeap
class ArrayListCompleteBinaryTree<E>
        implements CompleteBinaryTree<E>  {
    protected ArrayList<BTPos<E>> T;  // indexed list of tree positions
    /** Nested class for a index list-based complete binary tree node. */
    protected static class BTPos<E> implements Position<E> {
        E element; // element stored at this position
        int index;      // index of this position in the array list
        public BTPos(E elt, int i) {
            element = elt;
            index = i;
        }
        public E element() { return element; }
        public int index() { return index; }
        public E setElement(E elt) {
            E temp = element;
            element = elt;
            return temp;
        }
        //end#fragment VectorHeap
        public String toString() {
            return("[" + element + "," + index + "]");
        }
//begin#fragment VectorHeap
    }
    /** default constructor */
    public ArrayListCompleteBinaryTree() {
        T = new ArrayList<BTPos<E>>();
        T.add(0, null); // the location at rank 0 is deliberately empty
    }
    /** Returns the number of (internal and external) nodes. */
    public int size() { return T.size() - 1; }
    /** Returns whether the tree is empty. */
    public boolean isEmpty() { return (size() == 0); }
//end#fragment VectorHeap
//begin#fragment VectorHeap2
    /** Returns whether v is an internal node. */
    public boolean isInternal(Position<E> v) throws InvalidPositionException {
        return hasLeft(v);  // if v has a right child it will have a left child
    }
    /** Returns whether v is an external node. */
    public boolean isExternal(Position<E> v) throws InvalidPositionException {
        return !isInternal(v);
    }
    /** Returns whether v is the root node. */
    public boolean isRoot(Position<E> v) throws InvalidPositionException {
        BTPos<E> vv = checkPosition(v);
        return vv.index() == 1;
    }
    /** Returns whether v has a left child. */
    public boolean hasLeft(Position<E> v) throws InvalidPositionException {
        BTPos<E> vv = checkPosition(v);
        return 2*vv.index() <= size();
    }
    /** Returns whether v has a right child. */
    public boolean hasRight(Position<E> v) throws InvalidPositionException {
        BTPos<E> vv = checkPosition(v);
        return 2*vv.index() + 1 <= size();
    }
    /** Returns the root of the tree. */
    public Position<E> root() throws EmptyTreeException {
        if (isEmpty()) throw new EmptyTreeException("Tree is empty");
        return T.get(1);
    }
    /** Returns the left child of v. */
    public Position<E> left(Position<E> v)
            throws InvalidPositionException, BoundaryViolationException {
        if (!hasLeft(v)) throw new BoundaryViolationException("No left child");
        BTPos<E> vv = checkPosition(v);
        return T.get(2*vv.index());
    }
    /** Returns the right child of v. */
    public Position<E> right(Position<E> v)
            throws InvalidPositionException {
        if (!hasRight(v)) throw new BoundaryViolationException("No right child");
        BTPos<E> vv = checkPosition(v);
        return T.get(2*vv.index() + 1);
    }
//end#fragment VectorHeap2
//begin#fragment VectorHeap3
    /** Returns the parent of v. */
    public Position<E> parent(Position<E> v)
            throws InvalidPositionException, BoundaryViolationException {
        if (isRoot(v)) throw new BoundaryViolationException("No parent");
        BTPos<E> vv = checkPosition(v);
        return T.get(vv.index()/2);
    }
//end#fragment VectorHeap3
    /** Returns an iterable collection of the children of v. */
    public Iterable<Position<E>> children(Position<E> v) throws InvalidPositionException {
        PositionList<Position<E>> children = new NodePositionList<Position<E>>();
        if (hasLeft(v))
            children.addLast(left(v));
        if (hasRight(v))
            children.addLast(right(v));
        return children;
    }
    /** Returns an iterable collection of all the nodes in the tree. */
    public Iterable<Position<E>> positions() {
        ArrayList<Position<E>> P = new ArrayList<Position<E>>();
        Iterator<BTPos<E>> iter = T.iterator();
        iter.next(); // skip the first position
        while (iter.hasNext())
            P.add(iter.next());
        return P;
    }
//begin#fragment VectorHeap3
    /** Replaces the element at v. */
    public E replace(Position<E> v, E o) throws InvalidPositionException {
        BTPos<E> vv = checkPosition(v);
        return vv.setElement(o);
    }
    /** Adds an element just after the last node (in a level numbering). */
    public Position<E> add(E e) {
        int i = size() + 1;
        BTPos<E> p = new BTPos<E>(e,i);
        T.add(i, p);
        return p;
    }
    /** Removes and returns the element at the last node. */
    public E remove() throws EmptyTreeException {
        if(isEmpty()) throw new EmptyTreeException("Tree is empty");
        return T.remove(size()).element();
    }
    /** Determines whether the given position is a valid node. */
    protected BTPos<E> checkPosition(Position<E> v)
            throws InvalidPositionException
    {
        if (v == null || !(v instanceof BTPos))
            throw new InvalidPositionException("Position is invalid");
        return (BTPos<E>) v;
    }
//end#fragment VectorHeap3
    // Additional Methods
    /** Returns the sibling of v. */
    public Position<E> sibling(Position<E> v)
            throws InvalidPositionException, BoundaryViolationException {
        try {
            Position<E> p = parent(v);
            Position<E> lc = left(p);
            if (v == lc)
                return right(p);
            else
                return lc;
        }
        catch(BoundaryViolationException e) {
            throw new BoundaryViolationException("Node has no sibling");
        }
    }
    /** Swaps the elements at two nodes. */
    public void swapElements(Position<E> v, Position<E> w)
            throws InvalidPositionException {
        BTPos<E> vv = checkPosition(v);
        BTPos<E> ww = checkPosition(w);
        E temp = vv.element();
        vv.setElement(ww.element());
        ww.setElement(temp);
    }
//begin#fragment VectorHeap3
    /** Returns an iterator of the elements stored at all nodes in the tree. */
    public Iterator<E> iterator() {
        ArrayList<E> list = new ArrayList<E>();
        Iterator<BTPos<E>> iter = T.iterator();
        iter.next(); // skip the first element
        while (iter.hasNext())
            list.add(iter.next().element());
        return list.iterator();
    }
//end#fragment VectorHeap3
    /** Returns a String representing this complete binary tree. */
    public String toString() { return T.toString(); }
//begin#fragment VectorHeap3
}
//end#fragment VectorHeap3

//begin#fragment DefaultComparator
/** Comparator based on the natural ordering
 //end#fragment DefaultComparator
 *
 *  @author Michael Goodrich
//begin#fragment DefaultComparator
 */
class DefaultComparator<E> implements Comparator<E> {
    /** Compares two given elements
     //end#fragment DefaultComparator
     *
     * @return a negative integer if <tt>a</tt> is less than <tt>b</tt>,
     * zero if <tt>a</tt> equals <tt>b</tt>, or a positive integer if
     * <tt>a</tt> is greater than <tt>b</tt>
    //begin#fragment DefaultComparator
     */
    public int compare(E a, E b) throws ClassCastException {
        return ((Comparable<E>) a).compareTo(b);
    }
}
//begin#fragment DefaultComparator

class EmptyPriorityQueueException  extends RuntimeException {
    public EmptyPriorityQueueException (String message) {
        super (message);
    }
}

class BoundaryViolationException  extends RuntimeException {
    public BoundaryViolationException (String message) {
        super (message);
    }
}

//begin#fragment InvalidPositionException
// A run-time exception for invalid positions
class InvalidPositionException extends RuntimeException {
    public InvalidPositionException(String err) {
        super(err);
    }
    //end#fragment InvalidPositionException
    public InvalidPositionException() {
    /* default constructor */
    }
//begin#fragment InvalidPositionException
}
//end#fragment InvalidPositionException
class EmptyTreeException extends RuntimeException {
    public EmptyTreeException(String err) {
        super(err);
    }
}

//begin#fragment Header
interface PositionList<E> extends Iterable<E> {
//end#fragment Header
//begin#fragment List
    /** Returns the number of elements in this list. */
    public int size();
    /** Returns whether the list is empty. */
    public boolean isEmpty();
    /** Returns the first node in the list. */
    public Position<E> first();
    /** Returns the last node in the list. */
    public Position<E> last();
    /** Returns the node after a given node in the list. */
    public Position<E> next(Position<E> p)
            throws InvalidPositionException, BoundaryViolationException;
    /** Returns the node before a given node in the list. */
    public Position<E> prev(Position<E> p)
            throws InvalidPositionException, BoundaryViolationException;
    /** Inserts an element at the front of the list, returning new position. */
    public void addFirst(E e);
    /** Inserts and element at the back of the list, returning new position. */
    public void addLast(E e);
    /** Inserts an element after the given node in the list. */
    public void addAfter(Position<E> p, E e)
            throws InvalidPositionException;
    /** Inserts an element before the given node in the list. */
    public void addBefore(Position<E> p, E e)
            throws InvalidPositionException;
    /** Removes a node from the list, returning the element stored there. */
    public E remove(Position<E> p) throws InvalidPositionException;
    /** Replaces the element stored at the given node, returning old element. */
    public E set(Position<E> p, E e) throws InvalidPositionException;
//end#fragment List
//begin#fragment Positions
    /** Returns an iterable collection of all the nodes in the list. */
    public Iterable<Position<E>> positions();
//end#fragment Positions
//begin#fragment Iterator
    /** Returns an iterator of all the elements in the list. */
    public Iterator<E> iterator();
//end#fragment Iterator
//begin#fragment Tail
}
//end#fragment Tail

//begin#fragment Header
class NodePositionList<E> implements PositionList<E> {
    //end#fragment Header
//begin#fragment Listvars
    protected int numElts;            	// Number of elements in the list
    protected DNode<E> header, trailer;	// Special sentinels
//end#fragment Listvars
//begin#fragment checkPosition
    /** Constructor that creates an empty list; O(1) time */
    public NodePositionList() {
        numElts = 0;
        header = new DNode<E>(null, null, null);	// create header
        trailer = new DNode<E>(header, null, null);	// create trailer
        header.setNext(trailer);	// make header and trailer point to each other
    }
    /** Checks if position is valid for this list and converts it to
     *  DNode if it is valid; O(1) time */
    protected DNode<E> checkPosition(Position<E> p)
            throws InvalidPositionException {
        if (p == null)
            throw new InvalidPositionException("Null position passed to NodeList");
        if (p == header)
            throw new InvalidPositionException("The header node is not a valid position");
        if (p == trailer)
            throw new InvalidPositionException("The trailer node is not a valid position");
        try {
            DNode<E> temp = (DNode<E>) p;
            if ((temp.getPrev() == null) || (temp.getNext() == null))
                throw new InvalidPositionException("Position does not belong to a valid NodeList");
            return temp;
        } catch (ClassCastException e) {
            throw new InvalidPositionException("Position is of wrong type for this list");
        }
    }
    //end#fragment checkPosition
    //begin#fragment first
    /** Returns the number of elements in the list;  O(1) time */
    public int size() { return numElts; }
    /** Returns whether the list is empty;  O(1) time  */
    public boolean isEmpty() { return (numElts == 0); }
    /** Returns the first position in the list; O(1) time */
    public Position<E> first()
            throws EmptyListException {
        if (isEmpty())
            throw new EmptyListException("List is empty");
        return header.getNext();
    }
    //end#fragment first
    /** Returns the last position in the list; O(1) time */
    public Position<E> last()
            throws EmptyListException {
        if (isEmpty())
            throw new EmptyListException("List is empty");
        return trailer.getPrev();
    }
    //begin#fragment first
    /** Returns the position before the given one; O(1) time */
    public Position<E> prev(Position<E> p)
            throws InvalidPositionException, BoundaryViolationException {
        DNode<E> v = checkPosition(p);
        DNode<E> prev = v.getPrev();
        if (prev == header)
            throw new BoundaryViolationException("Cannot advance past the beginning of the list");
        return prev;
    }
    //end#fragment first
    /** Returns the position after the given one; O(1) time */
    public Position<E> next(Position<E> p)
            throws InvalidPositionException, BoundaryViolationException {
        DNode<E> v = checkPosition(p);
        DNode<E> next = v.getNext();
        if (next == trailer)
            throw new BoundaryViolationException("Cannot advance past the end of the list");
        return next;
    }
    //begin#fragment first
    /** Insert the given element before the given position;
     * O(1) time  */
    public void addBefore(Position<E> p, E element)
            throws InvalidPositionException {
        DNode<E> v = checkPosition(p);
        numElts++;
        DNode<E> newNode = new DNode<E>(v.getPrev(), v, element);
        v.getPrev().setNext(newNode);
        v.setPrev(newNode);
    }
    //end#fragment first
    /** Insert the given element after the given position;
     * O(1) time  */
    public void addAfter(Position<E> p, E element)
            throws InvalidPositionException {
        DNode<E> v = checkPosition(p);
        numElts++;
        DNode<E> newNode = new DNode<E>(v, v.getNext(), element);
        v.getNext().setPrev(newNode);
        v.setNext(newNode);
    }
    //begin#fragment remove
    /** Insert the given element at the beginning of the list, returning
     * the new position; O(1) time  */
    public void addFirst(E element) {
        numElts++;
        DNode<E> newNode = new DNode<E>(header, header.getNext(), element);
        header.getNext().setPrev(newNode);
        header.setNext(newNode);
    }
    //end#fragment remove
    /** Insert the given element at the end of the list, returning
     * the new position; O(1) time  */
    public void addLast(E element) {
        numElts++;
        DNode<E> oldLast = trailer.getPrev();
        DNode<E> newNode = new DNode<E>(oldLast, trailer, element);
        oldLast.setNext(newNode);
        trailer.setPrev(newNode);
    }
    //begin#fragment remove
    /**Remove the given position from the list; O(1) time */
    public E remove(Position<E> p)
            throws InvalidPositionException {
        DNode<E> v = checkPosition(p);
        numElts--;
        DNode<E> vPrev = v.getPrev();
        DNode<E> vNext = v.getNext();
        vPrev.setNext(vNext);
        vNext.setPrev(vPrev);
        E vElem = v.element();
        // unlink the position from the list and make it invalid
        v.setNext(null);
        v.setPrev(null);
        return vElem;
    }
    /** Replace the element at the given position with the new element
     * and return the old element; O(1) time  */
    public E set(Position<E> p, E element)
            throws InvalidPositionException {
        DNode<E> v = checkPosition(p);
        E oldElt = v.element();
        v.setElement(element);
        return oldElt;
    }
    //end#fragment remove

//begin#fragment Iterator
    /** Returns an iterator of all the elements in the list. */
    public Iterator<E> iterator() { return new ElementIterator<E>(this); }
//end#fragment Iterator
//begin#fragment PIterator
    /** Returns an iterable collection of all the nodes in the list. */
    public Iterable<Position<E>> positions() {     // create a list of posiitons
        PositionList<Position<E>> P = new NodePositionList<Position<E>>();
        if (!isEmpty()) {
            Position<E> p = first();
            while (true) {
                P.addLast(p); // add position p as the last element of list P
                if (p == last())
                    break;
                p = next(p);
            }
        }
        return P; // return P as our Iterable object
    }
//end#fragment PIterator

    // Convenience methods
    /** Returns whether a position is the first one;  O(1) time */
    public boolean isFirst(Position<E> p)
            throws InvalidPositionException {
        DNode<E> v = checkPosition(p);
        return v.getPrev() == header;
    }
    /** Returns whether a position is the last one;  O(1) time */
    public boolean isLast(Position<E> p)
            throws InvalidPositionException {
        DNode<E> v = checkPosition(p);
        return v.getNext() == trailer;
    }
    /** Swap the elements of two give positions;  O(1) time */
    public void swapElements(Position<E> a, Position<E> b)
            throws InvalidPositionException {
        DNode<E> pA = checkPosition(a);
        DNode<E> pB = checkPosition(b);
        E temp = pA.element();
        pA.setElement(pB.element());
        pB.setElement(temp);
    }
    /** Returns a textual representation of a given node list using for-each */
    public static <E> String forEachToString(PositionList<E> L) {
        String s = "[";
        int i = L.size();
        for (E elem: L) {
            s += elem; // implicit cast of the element to String
            i--;
            if (i > 0)
                s += ", "; // separate elements with a comma
        }
        s += "]";
        return s;
    }
//begin#fragment toString
    /** Returns a textual representation of a given node list */
    public static <E> String toString(PositionList<E> l) {
        Iterator<E> it = l.iterator();
        String s = "[";
        while (it.hasNext()) {
            s += it.next();	// implicit cast of the next element to String
            if (it.hasNext())
                s += ", ";
        }
        s += "]";
        return s;
    }
//end#fragment toString
    /** Returns a textual representation of the list */
    public String toString() {
        return toString(this);
    }
}

class InvalidKeyException  extends RuntimeException {
    public InvalidKeyException (String message) {
        super (message);
    }
    public static final long serialVersionUID = 424242L;
}

//begin#fragment DNode
class DNode<E> implements Position<E> {
    private DNode<E> prev, next;	// References to the nodes before and after
    private E element;	// Element stored in this position
    /** Constructor */
    public DNode(DNode<E> newPrev, DNode<E> newNext, E elem) {
        prev = newPrev;
        next = newNext;
        element = elem;
    }
    // Method from interface Position
    public E element() throws InvalidPositionException {
        if ((prev == null) && (next == null))
            throw new InvalidPositionException("Position is not in a list!");
        return element;
    }
    // Accessor methods
    public DNode<E> getNext() { return next; }
    public DNode<E> getPrev() { return prev; }
    // Update methods
    public void setNext(DNode<E> newNext) { next = newNext; }
    public void setPrev(DNode<E> newPrev) { prev = newPrev; }
    public void setElement(E newElement) { element = newElement; }
}
//end#fragment DNode
//begin#fragment Iterator
 class ElementIterator<E> implements Iterator<E> {
    protected PositionList<E> list; // the underlying list
    protected Position<E> cursor; // the next position
    /** Creates an element iterator over the given list. */
    public ElementIterator(PositionList<E> L) {
        list = L;
        cursor = (list.isEmpty())? null : list.first();
    }
//end#fragment Iterator
    /** Returns whether the iterator has a next object. */
//begin#fragment Iterator
    public boolean hasNext() { return (cursor != null);  }
//end#fragment Iterator
    /** Returns the next object in the iterator. */
//begin#fragment Iterator
    public E next() throws NoSuchElementException {
        if (cursor == null)
            throw new NoSuchElementException("No next element");
        E toReturn = cursor.element();
        cursor = (cursor == list.last())? null : list.next(cursor);
        return toReturn;
    }
//end#fragment Iterator
    /** Throws an {@link UnsupportedOperationException} in all cases,
     * because removal is not a supported operation in this iterator.
     */
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("remove");
    }
//begin#fragment Iterator
}
//end#fragment Iterator
class EmptyListException  extends RuntimeException {
    public EmptyListException (String message) {
        super (message);
    }
}