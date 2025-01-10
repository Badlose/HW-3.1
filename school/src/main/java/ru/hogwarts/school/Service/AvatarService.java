package ru.hogwarts.school.Service;

import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.Model.Avatar;

import java.io.IOException;

public interface AvatarService {


    void uploadAvatar(Long id, MultipartFile avatar) throws IOException;

    Avatar findAvatar(Long studentId);
}
