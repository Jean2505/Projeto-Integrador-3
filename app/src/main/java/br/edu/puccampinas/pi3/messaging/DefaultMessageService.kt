package br.edu.puccampinas.pi3.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import br.edu.puccampinas.pi3.EmergenciaActivity
import br.edu.puccampinas.pi3.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class DefaultMessageService : FirebaseMessagingService() {

    /***
     * Evento disparado automaticamente
     * quando o FCM envia uma mensagem. Lembrando que este serviço
     * "DefaultMessageService" está registrado no Manifesto como um serviço.
     */

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        showNotification("Pressione para ver detalhes")

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

    private fun showNotification(messageBody: String) {
        val intent = Intent(this, EmergenciaActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE)
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