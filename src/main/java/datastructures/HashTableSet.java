package datastructures;

import org.apache.commons.lang3.RandomUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class HashTableSet<T> implements Iterable<T> {
    private  SimpleDoublyLinkedList<T>[] keyArray;

    private static Double DEFAULT_FILL_RATIO = .6;
    private static final Long LARGE_PRIME = (long) (Math.pow(2, 16) - 1);

    private Comparator<T> comparator = (Comparator<T>) Comparator.naturalOrder();
    private Long a;
    private Long b;
    private Long size = 0l;
    private Double loadFactor;
    private Function<T , Integer> keyMapper;
    private Long upper = 0L;
    private Long lower = 0L;

    HashTableSet(Double loadFactor , Function<T, Integer> keyMapper){
        keyArray = getEmptyKeyArray(0);
        size = 0L;
        this.loadFactor = (loadFactor >= 1) ? DEFAULT_FILL_RATIO : loadFactor;
        a = RandomUtils.nextLong(1,LARGE_PRIME-1);
        b = RandomUtils.nextLong(1,LARGE_PRIME-1);
        this.keyMapper = keyMapper;
        computeBounds();
        resize(0L);
    }

    public HashTableSet(Function<T, Integer> keyMapper){
        this(DEFAULT_FILL_RATIO,keyMapper);
    }

    public HashTableSet(){
        this(DEFAULT_FILL_RATIO , (Object::hashCode));
    }

    private void computeBounds(){
        upper = (long) keyArray.length;
        lower = (long) Math.ceil(upper * loadFactor * loadFactor);
    }

    public HashTableSet withComparator(Comparator<T> comparator){
        this.comparator = comparator;
        return this;
    }
    private Integer getKey(T t){
        return keyMapper.apply(t);
    }
    private SimpleDoublyLinkedList<T>[] getEmptyKeyArray(int size){
        return new ArrayList<SimpleDoublyLinkedList<T>>(size).toArray(new SimpleDoublyLinkedList[size]);
    }

    public Integer size(){
       return size.intValue();
    }

    public void build(Collection<T> collection){
        collection.forEach(
                this::insert
        );
    }

    private Long hash(T obj , Integer m){
        return (((((a%LARGE_PRIME) * (getKey(obj)%LARGE_PRIME))%LARGE_PRIME) + b%LARGE_PRIME)%LARGE_PRIME)%m;
    }



    private void resize(Long n){
        if((lower >= n )|| (n >= upper)){
            var f = Math.ceil(1 / loadFactor);
            var m = Long.max(n , 1) * f;
            var tempArray = getEmptyKeyArray((int) m);
            for( T t : this){
                var hash = hash(t , tempArray.length).intValue();
                if(tempArray[hash]==null) {
                    tempArray[hash] = new SimpleDoublyLinkedList<>(t);
                    continue;
                }
                tempArray[hash].insertLast(t);
            }
            keyArray = tempArray;
            computeBounds();
        }
    }

    public T find(T key) {
        var hash = hash(key , keyArray.length);
        var hashList =  keyArray[Math.toIntExact(hash)];
        if(hashList == null) return null;
        else return hashList.find(key);

    }

    public T insert(T key){
        resize(size+1);
        var hash = hash(key, keyArray.length).intValue();
        if(keyArray[hash]==null){
            keyArray[hash] = new SimpleDoublyLinkedList<>(key);
            size++;
        }else if(keyArray[hash].find(key)==null) {
            keyArray[hash].insertLast(key);
            size++;
        }
        return key;
    }

    public T max(){
        T max = null;
        for(T t : this){
            if(max==null) max = t;
            if(comparator.compare(t,max) > 0 ) max = t;
        }
        return max;
    }

    public T min(){
        T min = null;
        for(T t : this){
            if(min ==null) min = t;
            if(comparator.compare(t, min) < 0 ) min = t;
        }
        return min;
    }

    public boolean delete(T key){
        if(size<=0)return false;
        var hash = hash(key , keyArray.length).intValue();
        if(keyArray[hash] == null) return false;
        var deletedValue = keyArray[hash].remove(key);
        if(deletedValue!=null)size--;
        return deletedValue!=null;
    }


    @NotNull
    @Override
    public Iterator<T> iterator() {
      return toList().iterator();
    }
    List<T> toList(){
        ArrayList<T> items = new ArrayList<>(size.intValue());
        for(var list : keyArray){
                if(list==null)continue;
                items.addAll(list.toList());
        }
        return items;
    }


}
