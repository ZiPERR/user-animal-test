package kit.misc;

import kit.models.Animal;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface ExcelFileService {
    ByteArrayInputStream export(List<Animal> animals);
}
