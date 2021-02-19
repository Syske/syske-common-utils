package io.github.syske.commont.utils.pdf;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.awt.geom.RectangularShape;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.security.*;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.UUID;

/**
 * @program: syske-common-utils
 * @description: 个人图片签章工具类
 * @author: CaoLei
 * @create: 2019-12-03 22:54
 */

public class SignPdfUtil {
    private static Logger logger = Logger.getLogger(SignPdfUtil.class);
    private static final String PASSWORD = "123456"; // 秘钥密码
    private static final String KEY_STORE_PATH = "./keystore.p12"; // 秘钥文件路径

    private SignPdfUtil() {
    }

    /**
     * 图片签章，指定签名坐标位置
     *
     * @param signPdfSrc       签名的PDF文件
     * @param signedPdfOutFile 签名后的的PDF文件
     * @param signImage        签名图片完整路径
     * @param x                以左下角为原点x坐标值
     * @param y                以左下角为原点Y坐标值
     * @param numberOfPages    签名页码，如果是最后一页则传null
     * @param pageStyle        页面布局，横向或者纵向
     * @param isPersonal       是否私人签章
     *
     * @throws Exception
     */
    public static void sign(String signPdfSrc, String signedPdfOutFile,
                            String signImage, Float x, Float y, Integer numberOfPages,
                            PageStyle pageStyle, boolean isPersonal) throws Exception {
        sign(signPdfSrc, signedPdfOutFile, signImage, x, y, null,
                numberOfPages, pageStyle, isPersonal);
    }

    /**
     * 图片签章，指定关键字
     *
     * @param signPdfSrc    签名的PDF文件
     * @param signedPdfFile 签名后的的PDF文件
     * @param signImage     签名图片完整路径
     * @param keyWords      关键字
     * @param numberOfPages 签名页码，如果是最后一页则传null
     * @param pageStyle     页面布局，横向或者纵向
     * @param isPersonal    是否私人签章
     */
    public static void sign(String signPdfSrc, String signedPdfFile,
                            String signImage, String keyWords, Integer numberOfPages,
                            PageStyle pageStyle, boolean isPersonal) throws Exception {
        sign(signPdfSrc, signedPdfFile, signImage, null, null, keyWords,
                numberOfPages, pageStyle, isPersonal);
    }

    /**
     * 私人签章
     *
     * @param signPdfSrc       签名的PDF文件
     * @param signedPdfOutFile 签名后的的PDF文件
     * @param signImage        签名图片完整路径
     * @param x                以左下角为原点x坐标
     * @param y                以左下角为原点y坐标
     * @param keyWords         关键字
     * @param numberOfPages    签名页码，如果是最后一页则传null
     * @param pageStyle        页面布局，横向或者纵向
     * @param isPersonal       是否私人签章
     *
     * @return
     */
    public static void sign(String signPdfSrc, String signedPdfOutFile,
                            String signImage, Float x, Float y, String keyWords,
                            Integer numberOfPages, PageStyle pageStyle, boolean isPersonal)
            throws Exception {
        File signPdfSrcFile = new File(signPdfSrc);
        PdfReader reader = null;
        ByteArrayOutputStream signPDFData = null;
        PdfStamper stp = null;
        FileInputStream fos = null;
        FileOutputStream pdfOutputStream = null;
        try {
            BouncyCastleProvider provider = new BouncyCastleProvider();
            Security.addProvider(provider);
            KeyStore ks = KeyStore.getInstance("PKCS12",
                    new BouncyCastleProvider());
            fos = new FileInputStream(KEY_STORE_PATH);
            // 私钥密码 为Pkcs生成证书是的私钥密码 123456
            ks.load(fos, PASSWORD.toCharArray());
            String alias = ks.aliases().nextElement();
            PrivateKey key = (PrivateKey) ks.getKey(alias,
                    PASSWORD.toCharArray());
            Certificate[] chain = ks.getCertificateChain(alias);
            reader = new PdfReader(signPdfSrc); // 也可以输入流的方式构建
            signPDFData = new ByteArrayOutputStream();
            numberOfPages = numberOfPages == null ? reader.getNumberOfPages()
                    : 0;

            // 临时pdf文件
            File temp = new File(signPdfSrcFile.getParent(),
                    System.currentTimeMillis() + ".pdf");
            stp = PdfStamper.createSignature(reader, signPDFData, '\0', temp,
                    true);
            stp.setFullCompression();
            PdfSignatureAppearance sap = stp.getSignatureAppearance();
            sap.setReason("数字签名，不可改变");
            // 使用png格式透明图片
            Image image = Image.getInstance(signImage);

            sap.setImageScale(0);
            sap.setSignatureGraphic(image);
            sap.setRenderingMode(RenderingMode.GRAPHIC);
            // 创建签名区域
            Rectangle rectangle = isPersonal ? getPersonalSignRectangle(x, y,
                    keyWords, numberOfPages, pageStyle, reader, image)
                    : getPublicSignRectangle(x, y, keyWords, numberOfPages,
                    pageStyle, reader, image);
            // 是对应x轴和y轴坐标
            sap.setVisibleSignature(rectangle, numberOfPages, UUID.randomUUID()
                    .toString().replaceAll("-", ""));
            stp.getWriter().setCompressionLevel(5);
            ExternalDigest digest = new BouncyCastleDigest();
            ExternalSignature signature = new PrivateKeySignature(key,
                    DigestAlgorithms.SHA512, provider.getName());
            MakeSignature.signDetached(sap, digest, signature, chain, null,
                    null, null, 0, CryptoStandard.CADES);
            stp.close();
            reader.close();
            pdfOutputStream = new FileOutputStream(signedPdfOutFile);
            pdfOutputStream.write(signPDFData.toByteArray());
            pdfOutputStream.close();
        } catch (KeyStoreException e) {
            logger.error("签名验证失败", e);
            throw new Exception("签名验证失败", e);
        } catch (FileNotFoundException e) {
            logger.error("文件未找到", e);
            throw new Exception("文件未找到", e);
        } catch (IOException e) {
            logger.error("IO异常", e);
            throw new Exception("IO异常", e);
        } catch (Exception e) {
            logger.error("签章失败", e);
            throw new Exception("签章失败", e);
        } finally {
            if (signPDFData != null) {
                try {
                    signPDFData.close();
                } catch (IOException e) {
                    logger.error("资源关闭失败", e);
                    throw new Exception("资源关闭失败", e);
                }
            }

            closeStream(fos);
            if (pdfOutputStream != null) {
                try {
                    pdfOutputStream.close();
                } catch (IOException e) {
                    logger.error("资源关闭失败", e);
                    throw new Exception("资源关闭失败", e);
                }
            }

        }
    }

    private static void closeStream(FileInputStream fos) throws Exception {
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                logger.error("资源关闭失败", e);
                throw new Exception("资源关闭失败", e);
            }
        }
    }

    /**
     * 公章
     *
     * @param x
     * @param y
     * @param keyWords
     * @param numberOfPages
     * @param pageStyle
     * @param reader
     * @param image
     *
     * @return
     *
     * @throws IOException
     * @throws Exception
     */
    private static Rectangle getPublicSignRectangle(Float x, Float y,
                                                    String keyWords, Integer numberOfPages, PageStyle pageStyle,
                                                    PdfReader reader, Image image) throws IOException, Exception {
        float llx = 0f;
        float lly = 0f;


        float urx = 0.0f;
        float ury = 0.0f;
        if (keyWords != null && !keyWords.isEmpty()) {
            KeyWordInfo keyWordInfo = getKeyWordLocation(numberOfPages,
                    keyWords, reader);
            logger.info("关键字信息：" + keyWordInfo);
            Rectangle pageSize = reader.getPageSize(numberOfPages);
            float width = pageSize.getWidth();
            if (PageStyle.PAGE_STYLE_LANDSCAPE.equals(pageStyle)) {
                llx = keyWordInfo.getY() + (float) keyWordInfo.getHeight() / 2 - 60;
                lly = width - keyWordInfo.getX() - 60;
            } else if (PageStyle.PAGE_STYLE_PORTRAIT.equals(pageStyle)) {
                llx = keyWordInfo.getX() + (float) keyWordInfo.getWidth() / 2 - 60;
                lly = keyWordInfo.getY() - 60;
            }
            urx = llx + 120;
            ury = lly + 120;

        } else if (x != null && y != null) {
            llx = x;
            lly = y;
        } else {
            throw new Exception("坐标和关键字不能同时为空！");
        }
        logger.info("lly:" + lly);
        logger.info("llx:" + llx);
        logger.info("urx:" + urx);
        logger.info("ury:" + ury);
        Rectangle rectangle = new Rectangle(llx, lly, urx, ury);
        return rectangle;
    }

    /**
     * 私章
     *
     * @param x
     * @param y
     * @param keyWords
     * @param numberOfPages
     * @param pageStyle
     * @param reader
     * @param image
     *
     * @return
     *
     * @throws IOException
     * @throws Exception
     */
    private static Rectangle getPersonalSignRectangle(Float x, Float y,
                                                      String keyWords, Integer numberOfPages, PageStyle pageStyle,
                                                      PdfReader reader, Image image) throws IOException, Exception {
        float llx = 0f;
        float lly = 0f;

        float signImageWidth = image.getWidth();
        float signImageHeight = image.getHeight();

        float urx = 0.0f;
        float ury = 0.0f;
        float signImageHeightSocale = 0.0f;
        signImageHeightSocale = 85 / signImageWidth * signImageHeight;

        if (keyWords != null && !keyWords.isEmpty()) {
            KeyWordInfo keyWordInfo = getKeyWordLocation(numberOfPages,
                    keyWords, reader);
            Rectangle pageSize = reader.getPageSize(numberOfPages);
            float width = pageSize.getWidth();
            if (PageStyle.PAGE_STYLE_LANDSCAPE.equals(pageStyle)) {
                llx = keyWordInfo.getY() + 472 / 2;
                lly = width - keyWordInfo.getX() - signImageHeightSocale * 3
                        / 4;
                logger.info("width:" + width);
                logger.info("getX():" + keyWordInfo.getX());
                logger.info("lly:" + lly);
                logger.info("signImageHeightSocale:" + signImageHeightSocale
                        / 2);
            } else if (PageStyle.PAGE_STYLE_PORTRAIT.equals(pageStyle)) {
                llx = keyWordInfo.getX() + (float) keyWordInfo.getWidth();
                lly = keyWordInfo.getY() - signImageHeightSocale / 2;
            }
            urx = llx + 85 / 2;

            ury = lly + signImageHeightSocale;

        } else if (x != null && y != null) {
            llx = x;
            lly = y;
        } else {
            throw new Exception("坐标和关键字不能同时为空！");
        }

        Rectangle rectangle = new Rectangle(llx, lly, urx, ury);
        return rectangle;
    }

    /**
     * 查找关键字定位
     *
     * @param numberOfPages
     * @param keyWords      关键字
     * @param reader
     *
     * @return
     *
     * @throws IOException
     */
    private static KeyWordInfo getKeyWordLocation(Integer numberOfPages,
                                                  final String keyWords, PdfReader reader) throws IOException {
        PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(
                reader);

        final KeyWordInfo keyWordInfo = new KeyWordInfo();
        pdfReaderContentParser.processContent(numberOfPages,
                new RenderListener() {
                    @Override
                    public void renderText(TextRenderInfo textRenderInfo) {
                        String text = textRenderInfo.getText(); // 整页内容

                        if (null != text && text.contains(keyWords)) {
                            RectangularShape rectBase = textRenderInfo.getBaseline().getBoundingRectange();
                            //获取文字下面的矩形
                            Rectangle2D.Float rectAscen = textRenderInfo.getAscentLine().getBoundingRectange();
                            //计算出文字的边框矩形
                            float leftY = (float) rectBase.getMinY() - 1;
                            float rightY = (float) rectAscen.getMaxY() + 1;

                            float leftX = (float) rectBase.getMinX();
                            float rightX = (float) rectAscen.getMaxX();

                            logger.info(rectAscen.x + "--"
                                    + rectAscen.y + "---");

                            keyWordInfo.setHeight(rightY - leftY);
                            keyWordInfo.setWidth((rightX - leftX));
                            keyWordInfo.setX(rectAscen.x);
                            keyWordInfo.setY(rectAscen.y);
                        }

                    }

                    @Override
                    public void renderImage(ImageRenderInfo arg0) {
                    }

                    @Override
                    public void endTextBlock() {
                    }

                    @Override
                    public void beginTextBlock() {
                    }
                });
        return keyWordInfo;
    }

    private static class KeyWordInfo {
        private float x;
        private float y;
        private double width;
        private double height;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public double getWidth() {
            return width;
        }

        public void setWidth(double width) {
            this.width = width;
        }

        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height;
        }

        @Override
        public String toString() {
            return "KeyWordInfo [x=" + x + ", y=" + y + ", width=" + width
                    + ", height=" + height + "]";
        }


    }

    enum PageStyle {
        // 横向
        PAGE_STYLE_LANDSCAPE,
        // 纵向
        PAGE_STYLE_PORTRAIT
    }

    public static PageStyle getPageStyle_LANDSCAPE() {
        return PageStyle.PAGE_STYLE_LANDSCAPE;
    }

    public static PageStyle getPageStyle_PORTRAIT() {
        return PageStyle.PAGE_STYLE_PORTRAIT;
    }
}