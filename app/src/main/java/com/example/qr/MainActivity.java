package com.example.qr;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = editText.getText().toString().trim();
                if (!url.isEmpty()) {
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;
                    QRCodeWriter qrCodeWriter = new QRCodeWriter();
                    try {
                        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, smallerDimension, smallerDimension);
                        int matrixWidth = bitMatrix.getWidth();
                        int matrixHeight = bitMatrix.getHeight();
                        Bitmap bitmap = Bitmap.createBitmap(matrixWidth, matrixHeight, Bitmap.Config.RGB_565);
                        for (int x = 0; x < matrixWidth; x++) {
                            for (int y = 0; y < matrixHeight; y++) {
                                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.black) :
                                        getResources().getColor(R.color.white));
                            }
                        }
                        imageView.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Log.e("QRCode", "Error en la creación del código QR: " + e);
                    }
                } else {
                    editText.setError("Ingrese una URL válida");
                }
            }
        });
    }

}