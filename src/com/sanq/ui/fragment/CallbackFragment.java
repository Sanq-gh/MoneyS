package com.sanq.ui.fragment;

import com.sanq.entity.AbstractEntity;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 19.06.13
 * Time: 23:54
 * To change this template use File | Settings | File Templates.
 */
public interface CallbackFragment {
    public void onFragmentEvent(Integer idFragmentCaption, AbstractEntity ent);
}
