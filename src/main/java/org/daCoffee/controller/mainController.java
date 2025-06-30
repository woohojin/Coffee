package org.daCoffee.controller;

import org.daCoffee.dto.CartDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.daCoffee.dao.ImageDAO;
import org.daCoffee.dao.ProductDAO;
import org.daCoffee.dao.CartDAO;
import org.daCoffee.dto.ProductDTO;

import java.io.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/board/")
public class mainController {
    private static final Logger LOGGER = LoggerFactory.getLogger(mainController.class);
    private final DataSource ds;
    private final ProductDAO productDao;
    private final CartDAO cartDao;
    private final ImageDAO imageDao;

    @Autowired
    public mainController(DataSource ds, ProductDAO productDao, CartDAO cartDao, ImageDAO imageDao) {
    this.ds = ds;
    this.productDao = productDao;
    this.cartDao = cartDao;
    this.imageDao = imageDao;
    }

    @RequestMapping("main")
    public String main(HttpSession session) throws Exception {

        try{
            Connection conn = (Connection) ds.getConnection();
            System.out.println("Success : " + conn);
        } catch (Exception e) {
            System.out.println("Failed to Connect DB");
            LOGGER.error(e.getMessage());
        }

        Integer memberTier = (Integer) session.getAttribute("memberTier");
        String memberId = (String) session.getAttribute("memberId");

        session.setAttribute("memberId", memberId);
        session.setAttribute("memberTier", memberTier);

        return "main";
    }

    @RequestMapping("terms")
    public String terms() throws Exception {
        return "terms";
    }

    @RequestMapping("privacy")
    public String privacy() throws Exception {
        return "privacy";
    }

    @RequestMapping("product")
    public String product(HttpServletRequest request, HttpSession session, Model model,
                          @RequestParam(value = "pageType", defaultValue = "bean") String pageType,
                          @RequestParam String pageNum,
                          @SessionAttribute Integer memberTier) throws Exception {

        if(memberTier == null) {
            memberTier = 0;
        }

        if (pageNum == null) {
            pageNum = "1";
        }

        int pageInt = Integer.parseInt(pageNum);

        int limit = 16; // 한 page당 게시물 개수
        int bottomLine = 100; // pagination 개수

        int productCount = 0;

        int productType;

        if(memberTier != 0) {
            if(pageType.equals("bean")) {
                productType = 0;
                productDao.rownumSet();
                List<ProductDTO> list = productDao.productListByMemberTierByProductType(pageInt, limit, memberTier, productType);
                productCount = productDao.productCountByTierByProductType(memberTier, productType);
                model.addAttribute("list", list);
            }

            if(pageType.equals("mix")) {
                productType = 1;
                productDao.rownumSet();
                List<ProductDTO> list = productDao.productListByMemberTierByProductType(pageInt, limit, memberTier, productType);
                productCount = productDao.productCountByTierByProductType(memberTier, productType);
                model.addAttribute("list", list);
            }

            if(pageType.equals("cafe")) {
                productType = 2;
                memberTier = 1; // 카페용품은 등급이 항상 1임
                productDao.rownumSet();
                List<ProductDTO> list = productDao.productListByMemberTierByProductType(pageInt, limit, memberTier, productType);
                productCount = productDao.productCountByTierByProductType(memberTier, productType);
                model.addAttribute("list", list);
            }
        }

        int start = (pageInt - 1) / bottomLine * bottomLine + 1;
        int end = start + bottomLine - 1;
        int maxPage = (productCount / limit) + (productCount % limit == 0 ? 0 : 1);
        if (end > maxPage) {
            end = maxPage;
        }
        if (end > productCount) {
            end = productCount;
        }

        model.addAttribute("pageType", pageType);
        model.addAttribute("memberTier", memberTier);
        model.addAttribute("productCount", productCount);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("bottomLine", bottomLine);
        model.addAttribute("maxPage", maxPage);
        model.addAttribute("pageInt", pageInt);

        return "board/product/productList";
    }

    @RequestMapping("beanDetail")
    public String beanDetail(HttpSession session, Model model, String productCode,
                             @SessionAttribute Integer memberTier) throws Exception {

        if(memberTier == null) {
            memberTier = 0;
        }

        int productCount = 0;

        int productType = 0;

        if(memberTier != 0) {
            productCount = productDao.productCountByTierByProductType(memberTier, productType);
        }

        ProductDTO productDTO = productDao.beanSelectOne(productCode);

        String detailImageName = imageDao.selectDetailImage(productCode);

        model.addAttribute("memberTier", memberTier);
        model.addAttribute("productCount", productCount);
        model.addAttribute("product", productDTO);
        model.addAttribute("detailImageName", detailImageName);

        return "board/product/beanDetail";
    }

    @RequestMapping("mixDetail")
    public String mixDetail(HttpSession session, Model model, String productCode,
                            @SessionAttribute Integer memberTier) throws Exception {

        if(memberTier == null) {
            memberTier = 0;
        }

        int productCount = 0;

        int productType = 1;

        if(memberTier != 0) {
            productCount = productDao.productCountByTierByProductType(memberTier, productType);
        }

        ProductDTO productDTO = productDao.mixSelectOne(productCode);

        String detailImageName = imageDao.selectDetailImage(productCode);

        model.addAttribute("memberTier", memberTier);
        model.addAttribute("productCount", productCount);
        model.addAttribute("product", productDTO);
        model.addAttribute("detailImageName", detailImageName);

        return "board/product/mixDetail";
    }

    @RequestMapping("cafeDetail")
    public String cafeDetail(HttpSession session, Model model, String productCode,
                             @SessionAttribute Integer memberTier) throws Exception {

        if(memberTier == null) {
            memberTier = 0;
        }

        int productCount = 0;

        int productType = 2;

        if(memberTier != 0) {
            productCount = productDao.productCountByTierByProductType(memberTier, productType);
        }

        ProductDTO productDTO = productDao.productSelectOne(productCode);

        String detailImageName = imageDao.selectDetailImage(productCode);

        model.addAttribute("memberTier", memberTier);
        model.addAttribute("productCount", productCount);
        model.addAttribute("product", productDTO);
        model.addAttribute("detailImageName", detailImageName);

        return "board/product/cafeDetail";
    }

    @RequestMapping("machineDetail")
    public String machineDetail() throws Exception {
        return "board/product/machineDetail";
    }

    @PostMapping("productDetailPro")
    @ResponseBody
    public Map<String, Object> productDetailPro(HttpSession session, @RequestParam(value = "additionalProducts", required = false) List<String> additionalProductsCodes, CartDTO cartDTO) throws Exception {
        Map<String, Object> map = new HashMap<>();

        String msg = "장바구니 추가 실패";
        String memberId = (String) session.getAttribute("memberId");

        //추가 상품 부분

        if (additionalProductsCodes != null && !additionalProductsCodes.isEmpty()) {
            for (String additionalProductsCode : additionalProductsCodes) {
                if(!additionalProductsCode.equals("none")) {
                    CartDTO cartDTOCheck = cartDao.cartSelectOne(memberId, additionalProductsCode);

                    if(cartDTOCheck == null) {
                        CartDTO additionalCartDTO = new CartDTO();
                        additionalCartDTO.setProductCode(additionalProductsCode);
                        additionalCartDTO.setMemberId(memberId);
                        additionalCartDTO.setQuantity(1);
                        cartDao.cartInsert(additionalCartDTO);
                    } else {
                        int additionalProductQuantity = 1 + cartDTOCheck.getQuantity();
                        cartDao.cartQuantityUpdate(memberId, additionalProductsCode, additionalProductQuantity);
                    }
                }
            }
        }

        //일반 상품 부분

        int quantity = cartDTO.getQuantity();

        if(quantity < 1) {
            quantity = 1;
            cartDTO.setQuantity(quantity);
        }

        String productCode = cartDTO.getProductCode();

        ProductDTO productDTO = productDao.productSelectOne(productCode);

        CartDTO cartDTOCheck = cartDao.cartSelectOne(memberId, productCode);
        if(cartDTOCheck == null) {
            cartDTO.setMemberId(memberId);
            int num = cartDao.cartInsert(cartDTO);

            if(num > 0) {
                msg = "장바구니 추가 성공";
                map.put("productCode", productDTO.getProductCode());
                map.put("productName", productDTO.getProductName());
                map.put("productUnit", productDTO.getProductUnit());
//                map.put("productGrinding", cart.getProductGrinding());
                map.put("quantity", cartDTO.getQuantity());
                map.put("productPrice", productDTO.getProductPrice());
                map.put("productFile", productDTO.getProductFile());
            }
        } else {
            quantity = quantity + cartDTOCheck.getQuantity();
            cartDao.cartQuantityUpdate(memberId, productCode, quantity);
            msg = "장바구니 추가 성공";
            map.put("productCode", productDTO.getProductCode());
            map.put("productName", productDTO.getProductName());
            map.put("productUnit", productDTO.getProductUnit());
//            map.put("productGrinding", product.getProductGrinding());
            map.put("quantity", cartDTO.getQuantity());
            map.put("productPrice", productDTO.getProductPrice());
            map.put("productFile", productDTO.getProductFile());
        }

        map.put("cartStatus", msg);
        return map;
    }

    @RequestMapping("productSearch")
    public String productSearch(HttpServletRequest request, HttpSession session, Model model,
                                @RequestParam String pageNum,
                                @SessionAttribute Integer memberTier) throws Exception {

        if(memberTier == null) {
            memberTier = 0;
        }

        if (pageNum == null) {
            pageNum = "1";
        }

        int pageInt = Integer.parseInt(pageNum);

        int limit = 4; // 한 page당 게시물 개수

        String searchText = request.getParameter("searchText");

        int productSearchCount = 0;

        if(memberTier != 0) {
            productDao.rownumSet();
            List<ProductDTO> list = productDao.productSearchListByMemberTier(pageInt, limit, memberTier, searchText);
            productSearchCount = productDao.productSearchCountByTier(memberTier, searchText);
            model.addAttribute("list", list);
        }

        int start = 1;
        int end = 1;
        int bottomLine = 100; // pagination 개수

        if(productSearchCount < 1) {
            start = 0;
            end = 0;
        } else if (productSearchCount > 1) {
            // 페이지 번호를 표시하는 로직
            start = (pageInt - 1) / bottomLine * bottomLine + 1;
            end = (productSearchCount / limit) + (productSearchCount % limit == 0 ? 0 : 1);
            if (end > productSearchCount) {
                end = productSearchCount;
            }
        }

        model.addAttribute("searchText", searchText);
        model.addAttribute("memberTier", memberTier);
        model.addAttribute("productSearchCount", productSearchCount);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "board/product/productList";
    }
//
//    @RequestMapping("fileUploadForm")
//    public String fileUploadForm() throws Exception {
//        return "board/fileUploadForm";
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
//        return "board/fileUploadPro";
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



