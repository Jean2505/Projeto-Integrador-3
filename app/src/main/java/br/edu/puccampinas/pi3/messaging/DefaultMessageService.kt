package br.edu.puccampinas.pi3.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import br.edu.puccampinas.pi3.Emergencia
import br.edu.puccampinas.pi3.EmergenciaActivity
import br.edu.puccampinas.pi3.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class DefaultMessageService : FirebaseMessagingService() {

    public var ListaEmerg: List<Emergencia> = emptyList()

    /***
     * Evento disparado automaticamente
     * quando o FCM envia uma mensagem. Lembrando que este serviço
     * "DefaultMessageService" está registrado no Manifesto como um serviço.
     */

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val msgData = remoteMessage.data

        Intent().also { intent ->
            intent.setAction("br.edu.puccampinas.pi3.RecieverEmergencia")
            intent.putExtra("nome", msgData["nome"])
            intent.putExtra("telefone", msgData["telefone"])
            intent.putExtra("Foto1", msgData["Foto1"])
            intent.putExtra("Foto2", msgData["Foto2"])
            intent.putExtra("Foto3", msgData["Foto3"])
            sendBroadcast(intent)
        }

        showNotification("Pressione para ver detalhes", msgData["nome"].toString(),
            msgData["telefone"].toString(), msgData["Foto1"].toString(), msgData["Foto2"].toString(),
            msgData["Foto3"].toString())
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
                                    Foto2: String, Foto3: String) {
        val intent = Intent(this, EmergenciaActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("nome", nome)
        intent.putExtra("telefone", telefone)
        intent.putExtra("Foto1", Foto1)
        intent.putExtra("Foto2", Foto2)
        intent.putExtra("Foto3", Foto3)
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