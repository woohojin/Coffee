package org.daCoffee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class MainController {
    private final DataSource ds;

    @RequestMapping("")
    public String root() {
        return "redirect:/main";
    }
    
    @RequestMapping("main")
    public String main() {
        return "main";
    }

    @RequestMapping("terms")
    public String terms() {
        return "terms";
    }

    @RequestMapping("privacy")
    public String privacy() {
        return "privacy";
    }

    @RequestMapping("fileDownload")
    public void fileDownload(HttpServletRequest request, HttpServletResponse response) {
        try {
            String filePath = request.getServletContext().getRealPath("/") + "view/files/"; // 다운로드할 파일 경로
            String fileName = request.getParameter("fileName");
            filePath = filePath + fileName;

            File file = new File(filePath);

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            response.setContentLength((int) file.length());

            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



