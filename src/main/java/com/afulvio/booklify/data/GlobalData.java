package com.afulvio.booklify.data;

import com.afulvio.booklify.model.Book;

import java.util.ArrayList;
import java.util.List;

public class GlobalData {

    public static List<Book> cart;

    static {
        cart = new ArrayList<Book>();
    }
}
