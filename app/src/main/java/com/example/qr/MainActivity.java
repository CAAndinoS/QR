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
                // Obtiene la URL ingresada por el usuario desde el campo de texto y elimina los espacios en blanco iniciales y finales
                String url = editText.getText().toString().trim();
                // Verifica si la URL no está vacía
                if (!url.isEmpty()) {
                    // Obtiene la instancia del servicio de ventana y la pantalla por defecto
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    // Obtiene el tamaño de la pantalla
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    // Calcula la dimensión más pequeña y la reduce en un 25%
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;
                    // Crea un objeto QRCodeWriter y codifica la URL en un objeto BitMatrix
                    QRCodeWriter qrCodeWriter = new QRCodeWriter();
                    try {
                        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, smallerDimension, smallerDimension);
                        // Obtiene el ancho y la altura de la matriz BitMatrix
                        int matrixWidth = bitMatrix.getWidth();
                        int matrixHeight = bitMatrix.getHeight();
                        // Crea un objeto Bitmap y recorre la matriz BitMatrix para establecer los píxeles en negro o blanco
                        Bitmap bitmap = Bitmap.createBitmap(matrixWidth, matrixHeight, Bitmap.Config.RGB_565);
                        for (int x = 0; x < matrixWidth; x++) {
                            for (int y = 0; y < matrixHeight; y++) {
                                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.black) :
                                        getResources().getColor(R.color.white));
                            }
                        }
                        // Establece el Bitmap creado en un ImageView para mostrar el código QR generado
                        imageView.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        // Captura una excepción si ocurre un error al crear el código QR
                        Log.e("QRCode", "Error en la creación del código QR: " + e);
                    }
                } else {
                    // Establece un mensaje de error en el campo de texto si la URL está vacía
                    editText.setError("Ingrese una URL válida");
                }
            }
        });
    }

}