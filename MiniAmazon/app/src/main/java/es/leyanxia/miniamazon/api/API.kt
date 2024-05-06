package es.leyanxia.miniamazon.api

import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

object API {
    //Cambiar IP
    const val BASE_URL = "http://192.168.1.44/api-tienda"
    const val URL_LOGIN = BASE_URL + "/login.php"
    const val URL_CUENTA = BASE_URL + "/cuenta.php"
    const val URL_CATEGORIA = BASE_URL + "/categorias.php"
    const val URL_PRODUCTOS = BASE_URL + "/productos.php?idCategoria="
    const val URL_PRODUCTO = BASE_URL + "/productos.php?idProducto="
    const val URL_PEDIDOS = BASE_URL + "/pedidos.php"
    const val URL_PED = BASE_URL + "/pedidos.php?idPedido="
    const val URL_ENVIAR = BASE_URL + "/enviar.php"
    const val URL_FOTO = BASE_URL + "/fotos/"
    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

    suspend fun call(url: String, metodo: String, objetoPeticion: Any?, auth: String)
            : ResultadoAPI? {
        var res: ResultadoAPI? = null

        withContext(Dispatchers.IO) {
            try {
                val cnn = URL(url).openConnection() as HttpURLConnection
                cnn.requestMethod = metodo
                if (auth != "")
                    cnn.addRequestProperty("X-Auth", auth)
                if (objetoPeticion != null) {
                    val json = gson.toJson(objetoPeticion)
                    cnn.doOutput = true
                    OutputStreamWriter(cnn.outputStream, StandardCharsets.UTF_8).use { writer ->
                        writer.write(json)
                    }
                }

                if (cnn.responseCode == HttpURLConnection.HTTP_OK) {
                    InputStreamReader(cnn.inputStream, StandardCharsets.UTF_8).use { reader ->
                        val contenido = reader.readText()
                        res = ResultadoAPI(cnn.responseCode, contenido)
                    }
                } else {

                    InputStreamReader(cnn.errorStream, StandardCharsets.UTF_8).use { reader ->
                        val contenido = reader.readText()
                        res = ResultadoAPI(cnn.responseCode, contenido)
                    }

                }

            } catch (e: Exception) {
                Log.e("API", "Error llamando  ${metodo} $url", e)
            }

        }
        return res

    }

}
