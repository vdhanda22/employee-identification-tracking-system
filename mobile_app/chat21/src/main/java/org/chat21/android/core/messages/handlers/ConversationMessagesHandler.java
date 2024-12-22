package org.chat21.android.core.messages.handlers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.chat21.android.core.ChatManager;
import org.chat21.android.core.exception.ChatFieldNotFoundException;
import org.chat21.android.core.exception.ChatRuntimeException;
import org.chat21.android.core.messages.listeners.ConversationMessagesListener;
import org.chat21.android.core.messages.listeners.SendMessageListener;
import org.chat21.android.core.messages.models.Message;
import org.chat21.android.core.users.models.IChatUser;
import org.chat21.android.utils.StringUtils;
import org.chat21.android.utils.Utils;
import org.chat21.android.utils.http_manager.HttpManager;
import org.chat21.android.utils.http_manager.OnResponseRetrievedCallback;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by andrealeo on 05/12/17.
 */

public class ConversationMessagesHandler {
    private static final String TAG = ConversationMessagesHandler.class.getName();

    private List<Message> messages = new ArrayList<Message>(); // messages in memory

    private IChatUser currentUser;
    private IChatUser recipient;

    private DatabaseReference conversationMessagesNode;

    private ChildEventListener conversationMessagesChildEventListener;

    private List<ConversationMessagesListener> conversationMessagesListeners;

    private Comparator<Message> messageComparator;

    private HttpManager httpManager = new HttpManager();
    private String URL = "https://us-central1-speed-1490596329045.cloudfunctions.net/api/gettranslation?";

    public ConversationMessagesHandler(String firebaseUrl, String appId, IChatUser currentUser, IChatUser recipient) {

        conversationMessagesListeners = new ArrayList<>();

        this.currentUser = currentUser;

        this.recipient = recipient;

        if (StringUtils.isValid(firebaseUrl)) {
            this.conversationMessagesNode = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(firebaseUrl)
                    .child("/apps/" + appId + "/users/" + currentUser.getId() + "/messages/" + recipient.getId());
        } else {
            this.conversationMessagesNode = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("/apps/" + appId + "/users/" + currentUser.getId() + "/messages/" + recipient.getId());
        }

        this.conversationMessagesNode.keepSynced(true);
        Utils.d(TAG, "conversationMessagesNode : " + conversationMessagesNode.toString());

        messageComparator = new Comparator<Message>() {
            @Override
            public int compare(Message m1, Message m2) {
                try {
                    return m2.getTimestamp().compareTo(m1.getTimestamp());
                } catch (Exception e) {
                    Utils.e(TAG, "ConversationMessagesHandler.sortMessagesInMemory: " +
                            "cannot compare messages timestamp", e);
                    return 0;
                }
            }
        };

//        this.conversationMessagesListeners = new ArrayList<ConversationsListener>();
//        this.conversationMessagesListeners.add(conversationMessagesListener);
    }

    public void sendMessage(String type, String text, String channelType, final Map<String,
            Object> metadata, final SendMessageListener sendMessageListener) {
        Utils.v(TAG, "sendMessage called");
        String senderFullName = currentUser.getFullName();
        String recipientFullName = recipient.getFullName();
        if( currentUser.getTenantName()!=null && currentUser.getTenantName().length()>0)
            senderFullName+="-"+currentUser.getTenantName();
        if (recipient.getTenantName()!=null && recipient.getTenantName().length()>0)
            recipientFullName+="-"+recipient.getTenantName();
        // the message to send
        final Message message = new Message();

        message.setSender(currentUser.getId());
        message.setSenderFullname(senderFullName);

        message.setRecipient(this.recipient.getId());
        message.setRecipientFullname(recipientFullName);

        message.setStatus(Message.STATUS_SENDING);

        message.setText(text);
        message.setTranslatedText("");
        message.setType(type);

        message.setChannelType(channelType);

//        message.setStatus(Message.STATUS_SENDING);
        message.setTimestamp(new Date().getTime());
        message.setMetadata(metadata);

        Utils.d(TAG, "sendMessage.message: " + message.toString());

        // generate a message id
        DatabaseReference newMessageReference = conversationMessagesNode.push();
        String messageId = newMessageReference.getKey();

        Utils.d(TAG, "Generated messagedId with value : " + messageId);
        message.setId(messageId); // assign an id to the message

        saveOrUpdateMessageInMemory(message);

//        conversationMessagesNode
//                .push()
//        Message messageForSending = createMessageForFirebase(message);
        Map messageForSending = message.asFirebaseMap();
        Utils.d(TAG, "sendMessage.messageForSending: " + messageForSending.toString());

        newMessageReference
                .setValue(messageForSending, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Utils.v(TAG, "sendMessage.onComplete");

                        if (databaseError != null) {
                            String errorMessage = "sendMessage.onComplete Message not sent. " +
                                    databaseError.getMessage();
                            Utils.e(TAG, errorMessage);
                            FirebaseCrash.report(new Exception(errorMessage));
                            if (sendMessageListener != null) {
                                sendMessageListener.onMessageSentComplete(null,
                                        new ChatRuntimeException(databaseError.toException()));
                            }

                        } else {
                            Utils.d(TAG, "message sent with success to : " + databaseReference.toString());

                            //the cloud code set status to 100 automaticaly
                            //message.setStatus(Message.STATUS_SENT);
                            saveOrUpdateMessageInMemory(message);


//                            databaseReference.child("status").setValue(Message.STATUS_RECEIVED);
                            // databaseReference.child("customAttributes").setValue(customAttributes);
                            //TODO lookup and return the message from the firebase server to retrieve all the fields (timestamp, status, etc)
                            if (sendMessageListener != null) {
                                sendMessageListener.onMessageSentComplete(message, null);
                            }
                        }
                    }
                }); // save message on db

        if (sendMessageListener != null) {
            //set sender and recipiet because MessageListActivity use this message to update the view immediatly and MessageListAdapter use message.sender
            Utils.d(TAG, "onBeforeMessageSent called with message : " + message);

            sendMessageListener.onBeforeMessageSent(message, null);
        }
    }

//    private Message createMessageForFirebase(Message message) {
//        Message messageForFirebase = (Message) message.clone();
//        messageForFirebase.setSender(null);
//        messageForFirebase.setRecipient(null);
//        messageForFirebase.setStatus(null);
//        messageForFirebase.setTimestamp(ServerValue.TIMESTAMP);
//
//        return messageForFirebase;
//    }

    // it checks if the message already exists.
    // if the message exists update it, add it otherwise
    private void saveOrUpdateMessageInMemory(Message newMessage) {
        Utils.d(TAG, "saveOrUpdateMessageInMemory  for message : " + newMessage);

        // look for the message

        int index = -1;
        for (Message tempMessage : messages) {
            if (tempMessage.equals(newMessage)) {
                index = messages.indexOf(tempMessage);
                break;
            }
        }

        if (index != -1) {
            // message already exists
            messages.set(index, newMessage); // update the existing message
            Utils.v(TAG, "message " + newMessage + "updated into messages at position " + index);

        } else {
            // message not exists
            messages.add(newMessage); // insert a new message
            Utils.v(TAG, "message " + newMessage + "is not found into messages. The message was added at the end of the list");

        }

        sortMessagesInMemory();

    }

    private void sortMessagesInMemory() {
        if (messages.size() > 1) {
            Collections.sort(messages, messageComparator);
            Collections.reverse(messages);
        }
    }

    public List<Message> getMessages() {
        return messages;
    }

    public ChildEventListener connect(ConversationMessagesListener conversationMessagesListener) {
        this.upsertConversationMessagesListener(conversationMessagesListener);

        return connect();
    }

    public ChildEventListener connect() {
        Utils.d(TAG, "connecting  for recipientId : " + this.recipient.getId());

//        final List<ConversationMessagesListener> conversationMessagesListeners = new ArrayList<ConversationMessagesListener>();
//        conversationMessagesListeners.add(conversationMessagesListener);

        if (conversationMessagesChildEventListener == null || conversationMessagesChildEventListener !=null ) {
            Utils.d(TAG, "creating a new conversationMessagesChildEventListener");

            conversationMessagesChildEventListener = conversationMessagesNode.orderByChild(Message.TIMESTAMP_FIELD_KEY)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                            Utils.v(TAG, "ConversationMessagesHandler.connect.onChildAdded");

                            try {
                                Message message = decodeMessageSnapShop(dataSnapshot);
                                Utils.d(TAG, "ConversationMessagesHandler.connect.onChildAdded.message : " + message);


                                if (message.getStatus() < Message.STATUS_RECEIVED_FROM_RECIPIENT_CLIENT
                                        && !message.getSender().equals(currentUser.getId())
                                        && message.isDirectChannel()) {

                                    dataSnapshot.getRef().child(Message.STATUS_FIELD_KEY)
                                            .setValue(Message.STATUS_RECEIVED_FROM_RECIPIENT_CLIENT);
                                    Utils.d(TAG, "Message with id : " + message.getId() +
                                            " is received from the recipient client and the status field of the message has beed set to " +
                                            Message.STATUS_RECEIVED_FROM_RECIPIENT_CLIENT);
                                }



                                saveOrUpdateMessageInMemory(message);

                                if (!message.getSender().equals(currentUser.getId()))
                                translateMessage(dataSnapshot.getRef(),message); // if translation is enabled

                                if (conversationMessagesListeners != null) {
                                    for (ConversationMessagesListener conversationMessagesListener : conversationMessagesListeners) {
                                        conversationMessagesListener.onConversationMessageReceived(message, null);
                                    }
                                }

                                //TODO settare status a 200 qui

                            } catch (ChatFieldNotFoundException cfnfe) {
                                Utils.w(TAG, "Error decoding message on onChildAdded " + cfnfe.getMessage());
                            } catch (Exception e) {
                                if (conversationMessagesListeners != null) {
                                    for (ConversationMessagesListener conversationMessagesListener : conversationMessagesListeners) {
                                        conversationMessagesListener.onConversationMessageReceived(null, new ChatRuntimeException(e));
                                    }
                                }
                            }
                        }

                        //for return recepit
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                            Utils.v(TAG, "ConversationMessagesHandler.connect.onChildChanged");

                            try {
                                Message message = decodeMessageSnapShop(dataSnapshot);

                                Utils.d(TAG, "ConversationMessagesHandler.connect.onChildChanged.message : " + message);

                                saveOrUpdateMessageInMemory(message);

                                if (conversationMessagesListeners != null) {
                                    for (ConversationMessagesListener conversationMessagesListener : conversationMessagesListeners) {
                                        conversationMessagesListener.onConversationMessageChanged(message, null);
                                    }
                                }

                            } catch (ChatFieldNotFoundException cfnfe) {
                                Utils.w(TAG, "Error decoding message on onChildChanged " + cfnfe.getMessage());
                            } catch (Exception e) {
                                if (conversationMessagesListeners != null) {
                                    for (ConversationMessagesListener conversationMessagesListener : conversationMessagesListeners) {
                                        conversationMessagesListener.onConversationMessageChanged(null, new ChatRuntimeException(e));
                                    }
                                }
                            }
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Utils.d(TAG, "observeMessages.onChildRemoved");
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
//                Utils.d(TAG, "observeMessages.onChildMoved");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
//                Utils.d(TAG, "observeMessages.onCancelled");

                        }
                    });

            Utils.i(TAG, "connected for recipientId: " + recipient.getId());

        } else {
            Utils.i(TAG, "already connected form recipientId : " + recipient.getId());
        }

        return conversationMessagesChildEventListener;
    }

    public void translateMessage(final DatabaseReference dbPath, Message message)
    {
        Context context = ChatManager.getInstance().getContext();

        if ( context==null || !Utils.isTranslationEnabled(context) || message.getTranslatedText() ==null )
            //|| message.getTranslatedText().trim().length()>0)
        {
            if (!Utils.isTranslationEnabled(context) && message.getTranslatedText() !=null && message.getTranslatedText().trim().length()<1)
            {
                dbPath.child("translatedText")
                        .setValue(message.getText());
            }
            return;
        }

        Utils.e("Translating....",""+message.getText());
        final String langCode = Utils.getLanguageCode(context,"en");
        final String url = URL + "target=" + langCode + "&q=" + message.getText();

        FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful())
                {
                    //Log.e("User Token",""+task.getResult().getToken());
                    String auth = "Bearer "+task.getResult().getToken();

                    httpManager.makeHttpGETCall(new OnResponseRetrievedCallback<String>() {
                        @Override
                        public void onSuccess(String response) {

                            Utils.e("Translation result",""+response);

                            if (response==null)
                                return;

                            try{

                                JSONObject obj = new JSONObject(response).getJSONObject("data");
                                JSONArray arr = obj.getJSONArray("translations");
                                if (arr != null && arr.length() > 0) {
                                    JSONObject myobj = arr.getJSONObject(0);
                                    String translatedMessageText = myobj.getString("translatedText");
                                    //detectiveLangCode = myobj.getString("detectedSourceLanguage");
                                    dbPath.child("translatedText")
                                            .setValue(translatedMessageText);
                                }
                            }catch (Exception ex){ex.printStackTrace();}
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();

                        }
                    },url,auth);
                }
            }
        });


        // httpManager.
    }


    /**
     * @param dataSnapshot the datasnapshot to decode
     * @return the decoded message
     */
    public static Message decodeMessageSnapShop(DataSnapshot dataSnapshot) throws ChatFieldNotFoundException {
        Utils.v(TAG, "decodeMessageSnapShop called");

        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

        String messageId = dataSnapshot.getKey();

        String sender = (String) map.get("sender");
        if (sender == null) {
            throw new ChatFieldNotFoundException("Required sender field is null for message id : " + messageId);
        }

        String recipient = (String) map.get("recipient");
        if (recipient == null) {
            throw new ChatFieldNotFoundException("Required recipient field is null for message id : " + messageId);
        }

        String sender_fullname = (String) map.get("sender_fullname");
        String recipient_fullname = (String) map.get("recipient_fullname");

        Long status = null;
        if (map.containsKey("status")) {
            status = (Long) map.get("status");
        }

        String text = (String) map.get("text");

        String translatedText = null;
        if (map.containsKey("translatedText") && map.get("translatedText")!=null)
            translatedText = (String) map.get("translatedText");

        Long timestamp = null;
        if (map.containsKey("timestamp")) {
            timestamp = (Long) map.get("timestamp");
        }

        String type = (String) map.get("type");

        String channelType = (String) map.get("channel_type");

        // if metadata is a string ignore it
        Map<String, Object> metadata = null;
        if (map.containsKey("metadata") && !(map.get("metadata") instanceof String)) {
            metadata = (Map<String, Object>) map.get("metadata");
        }

        // if metadata is a string ignore it
        Map<String, Object> attributes = null;
        if (map.containsKey("attributes") && !(map.get("attributes") instanceof String)) {
            attributes = (Map<String, Object>) map.get("attributes");
        }

        Message message = new Message();

        message.setId(messageId);
        message.setSender(sender);
        message.setSenderFullname(sender_fullname);
        message.setRecipient(recipient);
        message.setRecipientFullname(recipient_fullname);
        message.setStatus(status);
        message.setText(text);
        message.setTranslatedText(translatedText);
        message.setTimestamp(timestamp);
        message.setType(type);
        message.setChannelType(channelType);
        if (metadata != null) message.setMetadata(metadata);
        if (attributes != null) message.setAttributes(attributes);

        Utils.v(TAG, "decodeMessageSnapShop.message : " + message);

//        Utils.d(TAG, "message >: " + dataSnapshot.toString());

        return message;
    }

    public List<ConversationMessagesListener> getConversationMessagesListener() {
        return conversationMessagesListeners;
    }

    public void setConversationMessagesListeners(List<ConversationMessagesListener> conversationMessagesListeners) {
        this.conversationMessagesListeners = conversationMessagesListeners;
        Utils.i(TAG, "  ConversationMessagesListeners setted");
    }

    public void addConversationMessagesListener(ConversationMessagesListener conversationMessagesListener) {
        Utils.v(TAG, "  addConversationMessagesListener called");

        this.conversationMessagesListeners.add(conversationMessagesListener);
        Utils.i(TAG, "  conversationMessagesListener with hashCode: " + conversationMessagesListener.hashCode() + " added");
    }

    public void upsertConversationMessagesListener(ConversationMessagesListener conversationMessagesListener) {
        Utils.v(TAG, "  upsertConversationMessagesListener called");

        if (conversationMessagesListeners.contains(conversationMessagesListener)) {
            this.conversationMessagesListeners.remove(conversationMessagesListener);
            this.conversationMessagesListeners.add(conversationMessagesListener);
            Utils.i(TAG, "  conversationMessagesListener with hashCode: " + conversationMessagesListener.hashCode() + " updated");
        } else {
            this.conversationMessagesListeners.add(conversationMessagesListener);
            Utils.i(TAG, "  conversationMessagesListener with hashCode: " + conversationMessagesListener.hashCode() + " added");
        }
    }

    public void removeConversationMessagesListener(ConversationMessagesListener conversationMessagesListener) {
        Utils.v(TAG, "  removeConversationMessagesListener called");
        if (this.conversationMessagesListeners!=null)
        this.conversationMessagesListeners.remove(conversationMessagesListener);
        Utils.i(TAG, "  conversationMessagesListener with hashCode: " + conversationMessagesListener.hashCode() + " removed");
    }

    public void removeAllConversationMessagesListeners() {
        this.conversationMessagesListeners = null;
        Utils.i(TAG, "Removed all ConversationMessagesListeners");
    }

    public ChildEventListener getConversationMessagesChildEventListener() {
        return conversationMessagesChildEventListener;
    }

    public void disconnect() {
        this.conversationMessagesNode.removeEventListener(conversationMessagesChildEventListener);
        this.removeAllConversationMessagesListeners();
    }
}