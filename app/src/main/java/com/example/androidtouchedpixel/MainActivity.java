package com.example.androidtouchedpixel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;

public class MainActivity extends Activity {

	private EditText et1;
	private static int RESULT_LOAD_IMAGE = 1;
	private ImageView imagen;
	TextView touchedXY, invertedXY, imgSize, colorRGB;
	ImageView imgSource1, imgSource2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        touchedXY = (TextView)findViewById(R.id.xy);
        invertedXY = (TextView)findViewById(R.id.invertedxy);
        imgSize = (TextView)findViewById(R.id.size);
        colorRGB = (TextView)findViewById(R.id.colorrgb);
    	imgSource1 = (ImageView)findViewById(R.id.source1);
    	imgSource2 = (ImageView)findViewById(R.id.source2);
		et1= (EditText) findViewById(R.id.text);
    	imgSource1.setOnTouchListener(imgSourceOnTouchListener);
    	imgSource2.setOnTouchListener(imgSourceOnTouchListener);
    	
    }
    
    OnTouchListener imgSourceOnTouchListener
    = new OnTouchListener(){

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			
			float eventX = event.getX();
			float eventY = event.getY();
			float[] eventXY = new float[] {eventX, eventY};
			
			Matrix invertMatrix = new Matrix();
			((ImageView)view).getImageMatrix().invert(invertMatrix);
			
			invertMatrix.mapPoints(eventXY);
			int x = Integer.valueOf((int)eventXY[0]);
			int y = Integer.valueOf((int)eventXY[1]);
			
			touchedXY.setText(
					"touched position: "
					+ String.valueOf(eventX) + " / " 
					+ String.valueOf(eventY));
			invertedXY.setText(
					"touched position: "
					+ String.valueOf(x) + " / " 
					+ String.valueOf(y));

			Drawable imgDrawable = ((ImageView)view).getDrawable();
			Bitmap bitmap = ((BitmapDrawable)imgDrawable).getBitmap();
			
			imgSize.setText(
					"drawable size: "
					+ String.valueOf(bitmap.getWidth()) + " / " 
					+ String.valueOf(bitmap.getHeight()));
			
			//Limit x, y range within bitmap
			if(x < 0){
				x = 0;
			}else if(x > bitmap.getWidth()-1){
				x = bitmap.getWidth()-1;
			}
			
			if(y < 0){
				y = 0;
			}else if(y > bitmap.getHeight()-1){
				y = bitmap.getHeight()-1;
			}

			int touchedRGB = bitmap.getPixel(x, y);
			
			colorRGB.setText("touched color: " + "#" + Integer.toHexString(touchedRGB));
			colorRGB.setTextColor(touchedRGB);
			
			return true;
		}};
	public void Abre(View view) {


		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
		}
		if (requestCode == RESULT_LOAD_IMAGE) {

			//Get ImageURi and load with help of picasso
			//Uri selectedImageURI = data.getData();

			Picasso.with(MainActivity.this).load(data.getData()).noPlaceholder().centerCrop().fit()
					.into((ImageView) findViewById(R.id.source2));
		}}
	public void tomarFoto(View v) {
		Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File foto = new File(getExternalFilesDir(null), et1.getText().toString());
		intento1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(foto));
		startActivity(intento1);
          }
}
