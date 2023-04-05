package com.autovend.software.ach.virtual;
/*
Group Members: 
- Achraf Abdelrhafour: (30022366)
- Marianna Ferreira (30147733)
- Ryan Chrumka (30144174)
- Alireza vafisani  (30150496)
- Ali Savab Pour (30154744) 
- Aryan Nambiar (30140671)
- Shijia Wang (30018276)
- Carson Bergen (30127827)
- Md Abdullah Mehedi Patwary (30154770)
- Vita Vysochina (30118374)
- Michael Kacmar, (30113919)
- Deepshikha Dhammi (30140157) 
*/

public class OutOfBagsException  extends Exception {
    //  Basic constructor for no message
    public OutOfBagsException() {}
    /*  Basic constructor that allows for a message to be passed in
        @param message
                - Message to be shown
    */
    public OutOfBagsException(String message) {
        super(message);
    }
}