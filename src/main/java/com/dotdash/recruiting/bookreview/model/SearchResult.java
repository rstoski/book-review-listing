package com.dotdash.recruiting.bookreview.model;

import java.util.ArrayList;

/* Model class for storing parsed Goodreads API results and for parsing 
 * to JSON for client.
 */
public class SearchResult implements java.io.Serializable {
  private int page;
  private int numPages;
  private String error = null;
  private ArrayList<Book> books = null;

  public int getPage() {
    return page;
  }
  public void setPage(int page) {
    this.page = page;
  }

  public int getNumPages() {
    return numPages;
  }
  public void setNumPages(int numPages) {
    this.numPages = numPages;
  }

  public String getError() {
    return error;
  }
  public void setError(String error) {
    this.error = error;
  }

  public ArrayList<Book> getBooks() {
    return books;
  }
  public void setBooks(ArrayList<Book> books) {
    this.books = books;
  }
}
