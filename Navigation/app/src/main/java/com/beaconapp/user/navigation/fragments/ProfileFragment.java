package com.beaconapp.user.navigation.fragments;

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
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beaconapp.user.navigation.R;
import com.beaconapp.user.navigation.activities.MainActivity;
import com.beaconapp.user.navigation.services.TestCaseGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class ProfileFragment extends Fragment {

    private static int RESULT_LOAD_IMAGE = 1;
    View rootView;
    private SharedPreferences savednotes;
    SharedPreferences.Editor preferencesEditor;
    TextView textName,textCompanyName, cancel;
    EditText nameField,cnameField;
    TextInputLayout nameFieldLayout, cnameFieldLayout;
    ImageView photo;
    InputMethodManager imm;
    String path = null;
    boolean flag = false, isImageSet;
    String nameTag,companyNameTag;
    final MenuItem menuButton = MainActivity.reminder_action;
    Button testButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        ((MainActivity) getActivity()).setActionBarTitle("Profile");

        rootView =  inflater.inflate(R.layout.fragment_profile, container, false);
        savednotes = PreferenceManager.getDefaultSharedPreferences(getActivity());

        testButton = (Button)rootView.findViewById(R.id.testButton);

        cancel = (TextView)rootView.findViewById(R.id.cancel);
        textName = (TextView)rootView.findViewById(R.id.name_text);
        textCompanyName = (TextView)rootView.findViewById(R.id.company_name_text);
        nameFieldLayout = (TextInputLayout)rootView.findViewById(R.id.name_layout);
        cnameFieldLayout = (TextInputLayout)rootView.findViewById(R.id.cname_layout);
        nameField = (EditText)rootView.findViewById(R.id.name_edit);
        cnameField = (EditText)rootView.findViewById(R.id.company_name_edit);
        textName.setText(savednotes.getString("NAME_KEY", "Name"));
        textCompanyName.setText(savednotes.getString("COMPANY_NAME_KEY", "Company Name"));
        nameField.setText(savednotes.getString("NAME_KEY", ""));
        cnameField.setText(savednotes.getString("COMPANY_NAME_KEY", ""));
        nameField.setSelection(nameField.length());
        cnameField.setSelection(cnameField.length());
        cnameField.setImeOptions(EditorInfo.IME_ACTION_DONE);

        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        photo = (ImageView)rootView.findViewById(R.id.circle_image_view);
        Resources res = getActivity().getResources();
        int id = R.drawable.displaypic;
        photo.setImageBitmap(BitmapFactory.decodeResource(res, id));
        final ImageView buttonLoadImage = (ImageView)rootView.findViewById(R.id.load_image_Button);

        isImageSet = savednotes.getBoolean("IMAGE_KEY", false);
        if(isImageSet){
            path = savednotes.getString("PATH_KEY",path);
            Bitmap bitmap = loadImageFromStorage(path);
            photo.setImageBitmap(bitmap);
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

        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable sequence) {

                if (sequence.length() >= 26) {
                    sequence.delete(25, 26);
                }
            }

            @Override
            public void onTextChanged(CharSequence sequence, int start, int before, int count) {

            }
        });

        cnameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable sequence) {

                if (sequence.length() >= 26) {
                    sequence.delete(25, 26);
                }
            }

            @Override
            public void onTextChanged(CharSequence sequence, int start, int before, int count) {

            }
        });

        menuButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (!flag) {

                    menuButton.setIcon(R.drawable.ic_action_action_done);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    nameFieldLayout.setVisibility(View.VISIBLE);
                    cnameFieldLayout.setVisibility(View.VISIBLE);
                    textName.setVisibility(View.INVISIBLE);
                    cancel.setVisibility(View.VISIBLE);
                    textCompanyName.setVisibility(View.INVISIBLE);
                    buttonLoadImage.setVisibility(View.INVISIBLE);
                    flag = !flag;
                }
                else {

                    nameField.setSelection(nameField.length());
                    cnameField.setSelection(cnameField.length());
                    nameTag = nameField.getText().toString();
                    companyNameTag = cnameField.getText().toString();
                    if (nameTag.equals("")) {
                        nameField.setError("Name Not Set!!");
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                        nameField.startAnimation(shake);
                    }
                    else if (!nameTag.matches("[a-zA-Z ]+")) {
                        nameField.setError("Alphabets Only !!");
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                        nameField.startAnimation(shake);
                    }
                    else if (companyNameTag.equals("")) {
                        cnameField.setError("Company Name Not Set!!");
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                        cnameField.startAnimation(shake);
                    }
                    else {
                        menuButton.setIcon(R.drawable.pencil);
                        if (getView() != null) {
                            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                        }
                        preferencesEditor = savednotes.edit();
                        preferencesEditor.putString("NAME_KEY", nameTag);
                        preferencesEditor.putString("COMPANY_NAME_KEY", companyNameTag);
                        preferencesEditor.apply();
                        buttonLoadImage.setVisibility(View.VISIBLE);
                        nameFieldLayout.setVisibility(View.INVISIBLE);
                        cnameFieldLayout.setVisibility(View.INVISIBLE);
                        textName.setVisibility(View.VISIBLE);
                        textCompanyName.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.INVISIBLE);
                        textName.setText(savednotes.getString("NAME_KEY", "Name"));
                        textCompanyName.setText(savednotes.getString("COMPANY_NAME_KEY", "Company Name"));
                        Toast.makeText(getActivity(), "Personal Details Updated", Toast.LENGTH_SHORT).show();
                        flag = !flag;
                    }
                }

                return false;
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menuButton.setIcon(R.drawable.pencil);
                    flag = !flag;
                    if (getView() != null) {
                        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    }
                    buttonLoadImage.setVisibility(View.VISIBLE);
                    nameFieldLayout.setVisibility(View.INVISIBLE);
                    cnameFieldLayout.setVisibility(View.INVISIBLE);
                    textName.setVisibility(View.VISIBLE);
                    textCompanyName.setVisibility(View.VISIBLE);
                    cancel.setVisibility(View.INVISIBLE);
                    textName.setText(savednotes.getString("NAME_KEY", "Name"));
                    textCompanyName.setText(savednotes.getString("COMPANY_NAME_KEY", "Company Name"));
            }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TestCaseGenerator.class);
                getActivity().startService(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int RESULT_OK = -1;
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds=false;
            BitmapFactory.decodeFile(picturePath,options);
            options.inSampleSize=calculateInSampleSize(options, 200, 200);
            path = saveToInternalSorage(BitmapFactory.decodeFile(picturePath,options));

            preferencesEditor = savednotes.edit();
            preferencesEditor.putString("PATH_KEY", path);

            if(!isImageSet){
                preferencesEditor.putBoolean("IMAGE_KEY", true);
            }

            preferencesEditor.apply();
            Bitmap bitmap = loadImageFromStorage(path);
            photo.setImageBitmap(bitmap);
        }

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private String saveToInternalSorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getActivity());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile.png");

        FileOutputStream fos;
        try {

            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 10, fos);
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
            File f=new File(path, "profile.png");
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return  bitmap;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        menuButton.setOnMenuItemClickListener(null);
    }


}
