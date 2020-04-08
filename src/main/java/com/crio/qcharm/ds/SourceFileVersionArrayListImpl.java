package com.crio.qcharm.ds;

import com.crio.qcharm.request.PageRequest;
import com.crio.qcharm.request.SearchRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SourceFileVersionArrayListImpl implements SourceFileVersion {

  private String fileName;
  private List<String> lines;

  public SourceFileVersionArrayListImpl(SourceFileVersionArrayListImpl obj) {
  }


  // TODO: CRIO_TASK_MODULE_LOAD_FILE
  // Input:
  //     FileInfo - contains following information
  //         1. fileName
  //         2. List of lines
  // Steps:
  //     You task here is to construct SourceFileVersionArrayListImpl object by
  //     1. Storing the lines received from fileInfo object
  //     2. Storing the fileName received from fileInfo object.
  // Recommendations:
  //     1. Use Java ArrayList to store the lines received from fileInfo

  public SourceFileVersionArrayListImpl(FileInfo fileInfo) {
      this.fileName = fileInfo.fileName;
      this.lines = new ArrayList<String>(fileInfo.getLines().size());
      this.lines.addAll(fileInfo.getLines());
  }

  public String getFileName() {
    return this.fileName;
  }

  public List<String> getLines() {
    return this.lines;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setLines(List<String> lines) {
    this.lines = lines;
  }

  // TODO: CRIO_TASK_MODULE_LOAD_FILE
  // Input:
  //    1. lineNumber - The line number
  //    2. numberOfLines - Number of lines requested
  // Expected functionality:
  //    1. Get the requested number of lines starting before the given line number.
  //    2. Make page object of this and return.
  //    3. For cursor position use the value from pageRequest
  //    4. For fileName use the value from pageRequest
  // NOTE:
  //    If there less than requested number of lines, then return just those lines.
  //    Zero is the first line number of the file
  // Example:
  //    lineNumber - 50
  //    numberOfLines - 25
  //    Then lines returned is
  //    (line number 25, line number 26 ... , line number 48, line number49)


  @Override
  public Page getLinesBefore(PageRequest pageRequest) {
    int lineNumber = pageRequest.getStartingLineNo();
    int numberOfLines = pageRequest.getNumberOfLines();
    int startingLineNo = 0;
    List<String> requestedlines = new ArrayList<String>();
    if (lines != null) {
      for(int i = 1; i <= numberOfLines; i++) {

        if(lineNumber - i < 0)
          break;

        requestedlines.add(lines.get(lineNumber-i));
        startingLineNo = lineNumber - i;
      }
      Collections.reverse(requestedlines);
    }
    else {
      requestedlines = Collections.emptyList();
    }
    Page obj = new Page(requestedlines, startingLineNo, pageRequest.getFileName(), pageRequest.getCursorAt());
    return obj;
  }

  // TODO: CRIO_TASK_MODULE_LOAD_FILE
  // Input:
  //    1. lineNumber - The line number
  //    2. numberOfLines - Number of lines requested
  // Expected functionality:
  //    1. Get the requested number of lines starting after the given line number.
  //    2. Make page object of this and return.
  //    3. For cursor position use the value from pageRequest
  //    4. For fileName use the value from pageRequest
  // NOTE:
  //    If there less than requested number of lines, then return just those lines.
  //    Zero is the first line number of the file  @Override
  // Example:
  //    lineNumber - 50
  //    numberOfLines - 25
  //    Then lines returned is
  //    (line number 51, line number 52 ... , line number 74, line number75)

  @Override
  public Page getLinesAfter(PageRequest pageRequest) {
    int lineNumber = pageRequest.getStartingLineNo();
    int numberOfLines = pageRequest.getNumberOfLines();
    int startingLineNo = 0;
    List<String> requestedlines = new ArrayList<String>();
    if (lines != null) {
      if(lineNumber == lines.size()) {
        startingLineNo = lineNumber;
      }
      else {
        startingLineNo = lineNumber + 1;
      }
      for(int i = 1; i <= numberOfLines; i++) {
      
        if(lineNumber + i > lines.size() - 1)
          break;
        
        requestedlines.add(lines.get(lineNumber + i));
      }
    }
    else{
      requestedlines = Collections.emptyList();
    }
    //Collections.reverse(requestedlines);
    Page obj = new Page(requestedlines, startingLineNo, pageRequest.getFileName(), pageRequest.getCursorAt());
    return obj;

  }

  // TODO: CRIO_TASK_MODULE_LOAD_FILE
  // Input:
  //    1. lineNumber - The line number
  //    2. numberOfLines - Number of lines requested
  // Expected functionality:
  //    1. Get the requested number of lines starting from the given line number.
  //    2. Make page object of this and return.
  //    3. For cursor position should be (startingLineNo, 0)
  //    4. For fileName use the value from pageRequest
  // NOTE:
  //    If there less than requested number of lines, then return just those lines.
  //    Zero is the first line number of the file  @Override
  // Example:
  //    lineNumber - 50
  //    numberOfLines - 25
  //    Then lines returned is
  //    (line number 50, line number 51 ... , line number 73, line number74)

  @Override
  public Page getLinesFrom(PageRequest pageRequest) {
    int lineNumber = pageRequest.getStartingLineNo();
    int numberOfLines = pageRequest.getNumberOfLines();
    int startingLineNo = 0;
    List<String> requestedlines = new ArrayList<String>();
    if (lines != null) {
        startingLineNo = lineNumber;
      for(int i = 0; i < numberOfLines; i++) {
      
        if(lineNumber + i > lines.size() - 1)
          break;
        
        requestedlines.add(lines.get(lineNumber + i));
      }
    }
    else {
      requestedlines = Collections.emptyList();
    }
    //Collections.reverse(requestedlines);
    Page obj = new Page(requestedlines, startingLineNo, pageRequest.getFileName(), new Cursor(lineNumber, 0));
    return obj;
  }

  @Override
  public List<String> getAllLines() {
    return this.lines;
  }
}
