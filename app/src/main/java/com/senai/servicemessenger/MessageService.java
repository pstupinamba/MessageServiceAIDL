package com.senai.servicemessenger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MessageService extends Service {
    private final List<String> messageHistory = new ArrayList<>(); // Lista para armazenar mensagens

    private final IMessageService.Stub binder = new IMessageService.Stub() {
        @Override
        public void sendMessage(String message) throws RemoteException {
            messageHistory.add(message); // Armazena a mensagem no histórico
            Log.d("MessageService", "Mensagem recebida: " + message);
        }

        @Override
        public List<String> getMessages() throws RemoteException {
            return messageHistory; // Retorna a lista de mensagens
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
