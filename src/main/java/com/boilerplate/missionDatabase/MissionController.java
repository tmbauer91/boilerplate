package com.boilerplate.missionDatabase;

import com.boilerplate.missionDatabase.mission.Mission;
import com.boilerplate.missionDatabase.mission.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@RestController
public class MissionController {

    @Autowired
    private MissionRepository missionRepository;

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
    public String handleMissionFileUpload(@RequestParam("file") MultipartFile file,
                                          RedirectAttributes redirectAttributes) throws IOException {

        missionRepository.save(new Mission(file));
        redirectAttributes.addFlashAttribute("message",
                "File " + file.getOriginalFilename() + "Successfully Uploaded");
        return "redirect:/";
    }

    @PostMapping(value = "/missions/addBriefingMaterials")
    public String handleBriefingFileUpload(@RequestParam("file") MultipartFile[] files,
                                           @RequestParam("fileName") String fileName,
                                           RedirectAttributes redirectAttributes) throws IOException {

        Mission mission = missionRepository.findByName(fileName);
        missionRepository.delete(mission);
        mission.addBriefingFile(files);
        missionRepository.save(mission);
        redirectAttributes.addFlashAttribute("message",
                files.length + " Files Successfully Uploaded");
        return "redirect:/";
    }

    @DeleteMapping(value = "/missions/delete-mission")
    public ResponseEntity<String> deleteMission(@PathVariable String name){
        Mission mission = missionRepository.findByName(name);
        missionRepository.delete(mission);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
