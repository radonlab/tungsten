package com.radonlab.tungsten.dao;

import android.content.Context;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ScriptRepo {
    private final AppDatabase db;

    private ScriptRepo(AppDatabase db) {
        this.db = db;
    }

    public static ScriptRepo getInstance(Context context) {
        return new ScriptRepo(AppDatabase.getInstance(context));
    }

    public Observable<List<ScriptDO>> getAll() {
        return Observable.fromCallable(() -> db.scriptDAO().getAll())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ScriptDO> findById(int scriptId) {
        return Observable.fromCallable(() -> db.scriptDAO().findById(scriptId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insert(ScriptDO script) {
        return Completable.fromAction(() -> {
                    db.scriptDAO().insert(script);
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable upsert(ScriptDO script) {
        return Completable.fromAction(() -> {
                    db.scriptDAO().upsert(script);
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable delete(ScriptDO script) {
        return Completable.fromAction(() -> {
                    db.scriptDAO().delete(script);
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}