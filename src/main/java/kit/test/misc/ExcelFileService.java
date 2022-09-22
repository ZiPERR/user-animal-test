package artplancom.test.misc;

import artplancom.test.models.Animal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public interface ExcelFileService {
    ByteArrayInputStream export(List<Animal> animals);
}
