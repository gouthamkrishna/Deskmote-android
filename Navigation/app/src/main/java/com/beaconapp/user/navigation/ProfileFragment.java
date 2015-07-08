package com.beaconapp.user.navigation;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class ProfileFragment extends Fragment {

    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_OK = -1;
    View rootView;
    private SharedPreferences savednotes;
    SharedPreferences.Editor preferencesEditor;
    TextView text_name,text_cname;
    EditText name_field,cname_field;
    ImageView image,image1,imageView;
    InputMethodManager imm;
    String path = null;
    Boolean defaultval = false, test;

    public static Fragment newInstance(Context context) {
        ProfileFragment f = new ProfileFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

	((MainActivity) getActivity())
                .setActionBarTitle("Profile");

        rootView =  inflater.inflate(R.layout.fragment_profile, null);
        savednotes = PreferenceManager.getDefaultSharedPreferences(getActivity());
        image = (ImageView)rootView.findViewById(R.id.imageView);
        image1 = (ImageView)rootView.findViewById(R.id.imageView2);
        text_name = (TextView)rootView.findViewById(R.id.textView);
        text_cname = (TextView)rootView.findViewById(R.id.textView2);
        name_field = (EditText)rootView.findViewById(R.id.editText);
        cname_field = (EditText)rootView.findViewById(R.id.editText2);
        text_name.setText(savednotes.getString("NAME_KEY", "NAME"));
        text_cname.setText(savednotes.getString("COMPANY_NAME_KEY", "COMPANY_NAME"));
        name_field.setHint(savednotes.getString("NAME_KEY", "NAME"));
        cname_field.setHint(savednotes.getString("COMPANY_NAME_KEY", "COMPANY_NAME"));

        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        imageView = (ImageView)rootView.findViewById(R.id.imgView);
        Resources res = getActivity().getResources();
        int id = R.drawable.picture;
        imageView.setImageBitmap(BitmapFactory.decodeResource(res, id));
        ImageView buttonLoadImage = (ImageView)rootView.findViewById(R.id.imageButton);

        test = savednotes.getBoolean("IMAGE_KEY", defaultval);
        if(test){
            path = savednotes.getString("PATH_KEY",path);
            Bitmap bitmap = loadImageFromStorage(path);

            imageView.setImageBitmap(bitmap);
        }

        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                name_field.setVisibility(View.VISIBLE);
                cname_field.setVisibility(View.VISIBLE);
                text_name.setVisibility(View.INVISIBLE);
                text_cname.setVisibility(View.INVISIBLE);
                image.setVisibility(View.INVISIBLE);
                image1.setVisibility(View.VISIBLE);

            }
        });;

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                String name = name_field.getText().toString();
                preferencesEditor = savednotes.edit();
                preferencesEditor.putString("NAME_KEY", name);

                String cname = cname_field.getText().toString();
                preferencesEditor.putString("COMPANY_NAME_KEY", cname);
                preferencesEditor.commit();

                name_field.setVisibility(View.INVISIBLE);
                cname_field.setVisibility(View.INVISIBLE);
                image.setVisibility(View.VISIBLE);
                text_name.setVisibility(View.VISIBLE);
                text_cname.setVisibility(View.VISIBLE);
                image1.setVisibility(View.INVISIBLE);
                text_name.setText(savednotes.getString("NAME_KEY", "NAME"));
                text_cname.setText(savednotes.getString("COMPANY_NAME_KEY", "COMPANY_NAME"));
            }

            });
            return  rootView;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            path = saveToInternalSorage(BitmapFactory.decodeFile(picturePath));

            preferencesEditor = savednotes.edit();
            preferencesEditor.putString("PATH_KEY", path);

            if(!test){
                preferencesEditor.putBoolean("IMAGE_KEY", true);
            }

            preferencesEditor.commit();
            Bitmap bitmap = loadImageFromStorage(path);
            imageView.setImageBitmap(bitmap);
        }

    }

    private String saveToInternalSorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getActivity());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    private Bitmap loadImageFromStorage(String path)
    {

        Bitmap bitmap = null;
        try {
            File f=new File(path, "profile.jpg");
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return  bitmap;

    }

}
