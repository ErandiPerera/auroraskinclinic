package com.erskinclinic.app.Controller;

import com.erskinclinic.app.Enum.TreatmentType;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class TreatmentTypeController {
	@GetMapping("/app/TreatmentType")
    public List<TreatmentType> getTreatmentType() {
        return TreatmentType.getAllTreatmentType();
    }

}
