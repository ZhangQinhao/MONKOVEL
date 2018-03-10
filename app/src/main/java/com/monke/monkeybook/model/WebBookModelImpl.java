//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.monke.monkeybook.model;

import com.monke.monkeybook.bean.BookContentBean;
import com.monke.monkeybook.bean.BookShelfBean;
import com.monke.monkeybook.bean.SearchBookBean;
import com.monke.monkeybook.model.content.GxwztvBookModelImpl;
import com.monke.monkeybook.model.impl.IStationBookModel;
import com.monke.monkeybook.model.impl.IWebBookModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class WebBookModelImpl implements IWebBookModel {

    public static WebBookModelImpl getInstance() {
        return new WebBookModelImpl();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 网络请求并解析书籍信息
     * return BookShelfBean
     */
    @Override
    public Observable<BookShelfBean> getBookInfo(BookShelfBean bookShelfBean) {
        IStationBookModel bookModel = BookSourceManage.getBookSourceModel(bookShelfBean.getTag());
        if (bookModel != null) {
            return bookModel.getBookInfo(bookShelfBean);
        } else {
            return null;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 网络解析图书目录
     * return BookShelfBean
     */
    @Override
    public Observable<BookShelfBean> getChapterList(final BookShelfBean bookShelfBean) {
        IStationBookModel bookModel = BookSourceManage.getBookSourceModel(bookShelfBean.getTag());
        if (bookModel != null) {
            return bookModel.getChapterList(bookShelfBean);
        } else {
            return Observable.error(new Throwable(bookShelfBean.getBookInfoBean().getName()+"没有书源"));
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 章节缓存
     */
    @Override
    public Observable<BookContentBean> getBookContent(String durChapterUrl, int durChapterIndex, String tag) {
        IStationBookModel bookModel = BookSourceManage.getBookSourceModel(tag);
        if (bookModel != null) {
            return bookModel.getBookContent(durChapterUrl, durChapterIndex);
        } else
            return Observable.create(e -> {
                e.onNext(new BookContentBean());
                e.onComplete();
            });
    }

    /**
     * 其他站点集合搜索
     */
    @Override
    public Observable<List<SearchBookBean>> searchOtherBook(String content, int page, String tag) {
        //获取所有书源类
        IStationBookModel bookModel = BookSourceManage.getBookSourceModel(tag);
        if (bookModel != null) {
            return bookModel.searchBook(content, page);
        } else {
            return Observable.create(e -> {
                e.onNext(new ArrayList<>());
                e.onComplete();
            });
        }
    }

    /**
     * 获取分类书籍
     */
    @Override
    public Observable<List<SearchBookBean>> getKindBook(String url, int page) {
        return GxwztvBookModelImpl.getInstance().getKindBook(url, page);
    }
}
