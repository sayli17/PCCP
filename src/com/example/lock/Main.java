package com.example.lock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends ActionBarActivity {

	final String[] option = new String[] { "Take from Camera",
    "Select from Gallery" };
	private static final int CAMERA_REQUEST = 1;
	private static final int PICK_FROM_GALLERY = 2;
	DataHandler db;// = new DataHandler(getBaseContext());
	int touchX,touchY,imageX=0,imageY=0;
	byte[] img;
	ImageView imageView = (ImageView) findViewById(R.id.imageView1);
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button bt = (Button) findViewById(R.id.button1);
		db = new DataHandler(getBaseContext());
		db.open();
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
				builder.setTitle("Select Options");
				builder.setItems(option, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	if(which == 0)
		            	    callCamera();
		                if(which == 1)
		            		callGallery();
		            }
		    	});
		  		AlertDialog alert = builder.create();
		  		alert.show();
			}
		});
		
		
		Button bt2 = (Button) findViewById(R.id.button2);
		bt2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				db.open();
				Cursor c = db.returnData();
	           	String appname="";
	          // 	int imageX = 0,imageY = 0;
	      /*      if(c.moveToFirst()){
	            	do{
	            		appname = c.getString(0);
	              		img = c.getBlob(1);
	              		imageX = c.getInt(2);
	              		imageY = c.getInt(3);
	            	}while(c.moveToNext());
	            }
	            ImageView imageView = (ImageView) findViewById(R.id.imageView1);
	            ByteArrayInputStream inputStream = new ByteArrayInputStream(img);
	            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
	            imageView.setImageBitmap(bitmap);
	            Toast.makeText(getBaseContext(),"Name " + appname+ "Image " + bitmap + "X: " + imageX + "Y: " + imageY,Toast.LENGTH_LONG).show();
				db.close();*/
			}
		});
	}
	
	public void callCamera() {
	  	Intent cameraIntent = new Intent(
	  	android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	  	startActivityForResult(cameraIntent, CAMERA_REQUEST);
	}

	  	 /**
	  	  * open gallery method
	  	  */

	public void callGallery() {
	  	Intent intent = new Intent();
	  	intent.setType("image/*");
	  	//  intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
	  	intent.setAction(Intent.ACTION_GET_CONTENT);
	  	startActivityForResult(
	  	Intent.createChooser(intent, "Complete action using"),PICK_FROM_GALLERY);
	}
	
	  	 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  	//	final Intent i = data;
	  	if (resultCode != RESULT_OK)
	  		return;
		switch (requestCode) {
	  		case CAMERA_REQUEST:
				Bundle extras = data.getExtras();
				if (extras != null) {
		  		    Bitmap bitMap = (Bitmap) extras.get("data");
		            ImageView iv = (ImageView) findViewById(R.id.imageView1);
					iv.setImageBitmap(bitMap);
	  		 	}
	  		    break;

	  		case PICK_FROM_GALLERY:  
	  		    Uri selectedImage = data.getData();
	  		    String picturePath = getPath(selectedImage);          
	            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
	            Toast.makeText(getBaseContext(),"Click on the Image",Toast.LENGTH_LONG).show();
	            imageView.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						int[] values = new int[2]; 
						v.getLocationOnScreen(values);
		                int touchX = (int) event.getRawX();
		    	        int touchY = (int) event.getRawY();
		    	          
		    	        int imageX = touchX - values[0]; 
		    	        int imageY = touchY - values[1];
		                  Log.d("X & Y",imageX+" "+imageY);
		                  
						return true;
					}
				}); 
	            db.open();
	            TextView e = (TextView) findViewById(R.id.textView1);
	            break;
	  	}
	}		 
	  	 	
	public String getPath(Uri uri) {
		String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);        
        cursor.close();
        return cursor.getString(columnIndex);		
	}
	  	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
