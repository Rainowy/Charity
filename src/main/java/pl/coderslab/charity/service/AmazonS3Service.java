package pl.coderslab.charity.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AmazonS3Service {

    @Value("${aws.s3.bucket}")
    private String AWS_S3_BUCKET;
    @Value("${aws.access.key}")
    private String AWS_ACCESS_KEY;
    @Value("${aws.secret.key}")
    private String AWS_SECRET_KEY;


    public AmazonS3 getS3client() {

        AWSCredentials credentials = new BasicAWSCredentials(
                AWS_ACCESS_KEY,
                AWS_SECRET_KEY
        );

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }

    public String getAwsS3Bucket() {

        if (!getS3client().doesBucketExist(AWS_S3_BUCKET)) {
            getS3client().createBucket(AWS_S3_BUCKET);
        }
        return AWS_S3_BUCKET;
    }
}
