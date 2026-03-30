package com.ojassoft.astrosage.varta.interfacefile;

import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;

public interface LoadMoreList {
    void loadMoreAstrologerist(int listCount);
    void callAstrologer(AstrologerDetailBean astrologerDetailBean);
    void chatAstrologer(AstrologerDetailBean astrologerDetailBean);
}