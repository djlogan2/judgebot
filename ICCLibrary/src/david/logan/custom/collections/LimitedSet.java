package david.logan.custom.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class LimitedSet<E> implements Set<E> {

    private LinkedList<E> list = new LinkedList<E>();
    private int max;
    
    public LimitedSet(int max) {
        this.max = max;
    }
    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(E e) {
        if(list.size() == max)
            list.pop();
        return list.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if(c.size() + list.size() <= max) {
            return list.addAll(c);
        } else if(c.size() == max){
            list.clear();
            return list.addAll(c);
        } else {
            list.clear();
            for(E item : c)
            {
                if(!list.add(item))
                    return false;
            }
            return true;
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }
}
