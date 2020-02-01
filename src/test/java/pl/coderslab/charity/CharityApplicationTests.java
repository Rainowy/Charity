package pl.coderslab.charity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import pl.coderslab.charity.Repository.InstitutionPartialView;
import pl.coderslab.charity.Repository.InstitutionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CharityApplicationTests {

//    @Test
    public void contextLoads() {
    }

    @Autowired
    private InstitutionRepository institutionRepository;

    @Test
    public void whenUsingClosedProjections_thenViewWithRequiredPropertiesIsReturned() {
        List<InstitutionPartialView> allInstitutions = institutionRepository.findAllByOrderByIdAsc();

        String institutionName = "Fundacja \"Dbam o Zdrowie\"";
        String institutionDescription = "Cel i misja: Pomoc dzieciom z ubogich rodzin.";

        Optional<InstitutionPartialView> first = allInstitutions.stream()
                .findFirst();

        List<String> allInstitutionsNames = allInstitutions.stream()
                .map(inst -> inst.getName())
                .collect(Collectors.toList());

        assertThat(first.get().getName()).isEqualTo(institutionName);
        assertThat(first.get().getDescription()).isEqualTo(institutionDescription);
    }
}
