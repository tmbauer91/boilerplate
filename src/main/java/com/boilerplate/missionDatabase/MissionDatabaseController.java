package com.boilerplate.missionDatabase;

import com.boilerplate.missionDatabase.mission.Mission;
import com.boilerplate.missionDatabase.mission.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@RestController
public class MissionDatabaseController {

    private final MissionRepository missionRepository;

    @Autowired
    public MissionDatabaseController(MissionRepository missionRepository){
        this.missionRepository = missionRepository;
    }

    @GetMapping("/missions")
    public String listUploadedFiles(Model model) throws IOException {
            return missionRepository.findAll().toString();
    }

    @GetMapping("/missions/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable("filename")  String fileName) throws IOException {
        Mission mission = missionRepository.findByName(fileName);
        FileSystemResource file = new FileSystemResource(mission.getFile());

        return  ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + mission.getName() + "\"").body(file);
    }

    @PostMapping(value = "/missions/submit-mission")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                RedirectAttributes redirectAttributes) throws IOException {

        missionRepository.save(new Mission(file));
        redirectAttributes.addFlashAttribute("message",
                "File " + file.getOriginalFilename() + "Successfully Uploaded");
        return "redirect:/";
    }

}
