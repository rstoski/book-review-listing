package com.dotdash.recruiting.bookreview;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.RequestDispatcher;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

/* Barebones custom error handler for custom 404 / generic error pages.
 * In a real application, this should do more verbose logging to indicate the
 * specific conditions of the error.
 * This custom handler is used because nobody likes whitelabel error pages.
 */
@Controller
public class BookErrorController implements ErrorController {
    private final static Logger logger = LogManager.getLogger(BookErrorController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
      Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
      if (status != null) {
        Integer statusCode = Integer.parseInt(status.toString());
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
          logger.error("404 error");
          return "error-404";
        }
      }

      logger.error("Generic error logging");
      return "error";
    }

    @Override
    public String getErrorPath() {
      return "/error";
    }

}
