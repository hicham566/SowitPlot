package com.example.sowitplot.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PlotDao_Impl implements PlotDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PlotEntity> __insertionAdapterOfPlotEntity;

  private final EntityDeletionOrUpdateAdapter<PlotEntity> __deletionAdapterOfPlotEntity;

  private final EntityDeletionOrUpdateAdapter<PlotEntity> __updateAdapterOfPlotEntity;

  private final SharedSQLiteStatement __preparedStmtOfRename;

  public PlotDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlotEntity = new EntityInsertionAdapter<PlotEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `plots` (`id`,`name`,`polygonEncoded`,`centerLat`,`centerLng`,`areaSqMeters`,`thumbnailPath`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlotEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getPolygonEncoded() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPolygonEncoded());
        }
        statement.bindDouble(4, entity.getCenterLat());
        statement.bindDouble(5, entity.getCenterLng());
        statement.bindDouble(6, entity.getAreaSqMeters());
        if (entity.getThumbnailPath() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getThumbnailPath());
        }
      }
    };
    this.__deletionAdapterOfPlotEntity = new EntityDeletionOrUpdateAdapter<PlotEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `plots` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlotEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPlotEntity = new EntityDeletionOrUpdateAdapter<PlotEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `plots` SET `id` = ?,`name` = ?,`polygonEncoded` = ?,`centerLat` = ?,`centerLng` = ?,`areaSqMeters` = ?,`thumbnailPath` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlotEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getPolygonEncoded() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPolygonEncoded());
        }
        statement.bindDouble(4, entity.getCenterLat());
        statement.bindDouble(5, entity.getCenterLng());
        statement.bindDouble(6, entity.getAreaSqMeters());
        if (entity.getThumbnailPath() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getThumbnailPath());
        }
        statement.bindLong(8, entity.getId());
      }
    };
    this.__preparedStmtOfRename = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE plots SET name = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final PlotEntity plot, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPlotEntity.insertAndReturnId(plot);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final PlotEntity plot, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPlotEntity.handle(plot);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final PlotEntity plot, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPlotEntity.handle(plot);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object rename(final long id, final String newName,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfRename.acquire();
        int _argIndex = 1;
        if (newName == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, newName);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfRename.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PlotEntity>> getAllPlots() {
    final String _sql = "SELECT * FROM plots ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"plots"}, new Callable<List<PlotEntity>>() {
      @Override
      @NonNull
      public List<PlotEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPolygonEncoded = CursorUtil.getColumnIndexOrThrow(_cursor, "polygonEncoded");
          final int _cursorIndexOfCenterLat = CursorUtil.getColumnIndexOrThrow(_cursor, "centerLat");
          final int _cursorIndexOfCenterLng = CursorUtil.getColumnIndexOrThrow(_cursor, "centerLng");
          final int _cursorIndexOfAreaSqMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "areaSqMeters");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final List<PlotEntity> _result = new ArrayList<PlotEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlotEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpPolygonEncoded;
            if (_cursor.isNull(_cursorIndexOfPolygonEncoded)) {
              _tmpPolygonEncoded = null;
            } else {
              _tmpPolygonEncoded = _cursor.getString(_cursorIndexOfPolygonEncoded);
            }
            final double _tmpCenterLat;
            _tmpCenterLat = _cursor.getDouble(_cursorIndexOfCenterLat);
            final double _tmpCenterLng;
            _tmpCenterLng = _cursor.getDouble(_cursorIndexOfCenterLng);
            final double _tmpAreaSqMeters;
            _tmpAreaSqMeters = _cursor.getDouble(_cursorIndexOfAreaSqMeters);
            final String _tmpThumbnailPath;
            if (_cursor.isNull(_cursorIndexOfThumbnailPath)) {
              _tmpThumbnailPath = null;
            } else {
              _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            }
            _item = new PlotEntity(_tmpId,_tmpName,_tmpPolygonEncoded,_tmpCenterLat,_tmpCenterLng,_tmpAreaSqMeters,_tmpThumbnailPath);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
