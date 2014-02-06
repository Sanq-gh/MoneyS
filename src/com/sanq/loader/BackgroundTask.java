package com.sanq.loader;

import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 07.11.13
 * Time: 11:49
 */
public interface BackgroundTask<T> {
    public T load(Bundle args);
}
