package com.example.ehsueh.appygolucky.OfflineOnline;

import java.util.LinkedList;

import io.searchbox.core.Delete;

/**
 * Created by Maxwell on 2016-11-28.
 *This class keeps track of the delete requests made by elastic-search while offline.
 * They will be run by the SyncAdapter once the user goes back online again.
 * Requests that have run will then be removed from the queue.
 * @param <def> the type parameter
 */
public class OfflineJobsDeleteQueue <def>{
    private LinkedList<Delete> list = new LinkedList<Delete>();

    /**
     * Enqueue.
     *
     * @param item the item
     */
    public void enqueue(Delete item) {
        list.addLast(item);
    }

    /**
     * Dequeue delete.
     *
     * @return the delete
     */
    public Delete dequeue() {
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
    public void addItems(OfflineJobsDeleteQueue<? extends Delete> q) {
        while (q.hasItems())
            list.addLast(q.dequeue());
    }
}
