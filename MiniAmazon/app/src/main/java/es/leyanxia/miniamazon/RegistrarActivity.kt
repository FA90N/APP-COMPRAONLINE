package es.leyanxia.miniamazon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.google.gson.reflect.TypeToken
import es.leyanxia.miniamazon.adminView.MainActivityAdmin
import es.leyanxia.miniamazon.api.API
import es.leyanxia.miniamazon.modelos.Usuario
import es.leyanxia.miniamazon.usuarioView.MainActivityUsuario
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class RegistrarActivity : AppCompatActivity() {
    lateinit var txtNombre: EditText
    lateinit var txtTel: EditText
    lateinit var txtDireccion:EditText
    lateinit var txtEmail:EditText
    lateinit var txtPwd:EditText
    lateinit var btnEnviar:Button
    lateinit var btnLogin:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)
        txtNombre=findViewById(R.id.txtNombre)
        txtTel=findViewById(R.id.txtMovil)
        txtDireccion=findViewById(R.id.txtDireccion)
        txtEmail=findViewById(R.id.txtEmail)
        txtPwd=findViewById(R.id.txtPwd)
        btnEnviar=findViewById(R.id.btnEnviar)
        btnLogin=findViewById(R.id.btnLogin)

        btnEnviar.setOnClickListener {
            var usuario = Usuario()
            usuario.nombre = txtNombre.text.toString()
            usuario.telefono = txtTel.text.toString()
            usuario.direccion = txtDireccion.text.toString()
            usuario.email = txtEmail.text.toString()
            usuario.pwd = txtPwd.text.toString()
            if (usuario.nombre == "" || usuario.email == "" || usuario.pwd == "") {
                AlertDialog.Builder(this@RegistrarActivity)
                    .setTitle("Error")
                    .setMessage("Nombre, Email y Contraseña es obligatorio")
                    .setPositiveButton("Cerrar", null)
                    .create().show()
            } else {
                lifecycleScope.launch {
                    val res = API.call(API.URL_CUENTA, "POST", usuario, "")
                    if (res?.codigo == HttpURLConnection.HTTP_OK) {
                        val listType = object : TypeToken<Usuario>(){}.type
                        val us: Usuario = API.gson.fromJson(res.contenido, listType)
                        val prefs = PreferenceManager.getDefaultSharedPreferences(this@RegistrarActivity)
                        val editor = prefs.edit()

                        editor.putString(getString(R.string.auth), us.authkey)
                        editor.apply()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        AlertDialog.Builder(this@RegistrarActivity)
                            .setTitle("Error")
                            .setMessage(res?.contenido)
                            .setPositiveButton("Cerrar", null)
                            .create().show()
                    }
                }

            }
        }

        btnLogin.setOnClickListener {
            finish()
        }
    }
}