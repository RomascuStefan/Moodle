package com.POS_API_MONGO.Model;

import com.POS_API_MONGO.Model.POJO.Fisier;
import com.POS_API_MONGO.Model.POJO.Test;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;


@Document(collection = "materii")
@Getter
@Setter
@ToString
public class Materie {
    @Id
    private String id;

    @Field("cod_materie")
    private String codMaterie;

    @Field("fisiere_laborator")
    private List<Fisier> fisiereLaborator = new ArrayList<>();

    @Field("fisiere_curs")
    private List<Fisier> fisiereCurs = new ArrayList<>();

    @Field("probe_examinare")
    private List<Test> probeExaminare = new ArrayList<>();

    public boolean isPondereValid() {
        double sum = 0.0;

        for (Test test : probeExaminare) {
            sum += test.getPondere();
        }

        return sum == 1.;
    }

    public String getSumaPonderi()
    {
        double sum = 0.0;

        for (Test test : probeExaminare) {
            sum += test.getPondere();
        }

        return String.format("%.2f", sum * 100) + "%";
    }
}
