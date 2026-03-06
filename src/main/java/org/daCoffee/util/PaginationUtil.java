package org.daCoffee.util;

import java.util.Map;
import java.util.HashMap;

public class PaginationUtil {

  public static final int LIMIT = 15;
  private static final int BOTTOM_LINE = 100;

  public static Map<String, Integer> calculatePagination(int pageInt, int count) {
    int start = (pageInt - 1) / BOTTOM_LINE * BOTTOM_LINE + 1;
    int end = start + BOTTOM_LINE - 1;
    int maxPage = (int) Math.ceil((double) count / LIMIT);

    if (end > maxPage) end = maxPage;

    return Map.of("start", start, "end", end);
  }
}