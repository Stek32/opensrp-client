package org.ei.opensrp.indonesia.face.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.qualcomm.snapdragon.sdk.face.FaceData;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing;

import org.ei.opensrp.commonregistry.AllCommonsRepository;
import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonPersonObjectClient;
import org.ei.opensrp.indonesia.R;
import org.ei.opensrp.indonesia.anc.NativeKIANCSmartRegisterActivity;
import org.ei.opensrp.indonesia.child.NativeKIAnakSmartRegisterActivity;
import org.ei.opensrp.indonesia.face.camera.utils.FaceConstants;
import org.ei.opensrp.indonesia.face.camera.utils.Tools;
import org.ei.opensrp.indonesia.fragment.NativeKBSmartRegisterFragment;
import org.ei.opensrp.indonesia.fragment.NativeKIANCSmartRegisterFragment;
import org.ei.opensrp.indonesia.fragment.NativeKIAnakSmartRegisterFragment;
import org.ei.opensrp.indonesia.fragment.NativeKIPNCSmartRegisterFragment;
import org.ei.opensrp.indonesia.fragment.NativeKISmartRegisterFragment;
import org.ei.opensrp.indonesia.kartu_ibu.KIDetailActivity;
import org.ei.opensrp.indonesia.kartu_ibu.NativeKISmartRegisterActivity;
import org.ei.opensrp.indonesia.kb.NativeKBSmartRegisterActivity;
import org.ei.opensrp.indonesia.pnc.NativeKIPNCSmartRegisterActivity;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

public class ImageConfirmation extends Activity {

    private static String TAG = ImageConfirmation.class.getSimpleName();
    private Bitmap storedBitmap;
    private Bitmap workingBitmap;
    private Bitmap mutableBitmap;
    ImageView confirmationView;
    ImageView confirmButton;
    ImageView trashButton;
    private String entityId;
    private Rect[] rects;
    private boolean faceFlag = false;
    private boolean identifyPerson = false;
    private FacialProcessing objFace;
    private FaceData[] faceDatas;
    private int arrayPossition;
    Tools tools;
    HashMap<String, String> clientList;
    private String selectedPersonName = "";
    private Parcelable[] kiclient;

    String str_origin_class;

    byte[] data;
    int angle;
    boolean switchCamera;
    private byte[] faceVector;
    private boolean updated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fr_image_face_confirmation);

        init_gui();

        init_extras();

        process_img();

        buttonJob();
    }

    private void init_gui() {
        // Display New Photo
        confirmationView = (ImageView) findViewById(R.id.iv_confirmationView);
        trashButton = (ImageView) findViewById(R.id.iv_cancel);
        confirmButton = (ImageView) findViewById(R.id.iv_approve);
    }

    /**
     * Method to get Info from previous Intent
     */
    private void init_extras() {
        Bundle extras = getIntent().getExtras();
        data = getIntent().getByteArrayExtra("org.sid.sidface.ImageConfirmation");
        angle = extras.getInt("org.sid.sidface.ImageConfirmation.orientation");
        switchCamera = extras.getBoolean("org.sid.sidface.ImageConfirmation.switchCamera");
        entityId = extras.getString("org.sid.sidface.ImageConfirmation.id");
        identifyPerson = extras.getBoolean("org.sid.sidface.ImageConfirmation.identify");
        kiclient = extras.getParcelableArray("org.sid.sidface.ImageConfirmation.kiclient");
        str_origin_class = extras.getString("org.sid.sidface.ImageConfirmation.origin");
        updated = extras.getBoolean("org.sid.sidface.ImageConfirmation.updated");
        Log.e(TAG, "init_extras: "+updated );

    }


    private void process_img() {

        storedBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, null);
        objFace = SmartShutterActivity.faceProc;

        Matrix mat = new Matrix();
        if (!switchCamera) {
            mat.postRotate(angle == 90 ? 270 : (angle == 180 ? 180 : 0));
            mat.postScale(-1, 1);
            storedBitmap = Bitmap.createBitmap(storedBitmap, 0, 0, storedBitmap.getWidth(), storedBitmap.getHeight(), mat, true);
        } else {
            mat.postRotate(angle == 90 ? 90 : (angle == 180 ? 180 : 0));
            storedBitmap = Bitmap.createBitmap(storedBitmap, 0, 0, storedBitmap.getWidth(), storedBitmap.getHeight(), mat, true);
        }
//        TODO : Image from gallery

//        Retrieve data from Local Storage
        clientList = SmartShutterActivity.retrieveHash(getApplicationContext());

        boolean setBitmapResult = objFace.setBitmap(storedBitmap);
        faceDatas = objFace.getFaceData();

        int imageViewSurfaceWidth = storedBitmap.getWidth();
        int imageViewSurfaceHeight = storedBitmap.getHeight();
//        int imageViewSurfaceWidth = confirmationView.getWidth();
//        int imageViewSurfaceHeight = confirmationView.getHeight();

        // Face Confirmation view purpose
        workingBitmap = Bitmap.createScaledBitmap(storedBitmap,
                imageViewSurfaceWidth, imageViewSurfaceHeight, false);
//        mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        mutableBitmap = storedBitmap.copy(Bitmap.Config.ARGB_8888, true);

        objFace.normalizeCoordinates(imageViewSurfaceWidth, imageViewSurfaceHeight);

        // Set Bitmap Success
        if(setBitmapResult){
//            Log.e(TAG, "onCreate: SetBitmap objFace "+"Success" );

            // Face Data Exist
            if(faceDatas != null){
//                Log.e(TAG, "onCreate: faceDatas "+faceDatas.length );
                rects = new Rect[faceDatas.length];

                for (int i = 0; i < faceDatas.length; i++) {
                    Rect rect = faceDatas[i].rect;
                    rects[i] = rect;

                    int matchRate = faceDatas[i].getRecognitionConfidence();

                    float pixelDensity = getResources().getDisplayMetrics().density;

//                    Identify or new record
                    if (identifyPerson) {
                        String selectedPersonId = Integer.toString(faceDatas[i].getPersonId());
                        Iterator<HashMap.Entry<String, String>> iter = clientList.entrySet().iterator();
                        // Default name is the person is unknown
                        selectedPersonName = "Not Identified";
                        while (iter.hasNext()) {
                            Log.e(TAG, "In");
                            HashMap.Entry<String, String> entry = iter.next();
                            if (entry.getValue().equals(selectedPersonId)) {
                                selectedPersonName = entry.getKey();
                            }
                        }

                        Toast.makeText(getApplicationContext(), selectedPersonName, Toast.LENGTH_SHORT).show();

//                        Draw Info on Image
                        Tools.drawInfo(rect, mutableBitmap, pixelDensity, selectedPersonName);

                        showDetailUser(selectedPersonName);

                    } else {
                        // Not Identifiying, do new record.
//                        Draw Info on Image
                        Tools.drawRectFace(rect, mutableBitmap, pixelDensity);

                        Log.e(TAG, "onCreate: PersonId "+faceDatas[i].getPersonId() );

                        // Check Detected existing face
                        if(faceDatas[i].getPersonId() < 0){

                            arrayPossition = i;

                        } else {

                            showPersonInfo(matchRate);

                        }

//                        TODO: asign selectedPersonName to search
                        // Applied Image that came in to the view.
                        // Face only
//                        confirmationView.setImageBitmap(storedBitmap);

                        // Face and Rect
                        confirmationView.setImageBitmap(mutableBitmap);

                    } // end if-else mode Identify {True or False}

                } // end for count ic_faces

            } else {

                Log.e(TAG, "onCreate: faceDatas "+"Null" );
                Toast.makeText(ImageConfirmation.this, "No Face Detected", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                setResult(RESULT_CANCELED, resultIntent);
                ImageConfirmation.this.finish();
            }

        } else {

            Log.e(TAG, "onCreate: SetBitmap objFace"+"Failed" );

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_confirmation, menu);
        return true;
    }

    public void showDetailUser(String selectedPersonName) {

        AllCommonsRepository ibuRepository = org.ei.opensrp.Context.getInstance().allCommonsRepositoryobjects("ec_kartu_ibu");
        CommonPersonObject kiclient = ibuRepository.findByCaseID(selectedPersonName);

//        Log.e(TAG, "onCreate: IbuRepo "+ibuRepository );
//        Log.e(TAG, "onCreate: Id "+selectedPersonName );
//        Log.e(TAG, "onCreate: KiClient "+kiclient.getCaseId() );

        Class<?> origin_class = this.getClass();

//        Log.e(TAG, "showDetailUser: "+ origin_class.getSimpleName() );
//        Log.e(TAG, "showDetailUser: "+ str_origin_class);

        if(str_origin_class.equals(NativeKISmartRegisterFragment.class.getSimpleName())){
            origin_class = NativeKISmartRegisterActivity.class;
        } else if(str_origin_class.equals(NativeKBSmartRegisterFragment.class.getSimpleName())){
            origin_class = NativeKBSmartRegisterActivity.class;
        } else if(str_origin_class.equals(NativeKIAnakSmartRegisterFragment.class.getSimpleName())){
            origin_class = NativeKIAnakSmartRegisterActivity.class;
        } else if(str_origin_class.equals(NativeKIANCSmartRegisterFragment.class.getSimpleName())){
            origin_class = NativeKIANCSmartRegisterActivity.class;
        } else if(str_origin_class.equals(NativeKIPNCSmartRegisterFragment.class.getSimpleName())){
            origin_class = NativeKIPNCSmartRegisterActivity.class;
        }

        Intent intent = new Intent(ImageConfirmation.this, origin_class);
        intent.putExtra("org.ei.opensrp.indonesia.face.face_mode", true);
        intent.putExtra("org.ei.opensrp.indonesia.face.base_id", selectedPersonName);

        startActivity(intent);

    }

    private void showPersonInfo(int recognitionConfidence) {
        Log.e(TAG, "showPersonInfo: Similar face found " +
                Integer.toString(recognitionConfidence));

        AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setTitle("Are you Sure?");
        builder.setMessage("Similar Face Found! : Confidence "+recognitionConfidence);
        builder.setNegativeButton("CANCEL", null);
        builder.show();
        confirmButton.setVisibility(View.INVISIBLE);

    }

    /**
     *
     */
    private void buttonJob() {
        // If approved then save the image and close.
        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (!identifyPerson) {

                    Log.e(TAG, "onClick: class origin "+str_origin_class );

                    Tools.saveAndClose(getApplicationContext(), entityId, updated, objFace, arrayPossition, storedBitmap, str_origin_class);

                } else {
                    Log.e(TAG, "onClick: not identify ");
//                    SmartRegisterQueryBuilder sqb = new SmartRegisterQueryBuilder();
//                    Cursor cursor = getApplicationContext().
                    // TODO: detect origin class
                    KIDetailActivity.kiclient = (CommonPersonObjectClient) arg0.getTag();
                    Log.e(TAG, "onClick: " + KIDetailActivity.kiclient);
//                    Intent intent = new Intent(ImageConfirmation.this,KIDetailActivity.class);
                    Log.e(TAG, "onClick: " + selectedPersonName);
//                    startActivity(intent);
                }
            }

        });

        confirmButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    confirmButton.setImageResource(R.drawable.ic_confirm_highlighted_24dp);
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    confirmButton.setImageResource(R.drawable.ic_confirm_white_24dp);
                }

                return false;
            }
        });

        // Trash the image and return back to the camera preview.
        trashButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent resultIntent = new Intent();
                setResult(RESULT_CANCELED, resultIntent);
                ImageConfirmation.this.finish();
            }

        });

        trashButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    trashButton.setImageResource(R.drawable.ic_trash_delete_green);
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    trashButton.setImageResource(R.drawable.ic_trash_delete);
                }

                return false;
            }
        });

    }

    /*
    Save File and DB
     */
    private void saveAndClose(String entityId) {

        Log.e(TAG, "saveAndClose: updated "+ updated );

        faceVector = objFace.serializeRecogntionAlbum();

        Log.e(TAG, "saveAndClose: " + Arrays.toString(faceVector));

        int result = objFace.addPerson(arrayPossition);
        clientList.put(entityId, Integer.toString(result));

        byte[] albumBuffer = SmartShutterActivity.faceProc.serializeRecogntionAlbum();

        SmartShutterActivity.faceProc.resetAlbum();

//        Tools.WritePictureToFile(ImageConfirmation.this, storedBitmap, entityId, albumBuffer, updated);
        // TODO : change album buffer to String[]
//        Tools.WritePictureToFile(storedBitmap, entityId, albumBuffer, updated);

        ImageConfirmation.this.finish();

        Intent resultIntent = new Intent(this, KIDetailActivity.class);
        setResult(RESULT_OK, resultIntent);
        startActivityForResult(resultIntent, 1);

        Log.e(TAG, "saveAndClose: "+"end" );
    }

    public void saveHash(HashMap<String, String> hashMap, android.content.Context context) {
        SharedPreferences settings = context.getSharedPreferences(FaceConstants.HASH_NAME, 0);

        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        Log.e(TAG, "Hash Save Size = " + hashMap.size());
        for (String s : hashMap.keySet()) {
            editor.putString(s, hashMap.get(s));
        }
        editor.apply();
    }

    public void saveAlbum() {
        byte[] albumBuffer = SmartShutterActivity.faceProc.serializeRecogntionAlbum();
//		saveCloud(albumBuffer);
        Log.e(TAG, "Size of byte Array =" + albumBuffer.length);
        SharedPreferences settings = getSharedPreferences(FaceConstants.ALBUM_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(FaceConstants.ALBUM_ARRAY, Arrays.toString(albumBuffer));
        editor.apply();
    }

    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
//        DatabaseReference ref = FirebaseDatabase.getInstance()
//                .getReference(AllConstantsINA.FIREBASE_OPENSRP_INA)
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .child(mRestaurant.getPushId())
//                .child("imageUrl");
//        ref.setValue(imageEncoded);
    }


}