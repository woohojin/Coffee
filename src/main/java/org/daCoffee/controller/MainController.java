package org.daCoffee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class MainController {
    private final DataSource ds;
    
    @RequestMapping("main")
    public String main(HttpSession session, Model model) {

        try {
            Connection conn = ds.getConnection();
            log.info("Database connection successful: {}", conn);
        } catch (SQLException e) {
            log.error("Failed to connect to database: ", e);
            model.addAttribute("msg", "데이터베이스 연결에 실패했습니다.");
            model.addAttribute("url", "/main");
            return "alert";
        }

        Integer memberTier = (Integer) session.getAttribute("memberTier");
        String memberId = (String) session.getAttribute("memberId");

        session.setAttribute("memberId", memberId);
        session.setAttribute("memberTier", memberTier);

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

//
//    @RequestMapping("fileUploadForm")
//    public String fileUploadForm() throws Exception {
//        return "/fileUploadForm";
//    }
//
//    @RequestMapping("fileUploadPro")
//    public String fileUploadPro(MultipartHttpServletRequest files) throws Exception {
//
//        String filePath = request.getServletContext().getRealPath("/") + "view/files/";
//        String fileName = null;
//        File uploadPath = new File(filePath);
//
//        if (!uploadPath.exists()) {
//            uploadPath.mkdirs(); // 경로가 없으면 생성
//        }
//
//        List<MultipartFile> fileList = files.getFiles("files");
//
//        if (fileList.size() > 0) {
//            for(int i = 0; i < fileList.size(); i++) {
//                fileName = fileList.get(i).getOriginalFilename();
//                File file = new File(filePath, fileName);
//                try {
//                    fileList.get(i).transferTo(file);
//                } catch (IllegalStateException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            System.out.println("Failed to upload file");
//            System.out.println(fileList.size() + " / "  + fileList);
//        }
//
//        request.setAttribute("fileName", fileName);
//        return "fileUploadPro";
//    }

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



