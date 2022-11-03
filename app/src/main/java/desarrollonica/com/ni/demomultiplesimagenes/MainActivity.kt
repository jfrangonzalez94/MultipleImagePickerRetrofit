package desarrollonica.com.ni.demomultiplesimagenes

import android.R.attr.path
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var _Ubicacion: ArrayList<Uri>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Sv_Busqueda.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(_Texto: String?): Boolean {
                Log.i("RESULTADO", "TEXTCAMBIADO_1 -> ${_Texto.toString()}")
                return false
            }

            override fun onQueryTextChange(_Texto: String?): Boolean {
                Log.i("RESULTADO", "TEXTCAMBIADO_2 -> ${_Texto.toString()}")
                return false
            }
        })


        Btn_Buscar.setOnClickListener {
            FishBun.with(this)
                .setImageAdapter(GlideAdapter())
                .setAlbumSpanCount(2, 3)
                .setPickerCount(5) // Limite que puede escoger el usuario
                .setPickerSpanCount(4) // Cuantas Columnas
                .setIsUseDetailView(false) // No se puede ampliar imagen
                .setActionBarColor(
                    getColor(R.color.colorPrimary),
                    getColor(R.color.colorPrimary),
                    true
                )
                .setActionBarTitleColor(getColor(R.color.Blanco0)) // Color Titulo
                .setAllViewTitle("Todas tus Imágenes")
                .textOnImagesSelectionLimitReached("¡No se puede seleccionar más Imágenes!")
                .textOnNothingSelected("¡Minimo Subir 1 Imágen!")
                .startAlbumWithOnActivityResult(27)
        }


        Btn_Subir.setOnClickListener { Log.i("RESULTADO", "RESPUESTA DESPUES DE BUSCAR") }
    }

    //CONVERTIR DE URI A BITMAP
    fun ObtenerBitmapImagen(_Context: Activity, _Image: Uri): Bitmap {
        val _Bitmap: Bitmap =
            MediaStore.Images.Media.getBitmap(_Context.getContentResolver(), _Image)
        return _Bitmap
    }


    override fun onActivityResult(_CodigoSolic: Int, _CodigoResult: Int, _DatoImagen: Intent?) {
        super.onActivityResult(_CodigoSolic, _CodigoResult, _DatoImagen)

        when (_CodigoSolic) {
            FishBun.FISHBUN_REQUEST_CODE -> if (_CodigoResult === RESULT_OK) {
                _Ubicacion =
                    _DatoImagen?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
                Log.i("RESULTADO", "RESPUESTA_ -> $_Ubicacion")
                Log.i("RESULTADO", "RESPUESTA_INDIVIDUAL_ -> ${_Ubicacion.get(0)}")
                Log.i(
                    "RESULTADO",
                    "RESPUESTA_INDIVIDUAL_BINARY -> ${ObtenerBitmapImagen(this, _Ubicacion.get(0))}"
                )
                //Img_Resultado.setImageURI(_Ubicacion.get(0))
                Img_Resultado.setImageBitmap(ObtenerBitmapImagen(this, _Ubicacion.get(0)))
                Btn_Subir.isEnabled = true
            }
        }
    }


}