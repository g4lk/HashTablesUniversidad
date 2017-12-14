package material.maps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


/**
 * Separate chaining table implementation of hash tables. Note that all
 * "matching" is based on the equals method.
 *
 * @author A. Duarte, J. Vélez, J. Sánchez-Oro
 * @param <K> The key
 * @param <V> The stored value
 */
public class HashTableMapSC<K, V> implements Map<K, V> {

    private class HashEntry<T, U> implements Entry<T, U> {
    	private T key;
    	private U value;
        
    	public HashEntry(T k, U v) {
            this.key=k;
            this.value=v;
        }

        @Override
        public U getValue() {
            return value;
        }

        @Override
        public T getKey() {
            return key;
        }

        public U setValue(U val) {
            U oldValue = value;
            value = val;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {

            if (o.getClass() != this.getClass()) {
                return false;
            }

            HashEntry<T, U> ent;
            try {
                ent = (HashEntry<T, U>) o;
            } catch (ClassCastException ex) {
                return false;
            }
            return (ent.getKey().equals(this.key))
                    && (ent.getValue().equals(this.value));
        }

        /**
         * Entry visualization.
         */
        @Override
        public String toString() {
            return "(" + key + "," + value + ")";
        }
    }
    
    
    private class HashTableMapIterator<T, U> implements Iterator<Entry<T, U>> {
    	private int posArrayList;
    	private int pos;
        private ArrayList<HashEntry<T, U>>[] bucket;
        
        //Ejercicio 2.2
        public HashTableMapIterator(ArrayList<HashEntry<T, U>>[] map, int numElems) {
            this.bucket=map;
            if (numElems == 0) {
                this.pos = bucket.length;
            } else {
            	this.pos=0;
            	this.posArrayList=0;
            	goToNextElement(0);
            }
        }

        private void goToNextElement(int start) {
        	final int n = bucket.length;
            this.pos = start;
            while ((this.pos < n) && ((this.bucket[this.pos] == null))) {
                this.pos++;
            }
            
            
            
        }

        @Override
        public boolean hasNext() {
        	return (this.pos < this.bucket.length );
        }

        @Override
        public Entry<T, U> next() {
        	if (hasNext()) {
                int currentPos = this.pos;
                if (this.bucket[this.pos].size() - 1 == this.posArrayList) {
                	this.posArrayList=0;
                	this.pos+=1;
                	goToNextElement(this.pos);
                }
                else {
                	this.posArrayList += 1;
                }
                return (Entry<T, U>) this.bucket[currentPos].get(posArrayList);
            } else {
                throw new IllegalStateException("The map has not more elements");
            }
        }

        @Override
        public void remove() {
            // NO HAY QUE IMPLEMENTARLO
            throw new UnsupportedOperationException("Not implemented.");
        }
    }

    private class HashTableMapKeyIterator<T, U> implements Iterator<T> {
    	
    	 public HashTableMapIterator<T, U> it;
    	 
        public HashTableMapKeyIterator(HashTableMapIterator<T, U> it) {
        	this.it = it;
        }

        @Override
        public T next() {
        	return it.next().getKey();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public void remove() {
            // NO HAY QUE IMPLEMENTARLO
            throw new UnsupportedOperationException("Not implemented.");
        }
    }

    private class HashTableMapValueIterator<T, U> implements Iterator<U> {
    	
    	public HashTableMapIterator<T, U> it;
    	
    	public HashTableMapValueIterator(HashTableMapIterator<T, U> it) {
            this.it = it;
        }

        @Override
        public U next() {
            return it.next().getValue();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not implemented.");
        }
    }
    
    protected int n = 0; // number of entries in the dictionary
    protected int prime, capacity; // prime factor and capacity of bucket array
    protected long scale, shift; // the shift and scaling factors
    protected ArrayList<HashEntry<K, V>>[] bucket;// bucket array
    
    /**
     * Creates a hash table
     */
    public HashTableMapSC() {
    	this(109345121, 1000);
    }

    /**
     * Creates a hash table.
     *
     * @param cap initial capacity
     */
    public HashTableMapSC(int cap) {
    	this(109345121, cap);
    }

    /**
     * Creates a hash table with the given prime factor and capacity.
     *
     * @param p prime number
     * @param cap initial capacity
     */
    public HashTableMapSC(int p, int cap) {
        this.prime=p;
        this.capacity=cap;
        this.bucket = (ArrayList<HashEntry<K, V>>[]) new ArrayList[this.capacity];
        Random rand = new Random();
        this.scale = rand.nextInt(prime - 1) + 1;
        this.shift = rand.nextInt(prime);
    }

    /**
     * Hash function applying MAD method to default hash code.
     *
     * @param key Key
     * @return
     */
    protected int hashValue(K key) {
    	return (int) ((Math.abs(key.hashCode() * scale + shift) % prime) % capacity);
    }

    /**
     * Returns the number of entries in the hash table.
     *
     * @return the size
     */
    @Override
    public int size() {
        return n;
    }

    /**
     * Returns whether or not the table is empty.
     *
     * @return true if the size is 0
     */
    @Override
    public boolean isEmpty() {
    	return n==0;
    }

    /**
     * Returns the value associated with a key.
     *
     * @param key
     * @return value
     */
    @Override
    public V get(K key) throws IllegalStateException {
        checkKey(key);
        int i = hashValue(key);
        Iterator<HashEntry<K,V>> it = this.bucket[i].iterator();
        while(it.hasNext()) {
        	HashEntry<K,V> node = it.next();
        	if (node.getKey()==key) {
        		return node.getValue();
        	}
        }
        return null;
    }

    /**
     * Put a key-value pair in the map, replacing previous one if it exists.
     *
     * @param key
     * @param value
     * @return value
     */
    @Override
    public V put(K key, V value) throws IllegalStateException {
        checkKey(key);
    	int i = hashValue(key);
    	if (this.bucket[i] == null) {
    		float factor = this.n/this.capacity;
    		if (factor > 0.75) {
    			rehash(2*this.capacity);
    			return put(key,value);
    		}
    		this.bucket[i] = new ArrayList<HashEntry<K,V>>();
    		HashEntry<K,V> ent= new HashEntry<>(key,value);
    		this.bucket[i].add(ent);
    		n++;
    		return null;
    	}
    	else {
    		Iterator<HashEntry<K,V>> it = this.bucket[i].iterator();
    		while(it.hasNext()) {
    			HashEntry<K,V> ent = it.next();
    			if (ent.getKey() == key) {
    				return ent.setValue(value);
    			}
    		}
    		HashEntry<K,V> ent= new HashEntry<>(key,value);
    		this.bucket[i].add(this.bucket[i].size()-1,ent);
    		n++;
    		return null;
    	}
    }

    /**
     * Removes the key-value pair with a specified key.
     *
     * @param key
     * @return
     */
    @Override
    public V remove(K key) throws IllegalStateException {
        checkKey(key);
        int i = hashValue(key);
        if (this.bucket[i] == null) {
        	return null;
        }
        else {
        	Iterator<HashEntry<K,V>> it = this.bucket[i].iterator();
        	int i2 =0;
        	while(it.hasNext()) {
        		if (it.next().getKey() == key) {
        			return this.bucket[i].remove(i2).getValue();
        		}
        		i2++;
        	}
        	return null;
        }
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
    	return new HashTableMapIterator<K, V>(this.bucket,this.n); 
    }

    /**
     * Returns an iterable object containing all of the keys.
     *
     * @return
     */
    @Override
    public Iterable<K> keys() {
    	return new Iterable<K>() {
            public Iterator<K> iterator() {
                return new HashTableMapKeyIterator<K, V>(new HashTableMapIterator<K, V>(bucket, n));
            }
        };
    }

    /**
     * Returns an iterable object containing all of the values.
     *
     * @return
     */
    @Override
    public Iterable<V> values() {
    	return new Iterable<V>() {
            public Iterator<V> iterator() {
                return new HashTableMapValueIterator<K, V>(new HashTableMapIterator<K, V>(bucket, n));
            }
        };
    }

    /**
     * Returns an iterable object containing all of the entries.
     *
     * @return
     */
    @Override
    public Iterable<Entry<K, V>> entries() {
    	return new Iterable<Entry<K, V>>() {
            public Iterator<Entry<K, V>> iterator() {
                return new HashTableMapIterator<K, V>(bucket, n);
            }
        };
    }

    /**
     * Determines whether a key is valid.
     *
     * @param k Key
     */
    protected void checkKey(K k) {
    	 if (k == null) {
             throw new IllegalStateException("Invalid key: null.");
         }
    }

    /**
     * Increase/reduce the size of the hash table and rehashes all the entries.
     */
    protected void rehash(int newCap) {
    	capacity = newCap;
        ArrayList<HashEntry<K, V>>[] old = bucket;
        bucket = (ArrayList<HashEntry<K, V>>[]) new ArrayList[capacity];
        java.util.Random rand = new java.util.Random();
        // new hash scaling factor
        scale = rand.nextInt(prime - 1) + 1;
        // new hash shifting factor
        shift = rand.nextInt(prime);
        for (ArrayList<HashEntry<K,V>> ar: old) {
        	if (ar != null) {
        		Iterator<HashEntry<K,V>> it = ar.iterator();
        		while(it.hasNext()) {
        			put(it.next().getKey(),it.next().getValue());
        		}
        	}
        }
    }
    
}
