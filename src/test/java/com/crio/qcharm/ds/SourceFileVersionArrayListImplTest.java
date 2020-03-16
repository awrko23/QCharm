package com.crio.qcharm.ds;

import com.crio.qcharm.log.UncaughtExceptionHandler;
import com.crio.qcharm.request.PageRequest;
import com.crio.qcharm.request.SearchRequest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SourceFileVersionArrayListImplTest {

  @BeforeEach
  public void setupUncaughtExceptionHandler() {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
  }

  private FileInfo getSmallFile(String fileName) {
    List<String> lines = new ArrayList<>();
    lines.add("Hello World");
    lines.add("def square(x):");
    lines.add("    return x * x;");

    return makeFile(fileName, lines);
  }

  private FileInfo makeFile(String fileName, int n) {
    List<String> lines = new ArrayList<>();
    for (int i = 1; i <= n; ++i) {
      StringBuffer oneLine = new StringBuffer("lineno");
      oneLine.append(i);
      lines.add(oneLine.toString());
    }
    return new FileInfo(fileName, lines);
  }

  private FileInfo makeFile(String fileName, List<String> lines) {
    return new FileInfo(fileName, lines);
  }


  @Test
  void getAllLines() {
    FileInfo fileInfo = makeFile("getAllLines", 73);

    SourceFileVersion SourceFileVersion =
        new SourceFileVersionArrayListImpl(fileInfo);

    List<String> linesReceived = SourceFileVersion.getAllLines();

    assertEquals(linesReceived, fileInfo.getLines());
  }

  @Test
  void getLinesBefore() {
    String fileName = "getLinesBefore1";
    FileInfo fileInfo = makeFile(fileName, 73);

    SourceFileVersion SourceFileVersion =
        new SourceFileVersionArrayListImpl(fileInfo);

    Cursor cursorAt = new Cursor(10, 0);
    PageRequest pageRequest = new PageRequest(10, fileName, 25, cursorAt);

    Page linesBefore = SourceFileVersion.getLinesBefore(pageRequest);

    assertEquals(linesBefore.getLines(), fileInfo.getLines().subList(0, 10));
    assertEquals(linesBefore.getCursorAt(), cursorAt);
  }

  @Test
  void getLinesBeforeLineZero() {
    String fileName = "getLinesBefore1";
    FileInfo fileInfo = makeFile(fileName, 73);

    SourceFileVersion SourceFileVersion =
        new SourceFileVersionArrayListImpl(fileInfo);

    Cursor cursorAt = new Cursor(0, 0);
    PageRequest pageRequest = new PageRequest(0, fileName, 25, cursorAt);
    Page linesBefore = SourceFileVersion.getLinesBefore(pageRequest);

    assertEquals(linesBefore.getLines().size(), 0);
    assertEquals(linesBefore.getCursorAt(), cursorAt);
  }

  @Test
  void getLinesAfter() {
    String fileName = "getLinesAfter1";
    FileInfo fileInfo = makeFile(fileName, 73);

    SourceFileVersion SourceFileVersion =
        new SourceFileVersionArrayListImpl(fileInfo);

    Cursor cursorAt = new Cursor(10, 10);
    PageRequest pageRequest = new PageRequest(10, fileName, 25, cursorAt);
    Page linesBefore = SourceFileVersion.getLinesAfter(pageRequest);

    assertEquals(linesBefore.getLines(), fileInfo.getLines().subList(11, 11+25));
    assertEquals(linesBefore.getCursorAt(), cursorAt);
  }

  @Test
  void getLinesWhenStartIsCloseToFileEnd() {
    String fileName = "getLinesFrom1";
    FileInfo fileInfo = makeFile(fileName, 73);

    SourceFileVersion SourceFileVersion =
        new SourceFileVersionArrayListImpl(fileInfo);

    Cursor cursorAt = new Cursor(60, 0);
    PageRequest pageRequest = new PageRequest(60, fileName, 25, cursorAt);
    Page linesFrom = SourceFileVersion.getLinesFrom(pageRequest);

    assertEquals(linesFrom.getLines(), fileInfo.getLines().subList(60, 73));
    assertEquals(linesFrom.getCursorAt(), cursorAt);
  }

  @Test
  void getLinesFromGetsExactlyTheSameNumberOfLines() {
    String fileName = "getLinesFrom1";
    FileInfo fileInfo = makeFile(fileName, 73);

    SourceFileVersion SourceFileVersion =
        new SourceFileVersionArrayListImpl(fileInfo);

    Cursor cursorAt = new Cursor(10, 0);
    PageRequest pageRequest = new PageRequest(10, fileName, 25, cursorAt);
    Page linesFrom = SourceFileVersion.getLinesFrom(pageRequest);

    assertEquals(linesFrom.getLines(), fileInfo.getLines().subList(10, 10+25));
    assertEquals(linesFrom.getCursorAt(), cursorAt);
  }



  @Test
  void getSearchAndReplace() {
  }



//  @Test
//  void undo() {
//    String fileName = "apply1";
//    FileInfo fileInfo = getSmallFile(fileName);
//
//    List<String> lines = new ArrayList<>();
//    lines.add("def newSquareFunction(var)");
//    lines.add("    return var * var;");
//
//    Cursor cursor = new Cursor(1,0);
//
//    Edits updateLines = new UpdateLines(1, 2, lines, cursor);
//
//    SourceFileVersion SourceFileVersion = new SourceFileVersionArrayListImpl(updateLines, fileInfo);
//
//    assertEquals(SourceFileVersion.getAllLines(), fileInfo.getLines());
//
//    SourceFileVersion.apply();
//    List<String> allLines = SourceFileVersion.getAllLines();
//    assertEquals(allLines.get(0), fileInfo.getLines().get(0));
//    assertEquals(allLines.get(1), lines.get(0));
//    assertEquals(allLines.get(2), lines.get(1));
//
//    Page page = SourceFileVersion.undo();
//    assertEquals(SourceFileVersion.getAllLines(), fileInfo.getLines());
//  }

//  @Test
//  void getFileName() {
//    FileInfo fileInfo = getSmallFile("DummyFileName");
//    assertEquals(fileInfo.getFileName(), "DummyFileName");
//  }
}