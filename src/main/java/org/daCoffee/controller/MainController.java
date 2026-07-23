package org.daCoffee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class MainController {
    private static final String FILE_BASE_PATH = "view/files/";
    private static final int BUFFER_SIZE = 8192;

    @RequestMapping("")
    public String root() {
        return "redirect:/main";
    }
    
    @RequestMapping("fileDownload")
    public void fileDownload(HttpServletResponse response,
                             @RequestParam String fileName) throws IOException {

        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid file name");
            return;
        }
        Path filePath = Paths.get(FILE_BASE_PATH).resolve(fileName).normalize();
        File file = filePath.toFile();

        if (!file.exists() || !file.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
            return;
        }

        String encodedFileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8)
          .replace("+", "%20");

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
        response.setContentLengthLong(file.length());

        try(InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (FileNotFoundException e) {
            log.error("File not found: {}", fileName, e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
        } catch (IOException e) {
            log.error("File download IO error: {}", fileName, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "File download failed");
        }
    }
}



