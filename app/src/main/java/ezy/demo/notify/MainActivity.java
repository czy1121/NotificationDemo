package ezy.demo.notify;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import ezy.assist.app.NotifyUtil;
import ezy.demo.notify.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int[] DEFAULTS = new int[]{
            0,
            Notification.DEFAULT_ALL,

            Notification.DEFAULT_LIGHTS,
            Notification.DEFAULT_SOUND,
            Notification.DEFAULT_VIBRATE,

            Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND,
            Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE,
            Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE
    };

    NotificationCompat.Builder mBuilder;
    int mNotifyId = 1;

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setOnClick(this);

        binding.spPriority.setSelection(2);
        binding.spVisibility.setSelection(1);

        binding.spPriority.setOnItemSelectedListener(this);
        binding.spButtons.setOnItemSelectedListener(this);
        binding.spProgress.setOnItemSelectedListener(this);
        binding.spStyle.setOnItemSelectedListener(this);
        binding.spTime.setOnItemSelectedListener(this);
        binding.spAlert.setOnItemSelectedListener(this);
        binding.spVisibility.setOnItemSelectedListener(this);
        binding.spCategory.setOnItemSelectedListener(this);


        mBuilder = NotifyUtil.create(this, R.mipmap.ic_elder, "This is title", "This is content.");

        registerReceiver(new ActionReceiver(), new IntentFilter(ACTION));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
        case R.id.sp_priority:
            mBuilder.setPriority(position - 2);
            break;
        case R.id.sp_buttons:
            mBuilder.mActions.clear();
            if (position == 1) {
                mBuilder.addAction(android.R.drawable.ic_media_play, "Play", pi(1001));
            } else if (position == 2) {
                mBuilder.addAction(android.R.drawable.ic_media_previous, "Prev", pi(1001));
                mBuilder.addAction(android.R.drawable.ic_media_next, "Next", pi(1002));
            } else if (position == 3) {
                mBuilder.addAction(android.R.drawable.ic_media_previous, "Prev", pi(1001));
                mBuilder.addAction(android.R.drawable.ic_media_play, "Play", pi(1002));
                mBuilder.addAction(android.R.drawable.ic_media_next, "Next", pi(1003));
            }
            break;
        case R.id.sp_progress:
            if (position == 0) {
                mBuilder.setProgress(0, 0, false);
            } else {
                mBuilder.setProgress(100, 50, position == 2);
            }
            break;
        case R.id.sp_style:
            if (position == 0) {
                mBuilder.setStyle(null);
            } else if (position == 1) {
                mBuilder.setStyle(NotifyUtil.makeBigText("big text content\nbig text content\nbig text content\nbig text content\nbig text " +
                                                                 "content\nbig text content\n")
                                            .setBigContentTitle("big text title")
                                            .setSummaryText("big text summary"));
            } else if (position == 2) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.photo480);
                mBuilder.setStyle(NotifyUtil.makeBigPicture(bitmap).setBigContentTitle("big text title").setSummaryText("big text summary"));
            } else if (position == 3) {
                List<String> messages = Arrays.asList(new String[]{"line 1", "line 2", "line 3"});
                mBuilder.setStyle(NotifyUtil.makeInbox(messages).setBigContentTitle("big text title").setSummaryText("big text summary"));
            }
            break;
        case R.id.sp_time:
            if (position == 0) {
                mBuilder.setShowWhen(false);
                mBuilder.setUsesChronometer(false);
            } else if (position == 1) {
                mBuilder.setShowWhen(true);
                mBuilder.setUsesChronometer(false);
            } else if (position == 2) {
                mBuilder.setShowWhen(true);
                mBuilder.setUsesChronometer(true);
            }
            break;
        case R.id.sp_alert:
            mBuilder.setDefaults(DEFAULTS[position]);
            break;
        case R.id.sp_visibility:
            mBuilder.setVisibility(position - 1);
            break;
        case R.id.sp_category:
            mBuilder.setCategory(parent.getSelectedItem().toString());
            break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.switch_only_alert_once:
            mBuilder.setOnlyAlertOnce(binding.switchOnlyAlertOnce.isChecked());
            break;
        case R.id.switch_auto_cancel:
            mBuilder.setAutoCancel(binding.switchAutoCancel.isChecked());
            break;
        case R.id.switch_ongoing:
            mBuilder.setOngoing(binding.switchOngoing.isChecked());
            break;
        case R.id.switch_info:
            mBuilder.setContentInfo("info");
            break;
        case R.id.switch_public:
            if (binding.switchPublic.isChecked()) {
                mBuilder.setPublicVersion(NotifyUtil.create(this, R.mipmap.ic_wazhi, "public title", "public content.").build());
            } else {
                mBuilder.setPublicVersion(null);
            }

            break;
        case R.id.switch_large_icon:
            if (binding.switchLargeIcon.isChecked()) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.large_icon);
                mBuilder.setLargeIcon(bitmap);
            } else {
                mBuilder.setLargeIcon(null);
            }
            break;
        case R.id.switch_ticker:
            if (binding.switchTicker.isChecked()) {
                mBuilder.setTicker("this is ticker");
            } else {
                mBuilder.setTicker(null);
            }
            break;
        case R.id.switch_sub:
            if (binding.switchSub.isChecked()) {
                mBuilder.setSubText("his is sub");
            } else {
                mBuilder.setSubText(null);
            }
            break;
        case R.id.switch_content_intent:
            if (binding.switchContentIntent.isChecked()) {
                mBuilder.setContentIntent(pi(2000));
            } else {
                mBuilder.setContentIntent(null);
                mBuilder.mNotification.contentIntent = null;
            }
            break;
        case R.id.switch_delete_intent:
            if (binding.switchDeleteIntent.isChecked()) {
                mBuilder.setDeleteIntent(pi(2001));
            } else {
                mBuilder.setDeleteIntent(null);
                mBuilder.mNotification.deleteIntent = null;
            }
            break;
        case R.id.switch_full_screen_intent:
            if (binding.switchFullScreenIntent.isChecked()) {
                mBuilder.setFullScreenIntent(pi(2002), true);
            } else {
                mBuilder.setFullScreenIntent(null, false);
                mBuilder.mNotification.fullScreenIntent = null;
            }
            break;
        case R.id.clear_all:

            NotifyUtil.cancelAll(this);
            break;
        case R.id.clear:
            NotifyUtil.cancel(this, mNotifyId);
            break;
        case R.id.send:
            mNotifyId++;
            final Notification notification = mBuilder.build();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (notification.publicVersion != null && notification.visibility == Notification.VISIBILITY_PUBLIC) {
                    binding.getRoot().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            NotifyUtil.notify(mBuilder.mContext, mNotifyId, notification);
                        }
                    }, 5000);
                    break;
                }
            }
            if (binding.switchInsistent.isChecked()) {
                notification.flags |= Notification.FLAG_INSISTENT;
            }
            if (binding.switchNoClear.isChecked()) {
                notification.flags |= Notification.FLAG_NO_CLEAR;
            }
            NotifyUtil.notify(this, mNotifyId, notification);
            break;
        }
    }

    PendingIntent pi(int op) {
        Intent intent = new Intent(ACTION);
        intent.putExtra("op", op);
        return PendingIntent.getBroadcast(this, 0, intent, 0);
    }

    public static final String ACTION = "action.ezy.demo.notification";

    public static class ActionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (!ACTION.equals(action)) {
                return;
            }
            int op = intent.getIntExtra("op", 0);
            Log.e("ezy", "result ==> " + op);
            Toast.makeText(context, "result ==> " + op, Toast.LENGTH_LONG).show();
        }
    }
}
