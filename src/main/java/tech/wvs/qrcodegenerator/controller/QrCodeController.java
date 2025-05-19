package tech.wvs.qrcodegenerator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.wvs.qrcodegenerator.QrCodeGeneratorService;
import tech.wvs.qrcodegenerator.dto.QrCodeRequest;
import tech.wvs.qrcodegenerator.dto.QrCodeResponse;

@RestController
@RequestMapping(path = "/qrcode")
public class QrCodeController {

    private final QrCodeGeneratorService service;

    public QrCodeController(QrCodeGeneratorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<QrCodeResponse> generate(@RequestBody QrCodeRequest dto) {
        return ResponseEntity.ok(service.generateAndUploadQrCode(dto.text()));
    }
}
