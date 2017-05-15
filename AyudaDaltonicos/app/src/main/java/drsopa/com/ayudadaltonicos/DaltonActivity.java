package drsopa.com.ayudadaltonicos;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.io.File;

import static android.R.attr.data;

public class DaltonActivity extends Activity {

	private EditText et1;
	private static int RESULT_LOAD_IMAGE = 1;
	TextView touchedXY, invertedXY, imgSize, colorRGB;
	ImageView imgSource2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dalton);

		touchedXY = (TextView)findViewById(R.id.xy);
		invertedXY = (TextView)findViewById(R.id.invertedxy);
		imgSize = (TextView)findViewById(R.id.size);
		colorRGB = (TextView)findViewById(R.id.colorrgb);
		imgSource2 = (ImageView)findViewById(R.id.source2);
		et1= (EditText) findViewById(R.id.text);
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
			int x = (int) eventXY[0];
			int y = (int)eventXY[1];

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
			int rval = Color.red(touchedRGB);
			int gval = Color.green(touchedRGB);
			int bval = Color.blue(touchedRGB);



			if(bval >= 100 && gval<120  && rval<100){
				colorRGB.setText("Azul" + "R:"+ rval + "V:" + gval + "A:"+bval);}
				else if (gval >= 100 && bval<50 && rval<100) {
					colorRGB.setText("Verde" + "R:"+ rval + "V:" + gval + "A:"+bval);}
					else if (rval >= 125 && gval<70 && bval<70) {
						colorRGB.setText("Rojo" + "R:" + rval + "V:" + gval + "A:" + bval);}
			else if (rval >= 60 && rval <=240 && gval>0 && gval<255 && bval>0 && bval<255) {
				colorRGB.setText("Marrón" + "R:" + rval + "V:" + gval + "A:" + bval);}
            			else {
				colorRGB.setText("Sin definir" + "R:" + rval + "V:" + gval + "A:" + bval);
							}

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
				if (requestCode == RESULT_LOAD_IMAGE) {

			//Get ImageURi and load with help of picasso
			//Uri selectedImageURI = data.getData();

			Picasso.with(DaltonActivity.this).load(data.getData()).noPlaceholder().centerCrop().fit()
					.into((ImageView) findViewById(R.id.source2));
		}}

	public void tomarFoto(View v) {
		Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File foto = new File(getExternalFilesDir(null), et1.getText().toString());
		intento1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(foto));
		startActivity(intento1);

	}




	}

