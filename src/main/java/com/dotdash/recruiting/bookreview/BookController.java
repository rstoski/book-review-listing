package com.dotdash.recruiting.bookreview;

import com.dotdash.recruiting.bookreview.model.Book;
import com.dotdash.recruiting.bookreview.model.SearchResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class BookController {
    private final static Logger logger = LogManager.getLogger(BookController.class);

    @Value("${goodreads.key}")
    private String goodreadsKey;
    @Value("${goodreads.url}")
    private String goodreadsUrl;

    /* Handle call from client performing book search. Calls Goodreads API and
     * returnes parsed result as JSON. Error is logged and error string is
     * returned if exception encountered.
     */
    @GetMapping("/search")
    @ResponseBody
    public SearchResult search(@RequestParam(name="query", required=true) String query,
      @RequestParam(name="page", required=false, defaultValue="1") String page, Model model) {
      SearchResult searchResult = new SearchResult();

      try {
        String apiResult = searchGoodreads(query, page);
        searchResult = parseResults(apiResult);
      } catch (Exception e) {
        logger.error("Search failed", e);
        searchResult.setError("An error occurred while processing your request. Please try again.");
      }

      return searchResult;
    }

    /* Parses Goodreads API XML to an object. In a real application, especially
     * if many objects / fields were to be parsed, an OXM should be utilized
     * to limit amount of manual parsing. e.g. Jaxb2Marshaller
     * That is avoided here for time constraints and due to the small number
     * of elements to be parsed.
     */
    private static SearchResult parseResults(String rawXml) {
      SearchResult searchResult = new SearchResult();
      ArrayList<Book> bookList = new ArrayList<Book>();
      searchResult.setBooks(bookList);

      try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(rawXml.getBytes("UTF-8")));
        Element root = doc.getDocumentElement();
        Element search = (Element)root.getElementsByTagName("search").item(0);
        int resultsStart = Integer.parseInt(getString(search, "results-start"));
        int totalResults = Integer.parseInt(getString(search, "total-results"));
        searchResult.setNumPages((int)Math.ceil(totalResults/20.0f));
        searchResult.setPage((int)Math.ceil(resultsStart/20.0f));

        NodeList results = search.getElementsByTagName("results");
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList books = (NodeList) xPath.compile("//best_book").evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < books.getLength(); i++) {
          Element bookElem = (Element) books.item(i);

          Book book = new Book();
          book.setTitle(getString(bookElem, "title"));
          book.setAuthor(getString(bookElem, "author", "name"));
          book.setImageUrl(getString(bookElem, "small_image_url"));
          bookList.add(book);
        }
      } catch (Exception e) {
        logger.error("Error parsing search results", e);
      }

      return searchResult;
    }

    /* Convenience function for reading string from child elements.
     * Catching exceptions here allows us to handle discrepancies in returned
     * XML, such as no author on book "NOT A BOOK Hobbit". Fields not found
     * simply resolve as empty strings and parsing continues.
     */
    private static String getString(Element parent, String... nodeNames) {
      try {
        Element target = parent;
        for (String nodeName : nodeNames){
          target = (Element) target.getElementsByTagName(nodeName).item(0);
        }
        return target.getTextContent();
      } catch (Exception e) {
        logger.error("Error parsing string", nodeNames, e);
      }
      return "";
    }

    /* Call Goodreads API to search for books and return results as String.
     * More elegant way could be used to fill in url parameter (e.g.
     * apache http client URIBuilder).
     */
    private String searchGoodreads(String query, String page) throws Exception {
      Map<String, String> params = new HashMap<String, String>();
      params.put("q", query);
      params.put("page", page);
      params.put("key", goodreadsKey);

      RestTemplate restTemplate = new RestTemplate();
      return restTemplate.getForObject(goodreadsUrl, String.class, params);
    }

}
