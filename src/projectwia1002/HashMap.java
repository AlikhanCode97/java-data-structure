/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectwia1002;

import java.io.Serializable;


public class HashMap<K extends Comparable<K>, V> implements Serializable {
     static class Pair<K, V> implements Comparable<Pair<K, V>> , Serializable{
        private final K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V newValue) {
            V old = value;
            value = newValue;
            return old;
        }

         @Override
         public int compareTo(Pair<K, V> o) {
             return 0;
         }
     }

    private MyLinkedList<MyLinkedList<Pair<K, V>>> table;
    private int size;

    public HashMap() {
        table = new MyLinkedList<>();

        for (int i=0; i<20; i++) {
            table.addLast(new MyLinkedList<>());
        }

        size = 0;
    }


    /**
     * Finds the index of the LinkedList associated with the key's hashcode
     * @param key Type K for key
     * @return LinkedList at that index
     */
    private MyLinkedList<Pair<K, V>> getList(K key) {
        int index = Math.abs(key.hashCode() % table.getSize());
        return table.get(index);
    }

    public V put(K key, V value) {
        MyLinkedList<Pair<K, V>> list = getList(key);

        V old = null;

        if (containsKey(key)) {
            for(int i = 0 ; i < list.getSize(); i++){
                if (list.get(i).getKey().equals(key)) {
                    old = (V) list.get(i).getValue();
                    list.get(i).setValue(value);
                }
            }
        } else {
            list.addLast(new Pair<>(key, value));
            size++;
        }

        return old;
    }

    public V remove(Object obj) {
        K key = (K) obj;
        V removedValue = null;

        if (containsKey(key)) {
            MyLinkedList<Pair<K, V>> list = getList(key);
            V value = get(key);

            int index = list.indexOf(new Pair<>(key, value));

            removedValue = list.get(index).getValue();
            list.remove(index);

            size--;
        }

        return removedValue;
    }

    public V get(Object key) {
        MyLinkedList<Pair<K, V>> list = getList((K) key);
        for (int i = 0 ; i < list.getSize(); i++){
            if (list.get(i).getKey().equals(key))
                return (V) list.get(i).getValue();
        }
        return null;
    }

    public boolean containsKey(Object key) {
        MyLinkedList<Pair<K, V>> list = getList((K) key);
        for (int i = 0; i < list.getSize(); i++) {
            if (list.get(i).getKey().equals(key))
                return true;
        }
        return false;
    }

    public boolean containsValue(Object value) {
        for (int i = 0 ;i < table.getSize(); i++){
            for (int j = 0; j < table.get(i).getSize(); j++ ){
                if (table.get(i).get(j).getValue().equals(value))
                    return true;
            }
        }
        return false;
    }

    public MyLinkedList<Pair<K, V>> entrySet() {
        MyLinkedList<Pair<K, V>> set = new MyLinkedList<>();

        for (int i = 0 ; i < table.getSize(); i++){
            for (int j = 0 ;j < table.get(i).getSize(); j++){
                set.addLast(table.get(i).get(j));
                }
        }
        return set;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

}
