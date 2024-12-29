package com.POS_API_MONGO.DTO;

import com.POS_API_MONGO.Model.POJO.Test;
import lombok.Data;

import java.util.List;

@Data
public class GradingDTO {
    private List<Test> probeExaminare;
}
