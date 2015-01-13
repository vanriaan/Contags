package walt.der.van.riaan.contags.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by riaanvanderwalt on 15/01/13.
 */
public abstract class AbstractSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CoinTags";
    private static final int DATABASE_VERSION = 1;

    private static final String DB_CREATE =
            "CREATE TABLE tag (tagID INTEGER PRIMARY KEY, " +
                    "personID TEXT, " +
                    "mediaID TEXT);";

    public AbstractSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //this method needs to be able to create the entire database required by the application, not just the one table.
        db.execSQL(DB_CREATE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException("onUpgrade() not implemented.");
    }
}
