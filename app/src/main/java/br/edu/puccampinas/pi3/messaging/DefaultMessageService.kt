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
            Intent().also { intent ->
                intent.setAction("br.edu.puccampinas.pi3.RecieverEmergencia")
                intent.putExtra("nome", msgData["nome"])
                intent.putExtra("telefone", msgData["telefone"])
                intent.putExtra("Foto1", msgData["Foto1"])
                intent.putExtra("Foto2", msgData["Foto2"])
                intent.putExtra("Foto3", msgData["Foto3"])
                intent.putExtra("dataHora", msgData["dataHora"])
                intent.putExtra("emergencia", msgData["emergencia"])
                sendBroadcast(intent)
            }

            db.collection("dentistas").whereEqualTo("uid", Firebase.auth.currentUser?.uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.getBoolean("status") == true) {
                            showNotification(
                                "Pressione para ver detalhes",
                                msgData["nome"].toString(),
                                msgData["telefone"].toString(),
                                msgData["Foto1"].toString(),
                                msgData["Foto2"].toString(),
                                msgData["Foto3"].toString(),
                                msgData["dataHora"].toString(),
                                msgData["emergencia"].toString()
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
            Intent().also { aceite ->
                aceite.setAction("br.edu.puccampinas.pi3.RecieverAceite")
                aceite.putExtra("status", "rejeitada")
                sendBroadcast(aceite)
            }
        }


        if(msgData["text"] == "nova emergencia")
        if(msgData["text"] == "rejeitada" || msgData["text"] == "aceita") {
            print("CHEGOU MENSAGEM AQUI IRMA SADF");
        }
        /*Intent().also { intent ->
            intent.setAction("br.edu.puccampinas.pi3.RecieverEmergencia")
            intent.putExtra("nome", msgData["nome"])
            intent.putExtra("telefone", msgData["telefone"])
            intent.putExtra("Foto1", msgData["Foto1"])
            intent.putExtra("Foto2", msgData["Foto2"])
            intent.putExtra("Foto3", msgData["Foto3"])
            intent.putExtra("dataHora", msgData["dataHora"])
            intent.putExtra("emergencia", msgData["emergencia"])
            sendBroadcast(intent)
        }

        showNotification("Pressione para ver detalhes", msgData["nome"].toString(),
            msgData["telefone"].toString(), msgData["Foto1"].toString(), msgData["Foto2"].toString(),
            msgData["Foto3"].toString(), msgData["dataHora"].toString(), msgData["emergencia"].toString())*/

        if (msgData["text"] == "localizacao") {
            Intent().also { loc ->
                loc.setAction("br.edu.puccampinas.pi3.RecieverLocalizacao")
                loc.putExtra("lat", msgData["lat"])
                loc.putExtra("long", msgData["long"])
                sendBroadcast(loc)
            }
        }

    }

    /***
     * Aqui a gente vai adicionar um novo fcmToken no banco de dados através de um update
     */
    override fun onNewToken(token: String) {
    }

    /***
     * Este método cria uma Intent
     * para a activity Main, vinculada a notificação.
     * ou seja, quando acontecer a notificação, se o usuário clicar,
     * abrirá a activity Main.
     * Trabalhar isso para que dependendo da mensagem,
     * você poderá abrir uma ou outra activity
     * ou enviar um parametro na Intent para tratar qual fragment abrir.(desafio para vc fazer)
     */

    private fun showNotification(messageBody: String, nome: String, telefone: String, Foto1: String,
                                 Foto2: String, Foto3: String, dataHora: String, emergencia: String) {
        val intent = Intent(this, EmergenciaActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("nome", nome)
        intent.putExtra("telefone", telefone)
        intent.putExtra("Foto1", Foto1)
        intent.putExtra("Foto2", Foto2)
        intent.putExtra("Foto3", Foto3)
        intent.putExtra("dataHora", dataHora)
        intent.putExtra("emergencia", emergencia)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
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