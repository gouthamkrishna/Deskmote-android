package com.beaconapp.user.navigation.database;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by user on 14/7/15.
 */
public class DBTasks {

    Context mContext;

    public DBTasks(Context context) {
        mContext=context;
    }

    public void insertReminder (ReminderInsertionCompletion listener) {

        ReminderInsertionTask task = new ReminderInsertionTask(listener);
        task.execute();
    }

    public void insertDailyLog (DailyLogInsertionCompletion listener) {

        DailyLogInsertionTask task = new DailyLogInsertionTask(listener);
        task.execute();
    }


    class ReminderInsertionTask extends AsyncTask<Void, Void, Void> {

        private ReminderInsertionCompletion listener;

        public ReminderInsertionTask (ReminderInsertionCompletion listener) {
            this.listener = listener;
        }


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listener.reminderInsertionCompleted();
        }

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }
    }


    class DailyLogInsertionTask extends AsyncTask<Void, Void, Void> {

        private DailyLogInsertionCompletion listener;

        public DailyLogInsertionTask (DailyLogInsertionCompletion listener) {

            this.listener = listener;
        }


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
            listener.dailyLogInsertionCompleted();
        }

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }
    }


    public interface ReminderInsertionCompletion {

        public void reminderInsertionCompleted();
    }

    public interface DailyLogInsertionCompletion {

        public void dailyLogInsertionCompleted();
    }
}