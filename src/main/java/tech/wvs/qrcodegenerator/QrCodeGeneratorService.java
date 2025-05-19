package tech.wvs.qrcodegenerator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import tech.wvs.qrcodegenerator.dto.QrCodeResponse;
import tech.wvs.qrcodegenerator.ports.StoragePort;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class QrCodeGeneratorService {

    private final StoragePort storagePort;

    public QrCodeGeneratorService(StoragePort storagePort) {
        this.storagePort = storagePort;
    }

    public QrCodeResponse generateAndUploadQrCode(String text) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            byte[] pgnQrCodeData = outputStream.toByteArray();

            String url = storagePort.uploadFile(pgnQrCodeData,
                    UUID.randomUUID().toString(),
                    "image/png");

            return new QrCodeResponse(url);

        } catch (WriterException |
                IOException e) {
            throw new RuntimeException("Error generation QR code", e);
        }
    }
}
