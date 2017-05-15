package com.example.androidtouchedpixel;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
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
import java.io.File;
import static java.lang.reflect.Array.get;

public class MainActivity extends Activity {

	private EditText et1;
	private static int RESULT_LOAD_IMAGE = 1;
	TextView touchedXY, invertedXY, imgSize, colorRGB;
	ImageView imgSource2;
    String cadena3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		touchedXY = (TextView)findViewById(R.id.xy);
		invertedXY = (TextView)findViewById(R.id.invertedxy);
		imgSize = (TextView)findViewById(R.id.size);
		colorRGB = (TextView)findViewById(R.id.colorrgb);
		imgSource2 = (ImageView)findViewById(R.id.source2);
		et1= (EditText) findViewById(R.id.text);
				imgSource2.setOnTouchListener(imgSourceOnTouchListener);

	}

	public OnTouchListener imgSourceOnTouchListener
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
			int y = (int) eventXY[1];


			Drawable imgDrawable = ((ImageView)view).getDrawable();
			Bitmap bitmap = ((BitmapDrawable)imgDrawable).getBitmap();


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
		    Resources res = getResources();
            String[] Tipos_Dos = res.getStringArray(R.array.TiposDos);


            Integer rcompara;
            Integer gcompara;
            Integer bcompara;
            Integer difminima;
            int j=0;
            int[] Num_1 = res.getIntArray(R.array.Num1);
            int[] Num_2 = res.getIntArray(R.array.Num2);
            int[] Num_3 = res.getIntArray(R.array.Num3);
            int Temporal [] = new int[256];
            int diferencia;
            int indice = 0;

            for (int z = 0; z < Num_1.length; z++) {

                rcompara = (Integer) get(Num_1, z);
                gcompara = (Integer) get(Num_2, z);
                bcompara = (Integer) get(Num_3, z);
                diferencia =  ((rval - rcompara)*(rval - rcompara) + (gval - gcompara)*(gval - gcompara) + (bval - bcompara)*(bval - bcompara))/3;
                Temporal [z] = diferencia;

            }

            difminima=Temporal[0];
            for (int aTemporal : Temporal) {
                if (difminima > aTemporal) {
                    difminima = aTemporal;
                }
            }


            for (int aNum_1 : Num_1) {
                if (difminima == Temporal[j]) {
                    indice = j;
                } else {
                    j = j + 1;
                }
            }



            cadena3 = (String) get(Tipos_Dos, indice);
            colorRGB.setText("Color: " + cadena3);
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
		if (requestCode == RESULT_LOAD_IMAGE) {

			//Get ImageURi and load with help of picasso
			//Uri selectedImageURI = data.getData();

			Picasso.with(MainActivity.this).load(data.getData()).noPlaceholder().centerCrop().fit()
					.into((ImageView) findViewById(R.id.source2));
		}
		}
    }



	public void tomarFoto(View v) {
		Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File foto = new File(getExternalFilesDir(null), et1.getText().toString());
		intento1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(foto));
		startActivity(intento1);



	}
}
