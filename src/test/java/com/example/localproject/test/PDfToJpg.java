package com.example.localproject.test;

import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.util.GraphicsRenderingHints;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author xiazhengtao
 * @date 2020-06-20 20:27
 */
public class PDfToJpg {
    public static void main(String[] args) {
        PDfToJpg.pdf2jpg("/Users/xiazhengtao/文件/个人/身份证.pdf","/Users/xiazhengtao/文件/个人/身份证.jpg");
    }
    /**
     * pdf文件转jpg
     *
     * @param pdffilepath
     */
    public static void pdf2jpg(String pdffilepath, String jpgfilepath) {
        try {
            Document document = new Document();
            try {
                document.setFile(pdffilepath);
            } catch (PDFException e) {
                e.printStackTrace();
            } catch (PDFSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            float scale = 2.5f;// 缩放比例
            float rotation = 0f;// 旋转角度
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage image = (BufferedImage) document.getPageImage(1,
                        GraphicsRenderingHints.SCREEN,
                        org.icepdf.core.pobjects.Page.BOUNDARY_CROPBOX, rotation,
                        scale);
                RenderedImage rendImage = image;
                try {
                    File file = new File(jpgfilepath);
                    ImageIO.write(rendImage, "jpg", file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image.flush();
            }
            document.dispose();
//            for (int i = 0; i < document.getNumberOfPages(); i++) {
//                BufferedImage image = (BufferedImage) document.getPageImage(i,
//                        GraphicsRenderingHints.SCREEN,
//                        org.icepdf.core.pobjects.Page.BOUNDARY_CROPBOX, rotation,
//                        scale);
//                RenderedImage rendImage = image;
//                try {
//                    File file = new File(jpgfilepath);
//                    ImageIO.write(rendImage, "jpg", file);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                image.flush();
//            }
//            document.dispose();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
