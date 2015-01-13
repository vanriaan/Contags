package walt.der.van.riaan.contags.persistence.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import walt.der.van.riaan.contags.dto.Tag;
import walt.der.van.riaan.contags.persistence.AbstractSQLiteOpenHelper;

/**
 * Created by riaanvanderwalt on 15/01/12.
 */
public class TagPersistenceHelper extends AbstractSQLiteOpenHelper {

    private static final String DATABASE_NAME = "CoinTags";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tag";

    public TagPersistenceHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void insertRecord(Tag tag) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues initialValues = new ContentValues();

        initialValues.put("personID", tag.getPersonIdentifier());
        initialValues.put("mediaID", tag.getMediaIdentifier());

        db.insert(TABLE_NAME, null, initialValues);

        db.close();
    }

    public List<Tag> findAll() {

        List<Tag> list = new ArrayList<>();

        Cursor mCursor = getReadableDatabase().query(true, // is distinct
                TABLE_NAME, // table name
                new String[]{"tagID", "personID", "mediaID"},// select clause
                null, // where clause
                null, // where clause parameters
                null, // group by
                null, // having
                null, // order by
                null);// limit

        if (mCursor != null) {
            int count = mCursor.getCount();
            // mCursor.moveToFirst();
            if (mCursor.moveToFirst()) {
                do {
                    Tag tag = new Tag();
                    tag.setMediaIdentifier(mCursor.getString(2));
                    tag.setPersonIdentifier(mCursor.getString(1));
                    list.add(tag);
                } while (mCursor.moveToNext());
            }
        }
        if (!mCursor.isClosed()) {
            mCursor.close();
        }
        close();
        return list;
    }

}
