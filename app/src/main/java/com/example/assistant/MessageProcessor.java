package com.example.assistant;

import android.os.AsyncTask;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class MessageProcessor extends AsyncTask<String,String,String> {
    ChatBot chatBot ;
    ChatView chatView;

    public MessageProcessor(ChatView chatView,ChatBot chatBot) {
       this.chatBot = chatBot;
       this.chatView = chatView;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String response) {
        chatView.addMessage(new ChatMessage(response, System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
    }

    @Override
    protected String doInBackground(String[] queries) {
        if(!ChatBot.readyToLearnSomethingNew) {
            return chatBot.getResponse(queries[0]);
        }else{
            String res = chatBot.saveQuestionAndResponse(queries[0],queries[1]);
            ChatBot.readyToLearnSomethingNew = false;
            ChatBot.question = "";
            return res;
        }

    }


}
