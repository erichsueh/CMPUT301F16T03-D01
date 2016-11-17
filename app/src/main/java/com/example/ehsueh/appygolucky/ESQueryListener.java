package com.example.ehsueh.appygolucky;

import java.util.List;

/**
 * This abstract class is to be passed to any AsyncTask which executes an elastic search query.
 * This is to avoid calls to AsyncTask.get().  If this class is used effectively,
 * should avoid all stalls in the UI.
 */

public class ESQueryListener {
    private List<?> results;
    /**
     * This method may be overridden to directly handle the results.
     * For example, sometimes we may want this class to be directly responsible for populating
     * a list in the UI.
     */
    public void onQueryCompletion(List<?> results) {
        this.results = results;
    }

    public List getResults() {
        return this.results;
    }
}
