package com.example.xyzreader.data;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.RemoteException;
import android.support.annotation.NonNull;

import com.example.xyzreader.model.Article;
import com.example.xyzreader.utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class UpdaterService extends IntentService {
    private static final String TAG = "UpdaterService";

    public static final String BROADCAST_ACTION_STATE_CHANGE
            = "com.example.xyzreader.intent.action.STATE_CHANGE";
    public static final String EXTRA_REFRESHING
            = "com.example.xyzreader.intent.extra.REFRESHING";

    public UpdaterService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            Timber.tag(TAG).w("Not online, not refreshing.");
            return;
        }

        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));

        Uri dirUri = ItemsContract.Items.buildDirUri();

        Observable<List<Article>> articles = NetworkUtils.buildRetrofit().getArticles();
        articles.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver(dirUri));

    }

    @NonNull
    private DisposableObserver<List<Article>> getObserver(Uri dirUri) {
        return new DisposableObserver<List<Article>>() {
            @Override
            public void onNext(List<Article> value) {
                try {
                    insertArticles(value);
                } catch (RemoteException | OperationApplicationException e) {
                    e.printStackTrace();
                }
            }

            private void insertArticles(List<Article> value) throws RemoteException, OperationApplicationException {
                // Don't even inspect the intent, we only do one thing, and that's fetch content.
                ArrayList<ContentProviderOperation> cpo = new ArrayList<>();
                // Delete all items
                cpo.add(ContentProviderOperation.newDelete(dirUri).build());

                for (Article article : value) {
                    ContentValues values = new ContentValues();
                    values.put(ItemsContract.Items.SERVER_ID, article.getId());
                    values.put(ItemsContract.Items.AUTHOR, article.getAuthor());
                    values.put(ItemsContract.Items.TITLE, article.getTitle());
                    values.put(ItemsContract.Items.BODY, article.getBody());
                    values.put(ItemsContract.Items.THUMB_URL, article.getThumb());
                    values.put(ItemsContract.Items.PHOTO_URL, article.getPhoto());
                    values.put(ItemsContract.Items.ASPECT_RATIO, article.getAspectRatio());
                    values.put(ItemsContract.Items.PUBLISHED_DATE, article.getPublishedDate());
                    cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());
                }

                getContentResolver().applyBatch(ItemsContract.CONTENT_AUTHORITY, cpo);
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "Error updating content.");
                sendStickyBroadcast(
                        new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
            }

            @Override
            public void onComplete() {
                sendStickyBroadcast(
                        new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
            }
        };
    }
}
