package com.example.filedemo;

import static org.assertj.core.api.Assertions.assertThat;
import com.example.filedemo.property.FileStorageProperties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileDemoApplicationTests {

String uploadDirString="file";


   @Test
   public void verifyGettersSettersFromFileStorageProperties () {

      FileStorageProperties fsp = new FileStorageProperties();
      fsp.setUploadDir("file");
      String DirStringToTest = fsp.getUploadDir().toString();
      assertThat(DirStringToTest).isEqualTo(uploadDirString);
   }
}
