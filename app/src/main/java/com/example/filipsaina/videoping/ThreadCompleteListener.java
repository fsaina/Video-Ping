package com.example.filipsaina.videoping;

import java.util.List;

/**
 * Interface that every listener class must fulfill
 * Created by filipsaina on 15/05/15.
 */
public interface ThreadCompleteListener {
    public void notifyOfThreadComplete(final List<RecycleViewItemData> data);
}


