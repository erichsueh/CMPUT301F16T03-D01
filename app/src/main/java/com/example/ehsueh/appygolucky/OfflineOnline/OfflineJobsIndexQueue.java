package com.example.ehsueh.appygolucky.OfflineOnline;

import java.util.LinkedList;

import io.searchbox.core.Index;

/**
 * Created by Maxwell on 2016-11-28.
 * This class keeps track of the index requests made by elastic-search while offline.
 * They will be run by the SyncAdapter once the user goes back online again.
 * Requests that have run will then be removed from the queue.
 *
 * @param <fed> the type parameter
 */
public class OfflineJobsIndexQueue <fed>{
    private LinkedList<Index> list = new LinkedList<Index>();

    /**
     * Enqueue.
     *
     * @param item the item
     */
    public void enqueue(Index item) {
        list.addLast(item);
    }

    /**
     * Dequeue index.
     *
     * @return the index
     */
    public Index dequeue() {
        return list.poll();
    }

    /**
     * Has items boolean.
     *
     * @return the boolean
     */
    public boolean hasItems() {
        return !list.isEmpty();
    }

    /**
     * Size int.
     *
     * @return the int
     */
    public int size() {
        return list.size();
    }

    /**
     * Add items.
     *
     * @param q the q
     */
    public void addItems(OfflineJobsIndexQueue<? extends Index> q) {
        while (q.hasItems())
            list.addLast(q.dequeue());
    }
}
