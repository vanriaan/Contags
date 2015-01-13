package walt.der.van.riaan.contags;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import walt.der.van.riaan.contags.dto.Tag;
import walt.der.van.riaan.contags.persistence.dao.TagPersistenceHelper;


public class MainActivity extends ActionBarActivity {

    private static final int CONTACT_PICKER_RESULT = 1001;
    private static final int PHOTO_PICKER_RESULT = 2001;

    private TagPersistenceHelper tagDAO;

    private Tag currentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tagDAO = new TagPersistenceHelper(getApplicationContext());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void doLaunchContactPicker(View view) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    public void doLaunchPhotoPicker(View view) {
        if (currentTag == null || currentTag.getPersonIdentifier() == null) {
            Toast.makeText(getApplicationContext(), "You must select a person first.", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.setType("image/*");
            //i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            //startActivityForResult(Intent.createChooser(i, "Select image(s)"), PHOTO_PICKER_RESULT);
            startActivityForResult(i, PHOTO_PICKER_RESULT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    Cursor cursor = null;
                    String email = "";
                    try {
                        Uri result = data.getData();

                        // get the contact id from the Uri
                        String id = result.getLastPathSegment();

                        populateCurrentTag(id, null);

                        // query for everything email
                        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?", new String[] { id },
                                null);

                        int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

                        // let's just get the first email
                        if (cursor.moveToFirst()) {
                            email = cursor.getString(emailIdx);
                        } else {

                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Exception thrown.",
                                Toast.LENGTH_LONG).show();
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                        EditText emailEntry = (EditText) findViewById(R.id.invite_email);
                        emailEntry.setText(email);
                        if (email.length() == 0) {
                            Toast.makeText(this, "No email found for contact.",
                                    Toast.LENGTH_LONG).show();
                        }

                    }



                    break;
                case PHOTO_PICKER_RESULT:
                    if (data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };

                        Cursor pictureCursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        pictureCursor.moveToFirst();

                        int columnIndex = pictureCursor.getColumnIndex(filePathColumn[0]);
                        populateCurrentTag(null, pictureCursor.getString(columnIndex));
                        pictureCursor.close();
                    }

                    break;
            }

        } else {

        }
    }

    private void populateCurrentTag(String personID, String mediaID) {
        if (currentTag == null) {
            currentTag = new Tag();
        }
        if (personID != null && personID.length() > 0) {
            currentTag.setPersonIdentifier(personID);
        }
        if (mediaID != null && mediaID.length() > 0) {
            currentTag.setMediaIdentifier(mediaID);
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void save(View v) {
        tagDAO.insertRecord(currentTag);
        List<Tag> tags = tagDAO.findAll();

        Toast.makeText(getApplicationContext(), "Tags in DB: " + tags.size(), Toast.LENGTH_LONG).show();
    }
}
