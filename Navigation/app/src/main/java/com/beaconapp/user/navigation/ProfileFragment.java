package com.beaconapp.user.navigation;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class ProfileFragment extends Fragment {

    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_OK = -1;
    View rootView;
    private SharedPreferences savednotes;
    SharedPreferences.Editor preferencesEditor;
    TextView text,text1;
    EditText view,view1;
    ImageView image,image1,imageView;

    public static Fragment newInstance(Context context) {
        ProfileFragment f = new ProfileFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_profile, null);

        savednotes = PreferenceManager.getDefaultSharedPreferences(getActivity());
        image = (ImageView)rootView.findViewById(R.id.imageView);
        image1 = (ImageView)rootView.findViewById(R.id.imageView2);
        text = (TextView)rootView.findViewById(R.id.textView);
        text1 = (TextView)rootView.findViewById(R.id.textView2);
        view = (EditText)rootView.findViewById(R.id.editText);
        view1 = (EditText)rootView.findViewById(R.id.editText2);
        text.setText(savednotes.getString("NAME_KEY", "NAME"));
        text1.setText(savednotes.getString("COMPANY_NAME_KEY", "COMPANY_NAME"));
        view.setText(savednotes.getString("NAME_KEY", "NAME"));
        view1.setText(savednotes.getString("COMPANY_NAME_KEY", "COMPANY_NAME"));

        imageView = (ImageView)rootView.findViewById(R.id.imgView);
        Resources res = getActivity().getResources();
        int id = R.drawable.picture;
        imageView.setImageBitmap(BitmapFactory.decodeResource(res, id));
        ImageView buttonLoadImage = (ImageView)rootView.findViewById(R.id.imageButton);
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

                view.setVisibility(View.VISIBLE);
                view1.setVisibility(View.VISIBLE);
                text.setVisibility(View.INVISIBLE);
                text1.setVisibility(View.INVISIBLE);
                image.setVisibility(View.INVISIBLE);
                image1.setVisibility(View.VISIBLE);

            }
        });;

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                String name = view.getText().toString();
                preferencesEditor = savednotes.edit();
                preferencesEditor.putString("NAME_KEY", name);

                String cname = view1.getText().toString();

                preferencesEditor.putString("COMPANY_NAME_KEY", cname);
                preferencesEditor.commit();

                view.setVisibility(View.INVISIBLE);
                view1.setVisibility(View.INVISIBLE);
                image.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);
                text1.setVisibility(View.VISIBLE);
                image1.setVisibility(View.INVISIBLE);
                text.setText(savednotes.getString("NAME_KEY", "NAME"));
                text1.setText(savednotes.getString("COMPANY_NAME_KEY", "COMPANY_NAME"));
            }

            });
            return  rootView;

    }


 /*   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView imageView = (ImageView)getView().findViewById(R.id.imgView);
        Resources res = getActivity().getResources();
        int id = R.drawable.picture;
        imageView.setImageBitmap(BitmapFactory.decodeResource(res, id));
        ImageView buttonLoadImage = (ImageView)getView().findViewById(R.id.imageButton);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }*/


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

            ImageView imageView = (ImageView)getView().findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }

    }

}