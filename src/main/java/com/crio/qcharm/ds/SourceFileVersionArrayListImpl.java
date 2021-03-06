package com.crio.qcharm.ds;

import com.crio.qcharm.request.PageRequest;
import com.crio.qcharm.request.SearchRequest;

//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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

  public SourceFileVersionArrayListImpl() {
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
  public SourceFileVersion apply(List<Edits> edits) {
    List<String> lines = new ArrayList<>();
    lines.addAll(lines);

    //SourceFileVersionArrayListImpl latest = new SourceFileVersionArrayListImpl();

    for (Edits oneEdit : edits) {
      if (oneEdit instanceof UpdateLines) {
        apply((UpdateLines) oneEdit);
      } else {
        assert(oneEdit instanceof SearchReplace);
        apply((SearchReplace) oneEdit);
      }
    }
    return this;
  }


  @Override
  public void apply(SearchReplace searchReplace) {
  }


  // TODO: CRIO_TASK_MODULE_CUT_COPY_PASTE
  // Input:
  //     UpdateLines
  //        1. startingLineNo - starting line number of last time it received page from backend
  //        2. numberOfLines - number of lines received from backend last time.
  //        3. lines - present view of lines in range(startingLineNo,startingLineNo+numberOfLines)
  //        4. cursor
  // Description:
  //        1. Remove the line numbers in the range(starting line no, ending line no)
  //        2. Inserting the lines in new content starting position starting line no
  // Example:
  //        UpdateLines looks like this
  //            1. start line no - 50
  //            2. numberOfLines - 10
  //            3. lines - ["Hello world"]
  //
  //       Assume the file has 100 lines in it
  //
  //       File contents before edit:
  //       ==========================
  //       line no 0
  //       line no 1
  //       line no 2
  //          .....
  //       line no 99
  //
  //        File contents After Edit:
  //        =========================
  //        line no 0
  //        line no 1
  //        line no 2
  //        line no 3
  //         .....
  //        line no 49
  //        Hello World
  //        line no 60
  //        line no 61
  //          ....
  //        line no 99
  //



  @Override
  public void apply(UpdateLines updateLines) {
    for(int i = updateLines.getStartingLineNo();i < updateLines.getStartingLineNo() + updateLines.getNumberOfLines();i++) {
      lines.remove(i);
    }
    lines.addAll(updateLines.getStartingLineNo(), updateLines.getLines());
  }

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


  // TODO: CRIO_TASK_MODULE_SEARCH
  // Input:
  //    SearchRequest - contains following information
  //         1. pattern - pattern you want to search
  //         2. File name - file where you want to search for the pattern

  // TODO: CRIO_TASK_MODULE_IMPROVING_SEARCH
  // Input:
  //    SearchRequest - contains following information
  //        1. pattern - pattern you want to search
  //        2. File name - file where you want to search for the pattern
  // Description:
  //    1. Find all occurrences of the pattern in the SourceFile
  //    2. Create an empty list of cursors
  //    3. For each occurrence starting position add to the list of cursors
  //    4. return the list of cursors
  // Recommendation:
  //    1. Use the simplest string search algorithm that you know.
  // Reference:
  //     https://www.geeksforgeeks.org/naive-algorithm-for-pattern-searching/

  /*
  @Override
  public List<Cursor> getCursors(SearchRequest searchRequest) {
    //boolean efficient = true;
    int M = searchRequest.getPattern().length(); 
    List<Cursor> cursorsList = new ArrayList<Cursor>();
    for(int k = 0; k < lines.size(); k++) {
      int N = lines.get(k).length(); 
  
        for (int i = 0; i <= N - M; i++) { 
  
            int j;
            for (j = 0; j < M; j++) {
                if (lines.get(k).charAt(i + j) != searchRequest.getPattern().charAt(j)) 
                    break; 
            }
  
            if (j == M) {
                //System.out.println("Pattern found at index " + i); 
                cursorsList.add(new Cursor(k, i));
            }
        } 
    }

    return cursorsList;
  }
  */

  //    1. Use FASTER string search algorithm.
  //    2. Feel free to try any other algorithm/data structure to improve search speed.
  // Reference:
  //     https://www.geeksforgeeks.org/kmp-algorithm-for-pattern-searching/

  /*
  @Override
  public List<Cursor> getCursors(SearchRequest searchRequest) {
    //boolean efficient = true;
    int M = searchRequest.getPattern().length();
    List<Cursor> cursorsList = new ArrayList<Cursor>();
    for(int k = 0; k < lines.size(); k++) { 
      int N = lines.get(k).length(); 
      int lps[] = new int[M]; 
      int j = 0;
      char[] pat = searchRequest.getPattern().toCharArray();
      computeLPSArray(pat, M, lps); 
      int i = 0; 
      while (i < N) { 
        if (pat[j] == lines.get(k).charAt(i)) { 
          j++; 
          i++; 
        } 
        if (j == M) { 
          //System.out.println("Found pattern " + "at index " + (i - j)); 
          cursorsList.add(new Cursor(k, i - j));
          j = lps[j - 1]; 
        } 

        else if (i < N && pat[j] != lines.get(k).charAt(i)) { 
          if (j != 0) {
            j = lps[j - 1];
          } 
          else {
            i = i + 1;
          } 
        } 
      } 
    }

    return cursorsList;
  }

  public void computeLPSArray(char[] pat, int M, int lps[]) { 
  
    int len = 0; 
    int i = 1; 
    lps[0] = 0;
  
    while (i < M) { 
      if (pat[i] == pat[len]) { 
        len++; 
        lps[i] = len; 
        i++; 
      } 
      else { 
        if (len != 0) { 
          len = lps[len - 1]; 
        } 
        else { 
          lps[i] = len; 
          i++; 
        } 
      } 
    } 
  }
  */

  @Override
  public List<Cursor> getCursors(SearchRequest searchRequest) {
    char[] pat = searchRequest.getPattern().toCharArray();
    int M = pat.length; 
    int lps[] = new int[M]; 
    computeLPSArray(pat, M, lps); 
    List<Cursor> cursorsList = new ArrayList<Cursor>();
    for(int k = 0; k < lines.size(); k++) {
      char[] txt = lines.get(k).toCharArray();
      int N = txt.length; 
  
      int i = 0;
      int j = 0;
      while (i < N) { 
        if (pat[j] == txt[i]) { 
            j++; 
            i++; 
        } 
  
        if (j == M) { 
          cursorsList.add(new Cursor(k, i - j));
            j = lps[j - 1]; 
        } 
  
        else if (i < N && pat[j] != txt[i]) { 
          if (j != 0) 
            j = lps[j - 1]; 
          else
            i = i + 1; 
        } 
      } 
    }

    return cursorsList;
  }

  void computeLPSArray(char[] pat, int M, int lps[]) {  
    int len = 0; 
    lps[0] = 0;  
    int i = 1; 
    while (i < M) { 
      if (pat[i] == pat[len]) { 
        len++; 
        lps[i] = len; 
        i++; 
      } 
      else { 
        if (len != 0) { 
          len = lps[len - 1]; 
        } 
        else { 
          lps[i] = 0; 
          i++; 
        } 
      } 
    } 
  } 
}
 