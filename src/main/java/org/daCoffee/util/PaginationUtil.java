package org.daCoffee.util;

import java.util.Map;
import java.util.HashMap;

public class PaginationUtil {

  public static final int LIMIT = 15;
  private static final int BOTTOM_LINE = 100;

  public static Map<String, Integer> calculatePagination(int pageInt, int count) {
    Map<String, Integer> paginationInfo = new HashMap<>();

    int start = (pageInt - 1) / BOTTOM_LINE * BOTTOM_LINE + 1;
    int end = start + BOTTOM_LINE - 1;
    int maxPage = (count / LIMIT) + (count % LIMIT == 0 ? 0 : 1);

    if (end > maxPage) end = maxPage;
    if (end > count) end = count;

    paginationInfo.put("start", start);
    paginationInfo.put("end", end);

    return paginationInfo;
  }
}