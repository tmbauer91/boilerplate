package com.boilerplate.missionDatabase.mission;

import org.springframework.data.annotation.Id;

import java.io.*;

import org.springframework.data.mongodb.core.mapping.Document;

import org.bson.types.Binary;
import org.springframework.web.multipart.MultipartFile;

@Document(collection = "mission")
public class Mission {

    @Id
    public String id;
    private String name;
    private MissionType type;
    private Binary fileData;

    public Mission(){}

    public Mission(MultipartFile fileData) throws  IOException{

        String name = fileData.getOriginalFilename();
        if(name.contains(".miz")){
            this.type = MissionType.DCS;
        } else if (name.contains(".pbo")) {
            this.type = MissionType.ARMA;
        } else {
            throw new IOException("Wrong File Type");
        }

        //this.name = name.substring(0, name.indexOf('.'));
        this.name = name;
        this.fileData = new Binary(fileData.getBytes());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MissionType getType() {
        return type;
    }

    public void setType(MissionType type) {
        this.type = type;
    }

    public Binary getFileData() {
        return fileData;
    }

    public void setFileData(Binary fileData) {
        this.fileData = fileData;
    }

    public File getFile() throws IOException {
        return binaryToFile(fileData);
    }

    private File binaryToFile(Binary binary) throws IOException {

        File tempFile = File.createTempFile(name,type.getExtension(),null);
        try(FileOutputStream stream = new FileOutputStream(tempFile)){
            stream.write(binary.getData());
        }
        return tempFile;
    }

    @Override
    public String toString() {
        return String.format("File[id=%s, name=%s, type=%s']",
                id, name, type.getGame());
    }


}
