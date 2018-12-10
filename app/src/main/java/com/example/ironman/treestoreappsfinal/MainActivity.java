package com.example.ironman.treestoreappsfinal;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Trace;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ironman.treestoreappsfinal.cart.Cart;
import com.example.ironman.treestoreappsfinal.decoration_ideas.DecorationActivity;
import com.example.ironman.treestoreappsfinal.product.ProductAdapter;
import com.example.ironman.treestoreappsfinal.product.ProductValueHolder;
import com.example.ironman.treestoreappsfinal.serach_place.NearbyPlantStore;
import com.example.ironman.treestoreappsfinal.suggestion.SuggestionAdapter;
import com.example.ironman.treestoreappsfinal.suggestion.SuggestionsValueHolder;
import com.example.ironman.treestoreappsfinal.suggestion.dummySuggestionValueHolder;
import com.example.ironman.treestoreappsfinal.user_management.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;


import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

//    private static final int INPUT_SIZE = 224;
//    private static final int IMAGE_MEAN = 128;
//    private static final float IMAGE_STD = 128.0f;
//    private Classifier classifier;
//
//    // private static final String INPUT_NAME = "Mul";
//    private static final String OUTPUT_NAME = "final_result";
//    private static final String INPUT_NAME = "input";
//////  private static final String INPUT_NAME = "image_tensor";
////
//////  private static final String OUTPUT_NAME = "MobilenetV1/Predictions/Softmax";
////  private static final String OUTPUT_NAME = "final_result";
//
//
//    private static final String MODEL_FILE = "file:///android_asset/graph.pb";
//    private static final String LABEL_FILE = "file:///android_asset/labels.txt";
//
//
//    private static final int REQUEST_PERMISSIONS = 8989;
//    private static final int CAMERA_REQUEST = 9090;
    RecyclerView suggestionsRyc;
    RecyclerView productsRyc;
    RecyclerView.LayoutManager horizontalLayoutManger;
    GridLayoutManager gridLayoutManager;

    Handler handler;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference suggestionsDatabase;
    DatabaseReference productsDatabase;

    ArrayList<SuggestionsValueHolder> suggestionsValueModelArrayList;
    ArrayList<ProductValueHolder> productValueModelArrayList;

    SuggestionAdapter suggestionAdapter;
    ProductAdapter productAdapter;

    TextView headerText;

    ProgressDialog progressDialog;
    int currentSuggestionPosition=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


//
//        classifier =
//                TensorFlowImageClassifier.create(
//                        getAssets(),
//                        MODEL_FILE,
//                        LABEL_FILE,
//                        INPUT_SIZE,
//                        IMAGE_MEAN,
//                        IMAGE_STD,
//                        INPUT_NAME,
//                        OUTPUT_NAME);

        handler=new Handler();
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        headerText=findViewById(R.id.header_text);

        suggestionsDatabase=firebaseDatabase.getReference().child("suggestions");
        productsDatabase=firebaseDatabase.getReference().child("products");

//        search_edit=findViewById(R.id.search);
        suggestionsRyc =findViewById(R.id.suggestions_ryc);
        productsRyc =findViewById(R.id.products_ryc);
        suggestionsRyc.setNestedScrollingEnabled(false);
        productsRyc.setNestedScrollingEnabled(false);

        suggestionsValueModelArrayList=new ArrayList<>();
        productValueModelArrayList=new ArrayList<>();

        suggestionAdapter=new SuggestionAdapter(this,suggestionsValueModelArrayList,100);
        productAdapter=new ProductAdapter(this,productValueModelArrayList);

        gridLayoutManager=new GridLayoutManager(this,2);
        horizontalLayoutManger =new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        suggestionsRyc.setLayoutManager(horizontalLayoutManger);
        suggestionsRyc.stopNestedScroll();
        productsRyc.setLayoutManager(gridLayoutManager);

        suggestionsRyc.setAdapter(suggestionAdapter);
        productsRyc.setAdapter(productAdapter);


        if(!checkInternet()){
            Toast.makeText(this,"Internet is not connected",Toast.LENGTH_LONG).show();
        }

        getProductsData();
        getSuggestionData();

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                suggestionsRyc.smoothScrollToPosition(getPosition());
                handler.postDelayed(this,5000);
            }
        };
        handler.post(runnable);

        if(user!=null){
            String email=user.getEmail();
            int userEmailSize=email.length();
            if(userEmailSize>10){
                email=email.substring(0,9);
            }
            headerText.setText(email);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentSuggestionPosition=0;
    }

    int getPosition(){
        int totalSUggestions= suggestionAdapter.getItemCount()-1;

        if(totalSUggestions>currentSuggestionPosition && totalSUggestions!=0){
            currentSuggestionPosition++;
        }
        else {
            currentSuggestionPosition=0;
        }
        return currentSuggestionPosition;
    }


    void getProductsData(){
        productsDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                productValueModelArrayList.add(dataSnapshot.getValue(ProductValueHolder.class));
                productAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
    void getSuggestionData(){
        suggestionsDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                suggestionsValueModelArrayList.add(new SuggestionsValueHolder(
                        dataSnapshot.getValue(dummySuggestionValueHolder.class).getText(),
                        dataSnapshot.getValue(dummySuggestionValueHolder.class).getImageUrl(),
                        dataSnapshot.getValue(dummySuggestionValueHolder.class).getHeader(),
                        dataSnapshot.getKey()
                ));

//                suggestionsValueModelArrayList.add(dataSnapshot.getValue(SuggestionsValueHolder.class));
                suggestionAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void goToSearch(View view) {
        startActivity(new Intent(this,SearchActivity.class));
    }


    public void goToCart(View view) {
        if(checkIfSigndIn()){
            startActivity(new Intent(this, Cart.class));
        }else {
            Toast.makeText(this,"Create a account first",Toast.LENGTH_LONG).show();
            goToSignIn();
        }
    }

    public void goToAccount(View view) {
        if(checkIfSigndIn()){
            startActivity(new Intent(this,Account.class));
        }else {
            goToSignIn();
        }
    }

    boolean checkIfSigndIn(){
        FirebaseAuth auth=FirebaseAuth.getInstance();
        return auth.getCurrentUser() != null;
    }

    void goToSignIn(){
        Toast.makeText(this,"Please Login to buy plants",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,Login.class));
    }

    public void findPlantStore(View view) {
        startActivity(new Intent(this,NearbyPlantStore.class));
    }

    boolean checkInternet(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        return connected;
    }

    public void recog(View view) {
//        if(checkPermission()){
//            takePhoto();
//        }else {
//            reqPermission();
//        }
    }
    boolean checkPermission(){
        return  ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    void reqPermission(){
        String p[]={Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        ActivityCompat.requestPermissions(this, p, REQUEST_PERMISSIONS);
    }

    void takePhoto(){
        Intent cameraIntent = new
                Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            runRecog(photo);
//        }
    }

    void runRecog(final Bitmap croppedBitmap){
        progressDialog.show();
        Runnable runnable= new Runnable() {
            @Override
            public void run() {

//                final List<Classifier.Recognition> results = classifier.recognizeImage(
//                        Bitmap.createScaledBitmap(croppedBitmap, INPUT_SIZE, INPUT_SIZE, false));
//                Intent imageSearch=new Intent(MainActivity.this,SearchActivity.class);
//                imageSearch.putExtra("search_text",results.get(0).getTitle());
//                progressDialog.dismiss();
//                startActivity(imageSearch);
            }
        };

        AsyncTask.execute(runnable);
    }

    public void goToIdeas(View view) {
        startActivity(new Intent(this,DecorationActivity.class));
    }
}
