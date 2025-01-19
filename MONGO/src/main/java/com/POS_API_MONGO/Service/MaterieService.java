package com.POS_API_MONGO.Service;

import com.POS_API_MONGO.Advice.Exception.*;
import com.POS_API_MONGO.DTO.CreateMaterieRequestDTO;
import com.POS_API_MONGO.DTO.GradingDTO;
import com.POS_API_MONGO.DTO.MaterieFilesResponseDTO;
import com.POS_API_MONGO.Model.Materie;
import com.POS_API_MONGO.Model.POJO.Fisier;
import com.POS_API_MONGO.Model.POJO.Test;
import com.POS_API_MONGO.Repository.MaterieDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaterieService {

    private final MaterieDAO materieRepo;
    private static final String BASE_PATH = "src/main/resources/Files";

    @Autowired
    public MaterieService(MaterieDAO materieRepo) {
        this.materieRepo = materieRepo;
    }

    private void verifyDisciplineExists(String codMaterie) {
        if (!materieRepo.existsByCodMaterie(codMaterie)) {
            throw new ResourceNotFoundException("Materie", "id (MONGO)", codMaterie);
        }
    }

    public void saveFile(String locatie, String codMaterie, MultipartFile file) {
        verifyDisciplineExists(codMaterie);

        if (!(locatie.equals("laborator") || locatie.equals("curs"))) {
            throw new WrongLocationException(locatie);
        }

        Path materiePath = Paths.get(BASE_PATH, codMaterie);
        Path locatiePath = materiePath.resolve(locatie);

        try {
            File materieDir = materiePath.toFile();
            if (!materieDir.exists()) {
                materieDir.mkdirs();
            }

            File locatieDir = locatiePath.toFile();
            if (!locatieDir.exists()) {
                locatieDir.mkdirs();
            }

            File savedFile = new File(locatieDir, file.getOriginalFilename());

            if (savedFile.exists()) {
                throw new FileNameExistsException(file.getOriginalFilename(), locatie);
            }

            try (FileOutputStream fos = new FileOutputStream(savedFile)) {
                fos.write(file.getBytes());
            }

            Optional<Materie> optionalMaterie = materieRepo.findByCodMaterie(codMaterie);
            if (optionalMaterie.isPresent()) {
                Materie materie = optionalMaterie.get();

                Fisier fisier = new Fisier();
                fisier.setNume(file.getOriginalFilename());
                fisier.setPath(savedFile.getAbsolutePath());

                if (locatie.equals("laborator")) {
                    materie.getFisiereLaborator().add(fisier);
                } else if (locatie.equals("curs")) {
                    materie.getFisiereCurs().add(fisier);
                }

                materieRepo.save(materie);
            }

        } catch (IOException e) {
            throw new RuntimeException("Eroare la salvarea fișierului: " + e.getMessage(), e);
        }
    }


    public Resource getFileResource(String codMaterie, String locatie, String numeFisier) {
        verifyDisciplineExists(codMaterie);

        if (!(locatie.equals("laborator") || locatie.equals("curs"))) {
            throw new WrongLocationException(locatie);
        }

        Path filePath = Paths.get(BASE_PATH, codMaterie, locatie, numeFisier);
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new ResourceNotFoundException("File", "name", numeFisier);
        }

        return new FileSystemResource(file);
    }

    public MaterieFilesResponseDTO getAllFilesForMaterie(String codMaterie) {
        verifyDisciplineExists(codMaterie);

        Materie materie = materieRepo.findByCodMaterie(codMaterie).get();


        List<String> fisiereLaborator = materie.getFisiereLaborator().stream().map(Fisier::getNume).collect(Collectors.toList());

        List<String> fisiereCurs = materie.getFisiereCurs().stream().map(Fisier::getNume).collect(Collectors.toList());

        MaterieFilesResponseDTO responseDTO = new MaterieFilesResponseDTO();
        responseDTO.setFisiereLaborator(fisiereLaborator);
        responseDTO.setFisiereCurs(fisiereCurs);

        return responseDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createMaterie(CreateMaterieRequestDTO createMaterieRequestDTO) {
        String codMaterie = createMaterieRequestDTO.getCodMaterie();
        String examinare = createMaterieRequestDTO.getExaminare();

        Optional<Materie> materieOptional = materieRepo.findByCodMaterie(codMaterie);
        if (materieOptional.isPresent()) {
            throw new UniqueKeyException("Lecture");
        }

        Test test = new Test();
        test.setNume(examinare);
        test.setPondere(1.0);

        Materie materie = new Materie();
        materie.setCodMaterie(codMaterie);
        materie.setFisiereLaborator(new ArrayList<>());
        materie.setFisiereCurs(new ArrayList<>());
        materie.setProbeExaminare(new ArrayList<>(List.of(test)));

        materieRepo.save(materie);
    }

    public GradingDTO getGradingByCodMaterie(String codMaterie) {
        verifyDisciplineExists(codMaterie);

        Optional<Materie> materie = materieRepo.findByCodMaterie(codMaterie);

        if (materie.isEmpty()) {
            throw new ResourceNotFoundException("Materie", "id", codMaterie);
        }

        GradingDTO gradingDTO = new GradingDTO();
        gradingDTO.setProbeExaminare(materie.get().getProbeExaminare());

        return gradingDTO;
    }

    public GradingDTO updateGrading(String codMaterie, List<Test> testeNouaLista) {
        verifyDisciplineExists(codMaterie);
        Materie materie = materieRepo.findByCodMaterie(codMaterie).get();

        materie.setProbeExaminare(testeNouaLista);
        if (!materie.isPondereValid())
            throw new InvalidPonderiException(materie.getSumaPonderi());

        Materie savedMaterie = materieRepo.save(materie);

        GradingDTO updatedGrading = new GradingDTO();
        updatedGrading.setProbeExaminare(savedMaterie.getProbeExaminare());

        return updatedGrading;
    }

    public void deleteFile(String locatie, String codMaterie, String numeFisier) {
        verifyDisciplineExists(codMaterie);

        if (!(locatie.equals("laborator") || locatie.equals("curs"))) {
            throw new WrongLocationException(locatie);
        }

        Path filePath = Paths.get(BASE_PATH, codMaterie, locatie, numeFisier);
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new ResourceNotFoundException("File", "name", numeFisier);
        }

        if (!file.delete()) {
            throw new RuntimeException("Eroare la ștergerea fișierului: " + numeFisier);
        }

        Materie materie = materieRepo.findByCodMaterie(codMaterie).get();

        if (locatie.equals("laborator")) {
            materie.getFisiereLaborator().removeIf(fisier -> fisier.getNume().equals(numeFisier));
        } else {
            materie.getFisiereCurs().removeIf(fisier -> fisier.getNume().equals(numeFisier));
        }

        materieRepo.save(materie);

    }


}
