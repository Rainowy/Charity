package pl.coderslab.charity;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.charity.Repository.InstitutionPartialView;
import pl.coderslab.charity.Repository.InstitutionRepository;
import pl.coderslab.charity.service.AmazonS3Service;
import pl.coderslab.charity.service.UserService;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CharityApplicationTests {

    @Value("${aws.s3.bucket}")
    private String AWS_S3_BUCKET;

    @Autowired
    private InstitutionRepository institutionRepository;
    @Autowired
    private AmazonS3Service amazonS3Service;
    @Autowired
    private UserService userService;

    private AmazonS3 getS3client() {
        return amazonS3Service.getS3client();
    }

    @Test
    public void whenUsingClosedProjections_thenViewWithRequiredPropertiesIsReturned() {
        List<InstitutionPartialView> allInstitutions = institutionRepository.findAllByOrderByIdAsc();

        String institutionName = "Budzik";
        String institutionDescription = "Instytucja leczy osoby w śpiączce";

        Optional<InstitutionPartialView> name = allInstitutions.stream()
                .filter(inst -> inst.getName().equals(institutionName))
                .findFirst();

        Optional<InstitutionPartialView> description = allInstitutions.stream()
                .filter(inst -> inst.getDescription().equals(institutionDescription))
                .findFirst();

        assertTrue(name.isPresent());
        assertTrue(description.isPresent());
    }

    @Test
    public void whenUsingCredentialsConnectionEstablishedAndBucketsListed() {
        getS3client().listBuckets();
    }

    @Test
    public void checkIfListContainsMyBucket() {

        List<Bucket> buckets = getS3client().listBuckets();
        Optional<Bucket> myBucket = buckets.stream()
                .filter(bucket -> bucket.getName().equals(AWS_S3_BUCKET))
                .findFirst();
        assertEquals(myBucket.get().getName(), "rainowy-bucket");
    }

    @Test
    public void checkIfFileIsSendingToBucket() {

        MultipartFile test = new MockMultipartFile("test.txt",
                "test.txt",
                "text/plain",
                "This is a dummy file content".getBytes(StandardCharsets.UTF_8));

        userService.saveAvatar(test);
        String urlToFile = getS3client().getUrl(AWS_S3_BUCKET, test.getOriginalFilename()).toString();
        assertTrue(fileExists(urlToFile));
    }

    @Test
    public void ifFileExistDeleteIt() {

        String fileToDelete = "test.txt";
        ObjectListing objectListing = getS3client().listObjects(AWS_S3_BUCKET);
        boolean fileExist = false;
        for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
            if (os.getKey().equals(fileToDelete)) {
                fileExist = true;
            }
        }
        if (fileExist) getS3client().deleteObject(AWS_S3_BUCKET, fileToDelete);
        String urlToFile = "https://rainowy-bucket.s3.eu-central-1.amazonaws.com/" + fileToDelete;
        assertFalse(fileExists(urlToFile));
    }

    public static boolean fileExists(String URLName) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            // note : you may also need
            //        HttpURLConnection.setInstanceFollowRedirects(false)
            HttpURLConnection con =
                    (HttpURLConnection) new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
