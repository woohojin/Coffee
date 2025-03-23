package org.daCoffee.controller;

import org.daCoffee.model.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.daCoffee.service.ImageDAO;
import org.daCoffee.service.ProductDAO;
import org.daCoffee.service.CartDAO;
import org.daCoffee.model.Product;

import java.io.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        if(memberTier == null) {
            memberTier = 0;
        }

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
    public String product(HttpServletRequest request, HttpSession session, @RequestParam(value = "pageType", defaultValue = "bean") String pageType) throws Exception {
        Integer memberTier = (Integer) session.getAttribute("memberTier");
        if(memberTier == null) {
            memberTier = 0;
        }

        String pageNum = request.getParameter("pageNum");
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
                List<Product> list = productDao.productListByMemberTierByProductType(pageInt, limit, memberTier, productType);
                productCount = productDao.productCountByTierByProductType(memberTier, productType);
                request.setAttribute("list", list);
            }

            if(pageType.equals("mix")) {
                productType = 1;
                productDao.rownumSet();
                List<Product> list = productDao.productListByMemberTierByProductType(pageInt, limit, memberTier, productType);
                productCount = productDao.productCountByTierByProductType(memberTier, productType);
                request.setAttribute("list", list);
            }

            if(pageType.equals("cafe")) {
                productType = 2;
                memberTier = 1; // 카페용품은 등급이 항상 1임
                productDao.rownumSet();
                List<Product> list = productDao.productListByMemberTierByProductType(pageInt, limit, memberTier, productType);
                productCount = productDao.productCountByTierByProductType(memberTier, productType);
                request.setAttribute("list", list);
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

        request.setAttribute("pageType", pageType);
        request.setAttribute("memberTier", memberTier);
        request.setAttribute("productCount", productCount);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("start", start);
        request.setAttribute("end", end);
        request.setAttribute("bottomLine", bottomLine);
        request.setAttribute("maxPage", maxPage);
        request.setAttribute("pageInt", pageInt);

        return "board/product/productList";
    }

    @RequestMapping("beanDetail")
    public String beanDetail(HttpServletRequest request, HttpSession session, String productCode) throws Exception {
        Integer memberTier = (Integer) session.getAttribute("memberTier");
        if(memberTier == null) {
            memberTier = 0;
        }

        int productCount = 0;

        int productType = 0;

        if(memberTier != 0) {
            productCount = productDao.productCountByTierByProductType(memberTier, productType);
        }

        Product product = productDao.beanSelectOne(productCode);

        String detailImageName = imageDao.selectDetailImage(productCode);

        request.setAttribute("memberTier", memberTier);
        request.setAttribute("productCount", productCount);
        request.setAttribute("product", product);
        request.setAttribute("detailmageName", detailImageName);

        return "board/product/beanDetail";
    }

    @RequestMapping("mixDetail")
    public String mixDetail(HttpServletRequest request, HttpSession session, String productCode) throws Exception {
        Integer memberTier = (Integer) session.getAttribute("memberTier");
        if(memberTier == null) {
            memberTier = 0;
        }

        int productCount = 0;

        int productType = 1;

        if(memberTier != 0) {
            productCount = productDao.productCountByTierByProductType(memberTier, productType);
        }

        Product product = productDao.mixSelectOne(productCode);

        String detailImageName = imageDao.selectDetailImage(productCode);

        request.setAttribute("memberTier", memberTier);
        request.setAttribute("productCount", productCount);
        request.setAttribute("product", product);
        request.setAttribute("detailmageName", detailImageName);

        return "board/product/mixDetail";
    }

    @RequestMapping("cafeDetail")
    public String cafeDetail(HttpServletRequest request, HttpSession session, String productCode) throws Exception {
        Integer memberTier = (Integer) session.getAttribute("memberTier");
        if(memberTier == null) {
            memberTier = 0;
        }

        int productCount = 0;

        int productType = 2;

        if(memberTier != 0) {
            productCount = productDao.productCountByTierByProductType(memberTier, productType);
        }

        Product product = productDao.productSelectOne(productCode);

        String detailImageName = imageDao.selectDetailImage(productCode);

        request.setAttribute("memberTier", memberTier);
        request.setAttribute("productCount", productCount);
        request.setAttribute("product", product);
        request.setAttribute("detailmageName", detailImageName);

        return "board/product/cafeDetail";
    }

    @RequestMapping("machineDetail")
    public String machineDetail() throws Exception {
        return "board/product/machineDetail";
    }

    @PostMapping("productDetailPro")
    @ResponseBody
    public Map<String, Object> productDetailPro(HttpServletRequest request, HttpSession session, @RequestParam(value = "additionalProducts", required = false) List<String> additionalProductsCodes, Cart cart) throws Exception {
        Map<String, Object> map = new HashMap<>();

        String msg = "장바구니 추가 실패";
        String memberId = (String) session.getAttribute("memberId");

        //추가 상품 부분

        if (additionalProductsCodes != null && !additionalProductsCodes.isEmpty()) {
            for (String additionalProductsCode : additionalProductsCodes) {
                if(!additionalProductsCode.equals("none")) {
                    Cart cartCheck = cartDao.cartSelectOne(memberId, additionalProductsCode);

                    if(cartCheck == null) {
                        Cart additionalCart = new Cart();
                        additionalCart.setProductCode(additionalProductsCode);
                        additionalCart.setMemberId(memberId);
                        additionalCart.setQuantity(1);
                        cartDao.cartInsert(additionalCart);
                    } else {
                        int additionalProductQuantity = 1 + cartCheck.getQuantity();
                        cartDao.cartQuantityUpdate(memberId, additionalProductsCode, additionalProductQuantity);
                    }
                }
            }
        }

        //일반 상품 부분

        int quantity = cart.getQuantity();

        if(quantity < 1) {
            quantity = 1;
            cart.setQuantity(quantity);
        }

        String productCode = cart.getProductCode();

        Product product = productDao.productSelectOne(productCode);

        Cart cartCheck = cartDao.cartSelectOne(memberId, productCode);
        if(cartCheck == null) {
            cart.setMemberId(memberId);
            int num = cartDao.cartInsert(cart);

            if(num > 0) {
                msg = "장바구니 추가 성공";
                map.put("productCode", product.getProductCode());
                map.put("productName", product.getProductName());
                map.put("productUnit", product.getProductUnit());
//                map.put("productGrinding", cart.getProductGrinding());
                map.put("quantity", cart.getQuantity());
                map.put("productPrice", product.getProductPrice());
                map.put("productFile", product.getProductFile());
            }
        } else {
            quantity = quantity + cartCheck.getQuantity();
            cartDao.cartQuantityUpdate(memberId, productCode, quantity);
            msg = "장바구니 추가 성공";
            map.put("productCode", product.getProductCode());
            map.put("productName", product.getProductName());
            map.put("productUnit", product.getProductUnit());
//            map.put("productGrinding", product.getProductGrinding());
            map.put("quantity", cart.getQuantity());
            map.put("productPrice", product.getProductPrice());
            map.put("productFile", product.getProductFile());
        }

        map.put("cartStatus", msg);
        return map;
    }

    @RequestMapping("productSearch")
    public String productSearch(HttpServletRequest request, HttpSession session) throws Exception {
        Integer memberTier = (Integer) session.getAttribute("memberTier");
        if(memberTier == null) {
            memberTier = 0;
        }

        String pageNum = request.getParameter("pageNum");
        if (pageNum == null) {
            pageNum = "1";
        }

        int pageInt = Integer.parseInt(pageNum);

        int limit = 4; // 한 page당 게시물 개수

        String searchText = request.getParameter("searchText");

        int productSearchCount = 0;

        if(memberTier != 0) {
            productDao.rownumSet();
            List<Product> list = productDao.productSearchListByMemberTier(pageInt, limit, memberTier, searchText);
            productSearchCount = productDao.productSearchCountByTier(memberTier, searchText);
            request.setAttribute("list", list);
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

        request.setAttribute("searchText", searchText);
        request.setAttribute("memberTier", memberTier);
        request.setAttribute("productSearchCount", productSearchCount);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("start", start);
        request.setAttribute("end", end);

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



