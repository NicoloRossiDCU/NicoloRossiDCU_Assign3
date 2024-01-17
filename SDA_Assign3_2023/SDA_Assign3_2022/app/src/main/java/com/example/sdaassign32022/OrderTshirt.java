package com.example.sdaassign32022;


import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


/*
 * A simple {@link Fragment} subclass.
 * @author Chris Coughlan 2019
 */
public class OrderTshirt extends Fragment {


    public OrderTshirt() {
        // Required empty public constructor
    }

    //class wide variables
    private String mPhotoPath;
    private Spinner mSpinner;
    private EditText mCustomerName;
    private EditText meditDelivery;
    private ImageView mCameraImage;

    private TextView mEditCollect;
    private Switch mCollectionSwitch;


    // file that will contain picture
    private File mMediaFile = null;
    //static keys
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final String TAG = "OrderTshirt";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment get the root view.
        final View root = inflater.inflate(R.layout.fragment_order_tshirt, container, false);

        mCustomerName = root.findViewById(R.id.editCustomer);
        meditDelivery = root.findViewById(R.id.editDeliver);
        meditDelivery.setImeOptions(EditorInfo.IME_ACTION_DONE);
        meditDelivery.setRawInputType(InputType.TYPE_CLASS_TEXT);
        mEditCollect= root.findViewById(R.id.editCollect);


        mCollectionSwitch = root.findViewById(R.id.switch1);
        mCameraImage = root.findViewById(R.id.imageView);
        Button mSendButton = root.findViewById(R.id.sendButton);

        //set a listener on the the camera image
        mCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(v);
            }
        });

        //set a listener to start the email intent.
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(v);
            }
        });

        mCollectionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hideCollection(isChecked);
            }
        });


        //initialise spinner using the integer array
        mSpinner = root.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(), R.array.ui_time_entries, R.layout.spinner_days);
        mSpinner.setAdapter(adapter);
        mSpinner.setEnabled(true);
        mEditCollect.setVisibility(View.INVISIBLE);
        mSpinner.setVisibility(View.INVISIBLE);

        return root;
    }


    //Take a photo note the view is being passed so we can get context because it is a fragment.
    //update this to save the image so it can be sent via email
  /*  private void dispatchTakePictureIntent(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
            // Create a file to save the image
//            File imageFile = createImageFile();
*//*

            if (imageFile != null) {
                imageFilePath = imageFile.getAbsolutePath();

                // Set the file URI for the camera intent
                Uri imageUri = FileProvider.getUriForFile(v.getContext(), "your.package.name.fileprovider", imageFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
*//*





                // Start the camera intent
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                //  startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
*/

    /*
     * Returns the Email Body Message, update this to handle either collection or delivery
     */
    private String createOrderSummary(View v)
    {
        String orderMessage = "";
        String deliveryInstruction = meditDelivery.getText().toString();
        String customerName = getString(R.string.customer_name) + " " + mCustomerName.getText().toString();

        orderMessage += "Hi,\n" + "\n" + getString(R.string.order_message_1);

        if(mCollectionSwitch.isChecked()) {
            orderMessage += "\n" + getString(R.string.order_message_collect) + mSpinner.getSelectedItem().toString() + "days";
        }
        else {
            orderMessage += "\n" + "Deliver my order to the following address: ";
            orderMessage += "\n" + deliveryInstruction;
        }
        orderMessage += "\n\n" + getString(R.string.order_message_end) + "\n" + mCustomerName.getText().toString();

        return orderMessage;
    }

    private void hideCollection(boolean isChecked) {
        if(isChecked) {
            mSpinner.setVisibility(View.VISIBLE);
            mEditCollect.setVisibility(View.VISIBLE);
            meditDelivery.setVisibility(View.INVISIBLE);
        }
        else {
            mSpinner.setVisibility(View.INVISIBLE);
            mEditCollect.setVisibility(View.INVISIBLE);
            meditDelivery.setVisibility(View.VISIBLE);

        }
    }

    //Update me to send an email
    private void sendEmail(View v)
    {
        //check that Name is not empty, and ask do they want to continue
        String customerName = mCustomerName.getText().toString();
        if (mCustomerName == null || customerName.equals(""))
        {
            Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();

        }
        else if (mMediaFile == null) {
            Toast.makeText(getContext(), "Please take a picture for the T-shirt", Toast.LENGTH_SHORT).show();
        }

        else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:my-tshirt@sda.ie?subject=Order Request" + "&body=" + createOrderSummary(v));
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mMediaFile));
            intent.setData(data);
            startActivity(intent);
        }
    }

    //.......----------------.................-----------------.......................--------------

    // this method dispatchTakePictureIntent and createMediaFile
    // are used to take the picture and save it to a file already created
    // the methods are a modified version of method dispatchTakePictureIntent and
    // createMediaFile from MediaIntentActivity project of SDA-2021 repository

    /**
     * This method dispatch the action to take picture and is triggered by the
     * image view in the fragment_order_tshirt.
     * It send an intent to take picture from camera and saves it in a file created by the
     * the method createMediaFile. In case of error it logs the exception with meaningful message
     * @param v is the view on which is called, mainly used to get the context
     */
    private void dispatchTakePictureIntent(View v){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createMediaFile();
            } catch (IOException ex) {
                Log.e(TAG, "dispatchTakePictureIntent: Could not create Image file: " +ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(Objects.requireNonNull(v.getContext()),
                        BuildConfig.APPLICATION_ID + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);


                // FORSE IL PROBLEMA é CHE LO FA TROPPO PRESTO


                ImageView view = (ImageView) v.findViewById(R.id.imageView);
                // System.out.println(photoFile.getAbsolutePath());
            //    Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                view.setImageBitmap(BitmapFactory.decodeFile(photoFile.getAbsolutePath()));


               // view.setImageURI(photoURI);

            }
        }
    }

    /**
     * This method creates a file that will store the image taken from the camera
     * @return the new File
     * @throws IOException in case the file was not created for some reason
     */

    private File createMediaFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                String imageFileName = "JPEG_" + timeStamp + "_";
                String exState = Environment.getExternalStorageState();
                if (exState.equals(Environment.MEDIA_MOUNTED))
                {
                    //save it to the pictures directory in the package installed with the application on external storage.
                    File extStorageDir = Objects.requireNonNull(this.getContext()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    mMediaFile = File.createTempFile(
                            imageFileName,  /* prefix */
                            ".jpg",  /* suffix */
                            extStorageDir /* directory */
                    );
                } else {
                    //we should do something here if there is no storage
                    //for example use the local directory where the application is installed instead
                    Log.e(TAG, "createImageFile: External Storage is not available");
                }

                // Save a file: path for use with ACTION_VIEW intents
                if(mMediaFile != null){mPhotoPath = mMediaFile.getAbsolutePath();}
                Log.i(TAG, "createImageFile: "+ mPhotoPath);


        return mMediaFile;
    }
}
