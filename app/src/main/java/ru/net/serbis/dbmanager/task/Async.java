package ru.net.serbis.dbmanager.task;

public interface Async
{
    void preExecute();
    void postExecute();
    void inBackground();
}
