package com.example.sowitplot.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
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

  public PlotDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlotEntity = new EntityInsertionAdapter<PlotEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `plots` (`id`,`name`,`polygonEncoded`,`centerLat`,`centerLng`) VALUES (nullif(?, 0),?,?,?,?)";
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
      }
    };
  }

  @Override
  public Object insert(final PlotEntity plot, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlotEntity.insert(plot);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PlotEntity>> getAllPlots() {
    final String _sql = "SELECT * FROM plots ORDER BY name";
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
            _item = new PlotEntity(_tmpId,_tmpName,_tmpPolygonEncoded,_tmpCenterLat,_tmpCenterLng);
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

  @Override
  public Object getPlotById(final long id, final Continuation<? super PlotEntity> $completion) {
    final String _sql = "SELECT * FROM plots WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PlotEntity>() {
      @Override
      @Nullable
      public PlotEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPolygonEncoded = CursorUtil.getColumnIndexOrThrow(_cursor, "polygonEncoded");
          final int _cursorIndexOfCenterLat = CursorUtil.getColumnIndexOrThrow(_cursor, "centerLat");
          final int _cursorIndexOfCenterLng = CursorUtil.getColumnIndexOrThrow(_cursor, "centerLng");
          final PlotEntity _result;
          if (_cursor.moveToFirst()) {
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
            _result = new PlotEntity(_tmpId,_tmpName,_tmpPolygonEncoded,_tmpCenterLat,_tmpCenterLng);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
