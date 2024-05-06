package es.leyanxia.miniamazon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.google.gson.reflect.TypeToken
import es.leyanxia.miniamazon.adminView.MainActivityAdmin
import es.leyanxia.miniamazon.api.API
import es.leyanxia.miniamazon.modelos.Auth
import es.leyanxia.miniamazon.modelos.Usuario
import es.leyanxia.miniamazon.usuarioView.MainActivityUsuario
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class LoginActivity : AppCompatActivity() {
    lateinit var btnEntrar : Button
    lateinit var btnRegistrar: Button
    lateinit var editEmail: EditText
    lateinit var editPwd: EditText
    lateinit var registrarActividad: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)
        btnEntrar=findViewById(R.id.btnEntrar)
        btnRegistrar=findViewById(R.id.btnRegistrar)
        editEmail=findViewById(R.id.editEmail)
        editPwd=findViewById(R.id.editPwd)

        btnEntrar.setOnClickListener {
            var usuario= Usuario()
            usuario.email=editEmail.text.toString()
            usuario.pwd=editPwd.text.toString()
            lifecycleScope.launch {
                val res = API.call(API.URL_LOGIN,"GET",usuario,"")
                if(res?.codigo == HttpURLConnection.HTTP_OK){
                    val listType= object : TypeToken<Auth>(){}.type
                    val auth: Auth= API.gson.fromJson(res.contenido,listType)
                    val prefs = PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
                    val editor =prefs.edit()
                    editor.putString(getString(R.string.auth),auth.auth)
                    editor.apply()
                    lanzarActividad()

                }else{
                    AlertDialog.Builder(this@LoginActivity)
                        .setTitle("Error")
                        .setMessage(res?.contenido)
                        .setPositiveButton("Cerrar",null)
                        .create().show()
                }
            }

        }

        btnRegistrar.setOnClickListener {
            val intent= Intent(this,RegistrarActivity::class.java)
            registrarActividad.launch(intent)
        }
        registrarActividad=registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            this::resultado
        )
    }

    private fun resultado(res: ActivityResult?) {
        if(res?.resultCode == RESULT_OK){
            lanzarActividad()
        }

    }

    override fun onResume() {
        super.onResume()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val auth = prefs.getString(getString(R.string.auth), "").toString()
        if(auth!=""){
            lanzarActividad()
        }
    }

    fun lanzarActividad(){

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val auth = prefs.getString(getString(R.string.auth), "").toString()
        if(auth!=""){
                lifecycleScope.launch {
                    val res = API.call(API.URL_CUENTA,"GET",null,auth)
                    if(res?.codigo == HttpURLConnection.HTTP_OK){
                        val listType= object : TypeToken<Usuario>(){}.type
                        val usuario: Usuario= API.gson.fromJson(res.contenido,listType)
                        if(usuario.admin==1){
                            val intent = Intent(this@LoginActivity,MainActivityAdmin::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }else{
                            val intent = Intent(this@LoginActivity,MainActivityUsuario::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }else{
                        AlertDialog.Builder(this@LoginActivity)
                            .setTitle("Error")
                            .setMessage(res?.contenido)
                            .setPositiveButton("Cerrar",null)
                            .create().show()
                    }
                }

        }

    }
}