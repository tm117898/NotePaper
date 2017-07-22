package cesc.shang.notepaper.constant;

/**
 * Created by shanghaolongteng on 2016/9/5.
 */
public class TableConstant {
    public static class PointTable {
        public static final String TABLE_NAME = "point_table";
        public static final String ID_COLUMN_NAME = "_id";
        public static final String X_COLUMN_NAME = "x";
        public static final String Y_COLUMN_NAME = "y";
        public static final String PATH_ID_COLUMN_NAME = "path_id";

        public static final String CREATE_TABLE_SQL =
                "create table if not exists " + TABLE_NAME +
                        " (" + ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                        + X_COLUMN_NAME + " float , "
                        + Y_COLUMN_NAME + " float , "
                        + PATH_ID_COLUMN_NAME + " int )";
    }

    public static class PathTable {
        public static final String TABLE_NAME = "path_table";
        public static final String ID_COLUMN_NAME = "_id";
        public static final String COLOR_COLUMN_NAME = "color";
        public static final String WIDTH_COLUMN_NAME = "width";
        public static final String STATE_COLUMN_NAME = "state";
        public static final String PICTURE_ID_COLUMN_NAME = "picture_id";

        public static final String CREATE_TABLE_SQL =
                "create table if not exists " + TABLE_NAME +
                        " (" + ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                        + COLOR_COLUMN_NAME + " int , "
                        + WIDTH_COLUMN_NAME + " int , "
                        + STATE_COLUMN_NAME + " int , "
                        + PICTURE_ID_COLUMN_NAME + " int )";
    }

    public static class PictureTable {
        public static final String TABLE_NAME = "picture_table";
        public static final String ID_COLUMN_NAME = "_id";
        public static final String STATE_COLUMN_NAME = "state";
        public static final String BG_TYPE_COLUMN_NAME = "bg_type";
        public static final String BG_DESCRIBE_COLUMN_NAME = "bg_describe";

        public static final String CREATE_TABLE_SQL =
                "create table if not exists " + TABLE_NAME +
                        " (" + ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                        + BG_TYPE_COLUMN_NAME + " int , "
                        + BG_DESCRIBE_COLUMN_NAME + " text , "
                        + STATE_COLUMN_NAME + " int )";
    }
}
