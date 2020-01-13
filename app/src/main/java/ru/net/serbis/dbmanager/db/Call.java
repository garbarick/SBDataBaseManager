package ru.net.serbis.dbmanager.db;

import android.database.sqlite.*;

public interface Call<T>
{
    T call(SQLiteDatabase db);
}
