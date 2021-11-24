
# Array vs List vs ArrayList

ref:
 https://www.geeksforgeeks.org/array-vs-arraylist-in-java/
 https://www.geeksforgeeks.org/difference-between-list-and-arraylist-in-java/

* Array: Simple fixed sized arrays that we create in Java, like below
```
int arr[] = new int[10]   
```
* ArrayList : Dynamic sized arrays in Java that implement List interface
```
ArrayList<Type> arrL = new ArrayList<Type>();
```
* List: The List is a child interface of Collection. It is an ordered collection of objects in which duplicate values can be stored. Since List preserves the insertion order, it allows positional access and insertion of elements. List interface is implemented by the classes of ArrayList, LinkedList, Vector and Stack.

# Java Concurrency
https://www.youtube.com/playlist?list=PLhfHPmPYPPRk6yMrcbfafFGSbE2EPK_A6

# Spring Core
https://www.youtube.com/watch?v=ZwcHeLhvuq4

# HashMap vs LinkedHashMap vs TreeMap
https://www.youtube.com/watch?v=TTdheF15nIU
HashMap retain no order, LinkedHashMap maintain insert order (good for LRU cache), TreeMap maintain nature order
* TreeMap.lowerKey(key): Returns the greatest key strictly less than the given key, or null if there is no such key.
* TreeMap.higherKey(key): Returns the least key strictly greater than the given key, or null if there is no such key.

# 常用util
* ```Arrays.sort(array, Comparator.comparingInt(o -> o[1]))```
* ```Arrays.sort(twoDim, new Comparator<int[]>() {
    @Override
    public int compare(int[] o1, int[] o2) {
        return Integer.compare(o2[0], o1[0]);
    }
});
```
* ```Arrays.stream(array).sum()```
* ```Arrays.stream(array).max().getAsInt()```
* ```new PriorityQueue<>(Collections.reverseOrder())```
* ```PriorityQueue<Element> pq = new PriorityQueue<>((a, b) -> a.val - b.val);
pq.offer(int), pq.poll```
* ```Collections.reverse()```
* ```new StringBuilder(result).reverse().toString();```
* ```Map.computeIfAbsent & Map.getOrDefault```
* ```random = new Random();``` ```random.nextInt(list.size());```

# LinkedList and ArrayList
LinkedList and ArrayList are two different implementations of the List interface. LinkedList implements it with a doubly-linked list. ArrayList implements it with a dynamically re-sizing array.
