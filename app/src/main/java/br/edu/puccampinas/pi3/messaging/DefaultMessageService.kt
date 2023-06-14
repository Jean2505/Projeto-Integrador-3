package br.edu.puccampinas.pi3.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import br.edu.puccampinas.pi3.Emergencia
import br.edu.puccampinas.pi3.EmergenciaActivity
import br.edu.puccampinas.pi3.MapsActivity
import br.edu.puccampinas.pi3.PerfilActivity
import br.edu.puccampinas.pi3.R
import com.google.common.collect.Maps
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class DefaultMessageService : FirebaseMessagingService() {
    private var db = FirebaseFirestore.getInstance()
    public var ListaEmerg: List<Emergencia> = emptyList()

    /***
     * Evento disparado automaticamente
     * quando o FCM envia uma mensagem. Lembrando que este serviço
     * "DefaultMessageService" está registrado no Manifesto como um serviço.
     */

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        println("mensagem recebida")
        Firebase.auth.currentUser?.uid
        val msgData = remoteMessage.data

        if (msgData["text"] == "nova emergencia") {
            val nome = msgData["nome"]!!
            val telefone = msgData["telefone"]!!
            val foto1 = msgData["Foto1"]!!
            val foto2 = msgData["Foto2"]!!
            val foto3 = msgData["Foto3"]!!
            val dataHora = msgData["dataHora"]!!
            val emergencia = msgData["emergencia"]!!
            Intent().also { intent ->
                intent.setAction("br.edu.puccampinas.pi3.RecieverEmergencia")
                intent.putExtra("nome", nome)
                intent.putExtra("telefone", telefone)
                intent.putExtra("Foto1", foto1)
                intent.putExtra("Foto2", foto2)
                intent.putExtra("Foto3", foto3)
                intent.putExtra("dataHora", dataHora)
                intent.putExtra("emergencia", emergencia)
                sendBroadcast(intent)
            }

            db.collection("dentistas").whereEqualTo("uid", Firebase.auth.currentUser?.uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.getBoolean("status") == true) {
                            showNotification(
                                "Pressione para ver detalhes",
                                nome,
                                telefone,
                                foto1,
                                foto2,
                                foto3,
                                dataHora,
                                emergencia
                            )
                        }
                    }
                }
        }
        else if (msgData["text"] == "aceita") {
            Intent().also { aceite ->
                aceite.setAction("br.edu.puccampinas.pi3.RecieverAceite")
                aceite.putExtra("status", "aceita")
                sendBroadcast(aceite)
            }
        }
        else if (msgData["text"] == "rejeitada") {
            println("rejeitou")
            Intent().also { aceite ->
                aceite.setAction("br.edu.puccampinas.pi3.RecieverAceite")
                aceite.putExtra("status", "rejeitada")
                sendBroadcast(aceite)
            }
        }

        if (msgData["text"] == "localizacao") {
            Intent().also { loc ->
                loc.setAction("br.edu.puccampinas.pi3.RecieverLocalizacao")
                loc.putExtra("lat", msgData["lat"])
                loc.putExtra("long", msgData["long"])
                sendBroadcast(loc)
            }
        }

        if (msgData["text"] == "avaliacao") {
            println("chegou avaliação")
            println(msgData["aval"])
            println(msgData["coment"])
        }
    }

    /***
     * Aqui a gente vai adicionar um novo fcmToken no banco de dados através de um update
     */
    override fun onNewToken(token: String) {
    }

    private fun showNotification(messageBody: String, nome: String, telefone: String, Foto1: String,
                                 Foto2: String, Foto3: String, dataHora: String, emergencia: String) {
        val iNotif = Intent(this, EmergenciaActivity::class.java)
        iNotif.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        iNotif.putExtra("nome", nome)
        iNotif.putExtra("telefone", telefone)
        iNotif.putExtra("Foto1", Foto1)
        iNotif.putExtra("Foto2", Foto2)
        iNotif.putExtra("Foto3", Foto3)
        iNotif.putExtra("dataHora", dataHora)
        iNotif.putExtra("emergencia", emergencia)
        val pendingIntent = PendingIntent.getActivity(this,0,iNotif,PendingIntent.FLAG_IMMUTABLE)
        val channelId = "canal_padrao"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo2)
            .setContentTitle("Nova emergência!")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0, notificationBuilder.build())
    }
}