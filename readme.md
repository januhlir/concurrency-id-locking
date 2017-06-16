# Java Concurrency - mapping Locks to IDs solutions  

How to achieve highly granular locking in a heavy multi concurrent Java code.

This is not a library, but set of solutions for your inspiration, and to carefully copy and past.

Presenting 4 working solutions, all with small nuances in performance and usage, so please read carefully the small print.

## Solution 1 – ID to Lock mapping with refecence counting

The reference counting is necessary to prevent a race condition occurring with simpler concurrent map solutions, see `org.bitbucket.espinosa.concurrency.zzz.badidlock.BookingServiceZ1` and accompanied test proving race condition and providing additional information.

This solution is based on ID to Lock mapping `ConcurrentHashMap`; `idLocks.compute(resourceId, ..)` providing locks on demand. To prevent unrestricted growth of such map in case of large number of IDs, there has to be a removal operation. Lock is only removed if number of references reaches zero, that is there is no thread blocked by it or in the mutex for the given ID.

Note: `putIfAbsent()` method cannot be used as it returns previous mapped value, which would be frequently null. The `computeIfAbsent()` is better, it works similarly but it returns the new value. However, it cannot be used for counting references.

Note: `ConcurrentHashMap.compute(key, computeFunction)` removes entry if the new value would be null; that is the returning value of computeFunction is null.



## Solution 2 – ID to Lock mapping with GC driven clean up

Weak Key or Value hash map? Well, both would do, providing that after weak reference is marked for scraping, whole map entry is removed as well. 
`WeakHashMap` in JDK is such map, Guava's Weak Value Map is another usable implementation.

ConcurrentHashMap is not such Map. Defining weak key `ConcurrentHashMap<WeakReference<?>, ?>` or weak value `ConcurrentHashMap<?, ?>` won't prevent it from ever growing and hogging memory.

In practical terms, this solutions does has no explicit lock release. Locks and lock mappings 
are reclaimed only as a result of GC run, so typically when memory is starting to get low. 
It is safe, removal cannot happen when treads are inside mutex, or waiting to entry it, as 
ID and Locks are both also strongly referenced at that point.



### Solution 2A – ID to Lock mapping based on JDK's WeakHashMap

WeakHashMap provides weak key and hash backed map. When key object become unreferenced, whole entry is discarded as well.

Note: WekHashMap does cleaning in `WeakHashMap.expungeStaleEntries()` method which is called, directly or indirectly, in every `get()`, `put()`, `remove()`, `resize()`, `size()`; 
that is in every typical map operation.

There are few caveats with WeakHashMap though:
   * It is not thread safe; this can be easily remedied with Collections.synchronizedMap(), but this solution is much less performant then ConcurrentHashMap
   * WeakHashMap, contrary to other Map implementations, uses 'object identity' instead of 'object equality', so it is not suitable for all scenarios.



### Solution 2B – ID to Lock mapping based on Guavas's Weak Value Map

Solution based on a Guava's weak values hash map. This implementation implementation
prunes map of unreferenced mapped values and uses standard _object equality_
when comparing keys.

Guava's concurrent map with weak values is created as:  
`ConcurrentMap<Object, Object> idLocks = new MapMaker().weakValues().makeMap()`  
in current 19.0 version. Sadly, they keep changing the API frequently.

Proof that map is pruned:  
The `new MapMaker().weakValues().makeMap()` returns `com.google.common.collect.MapMakerInternalMap`.
In `MapMakerInternalMap.Segment.drainKeyReferenceQueue()` whole entry is deleted for all no longer 
referenced weak references, using `ReferenceQueue` of course. Similar method is for weak value maps. 
This draining methods are called as part of `postReadCleanup()` that runs after every read operation 
like `get()`, `contains()`, `compute()` and similar operations.

Note: one of the co-authors is _Doug Lea_, which is also author of JDK’s staple implementation of ConcurrentHashMap.



## Solution 3 – Guava Striped

Use Guava's Stripped utility, based on _lock stripping_ paradigm, in effect very similar to _ID to Lock mapping_ as other examples. The difference is, _lock stripping_ is statistically based, it does not offer 1 : 1 mapping, it hash based and allows collisions.

There is no explicit lock release, either explicit or GC driven; it does not have to, it operates on a different principle. Take for example `Striped.lock(1024)`, that creates simple `Lock[1024]` array, eagerly initialized with 1024 pre-generated Lock objects; see `Striped.CompactStriped`. There can be billions of unique IDs in the application but the lock pool stays at 1024 Locks, always the same Locks. `Striped` operates on statistically **very** low probability of 2, or more, IDs generating same hash trying to access mutex at the same time. 

Downside is that Striped allows ID collisions; multiple threads could be blocked despite locking on different IDs. The chances of this unnecessary depends on size of Striped backing array, eagerly preinitilized as with Striped.CompactStriped; or on maximum size of backing Map as in Striped.LargeLazyStriped. The bigger the collection is, the more hashes are covered, the lesser chance of collision.

Upside is, no cleaning up operation needed, no need to care for unused locks or unreferenced IDs, as described above.

This is probably the solution best serving most of common situations. 

JavaDocs: https://google.github.io/guava/releases/19.0/api/docs/com/google/common/util/concurrent/Striped.html
Tutorial: http://codingjunkie.net/striped-concurrency/