package tech.wvs.qrcodegenerator.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import tech.wvs.qrcodegenerator.ports.StoragePort;

@Component
public class S3StorageAdapter implements StoragePort {

    private final S3Client s3Client;
    private final String bucketName;
    private final String region;

    public S3StorageAdapter(@Value("${aws.s3.bucket-name}") String bucketName,
                            @Value("${aws.s3.region}") String region) {
        this.bucketName = bucketName;
        this.region = region;
        this.s3Client = S3Client.builder()
                .region(Region.of(this.region))
                .build();
    }


    @Override
    public String uploadFile(byte[] fileData, String fileName, String contentType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(this.bucketName)
                .key(fileName)
                .contentType(contentType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileData));

        return String.format("https://%s.s3.%s.amazonaws.com/%s", this.bucketName, this.region, fileName);
    }
}
