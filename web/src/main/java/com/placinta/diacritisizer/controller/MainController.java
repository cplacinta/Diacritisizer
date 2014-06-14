package com.placinta.diacritisizer.controller;

import com.placinta.diacritisizer.DiacriticsRestorer;
import com.placinta.diacritisizer.TextProcessingResult;
import com.placinta.diacritisizer.TextProcessor;
import com.placinta.diacritisizer.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles incoming requests
 */
@Controller
public class MainController {

  private static final String RESTORE_VIEW_NAME = "restore";

  @Autowired private DiacriticsRestorer restorer;
  @Autowired private TextProcessor textProcessor;
  @Autowired private WordService service;


  @RequestMapping(value = {"/", "/restore", "/train"}, method = {RequestMethod.GET})
  public String redirectHome() {
    return RESTORE_VIEW_NAME;
  }

  @RequestMapping(value = "/restore", method = RequestMethod.POST)
  public String compare(@RequestParam(value = "input", required = false) String input, Model model) {
    if (input == null) {
      return RESTORE_VIEW_NAME;
    }

    String result = restorer.restore(input);

    System.out.println("Result: ");
    System.out.println(result);

    model.addAttribute("output", result);
    return RESTORE_VIEW_NAME;
  }

  @RequestMapping(value = "/train", method = {RequestMethod.POST})
  public String train(@RequestParam(value = "result", required = false) String result, Model model) {
    if (result == null) {
      return redirectHome();
    }

    System.out.println("Processing: ");
    System.out.println(result);

    TextProcessingResult processingResult = textProcessor.processText(result);
    service.saveTextProcessingResult(processingResult);

    model.addAttribute("thank_you", true);
    return RESTORE_VIEW_NAME;
  }

}
