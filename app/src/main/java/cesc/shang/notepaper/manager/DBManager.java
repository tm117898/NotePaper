package cesc.shang.notepaper.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import cesc.shang.baselib.base.database.BaseSQLiteOpenHelper;
import cesc.shang.notepaper.constant.PictureConstant;
import cesc.shang.notepaper.constant.TableConstant;
import cesc.shang.notepaper.entity.PathEntity;
import cesc.shang.notepaper.entity.PictureEntity;
import cesc.shang.notepaper.entity.PointEntity;

/**
 * Created by shanghaolongteng on 2016/9/5.
 */
public class DBManager extends BaseSQLiteOpenHelper {
    public static final String DB_NAME = "pic_db";
    public static final int DB_FIRST_VERSION = 1;

    public DBManager(Context context) {
        super(context, DB_NAME, null, DB_FIRST_VERSION);
    }

    public DBManager(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DB_NAME, null, DB_FIRST_VERSION, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);

        db.execSQL(TableConstant.PointTable.CREATE_TABLE_SQL);
        db.execSQL(TableConstant.PathTable.CREATE_TABLE_SQL);
        db.execSQL(TableConstant.PictureTable.CREATE_TABLE_SQL);
    }

    public List<PictureEntity> selectAllPicture(Context context) {
        List<PictureEntity> list = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor picCursor = db.rawQuery("select * from " + TableConstant.PictureTable.TABLE_NAME, null);
        if (picCursor.moveToFirst()) {
            list = new ArrayList<>(picCursor.getCount());
            do {
                PictureEntity pic = new PictureEntity(context);
                int picId = picCursor.getInt(picCursor.getColumnIndex(TableConstant.PictureTable.ID_COLUMN_NAME));
                pic.setId(picId);
                pic.setState(picCursor.getInt(picCursor.getColumnIndex(TableConstant.PictureTable.STATE_COLUMN_NAME)));
                int bgType = picCursor.getInt(picCursor.getColumnIndex(TableConstant.PictureTable.BG_TYPE_COLUMN_NAME));
                String bgDescribe = picCursor.getString(picCursor.getColumnIndex(TableConstant.PictureTable.BG_DESCRIBE_COLUMN_NAME));
                pic.setBg(bgType, bgDescribe);

                selectPath(pic, db, picId);

                list.add(pic);
            } while (picCursor.moveToNext());
        }
        if (!picCursor.isClosed())
            picCursor.close();
        return list;
    }

    public PictureEntity selectPictureEntity(Context context, long picId) {
        PictureEntity pic = new PictureEntity(context);
        SQLiteDatabase db = getReadableDatabase();

        selectPicture(pic, db, picId);

        return pic;
    }

    private void selectPicture(PictureEntity pic, SQLiteDatabase db, long id) {
        Cursor picCursor = db.rawQuery("select * from " + TableConstant.PictureTable.TABLE_NAME +
                " where " + TableConstant.PictureTable.ID_COLUMN_NAME + " = ?", new String[]{String.valueOf(id)});
        if (picCursor.moveToFirst()) {
            int picId = picCursor.getInt(picCursor.getColumnIndex(TableConstant.PictureTable.ID_COLUMN_NAME));
            pic.setId(picId);
            pic.setState(picCursor.getInt(picCursor.getColumnIndex(TableConstant.PictureTable.STATE_COLUMN_NAME)));
            int bgType = picCursor.getInt(picCursor.getColumnIndex(TableConstant.PictureTable.BG_TYPE_COLUMN_NAME));
            String bgDescribe = picCursor.getString(picCursor.getColumnIndex(TableConstant.PictureTable.BG_DESCRIBE_COLUMN_NAME));
            pic.setBg(bgType, bgDescribe);

            selectPath(pic, db, picId);
        }
        if (!picCursor.isClosed())
            picCursor.close();
    }

    public PictureEntity selectCatchPictureEntity(Context context) {
        PictureEntity pic = new PictureEntity(context);
        SQLiteDatabase db = getReadableDatabase();

        selectCatchPicture(pic, db, PictureConstant.PIC_CATCH_STATE);

        return pic;
    }

    private void selectCatchPicture(PictureEntity pic, SQLiteDatabase db, int state) {
        Cursor picCursor = db.rawQuery("select * from " + TableConstant.PictureTable.TABLE_NAME +
                " where " + TableConstant.PictureTable.STATE_COLUMN_NAME + " = ?", new String[]{String.valueOf(state)});
        if (picCursor.moveToFirst()) {
            int picId = picCursor.getInt(picCursor.getColumnIndex(TableConstant.PictureTable.ID_COLUMN_NAME));
            pic.setId(picId);
            pic.setState(picCursor.getInt(picCursor.getColumnIndex(TableConstant.PictureTable.STATE_COLUMN_NAME)));
            int bgType = picCursor.getInt(picCursor.getColumnIndex(TableConstant.PictureTable.BG_TYPE_COLUMN_NAME));
            String bgDescribe = picCursor.getString(picCursor.getColumnIndex(TableConstant.PictureTable.BG_DESCRIBE_COLUMN_NAME));
            pic.setBg(bgType, bgDescribe);

            selectPath(pic, db, picId);
        }
        if (!picCursor.isClosed())
            picCursor.close();
    }

    public void selectPath(PictureEntity pic, SQLiteDatabase db, int picId) {
        Cursor pathCursor = db.rawQuery("select * from " + TableConstant.PathTable.TABLE_NAME +
                " where " + TableConstant.PathTable.PICTURE_ID_COLUMN_NAME + " = ?", new String[]{String.valueOf(picId)});
        if (pathCursor.moveToFirst()) {
            do {
                int pathId = pathCursor.getInt(pathCursor.getColumnIndex(TableConstant.PathTable.ID_COLUMN_NAME));
                int pathColor = pathCursor.getInt(pathCursor.getColumnIndex(TableConstant.PathTable.COLOR_COLUMN_NAME));
                int pathWidth = pathCursor.getInt(pathCursor.getColumnIndex(TableConstant.PathTable.WIDTH_COLUMN_NAME));
                int pathState = pathCursor.getInt(pathCursor.getColumnIndex(TableConstant.PathTable.STATE_COLUMN_NAME));
                int pathPicId = pathCursor.getInt(pathCursor.getColumnIndex(TableConstant.PathTable.PICTURE_ID_COLUMN_NAME));
                PathEntity path = new PathEntity();
                path.setId(pathId);
                path.setColor(pathColor);
                path.setWidth(pathWidth);
                path.setState(pathState);
                path.setPictureId(pathPicId);

                selectPoint(db, pathId, path);

                pic.add(path);
            } while (pathCursor.moveToNext());
        }
        if (!pathCursor.isClosed())
            pathCursor.close();
    }

    public void selectPoint(SQLiteDatabase db, int pathId, PathEntity path) {
        Cursor pointCursor = db.rawQuery("select * from " + TableConstant.PointTable.TABLE_NAME +
                " where " + TableConstant.PointTable.PATH_ID_COLUMN_NAME + " = ?", new String[]{String.valueOf(pathId)});
        if (pointCursor.moveToFirst()) {
            do {
                int id = pointCursor.getInt(pointCursor.getColumnIndex(TableConstant.PointTable.ID_COLUMN_NAME));
                float x = pointCursor.getFloat(pointCursor.getColumnIndex(TableConstant.PointTable.X_COLUMN_NAME));
                float y = pointCursor.getFloat(pointCursor.getColumnIndex(TableConstant.PointTable.Y_COLUMN_NAME));
                PointEntity point = new PointEntity(x, y);
                point.setId(id);
                path.getPoints().add(point);
            } while (pointCursor.moveToNext());
        }
        if (!pointCursor.isClosed())
            pointCursor.close();
    }

    public void insertPicture(PictureEntity picture) {
        String sql = "INSERT INTO " + TableConstant.PictureTable.TABLE_NAME +
                " ( " + TableConstant.PictureTable.STATE_COLUMN_NAME +
                " , " + TableConstant.PictureTable.BG_TYPE_COLUMN_NAME +
                " , " + TableConstant.PictureTable.BG_DESCRIBE_COLUMN_NAME +
                " ) VALUES ( ? ,? , ? )";
        long id = insert(sql, new BindStatementDataCallBack<PictureEntity>() {
            @Override
            public void bind(SQLiteStatement sqLiteStatement, PictureEntity pictureEntity) {
                sqLiteStatement.bindLong(1, pictureEntity.getState());
                sqLiteStatement.bindLong(2, pictureEntity.getBgType());
                sqLiteStatement.bindString(3, pictureEntity.getBgDescribe());
            }
        }, picture);
        picture.setId(id);
    }

    public void insertPath(final long picId, PathEntity pathEntity) {
        String sql = "INSERT INTO " + TableConstant.PathTable.TABLE_NAME +
                " ( " + TableConstant.PathTable.COLOR_COLUMN_NAME +
                " , " + TableConstant.PathTable.WIDTH_COLUMN_NAME +
                " , " + TableConstant.PathTable.STATE_COLUMN_NAME +
                " , " + TableConstant.PathTable.PICTURE_ID_COLUMN_NAME +
                " ) VALUES ( ? , ? , ? , ? )";
        long id = insert(sql, new BindStatementDataCallBack<PathEntity>() {
            @Override
            public void bind(SQLiteStatement sqLiteStatement, PathEntity pathEntity) {
                sqLiteStatement.bindLong(1, pathEntity.getColor());
                sqLiteStatement.bindDouble(2, pathEntity.getWidth());
                sqLiteStatement.bindLong(3, pathEntity.getState());
                sqLiteStatement.bindLong(4, picId);
                pathEntity.setPictureId(picId);
            }
        }, pathEntity);
        pathEntity.setId(id);
    }

    public void insertPoint(final long pathId, PointEntity pointEntity) {
        String sql = "INSERT INTO " + TableConstant.PointTable.TABLE_NAME +
                " ( " + TableConstant.PointTable.X_COLUMN_NAME +
                " , " + TableConstant.PointTable.Y_COLUMN_NAME +
                " , " + TableConstant.PointTable.PATH_ID_COLUMN_NAME +
                " ) VALUES ( ? , ? , ? )";
        long id = insert(sql, new BindStatementDataCallBack<PointEntity>() {
            @Override
            public void bind(SQLiteStatement sqLiteStatement, PointEntity pointEntity) {
                sqLiteStatement.bindDouble(1, pointEntity.getX());
                sqLiteStatement.bindDouble(2, pointEntity.getY());
                sqLiteStatement.bindLong(3, pathId);
                pointEntity.setPathId(pathId);
            }
        }, pointEntity);
        pointEntity.setId(id);
    }

    public void previousStep(PathEntity path) {
        int state = PictureConstant.PIC_CATCH_STATE;
        step(path, state);
    }

    public void step(PathEntity path, final int state) {
        String sql = "UPDATE " + TableConstant.PathTable.TABLE_NAME +
                " SET " + TableConstant.PathTable.STATE_COLUMN_NAME + " = ? " +
                " WHERE " + TableConstant.PathTable.ID_COLUMN_NAME + " = ? ";
        updateOrDelete(sql, new BindStatementDataCallBack<PathEntity>() {
            @Override
            public void bind(SQLiteStatement sqLiteStatement, PathEntity pathEntity) {
                sqLiteStatement.bindLong(1, state);
                sqLiteStatement.bindLong(2, pathEntity.getId());
            }
        }, path);
    }

    public void nextStep(PathEntity path) {
        int state = PictureConstant.PIC_VALID_STATE;
        step(path, state);
    }

    public void clear(PictureEntity picture) {
        String sql = "UPDATE " + TableConstant.PathTable.TABLE_NAME +
                " SET " + TableConstant.PathTable.STATE_COLUMN_NAME + " = ? " +
                " WHERE " + TableConstant.PathTable.PICTURE_ID_COLUMN_NAME + " = ? ";
        updateOrDelete(sql, new BindStatementDataCallBack<PictureEntity>() {
            @Override
            public void bind(SQLiteStatement sqLiteStatement, PictureEntity pictureEntity) {
                sqLiteStatement.bindLong(1, PictureConstant.PIC_CATCH_STATE);
                sqLiteStatement.bindLong(2, pictureEntity.getId());
            }
        }, picture);
    }

    public void releaseCatch(PictureEntity picture) {
        deletePath(picture.getId(), PictureConstant.PIC_CATCH_STATE);
    }

    public void releaseAll(PictureEntity picture) {
        deletePath(picture.getId(), PictureConstant.PIC_CATCH_STATE);
        deletePath(picture.getId(), PictureConstant.PIC_VALID_STATE);
        updatePicState(picture, PictureConstant.PIC_CATCH_STATE);
    }

    public void deletePath(long picId, int state) {
        Long[] pathIds = selectPathIds(picId, state);
        if (pathIds != null && pathIds.length > 0) {
            deletePaths(pathIds);
            deletePoints(pathIds);
        }
    }

    public Long[] selectPathIds(long picId, int state) {
        Long[] pathIds = null;
        String sql = "select _id from " + TableConstant.PathTable.TABLE_NAME +
                " where " + TableConstant.PathTable.PICTURE_ID_COLUMN_NAME + " = ? and " +
                TableConstant.PathTable.STATE_COLUMN_NAME + " = ? ";
        SQLiteDatabase db = getReadableDatabase();
        Cursor pathIdCursor = db.rawQuery(sql, new String[]{String.valueOf(picId), String.valueOf(state)});
        if (pathIdCursor.moveToFirst()) {
            int i = 0;
            pathIds = new Long[pathIdCursor.getCount()];
            do {
                pathIds[i] = pathIdCursor.getLong(pathIdCursor.getColumnIndex(TableConstant.PathTable.ID_COLUMN_NAME));
                i++;
            } while (pathIdCursor.moveToNext());
        }
        if (!pathIdCursor.isClosed()) {
            pathIdCursor.close();
        }
        return pathIds;
    }

    public void deletePaths(Long... pathIds) {
        String sql = "DELETE FROM " + TableConstant.PathTable.TABLE_NAME +
                " WHERE " + TableConstant.PathTable.ID_COLUMN_NAME + " = ?";
        updateOrDelete(sql, new BindStatementDataCallBack<Long>() {
            @Override
            public void bind(SQLiteStatement sqLiteStatement, Long pathId) {
                sqLiteStatement.bindLong(1, pathId);
            }
        }, pathIds);
    }

    public void deletePoints(Long... pathIds) {
        String sql = "DELETE FROM " + TableConstant.PointTable.TABLE_NAME +
                " WHERE " + TableConstant.PointTable.PATH_ID_COLUMN_NAME + " = ?";
        updateOrDelete(sql, new BindStatementDataCallBack<Long>() {
            @Override
            public void bind(SQLiteStatement sqLiteStatement, Long pathId) {
                sqLiteStatement.bindLong(1, pathId);
            }
        }, pathIds);
    }

    public void save(PictureEntity picture) {
        updatePicState(picture, PictureConstant.PIC_VALID_STATE);
    }

    private void updatePicState(PictureEntity picture, final int state) {
        String sql = "UPDATE " + TableConstant.PictureTable.TABLE_NAME +
                " SET " + TableConstant.PictureTable.STATE_COLUMN_NAME + " = ? " +
                " WHERE " + TableConstant.PictureTable.ID_COLUMN_NAME + " = ? ";
        updateOrDelete(sql, new BindStatementDataCallBack<PictureEntity>() {
            @Override
            public void bind(SQLiteStatement sqLiteStatement, PictureEntity pictureEntity) {
                sqLiteStatement.bindLong(1, state);
                sqLiteStatement.bindLong(2, pictureEntity.getId());
            }
        }, picture);
    }

    public void bgChanged(PictureEntity picture) {
        String sql = "UPDATE " + TableConstant.PictureTable.TABLE_NAME +
                " SET " + TableConstant.PictureTable.BG_TYPE_COLUMN_NAME + " = ? , " +
                TableConstant.PictureTable.BG_DESCRIBE_COLUMN_NAME + " = ? " +
                " WHERE " + TableConstant.PictureTable.ID_COLUMN_NAME + " = ? ";
        updateOrDelete(sql, new BindStatementDataCallBack<PictureEntity>() {
            @Override
            public void bind(SQLiteStatement sqLiteStatement, PictureEntity pictureEntity) {
                sqLiteStatement.bindLong(1, pictureEntity.getBgType());
                sqLiteStatement.bindString(2, pictureEntity.getBgDescribe());
                sqLiteStatement.bindLong(3, pictureEntity.getId());
            }
        }, picture);
    }

    public void delete(PictureEntity picture) {
        deletePic(picture);
        deletePath(picture.getId(), PictureConstant.PIC_CATCH_STATE);
        deletePath(picture.getId(), PictureConstant.PIC_VALID_STATE);
    }

    private void deletePic(PictureEntity picture) {
        String sql = "DELETE FROM " + TableConstant.PictureTable.TABLE_NAME +
                " WHERE " + TableConstant.PictureTable.ID_COLUMN_NAME + " = ?";
        updateOrDelete(sql, new BindStatementDataCallBack<PictureEntity>() {
            @Override
            public void bind(SQLiteStatement sqLiteStatement, PictureEntity pic) {
                sqLiteStatement.bindLong(1, pic.getId());
            }
        }, picture);
    }
}
