// IMessageService.aidl
package com.senai.servicemessenger;

// Declare any non-default types here with import statements

interface IMessageService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    //void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            //double aDouble, String aString);

    void sendMessage(String message);
    List<String> getMessages();
}