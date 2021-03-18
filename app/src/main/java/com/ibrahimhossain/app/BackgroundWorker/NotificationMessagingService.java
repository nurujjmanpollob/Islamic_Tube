/*
 *
 *  Copyright 2021 Nurujjaman Pollob.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.ibrahimhossain.app.BackgroundWorker;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ibrahimhossain.app.R;
import com.ibrahimhossain.app.Variables;
import com.ibrahimhossain.app.WebReferenceLoader;
import com.ibrahimhossain.app.dialogview.NJPollobDialogLayout;

import java.util.Objects;

public class NotificationMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        if(remoteMessage.toIntent().hasExtra(Variables.FIREBASE_NOTIFICATION_KEY_URL)) {


            NJPollobDialogLayout layout = new NJPollobDialogLayout(getApplicationContext());
            layout.setDialogDescription(Objects.requireNonNull(remoteMessage.getNotification()).getBody());

            Glide.with(getApplicationContext()).asBitmap().placeholder(R.drawable.loading_placeholder).load(remoteMessage.getNotification().getImageUrl().toString()).into(layout.getThumbnailView());

            layout.setCancelable(false);
            layout.setListenerOnDialogButtonClick("Visit", "Close", new NJPollobDialogLayout.DialogButtonClickListener() {
                @Override
                public void onLeftButtonClick(View view) {

                    Intent i = new Intent(getApplicationContext(), WebReferenceLoader.class);
                    i.putExtra(Variables.WEB_REFERENCE_INTENT_KEY, remoteMessage.toIntent().getStringExtra(Variables.FIREBASE_NOTIFICATION_KEY_URL));
                    getApplicationContext().startActivity(i);

                }

                @Override
                public void onRightButtonClick(View view) {

                    layout.dismiss();

                }
            });

            layout.show();

        }


        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onNewToken(@NonNull String s) {


        super.onNewToken(s);
    }

    @Override
    public void onSendError(@NonNull String s, @NonNull Exception e) {
        super.onSendError(s, e);
    }

}
