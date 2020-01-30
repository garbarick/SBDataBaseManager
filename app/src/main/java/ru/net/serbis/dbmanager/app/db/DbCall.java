package ru.net.serbis.dbmanager.app.db;

import android.database.sqlite.*;

public interface DbCall<T>
{
    T call(SQLiteDatabase db);
}
