package com.dotdash.recruiting.bookreview.model;

/* Model class for storing parsed Goodreads API results and for parsing
 * to JSON for client.
 */
public class Book implements java.io.Serializable {
  private String title = null;
  private String author = null;
  private String imageUrl = null;

  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }
  public void setAuthor(String author) {
    this.author = author;
  }

  public String getImageUrl() {
    return imageUrl;
  }
  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
